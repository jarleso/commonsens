/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.Shape;
import environment.Triple;
import environment.Environment;
import environment.LocationOfInterest;
import eventProcessor.DataTuple;
import eventProcessor.DataTupleFilter;
import eventProcessor.EventSet;
import eventProcessor.MonitoringRunner;
import eventProcessor.MainDataTupleFilter;
import eventProcessor.QueryPoolElement;
import eventProcessor.Timer;
import eventProcessor.Timestamp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import language.AtomicQuery;
import language.ConcurrencyOperator;
import language.ConsecutiveRelation;
import language.QueryElement;
import language.Strict;
import language.TimedListOfAtomicQueries;
import sensing.Capability;
import sensing.EmulatedTupleSource;
import sensing.PhysicalSensor;
import sensing.Sensor;
import statistics.PlotCreator;

/**
 *
 * @author jarleso
 */
public class Core {

    public static int SPEED = 100;
    private int stepsPerTimestamp = 1;//25;
    
    /**
     * The sample rate of pulling the sensors. Currently a
     * static variable.
     */
    
    public static int Hz = 1;

    private CommonSens mainClass;
    private MainView panel;

    private String expPrefix;;

    private MainDataTupleFilter mainFilter = null;
    private boolean firstStep = true;
    private EnvironmentCreator environmentCreator;
    private boolean environmentCreatorOn;

    private ObjectCreator objectCreator;
    private boolean objectCreatorOn;

    private SensorCreator physicalSensorCreator;
    private boolean sensorCreatorOn;

    private QueryElement tmpSt = null;

    private int simluationOption = -1;
    private long traceFileStartTime = -1;
    private long lastSimulationTimestamp = 0;
    private boolean isRunningExperiment = false;
    
    /** 
     * All the possible events.
     */
    
    private EventSet<DataTuple> E; 
    
    /**
     * All the current possible events for the complex queries.
     */
    
    //private EventSet<DataTuple> Ncq;
    
    /**
     * All the current possible correct events from the complex queries.
     */
    
    //private EventSet<DataTuple> Mcq;
    
    MonitoringRunner monitoringRunner = null; // Thread for running emulations.
	private QueryParser queryParser;

    public Core(CommonSens mainClass, MainView panel) {
        this.mainClass = mainClass;
        this.panel = panel;
        this.expPrefix = mainClass.getExpPrefix();
        
        this.E = new EventSet<DataTuple>();
        //this.Ncq = new EventSet<DataTuple>();
        //this.Mcq = new EventSet<DataTuple>();
    }

    public MainView getPanel() {
        return panel;
    }

    /**
     * Pulls all the sensors in the environment. The method is mainly for
     * debugging and is only valid if the EnvironmentCreator object is
     * not null.
     *
     * @param timeSeconds
     */

    public final void pullSensors(long timeMilliseconds) {

        if (environmentCreator != null) {

            for (Sensor sens : environmentCreator.getDrawPanel()
                    .getCurrEnv().getTupleSources()) {

                //System.err.println("Pulling " + sens.getName());

                sens.pullThisSensor(timeMilliseconds, false);
            }
        }
    }

    public final void setMovementCreatorOn(boolean movementCreatorOn,
    		MovementCreator movementCreator) {

        if (environmentCreatorOn) {

            environmentCreator.setMovementCreatorOn(movementCreatorOn);
            environmentCreator.setMovementCreator(movementCreator);
        }
    }

    public final void createMovementPattern() {
        if (environmentCreatorOn) {

            environmentCreator.setMovementCreatorOn(true);

            environmentCreator.setCurrPos(new ArrayList<Triple>());
        }
    }

    public final EnvironmentCreator getEnvironmentCreator() {
        return environmentCreator;
    }

    public final void setEnvironmentCreatorOn(boolean b) {
        environmentCreatorOn = b;
    }

    public final boolean getEnvironmentCreatorOn() {
        return environmentCreatorOn;
    }

    public final void setEnvironmentCreator(EnvironmentCreator 
    		environmentCreator) {
        this.environmentCreator = environmentCreator;
    }

    public final void readMovementFile(File selectedFile) {

        Scanner sc = null;
        try {
            sc = new Scanner(selectedFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainView.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        ArrayList<Triple> tmpList = mainClass.
                readMovementFromFile(sc, false);

        environmentCreator.getDrawPanel().getCurrEnv().setCurrPos(tmpList);

        Triple tmpTrip = tmpList.get(0);

        environmentCreator.getDrawPanel().getCurrEnv().getPersons().get(0).
                setStartPoint(tmpTrip.getX(), tmpTrip.getY(), tmpTrip.getZ());

        // Resetting

        firstStep = true;
    }

    public final void readEnvironmentFile(File selectedFile, int performance) {

        Scanner sc = null;
        try {
            sc = new Scanner(selectedFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainView.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        mainClass.getEnvironments().add(0, mainClass.
                readEnvironmentFromFile(sc, performance));

//        System.err.println("Environment = " + mainClass.getEnvironments().get(0).getEnvironment());
        
        setEnvironmentCreator(new EnvironmentCreator(this,
                mainClass.getEnvironments().get(0)));

        setEnvironmentCreatorOn(true);

    }

    /**
     * Reads a statement from file. numAnds is used for experiments to
     * emulate a given number of ANDs between a set of similar statements
     * (for the DEBS10 experiments).
     *
     * @param selectedFile
     * @param numAnds
     */
    public final void readStatementFile(File selectedFile, int numAnds) {

//    	System.err.println("reading statement with environment: " + mainClass.getEnvironments().get(0).getEnvironment());
    	
        Scanner in;

        // Only run the first statement for now:

        try {

            in = new Scanner(selectedFile);

            /**
             * Parse.
             */
            
            queryParser = new QueryParser(this, in, numAnds);

            if (!mainClass.getComplexStatements().isEmpty()) {
                tmpSt = mainClass.getComplexStatements().get(0);
            } else {
                System.err.println("Statement creation did not work.");
                return;
            }

            /**
             * Compile.
             */
            
            instantiateComplexStatement(tmpSt);

        } catch (IOException ex) {

            System.err.println("Exception!: " + ex.getMessage());
        }
    }


    public final boolean getFirstStep() {
        return firstStep;
    }

    public final void setFirstStep(boolean b) {
        firstStep = b;
    }

    public final void runMovement(boolean isExperiment) {

        if (getEnvironmentCreator().getDrawPanel() == null) {

            System.err.println("environmentCreator.getDrawPanel() == null");

        } else {
            getEnvironmentCreator().getDrawPanel().
                    runMovement(SPEED, isExperiment);
        }
    }

    public final boolean stepMovement(int numSteps) {

        if (getEnvironmentCreator() == null ||
                environmentCreator.getDrawPanel() == null) {

            System.err.println("environmentCreator.getDrawPanel() == null");

            return true;
        }

        return environmentCreator.getDrawPanel().stepMovement(numSteps);

    }

    public final void stopAll(int endState) {

        panel.changeCurrCond(MainView.printEndMessage(endState));

        getPanel().getJTextArea1().setText("Closing files");

        getPanel().getFrame().repaint();

//        try {
//            mainClass.writeToFiles();
//        } catch (IOException ex) {
//            Logger.getLogger(MainView.class.getName()).
//        		log(Level.SEVERE, null, ex);
//        }

        for (QueryPoolElement sm : mainClass.getQueryPool()) {
            sm.closeFile();

        }

        if(mainFilter != null) {
            mainFilter.closeFile();
        }

        panel.unsetIsRunning();
    }

    /**
     * For debugging. Lets you go through the movement and see how the
     * state machine behaves.
     *
     * @param sigmaFilter
     */
    public final void stepEventProcessing(MainDataTupleFilter mainFilter) {

        // Let the sigma filter empty the queue.

        if (mainFilter != null) {

            // Pull and evaluate the tuple sources for all the running
            // statements.

            mainFilter.pullAndEvaluate(mainClass
                    .getTimer().getCurrentMillisecondRounded(), 0, 0);
        }

        // Increase second for next

        mainClass.getTimer().increaseSecond();

        panel.showTime(mainClass.getTimer());

        if(environmentCreator != null) {
            environmentCreator.getDrawPanel().repaint();
        }

        if (getSimulationOption() == -1 &&
                stepMovement(1 /*stepsPerTimestamp*/) &&
                mainFilter != null) {

            MainView.printEndMessage(mainFilter.getEndMessage());
        } else {
//            System.err.println("stepMovement has returned false and " +
//            		"continues.");
        }
    }

    /**
     * The generic method that either runs the experiment in emulation mode
     * or simulation mode. The emulation mode has been slow, but for real
     * statements and real sensors, this mode should be used. It should work
     * fine, since real-world mode does not involve calculation of coverage
     * areas.
     *
     * @param monitoring
     * @param numRuns
     * @param numSensors
     * @param numAnds
     */
    public final RunResult startEventProcessing(boolean monitoring, 
    		int numRuns,
            int numSensors, int numAnds, int option, int runNumber,
            boolean reEval) {
    	
    	/**
    	 * Open a RunResult for experiment details.
    	 */
    	
    	RunResult tmpResult = new RunResult(); 
    	
    	/**
    	 * Save the current time, query name, query, and environment
    	 * name in a file. 
    	 */

    	try {
			saveExperimentInformation();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        if (monitoring) {

        	/**
        	 * Start a thread that runs the experiment.
        	 */
        	
            monitoringRunner = 
            	new MonitoringRunner(mainClass, panel, environmentCreator, 
            			this, monitoring,
            			numRuns, numSensors, numAnds, option,
            			runNumber, reEval);
        	
            Thread thr = new Thread(monitoringRunner);
            
            thr.start();
            
            tmpResult.setResult(0);
            
            return tmpResult;
        	
        } else {

            /**
             * For each run, a new tuple filter is created.
             */
            mainFilter = setupEventProcessingPhase(monitoring, runNumber, numSensors,
                    numAnds, option, reEval);

            if (mainFilter == null) {
            	
            	tmpResult.setResult(-1);
            	
                return tmpResult;
            }

            while (true) {

                if (mainFilter != null) {


                    // Pull and evaluate the tuple sources for all the running
                    // statements.

                    int retVal = mainFilter.pullAndEvaluate(mainClass.
                    		getTimer().getCurrentMillisecondRounded(), 0,
                            numSensors);

                    if (retVal == DataTupleFilter.M_FINISHED || 
                    		retVal == DataTupleFilter.M_TIMING_DEVIATION) {
                        break;
                    }
                }

                // Increase second for next run. This is done since it is
                // simulation mode.

                mainClass.getTimer().increaseSecond();

                panel.showTime(mainClass.getTimer());

                if(environmentCreator != null) {
                    environmentCreator.repaintDrawPanel();
                }

                /**
                 * Make sure that the simulation does not run infinitely.
                 * If getSimulationOption() > -1, it means that the system
                 * reads from a file and that it is not using the movement.
                 * Instead, the while-loop investigates the timestamp from
                 * the last entry of the trace file. If the current
                 * timestamp is greater than or equal to the last entry,
                 * the loop breaks. If not, the loop continues.
                 */

                if (getSimulationOption() > -1) {

//                    System.err.println("getMainClass().getTimer()." +
//                    		"getCurrentMillisecond() = " + 
//                    		getMainClass().getTimer().getCurrentMillisecond());
//                    System.err.println("getLastSimulationTimestamp() = " + 
//                    		getLastSimulationTimestamp());

                    if(mainClass.getTimer().getCurrentMillisecond()
                            > this.getLastSimulationTimestamp()) {

                        break;
                    }

                    System.err.println("Continuing!");

                    continue;
                } else {
                    if(stepMovement(stepsPerTimestamp) == true) {
                        break;
                    }
                }
            }

            mainClass.stopSensors();

            mainFilter.closeFile();

            MainView.printEndMessage(mainFilter.getEndMessage());
            
            /**
             * Put information in tmpResult
             */
            
            tmpResult.setEndTime(mainClass.getTimer().getCurrentSecond());
            tmpResult.setAvgProcessingTime(mainFilter.
            		getAverageProcessingTime());
        }

//            if(runOneFirst) {
//                runNumber = -1;
//                runOneFirst = false;
//            }

        // Comment out these, since they are not relevant now.

//        getMainClass().createGnuplotPulledSensors(numRuns, getMainClass().
//        		getTimer().getCurrentSecond());
//        getMainClass().createGnuplotLoadMeasurements(numRuns, getMainClass().
//        		getTimer().getCurrentSecond(),
//                numSensors);

        /**
         * For regression testing the system, the filter always returns 
         * the result from the
         * run. There are four types of return messages.
         *
         * * NoTrigger
         * * Finished
         * * TimedDeviation
         * * StrictnessDeviation
         *
         */
        if (mainFilter != null) {

        	tmpResult.setResult(mainFilter.getEndMessage());
        	
            return tmpResult;
        }

        tmpResult.setResult(-1);
        
        return tmpResult;
    }

    /**
	 * Saves the current time, query name, query, and environment
	 * name in a file called "experimentInfo.txt". 
     * @throws IOException 
	 */
    
    private void saveExperimentInformation() throws IOException {
	
    	BufferedWriter out = null;
    	
    	out = new BufferedWriter(new FileWriter(mainClass.getExpPrefix() + 
    			"experimentInfo.txt"));
				
    	Calendar cal = Calendar.getInstance();
    	
		out.write("Time: " + cal.get(Calendar.HOUR_OF_DAY) + ":" + 
				cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + 
				"_" + cal.get(Calendar.DATE) + "." + cal.get(Calendar.MONTH) +
				"/" + cal.get(Calendar.YEAR) + "\n\n");
		
		out.write("Environment: " + mainClass.getEnvironmentFilename() + 
				"\n\n");
		
		out.write("Query/Statement: " + mainClass.getStatementFilename() + 
				"\n\n");
		
		out.write("MovementPattern:" + mainClass.getMovementFileName());
		
		out.close();
    	
	}

	public final void setShape(Shape shape) {

        if (objectCreatorOn) {

            objectCreator.setShape(shape);

        } else if (sensorCreatorOn) {

            physicalSensorCreator.setShape(shape);

        } else if (environmentCreatorOn) {

            environmentCreator.getLoICreator().setShape(shape);

        }
    }

    public final void addCapability(Capability tmpCap) {
        if (environmentCreatorOn) {

            environmentCreator.addCapability(tmpCap);

        } else if (sensorCreatorOn) {

            physicalSensorCreator.addCapability(tmpCap);
        }
    }

    public final void addSensor(PhysicalSensor tmpSens) {

        environmentCreator.addSensor(tmpSens);
    }

    public final boolean addLoI(LocationOfInterest currLoI) {

        return this.mainClass.addLoI(currLoI);
    }

    /**
     * Resets the movement pattern if the DrawPanel object is not null, 
     * i.e., if
     * the system does not run trace files.
     */
    public final void resetMovement() {

        if(environmentCreator != null) {

            environmentCreator.getDrawPanel().resetMovement();
        }
    }
    

    /**
     * Sets the experiment, i.e., creating the state machine, and the
     * filter. The method also starts the timer and the sensors. 
     *
     * If the boolean reEval is true, the sensors are not reset; the 
     * system just leaves them on. This is important for sensors that take
     * long time to 'reboot', e.g., network cameras.
     */
    public final MainDataTupleFilter 
    		setupEventProcessingPhase(boolean monitoring, 
    		int runNumber, int numSensors,
            int numAnds, int option, boolean reEval) {

    	/**
    	 * Make sure that the environment is instantiated.
    	 */
    	
        if (!mainClass.getEnvironments().isEmpty()) {
            mainClass.startTimer(monitoring);

            if(option > -1) {

                /** 
                 * Synchronize time with trace file.
                 */

                Timer tmpTimer = mainClass.getTimer();

                if(tmpTimer.getCurrentMillisecond() < 
                		this.traceFileStartTime) {
                    tmpTimer.increaseMilliseconds((this.traceFileStartTime -
                            tmpTimer.getCurrentMillisecond()));
                }
            }

            mainClass.startSensors(monitoring, runNumber, option);
            
            if(option == -1) {
            	mainClass.restartTimer(monitoring);
            }

            mainFilter = new MainDataTupleFilter(mainClass.getCEPQueue(),
                    mainClass.getEnvironments().get(0),
                    mainClass.getTimer(), runNumber,
                    numSensors, numAnds, expPrefix);

            for (QueryElement tmpStmt : mainClass.
                    getComplexStatements()) {
                QueryPoolElement tmpSM = new QueryPoolElement(tmpStmt, this,
                        mainClass.getCEPQueue(), expPrefix, monitoring, 
                        runNumber, numSensors, numAnds);

                mainClass.getQueryPool().add(tmpSM);

                /** 
                 * Adding the first filter for the evaluation. This is 
                 * the outermost filter for the evaluation.
                 */

                mainFilter.addDataTupleFilter(tmpSM.getDataTupleFilter());
            }

            panel.showMovementPattern();

            /**
             * Set the movement to start. This does not happen during
             * emulation. 
             */

            if (option == -1 && !monitoring) {
                resetMovement();
            }

            return mainFilter;
        }

        JOptionPane.showMessageDialog(panel.getFrame(),
                "Environment is not instantiated.");

        return null;
    }

    /**
     * Finds the sensors that approximate the lois, if any, and if => used, 
     * those that should be in DevRep. If the environment is empty, it means 
     * that the system currently reads from a trace file, and that the 
     * environment is not loaded. The method then has to create a new and 
     * empty environment
     *
     * After having instantiated the chain of statements, the E set is shown.
     *
     * @param tmpSt
     */
    private void instantiateComplexStatement(QueryElement tmpSt) {

        instantiateStatementChain(tmpSt);
        
        /**
         * After instantiating the chain, show the elements in E.
         */

        Iterator<DataTuple> it = E.iterator();
        
        while(it.hasNext()) {
        	getPanel().appendCurrSets(it.next().getObject());
        }        
    }

    /**
     * Used for the EiMM10-paper to measure the time it takes to
     * calculate the coverage area of a set of sensors. Runs the
     * experiment numRuns times.
     */
    
    void calculateCoverageTime() {

        String fileName = JOptionPane.showInputDialog("Write base filename:");
        int numEnvs = Integer.
        	parseInt(JOptionPane.showInputDialog("Write maximal number " +
        			"of sensors:"));
        
        int numRuns = 10;

        File envFile = new File(CommonSens.dataPrefix + fileName);

        Environment currEnv;

        long[] calculationTime = new long[numRuns];
        long maxTime = -1;
        long minTime = -1;
        long sumValue = 0;
        double avgValue = 0;

        // Open result file:

        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new FileWriter(expPrefix + 
            		"coverageAreaCalc_" + fileName));
        } catch (IOException ex) {
        }

        for (int y = 0; y < numEnvs; y++) {

        	System.err.println("y = " + y);
        	
            // Open the respective file with y number of sensors.

            readEnvironmentFile(envFile, y);

            currEnv = this.environmentCreator.getCurrEnvironment();

            maxTime = Long.MIN_VALUE;
            minTime = Long.MAX_VALUE;

            for (int i = 0; i < numRuns; i++) {

                calculationTime[i] = System.currentTimeMillis();

                for (Sensor tmpSource : currEnv.getTupleSources()) {

                    if (tmpSource instanceof PhysicalSensor) {

                        PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;

                        Shape shape = tmpSens.getShape();

                        if (shape != null) {

                            //System.err.println("draws reduced polygon.");

                            tmpSens.getPolygonReduced(currEnv,
                                    tmpSens.getSignalType());

                        }
                    }
                }

                calculationTime[i] = System.currentTimeMillis() - 
                						calculationTime[i];
            }

            // Calculate avg and variance.

            sumValue = 0;
            avgValue = 0;

            for (int i = 0; i < numRuns; i++) {

                sumValue += calculationTime[i];

                if (calculationTime[i] < minTime) {
                    minTime = calculationTime[i];
                }

                if (calculationTime[i] > maxTime) {
                    maxTime = calculationTime[i];
                }
            }

            avgValue = ((double) sumValue / (double) numRuns);

            try {
                // Write the values to file.
                out.write(y + "\t" + avgValue + "\t" + minTime + "\t" + 
                		maxTime + "\n");
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).
                		log(Level.SEVERE, null, ex);
            }

            // Close current environment.

            this.environmentCreator.close();
        }

        try {
            // Close result file.
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).
            			log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Runs experiments for DEBS10, DMSN10, etc...
     *
     * @param fileName
     * @param pattern
     * @param statement
     * @param numSensors
     * @param numAnds
     */

    public final void moreSensPerLoI(String fileName, String pattern,
            String statement, int numSensors, int numAnds, int overnight) {

        isRunningExperiment = true;

        long startTime = System.currentTimeMillis();

        int numRuns = 10;//100;

        FileChooser envirChooser = null;
        FileChooser movementChooser = null;
        FileChooser statementChooser = null;

        if (numSensors == -1 && overnight == -1) {
            numSensors = Integer.parseInt(JOptionPane.
                    showInputDialog("Write maximal number of sensors:"));
        }

        if (numAnds == -1 && overnight == -1) {
            numAnds = Integer.parseInt(JOptionPane.
                    showInputDialog("Write maximal number of ands:"));
        }

        envirChooser = new FileChooser(CommonSens.dataPrefix);

        if (fileName == null) {

            envirChooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            //returnVal = envirChooser.showDialog(MainView.this, "Environment");
        } else {
            envirChooser.setSelectedFile(new File(CommonSens.dataPrefix + 
            		fileName));
        }

        movementChooser = new FileChooser(CommonSens.dataPrefix);

        if (pattern == null) {

            movementChooser.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            //returnVal = movementChooser.showDialog(MainView.this, "Movement");
        } else {
            movementChooser.setSelectedFile(new File(CommonSens.dataPrefix + 
            		pattern));
        }

        statementChooser = new FileChooser(CommonSens.dataPrefix);

        if (statement == null) {

            statementChooser.setDialogType(javax.swing.JFileChooser.
            		OPEN_DIALOG);
//            returnVal = statementChooser.showDialog(MainView.this, 
//            		"Statement");
        } else {
            statementChooser.setSelectedFile(new File(CommonSens.dataPrefix + 
            		statement));
        }

        BufferedWriter statementSetupTime = null;

        try {
            statementSetupTime = new BufferedWriter(
                    new FileWriter(mainClass.getExpPrefix() +
                    "/statementSetupTime.dat"));
        } catch (Exception e) {

            System.err.println("statementSetupTime did not open...");

        }

        /**
         * If the environmentCreator is on, close it
         * to make it look cleaner during the experiments.
         */

        // Close current environment.

        if(environmentCreator != null) {
            environmentCreator.close();
        }

        // Empty all the currEnv arraylist.

        mainClass.currEnvs.clear();

        /**
         * Run simulation. Note that we set <= since we want the first
         * sensors to run as well.
         */

        for (int a = 0; a <= numAnds * 10; a += 10) {

            for (int i = 0; i < numSensors * 10; i += 10) {

                /**
                 * It seems like Java needs to get warmed up with a single
                 * run. This is only done through the experiment, i.e.,
                 * when the numRuns > 1.
                 */

                boolean runOneFirst = false;

                if(numRuns > 1) {
                    runOneFirst = true;
                }

                for(int runNumber = 0; runNumber < numRuns; runNumber++) {

                    long statementSetupTimeLong = 0;

                    /**
                     * The environment is read with i + 1 since there is a 
                     * check in CommonSens.readEnvironmentFromFile against this 
                     * value. If the value is -1, the method only reads what is 
                     * stated in the environment file. Otherwise, it creates 
                     * 'artificial' tuple sources.
                     */

                    readEnvironmentFile(envirChooser.getSelectedFile(), 
                    		(i + 1));

                    readMovementFile(movementChooser.getSelectedFile());

                    statementSetupTimeLong = System.currentTimeMillis();

                    // Empty the statements before adding new.

                    this.mainClass.getComplexStatements().clear();

                    readStatementFile(statementChooser.getSelectedFile(), a);

                    statementSetupTimeLong = System.currentTimeMillis() - 
                    statementSetupTimeLong;

                    this.environmentCreator.getCurrEnvironment();

                    //System.err.println("Starting experiment with i = " + i);

                    /**
                     * We set the reEval to false, since this is a simulation.
                     */
                    
                    startEventProcessing(false, numRuns, i, a, -1, runNumber, false);

                    // Close current environment.

                    this.environmentCreator.close();

                    // Empty all the currEnv arraylist.

                    this.mainClass.currEnvs.clear();

                    try {
                        statementSetupTime.write(a + "\t" + i + "\t" + 
                        		statementSetupTimeLong + "\n");
                    } catch (IOException ex) {
                        Logger.getLogger(MainView.class.getName()).
                        log(Level.SEVERE, null, ex);
                    }

                    if(runOneFirst) {
                        runNumber = -1;
                        runOneFirst = false;
                    }
                }
            }
        }

        try {
            statementSetupTime.close();
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, 
            		null, ex);
        }

        // Create gnuplot in 2D.

        PlotCreator.createGnuplotTupleProcessing2D(numRuns,
                mainClass.getTimer().getCurrentSecond(),
                numSensors, numAnds, this.mainClass.getExpPrefix());

        long stopTime = System.currentTimeMillis();

        System.err.println("Start time = " + startTime + ", stopTime = " + 
        		stopTime);

        isRunningExperiment = false;
    }

    /**
     * Instantiates a chain of statements. This implies approximating the
     * locations of interest.
     * 
     * In addition, the method generates the E set.
     *
     * @param se
     */

    private void instantiateStatementChain(QueryElement se) {

        boolean strict = false;

        ArrayList<QueryElement> previousList = 
        	new ArrayList<QueryElement>();
        ArrayList<QueryElement> currentList = 
        	new ArrayList<QueryElement>();

        while (se != null) {

            if (se instanceof AtomicQuery) {

            	/**
            	 * Add the capability of the atomic statement in E.
            	 */
            	
            	//E.addAll(((AtomicQuery) se).getNset());
            	
                // Isec and NoIsec are set.

//                System.err.println("MainView.instantiateStatementChain: " +
//                		"se inst AtomicQuery");

                currentList.add(se);

                if(getCurrentEnvironment() != null) {

                    getCurrentEnvironment().
                        calculateError((AtomicQuery) se);
                }
                
                if (strict) {

                    /**
                     * Since there is a strict relation, include in the
                     * sensors that provide the same capabilities as in
                     * both prevST and currST.
                     */

                    if(getCurrentEnvironment() != null) {

                        getCurrentEnvironment().addStrict(previousList,
                            currentList);
                    }

                    strict = false;
                }
            } else if (se instanceof TimedListOfAtomicQueries) {

                instantiateStatementChain(((TimedListOfAtomicQueries) se).
                		getTheChain());

                currentList.add(se);

            } else if (se instanceof ConcurrencyOperator) {

                // Instantiate the two statement chains

                instantiateStatementChain(((ConcurrencyOperator) se).
                		getFirstChain());
                instantiateStatementChain(((ConcurrencyOperator) se).
                		getSecondChain());

                currentList.add(se);

            } else {

                if (se instanceof ConsecutiveRelation) {

                    previousList = currentList;
                    currentList = new ArrayList<QueryElement>();

                } else if (se instanceof Strict) {

                    strict = true;
                    previousList = currentList;
                    currentList = new ArrayList<QueryElement>();
                }
            }

            se = se.getNext();
        }
    }

    /**
     * Read emulation data from file. This data is used as input from the
     * sensors. The option decides the format of the data files.
     *
     * @param selectedFile
     * @param option
     */
    public void readDataFile(File selectedFile, int option) {

        // Set the option for this file. The option indicates
        // the schema for the file.

        setSimulationOption(option);

        Scanner in = null;

        // Only run the first statement for now:

        try {

            in = new Scanner(selectedFile);

        } catch (IOException ex) {

            System.err.println("Exception!: " + ex.getMessage());
        }

        if (getSimulationOption() == 0) {

            /**
             * From ailab.eecs.wsu.edu/casas
             */
            HashMap<String, ArrayList<DataTuple>> tupleSourceTraces =
                    new HashMap<String, ArrayList<DataTuple>>();

            // Create an empty environment object or take an existing one 
            // where the new sensors are placed.

            Environment tmpEnvir = null;
            
            if(mainClass.getEnvironments().size() > 0) {
            	tmpEnvir = mainClass.getEnvironments().get(0);
            } else {
            	tmpEnvir = new Environment(this);
            }
            
            /**
             * Read through the trace file and order the data tuples by
             * time.
             */
            while (in.hasNext()) {

                /* String tmpDate = */ in.next();
                String tmpTimestamp = in.next();
                String tmpTupleSourceName = in.next();
                String tmpSensorState = in.next();
                
                /**
                 * Set the start time to match the first timestamp in
                 * the tracefile.
                 */

                if(traceFileStartTime == -1) {

                    traceFileStartTime = Timestamp.toLong(tmpTimestamp);
                    
//                    System.err.println("readDataFiletraceFileStartTime = " + 
//                    		traceFileStartTime);
                }

//
//                System.err.println("readDataFile has read " +
//                        /*tmpDate +*/ ", " + tmpTimestamp + ", " +
//                        tmpTupleSourceName + ", " + tmpSensorState);

                // Find the capability by searching through the tuple
                // source arrayList:

                Sensor tmpTS = null; 
                
                if(tmpEnvir.getTupleSource(tmpTupleSourceName) != null) {
                	tmpTS = tmpEnvir.getTupleSource(tmpTupleSourceName); 
                } else {
                    tmpTS = mainClass.findTupleSource(tmpTupleSourceName);
                }

                if (tmpTS == null) {
                    JOptionPane.showMessageDialog(panel.getFrame(),
                            tmpTupleSourceName + " does not exist in the " +
                            		"repository.");
                    return;
                }

                DataTuple tmpTuple = new DataTuple(tmpTupleSourceName,
                        tmpTS.getProvidedCapability(null), tmpSensorState,
                        Timestamp.toLong(tmpTimestamp),
                        Timestamp.toLong(tmpTimestamp));

//                System.err.println("Core.readDataFile has read tmpTuple = " +
//                		tmpTuple.getObject());
                
                if (!tupleSourceTraces.containsKey(tmpTupleSourceName)) {
                    tupleSourceTraces.put(tmpTupleSourceName, 
                    		new ArrayList<DataTuple>());
                }

                tupleSourceTraces.get(tmpTupleSourceName).add(tmpTuple);

                /**
                 * Set the last timestamp for each run. The last timestamp
                 * is used for indicating when to stop the simulation.
                 */

                setLastSimulationTimestamp(Timestamp.toLong(tmpTimestamp));
            }

//            System.err.println("MainView.readDataFile: " +
//            		"tupleSourceTraces.size() = "
//                    + tupleSourceTraces.size());

            Iterator<String> it = tupleSourceTraces.keySet().iterator();

            /**
             * For each of the names, find the corresponding tuple source and
             * store the tuple source into tmpEnvir. In addition, the
             * emulated tuple source gets the corresponding trace.
             */
            while (it.hasNext()) {

                String tmpTupleSourceName = it.next();

                EmulatedTupleSource tmpETS = null;

//                System.err.println("Core.readDataFile checks out " + tmpTupleSourceName);
                
                if(tmpEnvir.getTupleSource(tmpTupleSourceName) != null) {
                	
                	int i = 0;
                	
                	for(; i < tmpEnvir.getTupleSources().size(); i++) {
                		
                		Sensor ts = tmpEnvir.getTupleSources().get(i);
                		
                		if(ts != null) {
                			
                			if(ts.getName().equals(tmpTupleSourceName)) {
                				
                				break;
               				
                			}
                		}                		
                	}
                	
                	if(i < tmpEnvir.getTupleSources().size()) {
                		
        				tmpETS = new EmulatedTupleSource(tmpEnvir.
                        		getTupleSource(tmpTupleSourceName));
        				
        				tmpEnvir.getTupleSources().remove(i);
        				tmpEnvir.getTupleSources().add(tmpETS);                				

                	}
                } else {
                
                	tmpETS = new EmulatedTupleSource(this.mainClass.
                        		findTupleSource(tmpTupleSourceName));
                }

//                System.err.println("new EmulatedTupleSource(" + 
//                		tmpTupleSourceName + ")");
//                System.err.println("Trace size = " + 
//                		tupleSourceTraces.get(tmpTupleSourceName).size());

                tmpETS.addTrace(tupleSourceTraces.get(tmpTupleSourceName));

                tmpEnvir.addTupleSource(tmpETS);
            }

            // Add the environment to the environment repository in the
            // main class.

            //System.err.println("Environment is added");

            mainClass.getEnvironments().add(0, tmpEnvir);
        }
    }

    private Environment getCurrentEnvironment() {

        if(!mainClass.getEnvironments().isEmpty())
            return mainClass.getEnvironments().get(0);

        return null;
    }

    /**
     * Returns the simulation option.
     *
     * @return
     */

    public int getSimulationOption() {
        return simluationOption;
    }

    public void setSimulationOption(int tmpOption) {
        this.simluationOption = tmpOption;
    }

    private void setLastSimulationTimestamp(long toLong) {

        lastSimulationTimestamp = toLong;
    }

    private long getLastSimulationTimestamp() {
        return lastSimulationTimestamp;
    }

    void setStepsPerTimestamp(int parseInt) {
        stepsPerTimestamp = parseInt;
    }

    void setSensorCreatorOn(boolean b) {
        sensorCreatorOn = b;
    }

    void setObjectCreatorOn(boolean b) {
        objectCreatorOn = b;
    }

    void setObjectCreator(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
    }

    void setPhysicalSensorCreator(
    		SensorCreator physicalSensorCreator) {
        this.physicalSensorCreator = physicalSensorCreator;
    }

    void setSimluationOption(int chooseSimulationMode) {
        this.simluationOption = chooseSimulationMode;
    }

    public CommonSens getMainClass() {
        return mainClass;
    }

    boolean isRunningExperiment() {
        return isRunningExperiment;
    }

    /**
     * Returns the main filter. If the system runs an emulation experiment,
     * the filter is in the monitoringRunner thread and is returned. Otherwise,
     * this.mainFilter is returned. 
     * 
     * @return
     */
    
	public MainDataTupleFilter getMainFilter() {
		if(monitoringRunner != null) {
			return monitoringRunner.getMainDataTupleFilter();
		}
		
		return mainFilter;
	}

	/**
	 * Calculates the MaxComb and MinComb of possible combinations of
	 * allowed sequences.
	 * 
	 * @param b
	 * @param samples
	 * @param pval
	 * @return
	 */
	
	public static long binCoff(boolean max, int samples, int pval) {

		long res = 0;
		int start;
		int stop;
		
		if(max) {
			
			start = Math.round((samples * pval) / 100);
			stop = samples;
			
		} else {
			
			start = 1;
			stop = Math.round((samples * pval) / 100);
			
		}
		
		for(; start <= stop; start++) {
			
			res += binom(stop, start);
			
			System.err.println("(" + ((double) start / (double) stop) + ") res = " + res);
		}
		
		System.err.println("RES = " + res);
		
		return res;
	}
	
	/**
	 * Copyright (c) 1998 by Bruno R. Preiss, P.Eng.  All rights reserved.
	 *
     * http://www.pads.uwaterloo.ca/Bruno.
     * 	Preiss/books/opus5/programs/pgm14_11.txt
	 * 
	 * @param n
	 * @param m
	 * @return
	 */
	
	public static long binom (final int n, final int m) {
		final long[] b = new long [n + 1];
		b [0] = 1;

		for (int i = 1; i <= n; ++i) {
			b [i] = 1;
	    
			for (int j = i - 1; j > 0; --j) {
				b [j] += b [j - 1];
			}
		}
		
		return b [m];
    }
	
	public static void /*ArrayList<int[][]>*/ nPerm(int tN, int sN) {
		
		int [] sVal = {0, 1};//, 2};
		
		int [][] sensors = new int[sN][sVal.length];
		
		for(int i = 0; i < sensors.length; i++) {
			sensors[i] = sVal;
		}

		/**
		 * Create all the possible combinations of the capability ranges. 
		 * This is sVal^{sN} combinations.
		 */
		
		int [][] combinations = 
			new int[(int) Math.round(Math.pow(sVal.length, sN))][sN];
				
		getCombinations(combinations, sensors, 0);
				
		// Print all the combinations...
		
		for(int i = 0; i < combinations.length; i++) {
			
			for(int y = 0; y < combinations[i].length; y++) {
				
				System.out.print(combinations[i][y] + " ");				
			}
			
			System.out.println();
		}				
	}

	private static void getCombinations(int [][] combinations,  
			int [][] sensors, int sensorIndex) {
			
		for(int i = 0; i < sensors[sensorIndex].length; i++) {

			/**
			 * Iterate back and update the prior values.
			 */
			
			for(int j = sensorIndex - 1; j >= 0; j--) {
				//combinations
			}
			
			combinations[i][sensorIndex] = sensors[sensorIndex][i];
			
			System.err.println("Setting combinations[" + i + 
					"][" + sensorIndex + "] to " + 
					combinations[i][sensorIndex]);
			
			if(sensorIndex < sensors.length - 1) {
				getCombinations(combinations, 
						sensors, sensorIndex + 1);
			}		
		}
		
		// Iterate through all the timestamps
		
		for(int ts = 0; ts < combinations.length; ts++) {
		
			// Iterate through all the sensors
		
			for(int i = 0; i < combinations[ts].length; i++) {
			
				//combinations[ts][i]
			}
		}
	}
}
