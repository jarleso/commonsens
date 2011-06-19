package eventProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import language.AtomicQuery;
import modelViewController.Core;
import modelViewController.MainView;

/**
 * This class is responsible for selecting the sensors that are relevant
 * for the current query. It also checks if the timing of the queries
 * is correct.
 * 
 * @author jarleso
 *
 */

public class DataTupleFilter {

	private Timer globalTime;
    private QueryPoolElement queryPoolElement;
//    private HashMap<String, HashSet<String>> sensors;
//    private String outPrefix;
//    private ConcurrentLinkedQueue<DataTuple> dataTupleQueue;
    private boolean isStrict;
//    private int numberOfTupleSourcesToPull = 0;
    // Timing issues.
    private boolean isMin;
    private double percentage;
    private Timestamp begin;
    private Timestamp end;
    // Messages from the states.
    public static final int M_FINISHED = 0;
    public static final int M_SATISFIED = 1;
    public static final int M_STRICTNESS_DEVIATION = 2;
    public static final int M_TIMING_DEVIATION = 3;
    public static final int M_UNKNOWN = 4;
    public static final int M_CONTINUE = 5;
    public static final int M_NO_TRIGGER = 6;
    public static final int M_QUERY_TIMING_DEVIATION = 8;
    public static final int M_CONCURRENCY_DEVIATION = 7;
    public static final int M_UNCERTAIN = 9;
	public static final int M_REWIND_FIRST = 10;
    public static final int M_REWIND_SECOND = 11;
	public static final int M_REWIND_BOTH = 12;
    int endState = M_NO_TRIGGER;
    int matchesCounter = 0;
    int tuplesCounter = 0;
    int numberOfTimeStepsMatched = 0;
    private boolean filterFinished = false;
    private boolean isUnderEvaluation = false;

    Box prevState = null;

    public DataTupleFilter(QueryPoolElement queryPoolElement,
            ConcurrentLinkedQueue<DataTuple> cepQueue,
            String outPrefix, Timer globalTime, int runNumber,
            int numSensors, int numAnds) {

        this.queryPoolElement = queryPoolElement;
//        this.cepQueue = dataTupleQueue;
//        this.outPrefix = outPrefix;
        this.globalTime = globalTime;

//        sensors = new HashMap<String, HashSet<String>>();        

        setBegin(new Timestamp(-1));
        setEnd(new Timestamp(-1));
        
    }

    /**
     * Used for cases where the whole complex statement has been tagged with
     * a timer and P-registration.
     *
     * @param begin
     * @param end
     * @param isMin
     * @param percentage
     */
    public void setTiming(Timestamp begin, Timestamp end, boolean isMin,
            double percentage) {

        this.begin = begin;
        this.end = end;
        this.isMin = isMin;
        this.percentage = percentage;
    }

    /**
     * Sets the next state in the state machine. If there is a message it
     * means that there is a deviation. 
     * 
     * @param currBox
     * @param message
     */

    private void updateFilter(Box currState, int message) {

        //System.err.println("updateFilter with msg: " + message);

        satisYet = false; // EiMM10-things.
        
        if (message == DataTupleFilter.M_QUERY_TIMING_DEVIATION) {

        	/**
        	 * The query has had a timing deviation.
        	 */
        	
            doPaperwork(message);
        	
    	} else if (message == DataTupleFilter.M_TIMING_DEVIATION) {
        	
            
            /** 
             * Check all the AndOperator chains. If all of them are deviations, the
             * state machine goes into a deviation state.
             */

            for (AndChainCalculations tmpCal : currState.getAndChains()) {

                if (tmpCal.getCurrCondition() != 
                	DataTupleFilter.M_TIMING_DEVIATION &&
                        tmpCal.getIsUnderEvaluation()) {

//                    System.err.println("returning since " +
//                    		"tmpCal.getCurrCondition() = "
//                            + tmpCal.getCurrCondition() + 
//                            " and tmpCal.getIsUnderEvaluation() = " +
//                            tmpCal.getIsUnderEvaluation());

                    /** 
                     * One of the statements are still under evaluation or not
                     * yet evaluated. Hence, we can not assume that there is a
                     * timing deviation.
                     */

                    return;
                } else {
//                	System.err.println("condition " + 
//                			tmpCal.getCurrCondition());
                }
            }
            
            doPaperwork(message);

        } else if (message == DataTupleFilter.M_STRICTNESS_DEVIATION) {

            for (AndChainCalculations tmpCal : currState.getAndChains()) {

                if (tmpCal.getCurrCondition() != 
                	DataTupleFilter.M_STRICTNESS_DEVIATION &&
                        tmpCal.getIsUnderEvaluation()) {

                    // One of the statements are still under evaluation or not
                    // yet evaluated. Hence, we can not assume that there is a
                    // strictness deviation.

                    return;
                }
            }

            doPaperwork(message);

        } else if (message == DataTupleFilter.M_CONCURRENCY_DEVIATION) {

            for (AndChainCalculations tmpCal : currState.getAndChains()) {

                if (tmpCal.getCurrCondition() != DataTupleFilter.
                		M_CONCURRENCY_DEVIATION
                        &&
                        tmpCal.getIsUnderEvaluation()) {

                    // One of the statements are still under evaluation or not
                    // yet evaluated. Hence, we can not assume that there is a
                    // concurrency deviation.

                    return;
                }
            }

            doPaperwork(message);

        } else {

            //System.err.println("updateSigmaFilter with message " + message);

            /**
             * Everything's ok. Time to change state. Investigate the
             * transition relation.
             */
            Transition trans = currState.getTransition();

            if (trans != null) {

                setEndState(message);

                if (trans.getIsStrict()) {

                    isStrict = true;

                } else {
                    //System.err.println("Transition is loose.");
                }

                prevState = queryPoolElement.setCurrentBox(trans.getNextBox(),
                		message);

//                System.err.println("new state: " + 
//                		queryPoolElement.getCurrentState().showState() + "\n");

            } else {

//                System.err.println("This part of the statement is done.");

                filterFinished = true;
                
                prevState = queryPoolElement.setCurrentBox(null,
                		message);
                
                /**
                 * Since there is no transition relation, this is the last 
                 * state and the evaluation is finished. 
                 */
                
                setEndState(DataTupleFilter.M_FINISHED);

            }
        }
    }

    /**
     * Finds out which tuple sources to pull.
     *
     * @param currentTime
     * @param numSensors
     */
    public HashMap<String, String> getSensorsToPull(/*long currentTime, 
    									int numSensors*/) {

        currBox = queryPoolElement.getCurrentBox();

        if (currBox != null) {

//        	System.err.println("DataTupleFilter.getTupleSourcesToPull: " +
//        			"currBox != null");
        	
            // currBox is always the state whose transitions need
            // to be evaluated. If it is timed and we have not reached
            // the time, don't bother pulling.

            HashMap<String, String> toPull = getSensorsToPull(prevState, 
            		globalTime.getCurrentMillisecondRounded(), isStrict);

//            numberOfTupleSourcesToPull = toPull.size(); // For statistics.

            return toPull;

        } else /* currBox == null */ {

            /**
             * Do not perform anything. When the currBox is null, it
             * might be that a deviation has occurred.
             */
//            System.err.println("DataTupleFilter.getTupleSourcesToPull: currBox == null");

        }

        return new HashMap<String, String>();
    }

    /**
     * Receives a batch with data tuples. Evaluates the current state of
     * the current state machine.
     *
     * @param batch
     * @return
     */
    
    public int getBatch(ArrayList<DataTuple> batch) {

        /**
         * Evaluate batch and the current state to see whether it is satisfied 
         * or not.
         */

        if (queryPoolElement.getCurrentBox() instanceof TimedChainOfStatesBox) {

//            System.err.println("The current state is TimedChainOfStatesBox");

            handleTimedChainOfState((TimedChainOfStatesBox) queryPoolElement.
            		getCurrentBox(),
                    batch);

        } else if(queryPoolElement.getCurrentBox() instanceof ConcurrencyBox) {

            /**
             * For each of the two chains in the concurrency state, send
             * the data tuples that match the chain to the chain.
             */

//            System.err.println("The current state is ConcurrencyBox");

//            if(!batch.isEmpty()) {
//                System.err.println("batch.size() = " + batch.size());
//            }

            handleConcurrencyState((ConcurrencyBox) queryPoolElement.
            		getCurrentBox(),
                    batch);

        } else {

//            System.err.println("The current state is evaluated in this" + 
//            		" state with filterFinished = " + filterFinished);

            evaluateBox(evaluateBatch(batch));
        }

        if (filterFinished) {

            /**
             * This might not be correct given the current configuration.
             */

            //queryPoolElement.getCore().stopAll(getEndState());

//            System.err.println("Experiment is finished, returning " +
//            		"endState = " + getEndState());

            return getEndState();
        }

        // Ohoh.. Might not work!
        
        return getEndState();//DataTupleFilter.M_CONTINUE;
    }

    /**
     * Checks the state machine and evaluates the current state given the
     * batch.
     *
     * @param batch
     * @return
     */
    private int evaluateBatch(ArrayList<DataTuple> batch) {

        int returnStatement = QueryPoolElement.ERROR;

        if (queryPoolElement.getCurrentBox() != null) {
            returnStatement = queryPoolElement.getCurrentBox().
            evaluateBatch(batch, this);
        }

        return returnStatement;
    }

    long howMuchTimeUsed;
    double timeUsedPercentage;
    double withMatchRate;
    AndChainCalculations currAndChain;
    int size = 0;
    int evalStateIt = 0;
    AtomicQuery tmpStmt;
    private long currTimeState;
    Box currBox;
	private boolean satisYet;
    
    /**
     * Denotes whether or not to use the window semantic that jumps to the
     * next state at once the event is satisfied in a window. 
     */
    
	private static boolean NEW_WINDOW_SEMANTICS = false;

    /**
     * Evaluates the state. This is called every timestamp. The method does
     * the following:
     *
     * 1. Checks if the evaluation of the state has started.
     * 2. If not started, checks the timing to see if the state is timed. If
     *    it is, check if there are any violations.
     * 3. Checks if the state has been satisfied. If the state has been 
     * 	  satisfied,
     *    the next state is in line. If the transition is =>, also fill DevRep.
     * 4. If has not been satisfied, it evaluates the timing and P-registration.
     */

    private void evaluateBox(int evaluation) {

//    	System.err.println("evaluateState with evaluation = " + 
//    			evaluation);
    	
        currTimeState = globalTime.getCurrentMillisecond(Core.Hz);

        currBox = queryPoolElement.getCurrentBox();

//        System.err.println("evaluateState with currBox = " + currBox);
        
        if (currBox != null && evaluation !=
                DataTupleFilter.M_STRICTNESS_DEVIATION) {

            /** 
             * Check the timing of the complex statement. If the complex 
             * statement is timed, check if the conditions have been met. 
             * If not, it means that a deviation has been made. 
             */
        	
        	//System.err.println("isDeltaTimed() = " + isDeltaTimed() + ", getIsUnderEvaluation() = " + getIsUnderEvaluation());
        	
            if (isDeltaTimed() && getIsUnderEvaluation()) {

//            	System.err.println("Setting timing for the DataTupleFilter.");
            	
                // Set the timing of the state.

                end = new Timestamp(currTimeState
                        + begin.getTimestamp());

                begin = new Timestamp(currTimeState);

            }

            /**
             * Set how much time is used for this round.
             */
            
            howMuchTimeUsed = currTimeState - begin.getTimestamp();
            
            /**
             * Note that if the query is delta timed and the evaluation
             * had started, the query turns timed, since the begin and
             * end timestamps now are set. 
             */
            
            if (isTimed()) {

//                System.err.println("Complex statement is timed.");

                if (currTimeState < begin.getTimestamp()
                        && getIsUnderEvaluation()) {

//                    System.err.println("Before time window and is under " +
//                    		"evaluation. ERROR!");

                	updateFilter(currBox, DataTupleFilter.
                			M_QUERY_TIMING_DEVIATION);
                    
                } else if (currTimeState >= begin.getTimestamp()
                        && currTimeState < end.getTimestamp()
                        && getIsUnderEvaluation()) {

//                    System.err.println("In time window and everything is " +
//                    		"ok.");
                } else if (currTimeState >= end.getTimestamp() &&
                		getIsUnderEvaluation()) {
                	
//                	System.err.println("After time window, and still under " +
//                			"evaluation. ERROR!");
                	
                	updateFilter(currBox, DataTupleFilter.
                			M_QUERY_TIMING_DEVIATION);
                }
            } // Check if timed.

            // Investigate the current state.

//            System.err.println("Investigates the current state.");

            /** 
             * All the atomic statements divided have to be evaluated if they
             * are under evaluation.
             */

            size = currBox.getAndChains().size();

            for (evalStateIt = 0; evalStateIt < size; evalStateIt++) {

                currAndChain = currBox.getAndChains().get(evalStateIt);

                tmpStmt = currAndChain.getReprStatement();
                
                /**
                 * If the query is delta-timed, the current time has
                 * exceeded beyond the deadline, and the query is not
                 * under evaluation, this is regarded
                 */
              
                if (currAndChain.isDeltaTimed() && 
                		!currAndChain.getIsUnderEvaluation() &&
                		currAndChain.getDeadline() != null &&
                		currTimeState > currAndChain.getDeadline().
                			getTimestamp()) {
                
                	// Timing deviation.
                	
                	currAndChain.setCurrCondition(DataTupleFilter.
                    		M_TIMING_DEVIATION);

                    updateFilter(currBox, DataTupleFilter.
                    		M_TIMING_DEVIATION);
                	
                } else if (tmpStmt.isTimed()) {

//                    System.err.println("Current chain ( " + evalStateIt + 
//                    		" ) currTimeState = " + currTimeState);
//                    System.err.println("currAndChain.getIsUnderEvaluation() " +
//                    		"= " + currAndChain.getIsUnderEvaluation());
//                    System.err.println("tmpStmt.getBegin().getTimestamp() = " +
//                    		tmpStmt.getBegin().getTimestamp());
//                    System.err.println("tmpStmt.getEnd().getTimestamp() = " +
//                    		tmpStmt.getEnd().getTimestamp());
//                    System.err.println("currAndChain.getIsUnderEvaluation() = "
//                    		+ currAndChain.getIsUnderEvaluation());
                                        
                    if (currTimeState >= tmpStmt.getBegin().getTimestamp()
                            && currTimeState < tmpStmt.getEnd().getTimestamp()
                            && currAndChain.getIsUnderEvaluation()) {

//                        System.err.println("Time is inside current state...");

                        /**
                         * The timing is ok.
                         * Check P-registration. If the percentage is satisfied, 
                         * set
                         * the state to satisfied and investigate next state.
                         */

                        // How much time of the interval has been used.
                        howMuchTimeUsed = currTimeState -
                                tmpStmt.getBegin().getTimestamp();

//                        System.err.println("howMuchTimeUsed = " + 
//                        		howMuchTimeUsed);
                        
                        /**
                         * Check how much percentage this time is out of total
                         * time window.
                         */

//                        System.err.println("currBox.getPercentage() = " + 
//                        		tmpStmt.getPercentage() +
//                                " against " + 
//                                (currAndChain.getMatchRate(howMuchTimeUsed) * 
//                                		100));
//
//                        System.err.println("\tReal ratio0: " + 
//                        		currAndChain.getMatchRate(-1));
//                        System.err.println("\tReal time usage in this window: " 
//                        		+ howMuchTimeUsed +
//                                " (" + ((double)howMuchTimeUsed/
//                                		(double)tmpStmt.getTimeWindow()) + ")");

                        if (tmpStmt.getIsMin()) {

                            if (tmpStmt.getPercentage() <= 
                            	currAndChain.getMatchRate(-1)) {
//                            	(currAndChain.getMatchRate(howMuchTimeUsed) * 
//                            			100)) {

                                /**
                                 * Ok. Write information that the percentage
                                 * is satisfied to file.
                                 * 
                                 */
                            	
                            	if(!satisYet) {
	                            	writeStateChangesFile(currTimeState +
	                                		"\t1\t" + M_SATISFIED + "\n");
                            	
	                            	satisYet = true;
                            	}
//                                System.err.println("min is satisfied.");

                                /**
                                 * I am uncertain of the semantics of the
                                 * windows. For so long, we decide by
                                 * using a variable.
                                 */
                                
                                if ( NEW_WINDOW_SEMANTICS ) {
                                
	                                currAndChain.
	                                setCurrCondition(DataTupleFilter.
	                                		M_CONTINUE);
	
	                                updateFilter(currBox, DataTupleFilter.
	                                		M_CONTINUE);
                                }
                            } else {
                                /** 
                                 * The minimum amount of correct readings are 
                                 * not yet fulfilled. Continue evaluation. But, 
                                 * set the endState to M_CONTINUE so to tell
                                 * the callers that the evaluation has started.
                                 */ 
                            	
                            	setEndState(DataTupleFilter.M_CONTINUE);
                            }
                        } else /* max */ {

//                            System.err.println("is max.");

                            if (tmpStmt.getPercentage() > 
                            (currAndChain.getMatchRate(howMuchTimeUsed) * 
                            		100)) {

//                                System.err.println("max still not voilated");

                            	/**
                            	 * Ok. But, a deviation based on violation of 
                            	 * max can not be detected before the window 
                            	 * time has been reached.
                            	 */

                            	// updateFilter(currBox, null);
                            } else {

//                                System.err.println("max is voilated");

                            	/**
                            	 * The minimum amount of correct readings are 
                            	 * not yet fulfilled. Continue evaluation.
                            	 */
                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_TIMING_DEVIATION);

                                updateFilter(currBox, DataTupleFilter.
                                		M_TIMING_DEVIATION);

                            }
                        }

                    } 
                
                    /**
                     * Evaluate the state after the time window is 
                     * finished. This applies to the situations where the 
                     * time stamp is equal or larger than the end timestamp
                     * of the statement. However, the state should also be
                     * evaluated if the next timestamp (timestamp + sensor's
                     * samplingFrequency value) is equal to the end timestamp. This means 
                     * that the current samplingFrequency value has to be evaluated.
                     * 
                     * Divide 1000 by the samplingFrequency to adjust for the samplingFrequency.
                     */
                    
                	if (currTimeState >= (tmpStmt.getEnd().getTimestamp() -
                			(1000 / Core.Hz))
                            && currAndChain.getIsUnderEvaluation()) {
                		
//                        System.err.println("currTime is larger than the" +
//                        		" timestamps of the current state" + 
//                        		" but the state is under evaluation");

                        /**
                         * Time has passed beyond the state's end time. Since 
                         * the state is still under evaluation, it can mean that 
                         * the state is still not satisfied. Either, the minimum 
                         * percentage has not been fulfilled, which means that 
                         * there might be a deviation.
                         *
                         * Another alternative is that it is a maximum 
                         * percentage that should be evaluated.
                         *
                         */
                        if (tmpStmt.getIsMin()) {

//                        	System.err.println("tmpStmt.getPercentage() = " +
//                        			tmpStmt.getPercentage());
//                        	System.err.println("currAndChain." +
//                        			"getMatchRate(" + howMuchTimeUsed + 
//                        			") * 100 = " +
//                        			currAndChain.
//                        			getMatchRate(howMuchTimeUsed) * 
//                        			100);
                        	
                            if (tmpStmt.getPercentage() <=
                            	currAndChain.getMatchRate(-1)) {
//                            	(currAndChain.getMatchRate(howMuchTimeUsed) * 
//                            			100)) {

                                // Ok.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_CONTINUE);

                                updateFilter(currBox, DataTupleFilter.
                                		M_CONTINUE);
                            } else {

                                /** 
                                 * Possibly a deviation. Note that for it to
                                 * be a deviation, all the AndOperator chains have to
                                 * be a deviation. Otherwise, one of the chains
                                 * matches the conditions and everything is 
                                 * fine.  
                                 */

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_TIMING_DEVIATION);

                                updateFilter(currBox, DataTupleFilter.
                                		M_TIMING_DEVIATION);
                            }

                        } else /* is max */ {

                            if (currAndChain.getMatchRate(-1) < 
                            		tmpStmt.getPercentage()) {

                                // Ok.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_CONTINUE);

                                updateFilter(currBox, DataTupleFilter.
                                		M_CONTINUE);
                            } else {

                                // Deviation.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_TIMING_DEVIATION);

                                updateFilter(currBox, DataTupleFilter.
                                		M_TIMING_DEVIATION);
                            }
                        }
                    } else if (currTimeState >= tmpStmt.getEnd().getTimestamp()
                            && !currAndChain.getIsUnderEvaluation()) {

//                        System.err.println("currTime is larger than the " +
//                        		"timestamps of the current state and " +
//                        		"the state is NotOperator under evaluation");

                        /**
                         * Time has passed beyond the state's end time and the
                         * evaluation has not started.
                         *
                         */
                        if (tmpStmt.getIsMin()) {

                            if (currAndChain.getMatchRate(-1) >= 
                            	tmpStmt.getPercentage()) {

                                // Ok, when percentage is 0%.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_CONTINUE);

                                updateFilter(currBox, DataTupleFilter.
                                		M_CONTINUE);
                            } else {

                                // Deviation.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_TIMING_DEVIATION);

                                updateFilter(currBox, DataTupleFilter.
                                		M_TIMING_DEVIATION);
                            }

                        } else /* is max */ {

                            if (currAndChain.getMatchRate(-1) < 
                            		tmpStmt.getPercentage()) {

                                /**
                                 * Ok. The semantics of max is that if there is
                                 * no match even though the statement is timed,
                                 * this does not cause a deviation.
                                 */

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_CONTINUE);

                                updateFilter(currBox, DataTupleFilter.
                                		M_CONTINUE);
                            } else {

                                // Deviation.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_TIMING_DEVIATION);

                                updateFilter(currBox, DataTupleFilter.
                                		M_TIMING_DEVIATION);
                            }
                        }

                    } else if (currTimeState >= tmpStmt.getBegin().
                    		getTimestamp() && 
                    		!currAndChain.getIsUnderEvaluation()) {

//                        System.err.println("currAndChain.getMatchRate() = " 
//                    		+ currAndChain.getMatchRate(-1));
//                        System.err.println("tmpStmt.getPercentage() = " + 
//                    		tmpStmt.getPercentage());

                        if (tmpStmt.getIsMin()) {

                            if (currAndChain.getMatchRate(-1) >= 
                            	tmpStmt.getPercentage()) {

                                /**
                                 * Ok if percentage is set to 0%, but pretty
                                 * strange to get here...
                                 */
                            	
                            	writeStateChangesFile(currTimeState +
                                		"\t1\t" + M_SATISFIED + "\n");
                            	
                            	if(NEW_WINDOW_SEMANTICS ) {
                            	
	                                currAndChain.setCurrCondition(
	                                		DataTupleFilter.
	                                		M_CONTINUE);
	
	                                updateFilter(currBox, DataTupleFilter.
	                                		M_CONTINUE);
                            	}
                            } else {

                                /**
                                 * Do nothing. Even though the timing is 
                                 * not yet satisfied, we do not know whether
                                 * the min is not satisfied. 
                                 */ 

//                            	System.err.println("evalState: min voilated");
                            	
//                                currAndChain.setCurrCondition(DataTupleFilter.
//                                		M_TIMING_DEVIATION);
//
//                                updateFilter(currBox, DataTupleFilter.
//                                		M_TIMING_DEVIATION);
                            }

                        } else /* is max */ {

                            if (currAndChain.getMatchRate(-1) < 
                            		tmpStmt.getPercentage()) {

                                // Ok.

                                /**
                                 * We do not update the filter, since it might 
                                 * still be so that the statement is evaluated 
                                 * and that the match rate increases above 
                                 * tmpStmt.getPercentage().
                                 */

                                // updateFilter(currBox, null);
                            } else {

                                // Deviation.

                                currAndChain.setCurrCondition(DataTupleFilter.
                                		M_TIMING_DEVIATION);

                                updateFilter(currBox, DataTupleFilter.
                                		M_TIMING_DEVIATION);
                            }
                        }
                    }
                } else /* NotOperator timed (i.e., timestamps in the statement
                	are not set.) */ {

                	/**
                	 * The meaning of the non-timed queries has been
                	 * changed. However, currently we have not implemented 
                	 * the change. 
                	 * 
                	 * But, for concurrent queries there is a slight change. 
                	 * The five classes behave differently and the behavior
                	 * is defined in the corresponding ConcurrencyBox 
                	 * subclass. 
                	 * 
                	 */
                	
                    if (currAndChain.getIsUnderEvaluation()) {
                    	
//                    	System.err.println("but it is underEvaluation...");
                    	
                        /**
                         * There has been a match. Now check with the next state
                         * and transition.
                         */
                        currAndChain.setCurrCondition(DataTupleFilter.
                        		M_CONTINUE);

                        updateFilter(currBox, DataTupleFilter.
                        		M_CONTINUE);
                    }
                }
            }
        } else if (evaluation == DataTupleFilter.M_STRICTNESS_DEVIATION) {

            currAndChain.setCurrCondition(DataTupleFilter.
            		M_STRICTNESS_DEVIATION);

            updateFilter(currBox, DataTupleFilter.M_STRICTNESS_DEVIATION);

        } else {
        	
//        	System.err.println("DataTupleFilter.evaluateState: The current " +
//        			"state is empty.");
        	
            // The current state is empty. 
        }

//        System.err.println("end of evaluateState with endState = " + 
//        		getEndState());
    }

    private void writeStateChangesFile(String string) {

    	queryPoolElement.getCore().getMainFilter().writeStateChangesFile(string);
    }

	/**
     * 
     * @return
     */
    
    String getTiming() {

        return begin.getTimestamp() + " " + end.getTimestamp() + " "
                + " " + (isMin ? "min" : "max") + " " + percentage;

    }

    /**
     * 
     * @return
     */
    
    private boolean isTimed() {
        return begin.getTimestamp() != -1
                && end.getTimestamp() != -1;
    }

    /**
     * Is updated by each state and every data tuple that is evaluated.
     *
     * @param newMatches
     */
    public void updateMatchesCounter(int newMatches) {
        matchesCounter += newMatches;
    }

    public void updateTupleCounter(int newTuples) {
        tuplesCounter += newTuples;
    }

    void updateNumberOfTimeStepsMatched(int i) {
        numberOfTimeStepsMatched += i;
    }

    //
    int getNumberOfTimeStepsMatched() {
        return numberOfTimeStepsMatched;
    }

    @SuppressWarnings("unused")
	private double getMatchRate() {

        return (((double) matchesCounter) / ((double) tuplesCounter));
    }

    @SuppressWarnings("unused")
	private boolean getIsMin() {
        return isMin;
    }

    @SuppressWarnings("unused")
	private double getPercentage() {
        return percentage;
    }

    public boolean isDeltaTimed() {
    	
    	//System.err.println("In isDeltaTimed: begin.getTimestamp() = " + begin.getTimestamp() + ", end.getTimestamp() = " + end.getTimestamp());
    	
        return begin.getTimestamp() != -1 && end.getTimestamp() == -1;
    }

    private boolean getIsUnderEvaluation() {
        return isUnderEvaluation;
    }

    public void setIsUnderEvalutaion(boolean what) {

        queryPoolElement.getCore().getPanel().setIsRunning();

        queryPoolElement.getCore().getMainFilter().writeStateChangesFile(
        		queryPoolElement.getCore().getMainClass().getTimer().
        		getCurrentMillisecondRounded() +
        		"\t1\t5\n");
        
        //System.err.println("DataTupleFilter is under evaluation.");
        
        this.isUnderEvaluation = what;
    }

    double getTimeWindow() {

        if (isTimed()) {
            return end.getTimestamp() - begin.getTimestamp();
        }

        return -1;
    }

    /**
     * Returns the end state of the system. By default, the end message
     * is set to NO_TRIGGER. However, if the movement pattern is too short, it
     * might sometimes be so that the statement evaluation is not finished.
     * Therefore, the function has to investigate whether there is a
     * consistency between endState and the states. If endState == NO_TRIGGER
     * and currBox.getIsUnderEvaluation() == true, the movement pattern
     * has ended before a final state could be made. In our system this
     * interpreted as an M_UNKNOWN.
     *
     * It might also be so that the current state is null. This is due to
     * situations where the state machine is part of a timed chain.
     *
     * @return
     */
    public int getEndMessage() {

        if (getEndState() == DataTupleFilter.M_NO_TRIGGER) {

//            System.err.println("SigmaFilter.getEndMessage() " +
//            		"queryPoolElement = " + queryPoolElement);

            if (queryPoolElement.getCurrentBox() != null) {

                for (AndChainCalculations tmpChain : queryPoolElement.
                		getCurrentBox().getAndChains()) {
                    if (tmpChain.getIsUnderEvaluation()) {
                        return DataTupleFilter.M_UNKNOWN;
                    }
                }
            }
        }
        
        return getEndState();
    }

    public void setEndState(int newMessage) {

//        System.err.println("Setting intermediate endState from " +
//                endState + " to " + newMessage);

        endState = newMessage;
    }

    private int getEndState() {
        return endState;
    }

    /**
     * If currBox is a concurrency state, the method investigates the two
     * filters for the two state machines. If the current state is an ordinary
     * state, the tuple sources for the state machine are pulled.
     *
     * @param prevState
     * @param currentTime
     * @param strict
     * @return
     */

    HashMap<String, String> getSensorsToPull(Box prevState, 
    		long currentTime, boolean strict) {

//        System.err.println("Inside getTupleSourcesToPull in DataTupleFilter.");

        currBox = queryPoolElement.getCurrentBox();
        
//        System.err.println("currBox = " + currBox);

        if (currBox != null) {
            return currBox.getSensorsToPull(prevState, currentTime, 
            		isStrict);
        }

        return new HashMap<String, String>();
    }

    private void setBegin(Timestamp timestamp) {
        begin = timestamp;
    }

    private void setEnd(Timestamp timestamp) {
        end = timestamp;
    }

    /**
     * Sends the batch to the part of the state machine that considers the
     * timed chains. If the chain is empty is means that that part of the
     * state machine is finished, and the system continues to evaluate
     * the current part of the state machine.
     *
     * @param timedChainOfStatesBox
     * @param batch
     */

    private void handleTimedChainOfState(TimedChainOfStatesBox timedChainOfStatesBox,
            ArrayList<DataTuple> batch) {

        timedChainOfStatesBox.getTheStateMachine().getDataTupleFilter().
                getBatch(batch);

        if (timedChainOfStatesBox
                .getTheStateMachine().getCurrentBox() != null) {

//        	System.err.println("timedChainOfStates sets end state to " + 
//        			timedChainOfStatesBox
//                    .getTheStateMachine().getDataTupleFilter().getEndMessage());
        	
            setEndState(timedChainOfStatesBox
                .getTheStateMachine().getDataTupleFilter().getEndMessage());

        } else {

            /**
             * The state machine in the timed chain of states is
             * finished. Transist into the next state.
             */

            updateFilter(currBox, timedChainOfStatesBox
                .getTheStateMachine().getDataTupleFilter().getEndMessage());
        }
    }

    /**
     * Finds out which of the data tuples the batch that are supposed to go
     * to which of the chains.
     *
     * @param concurrencyState
     * @param batch
     */

    ArrayList<DataTuple> batchToSend = new ArrayList<DataTuple>();
    int firstMachineResult, secondMachineResult, concurrencyResult;

    private void handleConcurrencyState(ConcurrencyBox concurrencyBox,
            ArrayList<DataTuple> batch) {

//        System.err.println("DataTupleFilter.handleConcurrencyState: " +
//        		"batch received");

//        for(DataTuple tmpTup : batch) {
////            System.err.println("\t" + tmpTup.showTuple());
//        }

        batchToSend.clear();

        HashMap<String, String> toPull;

        toPull = concurrencyBox.getFirstBoxList().getDataTupleFilter().
                getSensorsToPull();

//        System.err.println("DataTupleFilter.handleConcurrencyState: " +
//        		"toPull for first machine:");

//        Iterator<String> it = toPull.keySet().iterator();

//        while(it.hasNext()) {
//            System.err.println("\t" + it.next());
//        }

        for(DataTuple tmpTuple : batch) {

            if(toPull.containsKey(tmpTuple.getSensorName())) {

                batchToSend.add(tmpTuple);
            }
        }

//        System.err.println("DataTupleFilter.handleConcurrencyState: " +
//        		"Sending following batch to first machine:");

//        for(DataTuple tmpTup : batchToSend) {
//            System.err.println("\t" + tmpTup.showTuple());
//        }

        firstMachineResult = concurrencyBox.getFirstBoxList().
                getDataTupleFilter().
                getBatch(batchToSend);

//        System.err.println("firstMachineResult = " + firstMachineResult);
        
        batchToSend.clear();

        toPull = concurrencyBox.getSecondBoxList().getDataTupleFilter().
                getSensorsToPull();

//        System.err.println("DataTupleFilter.handleConcurrencyState: toPull " +
//        		"for second machine:");

//        it = toPull.keySet().iterator();
//
//        while(it.hasNext()) {
//            System.err.println("\t" + it.next());
//        }

        for(DataTuple tmpTuple : batch) {

            if(toPull.containsKey(tmpTuple.getSensorName())) {

                batchToSend.add(tmpTuple);
            }
        }

//        System.err.println("DataTupleFilter.handleConcurrencyState: " +
//        		"Sending following batch to second machine:");

//        for(DataTuple tmpTup : batchToSend) {
//            System.err.println("\t" + tmpTup.showTuple());
//        }

        secondMachineResult = concurrencyBox.getSecondBoxList().
                getDataTupleFilter().
                getBatch(batchToSend);

//        System.err.println("secondMachineResult = " + secondMachineResult);        
        
        concurrencyResult = concurrencyBox.evaluateResults(firstMachineResult,
                secondMachineResult);
        
//        System.err.println("handleConcurrencyState() concurrencyResult = " +
//                concurrencyResult);

        if(concurrencyResult != DataTupleFilter.M_CONCURRENCY_DEVIATION) {

        	if(concurrencyResult == DataTupleFilter.M_REWIND_FIRST) {
        		
        		/**
        		 * Rewind first chain and continue evaluation.
        		 */
        		
        		concurrencyBox.getFirstBoxList().rewind(true);        		
        	}

        	if(concurrencyResult == DataTupleFilter.M_REWIND_SECOND) {
        		
        		/**
        		 * Rewind second chain and continue evaluation.
        		 */      		
        		
        		concurrencyBox.getSecondBoxList().rewind(false);
        	}
        	
        	if(concurrencyResult == DataTupleFilter.M_REWIND_BOTH) {
        		
//        		System.err.println("Rewinds both machines.");
        		
        		concurrencyBox.getFirstBoxList().rewind(true);
        		concurrencyBox.getSecondBoxList().rewind(false);
        	}
        	
            if(concurrencyResult == DataTupleFilter.M_FINISHED) {

                /**
                 * The state machine in the two chains of states has
                 * finished correctly. Perform a transition into the 
                 * next state.
                 */

                updateFilter(currBox, DataTupleFilter.M_CONTINUE);
                
            } else if (concurrencyResult == DataTupleFilter.M_CONTINUE) {

                /**
                 * The state machine for the two chains of states
                 * is not yet finished. Do not update this filter.
                 */
            }

//            }
        } else {

            updateFilter(currBox, DataTupleFilter.M_CONCURRENCY_DEVIATION);
        }
    }

    /**
     * Method is called when a deviation has been detected.
     * 
     * Changes the GUI with the new message and jumps to a null state with
     * the message.
     * 
     * @param message
     */
    
    private void doPaperwork(int message) {

    	System.err.println("in doPaperwork with message " + message);
    	
        filterFinished = true;

        setEndState(message);
        
        queryPoolElement.showEndState(MainView.printEndMessage(message));
        prevState = queryPoolElement.setCurrentBox(null, message);
    }

    /**
     * Tells whether this filter is finished.
     *
     * @return
     */

    public boolean isFinished() {
        return filterFinished;
    }
    
    /**
     * Unfinishes the filter.
     */
    
    public void setFilterFinished(boolean val) {
    
    	filterFinished = val;
    }

	public Box getCurrState() {
		return currBox;
	}
}

