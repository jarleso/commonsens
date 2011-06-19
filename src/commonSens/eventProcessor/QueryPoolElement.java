package eventProcessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import language.AndOperator;
import language.QueryElement;
import language.AtomicQuery;
import language.ConOpDuring;
import language.ConOpEquals;
import language.TimedListOfAtomicQueries;
import language.ConcurrencyOperator;
import language.ConsecutiveRelation;
import language.NotOperator;
import language.OrOperator;
import language.Strict;
import modelViewController.Core;
import modelViewController.MainView;

public class QueryPoolElement {

    private DataTupleFilter dataTupleFilter;
    private Core core;
//    private Box firstState;
    private Box currBox;
//    private int currNumTSs;
    private TimedListOfAtomicQueries complexQuery;
//    private QueryElement firstStmt;
//    private QueryElement currStmt;
//    private QueryElement nextStmt;
//    private ArrayList<DataTuple> tupleBatch;
//    private HashMap<Sensor, Boolean> hasBeenChecked;
    private String outPrefix;

    public static final int CHANGE_STATE = 1;
    public static final int COND_OK = 2;
    public static final int DEVIATION = 3;
    public static final int NOT_FULL_BATCH = 4;
    public static final int NOT_ALL_ATTRIBUTES = 5;
    public static final int NO_MATCHES = 6;
    public static final int ISEC_MISMATCH = 7;
    public static final int NOISEC_MISMATCH = 8;
    public static final int MATCH = 9;
    public static final int ERROR = 10;
    public static final int IGNORE = 11;
//    private Relation relation;

    /**
     * Tuples will arrive this state machine, but only the tuple that triggers
     * the state machine is the one that this evaluated first.
     */

    private BufferedWriter out;
    private Timer globalTime;
    private ArrayList<Box> boxs;
    private boolean emulation = true;
    private QueryElement firstElement;
    private ConcurrentLinkedQueue<DataTuple> cepQueue;
    private int runNumber;
    private int numSensors;
    private int numAnds;
    private Timestamp begin = null;
    private Timestamp end = null;
    private boolean isMin = false;
    private double percentage = 0;


    public QueryPoolElement (QueryElement firstElement, Core panel,
            ConcurrentLinkedQueue<DataTuple> cepQueue, String outPrefix,
            boolean emulation, int runNumber, int numSensors, int numAnds) {

        setFirstElement(firstElement);
        setBegin(new Timestamp(-1));
        setEnd(new Timestamp(-1));
        setIsMin(false);
        setPercentage(0);

        setup(panel, cepQueue, outPrefix, emulation, runNumber,
                numSensors, numAnds);
    }

    public QueryPoolElement (QueryElement firstElement, Timestamp begin,
            Timestamp end, boolean isMin, double percentage,
            Core panel, ConcurrentLinkedQueue<DataTuple> cepQueue,
            String outPrefix,
            boolean emulation, int runNumber, int numSensors, int numAnds) {

        setFirstElement(firstElement);
        setBegin(begin);
        setEnd(end);
        setIsMin(isMin);
        setPercentage(percentage);

        setup(panel, cepQueue, outPrefix, emulation, runNumber,
                numSensors, numAnds);
    }

    public Core getCore() {
        return core;
    }

    public DataTupleFilter getDataTupleFilter () {
        return dataTupleFilter;
    }

    public void setDataTupleFilter (DataTupleFilter val) {
        this.dataTupleFilter = val;
    }

    @Deprecated
    public void startDataTupleFilter () {

        //new Thread(getDataTupleFilter()).start();
    }

    private long getTimeMillisecond(boolean emulation) {

        return core.getMainClass().getTimeMillisecond(emulation);
    }
    
    private long getTimeMillisecond(int Hz) {

        return core.getMainClass().getTimer().getCurrentMillisecond(Hz);
    }

    private void setupFile(int runNumber) {
        try {
            out = new BufferedWriter(new FileWriter(outPrefix + "/" +
                    runNumber + "_StateMachine.dat"));
        } catch (IOException ex) {
            Logger.getLogger(DataTupleFilter.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }

    public void closeFile() {
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(QueryPoolElement.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @SuppressWarnings("unused")
	private void writeToFile(String text) {
        try {
            out.write(getTimeMillisecond(emulation) + "\t" + text + "\n");
        } catch (IOException ex) {
            Logger.getLogger(DataTupleFilter.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    Box getCurrentBox() {

        return currBox;
    }

    /**
     * Usually used for setting the next state. However, can also be used
     * for setting the previous state.
     * 
     */
    
    Box prevState = null;
    
    Box setCurrentBox(Box next, int message) {

        prevState = currBox;

        currBox = next;

        // Show off

        if(prevState != null) {
//        	System.err.println("setCurrentState prevState == " + 
//        			prevState.showState());
        } else {
//        	System.err.println("setCurrentState prevState == null");
        }
        	
        if(next != null) {
            core.getPanel().changeCurrCond(currBox.showState());
        } else {
        	
//        	System.out.println("setCurrrentState.currState = " + currBox);
        	
            core.getPanel().changeCurrCond(MainView.
                    printEndMessage(message)); /*this.getDataTupleFilter().
                    		getEndMessage())*/
        }
        
        /**
         * Update information about state changes.
         */
        
        core.getMainFilter().writeStateChangesFile(getTimeMillisecond(Core.Hz) +
        		"\t1\t" + message + "\n");        

        return prevState;
    }

    void showEndState(String msg) {

        core.getPanel().unsetIsRunning();


        core.getPanel().changeCurrCond(msg);
    }

    /**
     * Instantiates the chain. Returns an array list of all the consecutive
     * boxs of various kinds.
     * 
     * For chains that contain delta-timed queries
     * followed by timed queries, it is necessary to set a deadline for the
     * delta-timed atomic queries. If the delta-timed atomic queries have not 
     * happened when the timed atomic query is supposed to start. 
     * 
     * @param currElem
     * @return
     */
        
    private ArrayList<Box> instantiateBoxes(QueryElement currElem) {

        Transition tmpTrans = null;
        Box tmpBox = null;
        boolean isNot = false;

        ArrayList<Box> retStates = new ArrayList<Box>();

        while(currElem != null) {

            //System.err.println("currElem = " + 
        	//currElem.showStatementElement());

            if(currElem instanceof AtomicQuery) {

                //System.err.println("currElemt is AtomicQuery");

                if(tmpBox == null) {
                    tmpBox = new Box();

                    if(tmpTrans != null) {

                        tmpTrans.setNextBox(tmpBox);
                        
                    }

                    tmpTrans = null;
                }

                tmpBox.addQueryElement(currElem,
                    core.getMainClass().getEnvironments().get(0), isNot);

                /**
                 * Check timing of the current state.
                 */
                
                if(((AtomicQuery) currElem).isTimed()) {
                
                	Timestamp tmpTimestamp = ((AtomicQuery) currElem).
                		getBegin();
                	
                	/**
                	 * Set the deadline on all the prior delta-
                	 * timed queries which have not been initialized with a
                	 * deadline.
                	 */
                	
                	for(Box deadlineState : retStates) {
                		
                		/**
                		 * Go through all the and-chains.
                		 */
                		
                		for(AndChainCalculations tmpAndChains : 
                			deadlineState.getAndChains()) {
                
                			if(tmpAndChains.isDeltaTimed() &&
                					tmpAndChains.getDeadline() == null) {
                				
                				/**
                				 * Set deadline.
                				 */
                				
                				tmpAndChains.setDeadline(tmpTimestamp);
                			}
                		}
                	}
                }
                
                /** 
                 * Always set to false, since if there were any NotOperator, it
                 * would apply to the current complexQuery element.
                 */                

                isNot = false;

            } else if(currElem instanceof TimedListOfAtomicQueries) {

//                System.err.println("currelem is " +
//                		"TimedListOfAtomicQueries");

                if(tmpBox == null) {

                    tmpBox = new TimedChainOfStatesBox();

                    TimedListOfAtomicQueries tmpTimed =
                            (TimedListOfAtomicQueries) currElem;

                    ((TimedChainOfStatesBox) tmpBox)
                            .setTheStateMachine(new 
                            		QueryPoolElement(tmpTimed.getTheChain(),
                            tmpTimed.getBegin(), tmpTimed.getEnd(),
                            tmpTimed.isIsMin(), tmpTimed.getPercentage(),
                            core, cepQueue, outPrefix, emulation,
                            runNumber, numSensors, numAnds));

                    if(tmpTrans != null) {

                        tmpTrans.setNextBox(tmpBox);

                    }

                    tmpTrans = null;

                    // Always set to false, since if there were any NotOperator, it
                    // would apply to the current complexQuery element.

                    isNot = false;
                }
            } else if(currElem instanceof ConcurrencyOperator) {
            	
                //System.err.println("currElem is ConcurrencyOperator.");

                /**
                 * Set up a new state machine object for each of the two
                 * queries. Currently, Equals and During are implemented. 
                 */

                if(tmpBox == null) {

                    if(currElem instanceof ConOpEquals) {

                        tmpBox = new EqualsConcurrencyBox();

                        ((ConcurrencyBox) tmpBox).
                                setFirstBoxList(
                                		new QueryPoolElement(((ConcurrencyOperator) 
                                				currElem).
                                getFirstChain(), core, cepQueue, outPrefix,
                                emulation, runNumber, numSensors, numAnds));

                        ((ConcurrencyBox) tmpBox).
                                setSecondBoxList(
                                		new QueryPoolElement(((ConcurrencyOperator) 
                                				currElem).
                                getSecondChain(), core, cepQueue, outPrefix,
                                emulation, runNumber, numSensors, numAnds));
                    } else if(currElem instanceof ConOpDuring) {

                        tmpBox = new DuringConcurrencyBox();

                        ((ConcurrencyBox) tmpBox).
                                setFirstBoxList(
                                		new QueryPoolElement(((ConcurrencyOperator) 
                                				currElem).
                                getFirstChain(), core, cepQueue, outPrefix,
                                emulation, runNumber, numSensors, numAnds));

                        ((ConcurrencyBox) tmpBox).
                                setSecondBoxList(
                                		new QueryPoolElement(((ConcurrencyOperator) 
                                				currElem).
                                getSecondChain(), core, cepQueue, outPrefix,
                                emulation, runNumber, numSensors, numAnds));
                    }

                    if(tmpTrans != null) {

                        tmpTrans.setNextBox(tmpBox);

                    }

                    tmpTrans = null;

                    // Always set to false, since if there were any NotOperator, it
                    // would apply to the current complexQuery element.

                    isNot = false;
                }

            } else {

                if(currElem instanceof AndOperator) {

                    //System.err.println("currElemt is AndOperator");

                } else if(currElem instanceof OrOperator) {

                    //System.err.println("currElemt is OrOperator");

                    tmpBox.newOr();

                } else if(currElem instanceof NotOperator) {

                    //System.err.println("currElemt is NotOperator");

                    isNot = true;

                } else if(currElem instanceof ConsecutiveRelation) {

                    //System.err.println("currElemt is ConsecutiveRelation");

                    tmpBox.addTransition("ConsecutiveRelation");

                    tmpTrans = tmpBox.getTransition();

                    /**
                     * The transition decides that the current state
                     * is to be stored.
                     */
                    
                    retStates.add(tmpBox);

                    tmpBox = null;

                } else if(currElem instanceof Strict) {

                    //System.err.println("currElemt is Strict");

                    tmpBox.addTransition("Strict");

                    tmpTrans = tmpBox.getTransition();

                    retStates.add(tmpBox);

                    tmpBox = null;

                } else {

                    System.err.println("Error parsing: "
                            + currElem.getEventString());

                }
            }

            if(currElem.getNext() != null) {
                currElem = currElem.getNext();
            } else {

                retStates.add(tmpBox);
                break;
            }
        }
        return retStates;
    }

    private void setup(Core panel, 
    		ConcurrentLinkedQueue<DataTuple> cepQueue,
            String outPrefix, boolean emulation, int runNumber, 
            int numSensors, int numAnds) {

        this.core = panel; // For showing off.
        this.outPrefix = outPrefix;
        this.emulation = emulation;
        this.cepQueue = cepQueue;
        this.runNumber = runNumber;
        this.numSensors = numSensors;
        this.numAnds = numAnds;

        this.globalTime = panel.getMainClass().getTimer();

        boxs = new ArrayList<Box>();

        setDataTupleFilter(new DataTupleFilter(this,
                cepQueue, outPrefix, globalTime, runNumber, numSensors, 
                numAnds));

//        System.err.println("Statement = " + 
//        		firstElement.showElementRecursive());
        
        /**
         * Construct the state machine by going through the atomic statements.
         */

        QueryElement currElem = null;

        //if(isTimed()) {

            /** 
             * First, set the timing from the complex event to the 
             * filter.
             */

            getDataTupleFilter().setTiming(getBegin(),
                    getEnd(),
                    getIsMin(),
                    getPercentage());


            currElem = firstElement;

//        } else {
//
//            currElem = firstElement;
//        }

        boxs = instantiateBoxes(currElem);

        currBox = boxs.get(0);

        panel.getPanel().changeCurrCond(currBox.showState());

        // All boxs are now set up.

        // Write the instantiated statement to file.

        BufferedWriter instST = null;

        try {
            instST = new BufferedWriter(new FileWriter(outPrefix + 
            		"/InstantiatedStatement.dat"));
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).
            log(Level.SEVERE, null, ex);
        }

        //System.err.println("boxs.size() = " + boxs.size());

//        int i = 0;

        for(Box tState : boxs) {

            //System.err.println("i = " + i++ + ", STATE: " + tState.showState());

            if(instST != null) {

                try {
                    instST.write("STATE: " + tState.showState());
                } catch (IOException ex) {
                    Logger.getLogger(QueryPoolElement.class.getName()).log(Level.
                    		SEVERE, null, ex);
                }

            }
        }

        // Also add the timing in the simga filter.

        if(complexQuery != null) {

            //System.err.println("COMPLEX EVENT TIME: " + dataTupleFilter.getTiming());

            try {
                instST.write("COMPLEX EVENT TIME: " + dataTupleFilter.getTiming());
            } catch (IOException ex) {
                Logger.getLogger(QueryPoolElement.class.getName()).log(Level.SEVERE, 
                		null, ex);
            }
        }

        if(instST != null) {
            try {
                instST.close();
            } catch (IOException ex) {
                Logger.getLogger(QueryPoolElement.class.getName()).log(Level.SEVERE, 
                		null, ex);
            }
        }

        panel.getPanel().changeCurrCond(currBox.showState());

        setupFile(runNumber);
    }

    private boolean isTimed() {

        if(getBegin() != null && getEnd() != null) {
            return getBegin().getTimestamp() != -1
                    && getEnd().getTimestamp() != -1;
        }
        
        return false;
    }

    private void setFirstElement(QueryElement firstElement) {
        this.firstElement = firstElement;
    }

    public QueryElement getFirstElement() {
        return firstElement;
    }

    private void setBegin(Timestamp begin) {

        if(begin == null) {
            System.err.println("In QueryPoolElement: begin == null");
        }

        this.begin = begin;
    }

    private Timestamp getBegin() {
        return begin;
    }

    private void setEnd(Timestamp end) {

        if(end == null) {
            System.err.println("In QueryPoolElement: end == null");
        }

        this.end = end;
    }

    private Timestamp getEnd() {
        return end;
    }

    private void setIsMin(boolean min) {
        this.isMin = min;
    }

    private boolean getIsMin() {
        return isMin;
    }

    private void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    private double getPercentage() {
        return percentage;
    }

    /**
     * Obtains the previous state to continue evaluation. Used for concurrency
     * queries.
     */
    
	public void rewind(boolean first) {
		
//		System.err.println("StateMachein.rewind: currentState = " + 
//				getCurrentBox() + " and prevState = " + 
//				prevState);
		
		setCurrentBox(prevState, 
				first? DataTupleFilter.M_REWIND_FIRST : 
					DataTupleFilter.M_REWIND_SECOND);

		if(getCurrentBox() instanceof TimedChainOfStatesBox) {

			TimedChainOfStatesBox tmpChain = 
				(TimedChainOfStatesBox) getCurrentBox();
			
			tmpChain.getTheStateMachine().rewind(first);
		}
		
		getDataTupleFilter().setFilterFinished(false);
		getDataTupleFilter().setEndState(DataTupleFilter.M_NO_TRIGGER);
		
		/**
		 * Resets the evaluation of the current state. This solution might
		 * not be good, since it resets all, but at least we check for
		 * nulls. 
		 */
		
		if(getDataTupleFilter().getCurrState() != null) {
		
			for(AndChainCalculations tmpCalc : getDataTupleFilter().
					getCurrState().getAndChains()) {
			
	// Possible solution:	if(tmpCalc.hasMatched or something)
				
				tmpCalc.setIsUnderEvaluation(false, getDataTupleFilter());
			}
			
//			System.err.println("QueryPoolElement has rewinded to state " +
//					getCurrentBox().showState());
		}
	}
}

