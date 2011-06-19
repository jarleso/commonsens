package modelViewController;

import environment.Environment;
import java.io.FileNotFoundException; 
import java.io.IOException;

import language.StrictnessFilter;

import java.text.DecimalFormat;
import java.util.ArrayList; 
import java.util.logging.Level;
import java.util.logging.Logger;
import environment.LocationOfInterest;
import environment.CommonSensObject;
import environment.Permeability;
import environment.Shape;
import environment.Triple;
import environment.Person;
import eventProcessor.DataTuple;
import eventProcessor.QueryPoolElement;
import eventProcessor.Timer;

import java.io.BufferedWriter;
import java.io.File; 
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner; 
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import language.QueryElement;
import sensing.Capability;
import sensing.CustomFunctionLoader;
import sensing.ExternalSource;
import sensing.LogicalSensor;
import sensing.PhysicalSensor;
import sensing.SignalType;
import sensing.Sensor;
import sensing.CapabilityDescription;
import sensing.CustomFunction;
import sensing.EmulatedTupleSource;
import sensing.RangeInt;
import sensing.RangeString;
import sensing.SensorDetected;

public final class CommonSens implements java.io.Serializable {

    public static String dataPrefix = "RuntimeData/";
	
	public static int GRANULARITY = 1;
	
	private static final String motionDetector = 
    	"C:\\Users\\" +
		"jarleso\\Documents\\" +
		"Visual Studio 2008\\Projects\\" +
		"ObjectDetected\\Release\\" +
		"ObjectDetected.exe ";
    public static final String SENSOR_HOST = "129.240.67.77";//"127.0.0.1";
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int ANTENNA_LENGTH = 3;

	public static final double INITIAL_SIGNAL_STRENGTH = 1;
    public static double THRESHOLD = 0.1;
    public static void main (String[] args) {

        ArrayList<String> tmpList = new ArrayList<String>();

        for(int i = 0; i < args.length; i++) {
            tmpList.add(i, args[i]);
        }

        new CommonSens(tmpList);
    }
    private ConcurrentLinkedQueue<DataTuple> cepQueue;
    
    private ArrayList<QueryElement> complexStatements;
    
    private ArrayList<Capability> currCapabilities;
    public ArrayList<Environment> currEnvs;
    private ArrayList<LocationOfInterest> currLoIs;
    public ArrayList<CommonSensObject> currObjects;
    public ArrayList<Shape> currShapes;
    public ArrayList<Sensor> currTupleSources;
    
    //private ArrayList<Thread> threads;
    private boolean doShow;

    private File envFile;

    private String environmentFilename = "empty";
    private String expID = "\b";

    private String expPrefix;
    private MainView mainPanel;
    private String movementFilename = "empty";
    private ArrayList<QueryPoolElement> mStateMachine;

    private File persFile;

    private ArrayList<Process> processes;

    private File sensFile;

    private boolean showPalette;

    private String statementFilename = "empty";

    private HashMap<Integer, StrictnessFilter> strictnessFilters;

    private Timer timer;

	private boolean reEval;
   

    public CommonSens (ArrayList<String> args) {

    	/**
    	 * Go through the arguments to setup the system.
    	 */
    	
        int index = args.indexOf("-exp");

        String time = String.valueOf(Calendar.getInstance().getTimeInMillis());

        int expIndex = args.indexOf("-id");

        if(expIndex == -1) {
            expID = "";
        } else {
            expID = args.get(expIndex + 1) + "/";
        }

        expPrefix = "Experiments/" + expID + time + "/";

        new File(expPrefix).mkdirs();

        if(index == -1) {

            doShow = true;

        } else {

            doShow = false;

        }

        ShowPalette(args.indexOf("-palette") != -1);
        
        reEval(args.indexOf("-reEval") != -1);

        // Start view and system core.

        setMainPanel(new MainView(this, doShow, expPrefix, ShowPalette(),
        		reEval()));
        
        // Start queue:

        cepQueue = new ConcurrentLinkedQueue<DataTuple>();

        // Statements:

        setComplexStatements(new ArrayList<QueryElement>());

        // Box machine:

        MStateMachine(new ArrayList<QueryPoolElement>());

        // Initiate objects

        currObjects = new ArrayList<CommonSensObject>();
        currTupleSources = new ArrayList<Sensor>();
        currShapes = new ArrayList<Shape>();
        currEnvs = new ArrayList<Environment>();
        currCapabilities = new ArrayList<Capability>();
        currLoIs = new ArrayList<LocationOfInterest>();
        
        // Instantiate external processes that are pulled.
        
        processes = new ArrayList<Process>();

        strictnessFilters = new HashMap<Integer, StrictnessFilter>();

        // Read objects

        //readObjects();
        //readEnvironments();
        readSensorsFromFile();
        readCapabilitiesFromFile();

        // Show environment

        index = args.indexOf("-env");

        if(index != -1) {

            setEnvironmentFilename(args.get(index + 1));

            System.err.println("Reading environment " + dataPrefix + 
            		getEnvironmentFilename());

            getMainPanel().getCore().readEnvironmentFile(new File(dataPrefix + 
            		args.get(args.indexOf("-env") + 1)), -1);
        }

        // If movement pattern and statement are already defined, set these
        // files in the getMainPanel.

        index = args.indexOf("-stmt");

        if(index != -1) {

            setStatementFilename(args.get(index + 1));

            getMainPanel().getCore().readStatementFile(new File(dataPrefix + 
            		getStatementFilename()), 0);
        }

        index = args.indexOf("-mvmt");

        if(index != -1) {

            setMovementFilename(args.get(index + 1));

            getMainPanel().getCore().readMovementFile(new File(dataPrefix + 
            		getMovementFileName()));
            getMainPanel().showMovementPattern();
        }
                
        // Read tracefile. The tracefile also has to be followed by a number
        // that indicates the format.

        index = args.indexOf("-trace");

        if(index != -1) {

            String fileName = args.get(index + 1);

            int option = Integer.parseInt(args.get(index + 2));

            getMainPanel().getCore().readDataFile(new File(dataPrefix + 
            		"/../../dataSets/"
                    + fileName), option);
        }

        // Run experiment:

        if(!doShow) {

            /**
             * Check if we should run a regression check.
             */

            index = args.indexOf("-reg");

            if(index != -1) {

                System.err.println("Running regression test:");

                Scanner in = null;
                BufferedWriter out = null;
                BufferedWriter outLatex = null;

                try {
                    // Read regression from file.
                    in = new Scanner(new File(dataPrefix + 
                    		"regression.txt"));
                    
//                    in = new Scanner(new File(dataPrefix + 
//            		"SmartE_test_setup.txt"));

                    out = new BufferedWriter(new FileWriter(expPrefix + 
                    		"/regression_result.txt"));
                    
                    outLatex = new BufferedWriter(new FileWriter(expPrefix + 
            		"/regression_result_LaTeX.txt"));
                    
//                    out = new BufferedWriter(new FileWriter(expPrefix + 
//            		"/SmartE_results.txt"));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CommonSens.class.getName()).
                    	log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }

                int experimentCounter = 0;
                int matchRate = 0;
                
                while(in.hasNext()) {

                    // Run through all the experiments.

                    String env = in.next();

                    if(env.startsWith("#")) {

                        // Skip lines that begin with #.

                        in.nextLine();

                        continue;
                    }

                    experimentCounter++;
                    
                    String mov = in.next();
                    String stm = in.next();
                    int expectedResult = Integer.parseInt(in.next());

                    System.err.println(env + "\t" + mov + "\t" + stm + "\t" + 
                    		expectedResult);

                    // Get the number of repetitions for the experiment.
                    
                    int numRuns = 0;
                    
                    if(args.indexOf("-numRuns") != -1) {
                    	numRuns = Integer.parseInt(args.get(args.
                    			indexOf("-numRuns") + 1));
                    } else {
                    	numRuns = 1;
                    }
                    
                    double avgTimeConsumption = 0;
                    double minTimeConsumption = Integer.MAX_VALUE;
                    double maxTimeConsumption = 0;
                    
                    RunResult runResult = null;
                    
                    for(int i = 0; i < numRuns; i++) {
    
                    	// null the main panel
                    	
                    	if(getMainPanel() != null) {
                    		
                    		getMainPanel().getFrame().dispose();
                    		
                    		setMainPanel(null);
                    	}
                    		
	                    // Setup a new main panel for each check.
	                	
	                    setMainPanel(new MainView(this, doShow, expPrefix, 
	                    		false, reEval()));
	
	                    getEnvironments().clear();
	                    
	                    getMainPanel().getCore().
	                    	readEnvironmentFile(new File(dataPrefix + env), 1);
	                    
	                    getMainPanel().getCore().
	                    	readMovementFile(new File(dataPrefix + mov));
	
	                    /** 
	                     * Since the system should be able to handle more 
	                     * statements, we have to empty the whole statement 
	                     * arrayList when running regression tests.
	                     */
	                    
	                    getComplexStatements().clear();
	                    getQueryPool().clear();
	
	                    getMainPanel().getCore().
	                    	readStatementFile(new File(dataPrefix + stm), 0);
	
	                    runResult = getMainPanel().getCore().
	                    	startEventProcessing(false, 1, 0, 0, -1, 0, reEval());
	
	                    // turn off environment creator.
	
	                    getMainPanel().closeEnvironmentCreatorPanel();
	
	                    // Update the processing time information.
	                    
	                    avgTimeConsumption = ((avgTimeConsumption * i) + 
	                    		runResult.getAvgProcessingTime()) /
	                    		(i + 1);
	                    
	                    if(runResult.getAvgProcessingTime() < 
                    			minTimeConsumption) {
	                    
	                    	minTimeConsumption = runResult.
	                    		getAvgProcessingTime();
	                    }
	                    
	                    if(runResult.getAvgProcessingTime() > 
                    			maxTimeConsumption) {
	                    
	                    	maxTimeConsumption = runResult.
	                    		getAvgProcessingTime();
	                    }

	                }
                    
                    System.err.println(
                              " expectedResult = " + expectedResult
                            + "\n runResult      = " + runResult.getResult() );

                    try {
                        out.write(env + "\t" + mov + "\t" + stm + "\t" + 
                        		avgTimeConsumption + "\t" +
                        		minTimeConsumption + "\t" +
                        		maxTimeConsumption + "\t" +
                        		runResult.getEndTime() + "\t");

                        outLatex.write(experimentCounter + " & " + 
                        		roundTwoDecimals(avgTimeConsumption) + " & " +
                        		roundTwoDecimals(minTimeConsumption) + " & " +
                        		roundTwoDecimals(maxTimeConsumption) + " & " +
                        		runResult.getEndTime() + " & ");
                        
                        if(expectedResult == runResult.getResult()) {

                        	matchRate++;
                        	
                            out.write("MATCH (exp = " + expectedResult + 
                            			", res = " + runResult.getResult() + 
                            			")\n");

                            outLatex.write("True ($" + expectedResult + " = " + 
                            		runResult.getResult() + "$) \\\\\n");
                            
                        } else {

                            out.write("NOMATCH (exp = " + expectedResult + 
                            			", res = " + runResult.getResult() + 
                            			") \\\\\n");
                            
                            outLatex.write("False ($" + expectedResult + 
                            		" \\neq " + 
                            		runResult.getResult() + "$)\n");

                        }

                    } catch (IOException ex) {
                        Logger.getLogger(CommonSens.class.getName()).
                        	log(Level.SEVERE, null, ex);
                    }
                }

                System.err.println("Number of experiments = " + 
                		experimentCounter);
                System.err.println("Match rate = " +
                		(100*((double)matchRate/experimentCounter)));
                
                try {
                    in.close();
                    out.close();
                    outLatex.close();
                } catch (IOException ex) {
                    Logger.getLogger(CommonSens.class.getName()).
                    	log(Level.SEVERE, null, ex);
                }

                // Close program.

                System.exit(0);

            } else {
                getMainPanel().getCore().
                	startEventProcessing(false, 100, -1, 0, -1, 0, reEval());
            }
        }
    }

    private boolean reEval() {
		
		return reEval;
	}

	private void reEval(boolean b) {

    	this.reEval = b; 
	}

	public void addCapability(Capability tmpCap) {

        currCapabilities.add(tmpCap);
    }

    public int addComplexStatement (QueryElement complexStatement) {

        int index = getComplexStatements().size();

//        System.err.println("Adding statement: " + complexStatement.
//                showElementRecursive() + " at index " + index);

        getComplexStatements().add(index, complexStatement);

        return index;
    }

    public boolean addEnvironment(Environment currEnv) {

    	System.err.println("Adding environment: " + currEnv.getEnvironment());
    	
        int i = 0;

        for(Environment tmpObj : currEnvs) {

            if(tmpObj.getName().equals(currEnv.getName())) {
                //return false;
                currEnvs.remove(i);
                break;
            }

            i += 1;
        }

        currEnvs.add(currEnv);

        return true;
    }

    public boolean addLoI(LocationOfInterest loi) {

        int i = 0;

        for(LocationOfInterest tmpLoI : currLoIs) {

            if(loi.getName().equals(tmpLoI.getName())) {
                currLoIs.remove(i);
                break;
            }

            i += 1;
        }

        currLoIs.add(loi);

        return true;
    }

    public boolean addObject(CommonSensObject currObject) {

        int i = 0;

        for(CommonSensObject tmpObj : currObjects) {

            if(tmpObj.getName().equals(currObject.getName())) {
                currObjects.remove(i);
                break;
            }

            i += 1;
        }

        currObjects.add(currObject);

        return true;
    }

    public boolean addShape(Shape shape) {

        int i = 0;

        for(Shape tmpObj : currShapes) {

            if(tmpObj.getName().equals(shape.getName())) {
                currShapes.remove(i);
                break;
            }

            i += 1;
        }

        currShapes.add(shape);

        return true;
    }

    /**
     * Adds a strictness filter to the hash map.
     *
     * @param tmpFilter
     * @return whether the filter was successfully stored.
     */

    public boolean addStricnessFilter(StrictnessFilter tmpFilter) {

        int key = tmpFilter.getKey();

        if (strictnessFilters.containsKey(key)) return false;

        strictnessFilters.put(key, tmpFilter);

        return true;
    }

    /**
     * If it already exists a tuple source with the same name than the new
     * tuple source, the old tuple source is overwritten.
     *
     * @param sensor
     * @return
     */

    public boolean addTupleSource(Sensor sensor) {

        if(sensor != null) {
//            System.err.println("CommonSens.addTupleSource: Adding" +
//            		" tuple source " + sensor.getName());
        } else {
            System.err.println("CommonSens.addTupleSource: tuple" +
            		" source is null!");
            return false;
        }

        int i = 0;

        for(Sensor tmpObj : currTupleSources) {

            if(tmpObj.getName() != null) {

                if(tmpObj.getName().equals(sensor.getName())) {

                    currTupleSources.remove(i);
                    break;
                }
            }

            i += 1;
        }

        //System.err.println("CommonSens.addTupleSource adding " + 
        //sensor.getName());

        currTupleSources.add(sensor);

        return true;
    }

    public File EnvFile () {
        return envFile;
    }

    public void EnvFile (File val) {
        this.envFile = val;
    }

    public Sensor findTupleSource(String tmpSensorName) {

        for(Sensor tmpTS : getTupleSources()) {

            if(tmpTS.getName().equals(tmpSensorName)) {
                return tmpTS;
            }
        }

        return null;
    }
    
    public ArrayList<Capability> getCapabilities() {

        return this.currCapabilities;
    }

    public Capability getCapability(String tmpCapability) {

        for(Capability cap : getCapabilities()) {
        	
            if(cap.getName().equals(tmpCapability)) {

                return cap;
            }
        }

        return null;
    }

    public ConcurrentLinkedQueue<DataTuple> getCEPQueue() {
        return cepQueue;
    }

    public ArrayList<QueryElement> getComplexStatements () {
        return complexStatements;
    }

    public String getEnvironmentFilename() {
        return environmentFilename;
    }

    public ArrayList<Environment> getEnvironments() {
        return this.currEnvs;
    }

    /**
     * Returns the epoch number that identifies this experiment.
     * 
     * @return
     */

    public String getExpPrefix() {
        return this.expPrefix;
    }

    public LocationOfInterest getLoI(String stringLoI) {

        for(LocationOfInterest tmpLoI : getLoIs()) {

            if(tmpLoI.getName().equals(stringLoI)) {
                return tmpLoI;
            }
        }

        return null;
    }

    public ArrayList<LocationOfInterest> getLoIs() {
        return currLoIs;
    }

    public MainView getMainPanel () {
        return mainPanel;
    }

    public String getMovementFileName() {
        return movementFilename;
    }

    public ArrayList<CommonSensObject> getObjects() {
        return currObjects;
    }

    public ArrayList<Shape> getShapes() {
        return currShapes;
    }

    public ArrayList<QueryPoolElement> getQueryPool () {
        return mStateMachine;
    }

    public String getStatementFilename() {
        return statementFilename;
    }

    public synchronized long getTime(boolean emulation) {

        if(timer == null) {
            startTimer(emulation);
        }

        return timer.getCurrentSecond();
    }

    public synchronized long getTimeMillisecond(boolean emulation) {

        if(timer == null) {
                startTimer(emulation);
        }

        return timer.getCurrentMillisecond();
    }

    public Timer getTimer() {
        return timer;
    }

    public final ArrayList<Sensor> getTupleSources() {
        return this.currTupleSources;
    }

    private CustomFunction loadCustomFunction(String function) {

//        ClassLoader loader = new NetworkClassLoader(host, port);
//        Object main = loader.loadClass("Main", true).newInstance();

        CustomFunctionLoader loader = new CustomFunctionLoader();

        return (CustomFunction) loader.getObject(function, true);
        
    }

//    private void readCapabilities() {
//
//        Scanner sc = null;
//
//        try {
//            sc = new Scanner(new File(this.envPrefix + "capabilities.dat"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CommonSens.class.getName()).log(Level.SEVERE, 
//    null, ex);
//        }
//
//        String dummy = sc.next();
//
//        if(dummy.equals("capabilities")) {
//
//            int numCaps = sc.nextInt();
//
//            for(int i = 0; i < numCaps; i++) {
//
//                this.currCapabilities.add(readCapabilityFromFile(sc));
//            }
//        }
//    }

    public void MStateMachine (ArrayList<QueryPoolElement> val) {
        this.mStateMachine = val;
    }

    public File PersFile () {
        return persFile;
    }

    public void PersFile (File val) {
        this.persFile = val;
    }

    private void readCapabilitiesFromFile() {

        Scanner sc = null;

        try {
            sc = new Scanner(new File(dataPrefix + "capabilities.dat"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CommonSens.class.getName()).
            log(Level.SEVERE, null, ex);
        }

        if(sc.hasNext()) {
            String dummy = sc.next();

            if(dummy.equals("capabilities")) {

                int numCaps = sc.nextInt();

                for(int i = 0; i < numCaps; i++) {

                    this.currCapabilities.add(readCapabilityFromFile(sc));
                }
            }
        }
    }

    /**
     * Reads the capability. Currently, the CapabilityDescription has
     * several sub-classes
     *
     * @param sc
     * @return
     */

    private Capability readCapabilityFromFile(Scanner sc) {

        String name = sc.next();

        //System.err.println("cap: name : " + name);

        String description = sc.next();

        //  System.err.println("cap: desc : " + description);

        CapabilityDescription desc = null;

        if(description.equals("range")) {

            int from = sc.nextInt();
            int to = sc.nextInt();

            desc = new RangeInt(from, to);
        } else if(description.equals("sensor")) {

            int from = sc.nextInt();
            String to = sc.next();

            desc = new SensorDetected(description, from, to);
            
        } else if(description.equals("switch")) {

            String from = sc.next();
            String to = sc.next();

            desc = new RangeString(description, from, to);

        } else if(description.equals("rSwitch")) {

        	/**
        	 * rSwitch connects to real sensors. 
        	 */
        	
            String from = sc.next();
            String to = sc.next();

            desc = new RangeString(description, from, to);
        } else if(description.equals("nSwitch")) {
        	
        	/**			
        	 * nSwitch connects to real sensors. The new version of
        	 * rSwitch. nSwitch = { ON , OFF }  
        	 */
    	
        	sc.next();
        	sc.next();
        	String from = sc.next();
        	sc.next();
        	String to = sc.next();
        	sc.next();

        	desc = new RangeString(description, from, to);
    }

        Capability tmpCap = new Capability(name, desc);

        return tmpCap;
    }

    /**
     * Reads trace file of the following format from a given file:
     *
     * [startTime    stopTime  sensorName  value]+
     *
     * The trace files generation will overwrite all start and stop times if
     * there are duplicates.
     *
     * @param sc
     * @return
     */

    private EmulatedTupleSource readEmulatedTupleSourceFromFile(Scanner sc) {

        EmulatedTupleSource tmpSens =
                new EmulatedTupleSource(readTupleSourceFromFile(sc));

        /**
         * Read trace file
         */

        String traceFile = sc.next();

        Scanner in = null;
        
        try {
            in = new Scanner(new File(dataPrefix + traceFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CommonSens.class.getName()).
            	log(Level.SEVERE, null, ex);
        }

        HashMap<Long, DataTuple> trace = new HashMap<Long, DataTuple>();

        double Hz = 0;

        if (tmpSens.getTupleSource() instanceof PhysicalSensor) {

            Hz = ((PhysicalSensor) tmpSens.getTupleSource()).getHz();

        } else if (tmpSens.getTupleSource() instanceof LogicalSensor) {

            Hz = ((LogicalSensor) tmpSens.getTupleSource()).getHz();

        } else /* ExternalSource */ {

            /**
             * Increase by 1 second for each iteration, i.e., this is not yet
             * fully implemented.
             */

            Hz = 1000;
        }

        while(in.hasNext()) {

            long startTimestamp = Long.parseLong(in.next());
            long stopTimestamp = Long.parseLong(in.next());
            String tmpTupleSourceName = in.next();
            String tmpSensorState = in.next();

            // The timestamps are in milliseconds.

            long i = startTimestamp;//Timestamp.toLong(startTimestamp);
            long stop = stopTimestamp;//Timestamp.toLong(stopTimestamp);

            for(; i < stop; i += Math.round(1000/Hz)) {

                trace.put(i, new DataTuple(tmpTupleSourceName,
                    tmpSens.getProvidedCapability(null), tmpSensorState,
                    i, i));

            }
        }

        ArrayList<DataTuple> aList = new ArrayList<DataTuple>(trace.size());

        // Sort the keys.

        Vector<Long> v = new Vector<Long>(trace.keySet());
        Collections.sort(v);

        Iterator<Long> it = v.iterator();

        // Put into arrayList

        while(it.hasNext()) {

            aList.add(trace.get(it.next()));
        }

        tmpSens.addTrace(aList);

        return tmpSens;
    }

    public Environment readEnvironmentFromFile(Scanner sc, 
    		int numTupleSources) {

        Environment tmpEnv = new Environment(mainPanel.getCore());

        String name = sc.next();

        tmpEnv.setName(name);

        int numObj = sc.nextInt();

        for(int i = 0; i < numObj; i++) {

            tmpEnv.addCEPObject(readObjectFromFile(sc));
        }

        String dummy;

        if(sc.hasNext()) {
            dummy = sc.next();

            if(dummy.equals("sensors")) {

                int numSens = sc.nextInt();

                if(numTupleSources == -1) {                    

                    for(int i = 0; i < numSens; i++) {
                    	
                        tmpEnv.addTupleSource(readTupleSourceFromFile(sc));
                    }
                } else {

                    for(int y = 0; y < numSens; y++) {
                    	
                        Sensor inTS = readTupleSourceFromFile(sc);

                        Sensor tmpTS;

                        /** 
                         * If this is the sensor attached to the person, do not
                         * multiply it.
                         */

                        if(((PhysicalSensor)inTS).
                                getObjectCoveredName() == null) {

                            for(int i = 0; i < numTupleSources; i++) {

                                tmpTS = 
                                	new PhysicalSensor((PhysicalSensor) inTS);

                                tmpTS.setName(tmpTS.getName() + "_" + i);

                                tmpEnv.addTupleSource(tmpTS);
                            }
                        } else {

                            tmpEnv.addTupleSource(inTS);
                        }
                    }
                }
            }
        }

        if(sc.hasNext()) {
            dummy = sc.next();

            if(dummy.equals("lois")) {

                int numLois = sc.nextInt();

                for(int i = 0; i < numLois; i++) {

                    tmpEnv.addCEPLoI(readLoIFromFile(sc));
                }
            }
        }

        if(sc.hasNext()) {
            dummy = sc.next();

            if(dummy.equals("persons")) {

                int numPersons = sc.nextInt();

                for(int i = 0; i < numPersons; i++) {

                    tmpEnv.addPerson(readPersonFromFile(sc));
                }
            }
        }

        if(sc.hasNext()) {

            dummy = sc.next();

            if(dummy.equals("currPos")) {

                tmpEnv.setCurrPos(readMovementFromFile(sc, true));
            }
        }

        return tmpEnv;
    }

    /**
     * We only have one environment per session. This method is
     * deprecated.
     */

    @Deprecated
    private void readEnvironments() {

//        Scanner sc = null;
//
//        try {
//            sc = new Scanner(new File(this.envPrefix + "environments.dat"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(CommonSens.class.getName()).
//    			log(Level.SEVERE, null, ex);
//        }
//
//        if(sc.hasNext()) {
//
//            String dummy = sc.next();
//
//            if(dummy.equals("environments")) {
//
//                int numEnvs = sc.nextInt();
//
//                for(int i = 0; i < numEnvs; i++) {
//
//                    this.currEnvs.add(readEnvironmentFromFile(sc, -1));
//                }
//            }
//        }
    }

    private ExternalSource readExternalSourceFromFile(Scanner sc) {

        ExternalSource tmpSens = new ExternalSource();

        String dummy = sc.next();

        if(dummy.equals("capabilities")) {

            int numCap = sc.nextInt();

            for(int i = 0; i < numCap; i++) {

                tmpSens.addProvidedCapability(readCapabilityFromFile(sc));
            }
        }

        dummy = sc.next();

        if(dummy.equals("type")) {

            String type = sc.next();

            tmpSens.setType(type);

        }

        dummy = sc.next();

        if(dummy.equals("name")) {

            String name = sc.next();

            tmpSens.setName(name);
        }

        return tmpSens;
    }

    private LogicalSensor readLogicalSensorFromFile(Scanner sc) {

        LogicalSensor tmpSens = new LogicalSensor();

        String dummy = sc.next();

        if(dummy.equals("Hz")) {

            String Hz = sc.next();

            tmpSens.setHz(Double.parseDouble(Hz));
        }

        dummy = sc.next();

        if(dummy.equals("capabilities")) {

            int numCap = sc.nextInt();

            tmpSens.setProvidedCapabilities(new ArrayList<Capability>());

            for(int i = 0; i < numCap; i++) {

                tmpSens.addProvidedCapability(readCapabilityFromFile(sc));
            }
        }

        dummy = sc.next();

        if(dummy.equals("dependedCapabilities")) {

            int numCap = sc.nextInt();

            for(int i = 0; i < numCap; i++) {

                tmpSens.addDependedCapability(readCapabilityFromFile(sc));
            }
        }

        dummy = sc.next();

        if(dummy.equals("function")) {

            String function = sc.next();
            
//            System.err.println("readLogSens = " + function);

            CustomFunction tmpFunc = loadCustomFunction(function);

            tmpSens.setFunction(tmpFunc);

        }

        dummy = sc.next();

        if(dummy.equals("type")) {

            String type = sc.next();

            tmpSens.setType(type);

        }

        dummy = sc.next();

        if(dummy.equals("name")) {

            String name = sc.next();

            tmpSens.setName(name);
        }

        return tmpSens;
    }

    private LocationOfInterest readLoIFromFile(Scanner sc) {

        LocationOfInterest tmpLoI = new LocationOfInterest();

        String dummy = sc.next();

        if(dummy.equals("capabilities")) {

            int numCap = sc.nextInt();

            for(int i = 0; i < numCap; i++) {
                tmpLoI.addCapability(readCapabilityFromFile(sc));
            }
        }

        dummy = sc.next();

        if(dummy.equals("shape")) {

            tmpLoI.setShape(readShapeFromFile(sc));
        }

        dummy = sc.next();

        if(dummy.equals("name")) {

            tmpLoI.setName(sc.next());
        }

//        System.err.println("readLoIFromFile returns " + tmpLoI.getObject());
        
        return tmpLoI;
    }

    public ArrayList<Triple> readMovementFromFile(Scanner sc, boolean env) {

        int size;

        if(env) {
            size = sc.nextInt();
        } else {
            @SuppressWarnings("unused")
			String dummy = sc.next();
            size = sc.nextInt();
        }

        ArrayList<Triple> tmpList = new ArrayList<Triple>(size);

        for(int i = 0; i < size; i++) {

            int x = Integer.valueOf(sc.next());
            int y = Integer.valueOf(sc.next());
            int z = Integer.valueOf(sc.next());

            tmpList.add(new Triple(x, y, z));
        }

        return tmpList;
    }

    private CommonSensObject readObjectFromFile(Scanner sc) {

        String name =  sc.next();

        //System.err.println("name = " + name);

        CommonSensObject tmpObject = new CommonSensObject(name);

        String dummy = sc.next();

        //System.err.println("dummy = " + dummy);

        if(dummy.equals("shape")) {
            tmpObject.setShape(readShapeFromFile(sc));
        }

        dummy = sc.next();

        //System.err.println("dummy = " + dummy);

        if(dummy.equals("permeabilities")) {

            int tmpInt = sc.nextInt();

            for(int i = 0; i < tmpInt; i++) {

                tmpObject.addPerm(readPermeabilityFromFile(sc));
            }
        }

        return tmpObject;
    }

    private void readObjects() {

        Scanner sc = null;

        try {
            sc = new Scanner(new File(dataPrefix + "objects.dat"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CommonSens.class.getName()).
            log(Level.SEVERE, null, ex);
        }

        String dummy = sc.next();

        if(dummy.equals("object")) {

            int numObjects = sc.nextInt();

            for(int i = 0; i < numObjects; i++) {

                this.currObjects.add(readObjectFromFile(sc));
            }
        }
    }

    private Permeability readPermeabilityFromFile(Scanner sc) {

        String type = sc.next();

        String val = sc.next();

        //System.err.println("perm type = " + type + " val = " + val);

        return new Permeability(new SignalType(type), Double.valueOf(val));

    }

    private Person readPersonFromFile(Scanner sc) {

        String name = sc.next();

        String dummy = sc.next();

        Shape tmpShape = null;

        if(dummy.equals("shape")) {
            tmpShape = readShapeFromFile(sc);
        }

        return new Person(name, tmpShape, cepQueue);
    }

    private PhysicalSensor readPhysicalSensorFromFile(Scanner sc) {

        PhysicalSensor tmpSens = new PhysicalSensor();

        String dummy = sc.next();
        
        if(dummy.equals("signalType")) {

            String signalType = sc.next();

            tmpSens.setSignalType(new SignalType(signalType));
        }

        dummy = sc.next();

        if(dummy.equals("coverageArea")) {

            tmpSens.setCoverageArea(readShapeFromFile(sc));
        }

        dummy = sc.next();

        if(dummy.equals("Hz")) {

            String Hz = sc.next();

            tmpSens.setHz(Double.parseDouble(Hz));
        }

        dummy = sc.next();

        if(dummy.equals("capabilities")) {

            int numCap = sc.nextInt();

            for(int i = 0; i < numCap; i++) {

            	Capability tmpCap = readCapabilityFromFile(sc);
            	            	
                tmpSens.addProvidedCapability(tmpCap);
            }
        }

        dummy = sc.next();

        if(dummy.equals("type")) {

            String type = sc.next();

            tmpSens.setType(type);

        }

        dummy = sc.next();

        if(dummy.equals("name")) {

            String name = sc.next();

            tmpSens.setName(name);
        }

        if(sc.hasNext()) {

            dummy = sc.next();

            if(dummy.equals("object")) {

                String objectName = sc.next();

                if(!objectName.equals("null")) {

                    /** 
                     * The sensors are only connected to objects when in an
                     * environment. Only when an environment is opened, the
                     * sensors and objects are connected. This is done in
                     * getMainPanel.
                     */

                    tmpSens.setObjectConnectedToString(objectName);
                }
            }
        }
        
        return tmpSens;
    }

    private void readSensorsFromFile() {

        Scanner sc = null;

        try {
            sc = new Scanner(new File(dataPrefix + "sensors.dat"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CommonSens.class.getName()).
            log(Level.SEVERE, null, ex);
        }

        if(sc.hasNext()) {
            String dummy = sc.next();

            if(dummy.equals("sensors")) {

                int numSensors = sc.nextInt();

                for(int i = 0; i < numSensors; i++) {

                    addTupleSource(readTupleSourceFromFile(sc));
                }
            }
        }
    }

    /**
     * The shape can be denoted "null", which means that the shape is empty.
     * This is typical for situations where the system only reads from
     * trace files and one does not have knowledge about the environment.
     *
     * @param sc
     * @return
     */

    private Shape readShapeFromFile(Scanner sc) {

        String shapeName = sc.next();

        if(shapeName.equals("null")) {
            return new Shape(shapeName);
        }

        int numTriples = sc.nextInt();

        //System.err.println("shape name = " + shapeName);

        Shape tmpShape = new Shape(shapeName);

        int startX = sc.nextInt();
        int startY = sc.nextInt();
        int startZ = sc.nextInt();

        tmpShape.setStartPoint(new Triple(startX, startY, startZ));

        for(int i = 0; i < numTriples; i++) {

            startX = sc.nextInt();
            startY = sc.nextInt();
            startZ = sc.nextInt();

            tmpShape.addTriple(new Triple(startX, startY, startZ));
        }

//        tmpShape.createRays();

        return tmpShape;
    }

    private Sensor readTupleSourceFromFile(Scanner sc) {

        String tupleSourceType = sc.next();
        
        //System.err.println("tupleSourceType = " + tupleSourceType);
        
        if(tupleSourceType.equals("PhysicalSensor")) {

            return readPhysicalSensorFromFile(sc);

        } else if(tupleSourceType.equals("LogicalSensor")) {

            return readLogicalSensorFromFile(sc);

        } else if (tupleSourceType.equals("ExternalSource")) {

            return readExternalSourceFromFile(sc);

        } else if (tupleSourceType.equals("EmulatedTupleSource")) {

            /**
             * Check which type of sensor that is emulated. We use recursion
             * to simplfy the code.
             */

            return readEmulatedTupleSourceFromFile(sc);

        } else if (tupleSourceType.equals("stored")) {

            /**
             * The tuple source is already stored in the system. This happens
             * sometimes if the tuple source is an EmulatedTupleSource.
             */

            return findTupleSource(sc.next());

        } 

        System.err.println("Error in parsing in readSensorFromFile = " +
                    tupleSourceType);
        
        return null;
    }

    public void restartTimer(boolean emulation) {
    	
    	timer.restart(emulation);
    }

    public File SensFile () {
        return sensFile;
    }

    public void SensFile (File val) {
        this.sensFile = val;
    }

    public void setComplexStatements (ArrayList<QueryElement> val) {
        this.complexStatements = val;
    }

    public void setEnvironmentFilename(String get) {
        environmentFilename = get;
    }

    public void setExpPrefix(String tmp) {
        this.expPrefix = tmp;
    }

    public void setMainPanel (MainView val) {
        this.mainPanel = val;
    }

    public void setMovementFilename(String get) {
        movementFilename = get;
    }

    public void setStatementFilename(String get) {
        statementFilename = get;
    }

    public boolean ShowPalette () {
        return showPalette;
    }

    public void ShowPalette (boolean val) {
        this.showPalette = val;
    }

    /**
     * Starts the sensors by setting environment, global time, shared queue,
     * out prefix (for evaluation results) and evaluation result files for
     * each of the sensors.
     *
     * In emulation mode, the system emulates the person who is resided in
     * the home.
     *
     * In the simulation mode, the system can read values from a stored
     * file. Currently, none of the instantiation parts described above
     * will be performed. When the system pulls the sensors, it will
     * simply get the content of the trace file.
     *
     * @param emulation
     * @param runNumber
     * @param option
     */

    public void startSensors(boolean emulation, int runNumber, int option) {

        if(!currEnvs.isEmpty()) {
            for(Sensor tmpSens : currEnvs.get(0).getTupleSources()) {

                tmpSens.startSensor(currEnvs.get(0),
                        timer, cepQueue, expPrefix, runNumber);

                if (emulation) {

                    System.err.println("Starting sensor " + tmpSens.getName());

                    /**
                     * Go through all the capabilities and see if the sensor
                     * provides a capability that needs to be started 
                     * externally.
                     */
                    
                    for(Capability tmpCap : tmpSens.
                    		getProvidedCapabilities()) { 
                    	
                    	System.err.println("Checking capability " + 
                    			tmpCap.getName());
                    
                    	if(tmpCap.getDescription().getDescription().
                    			equals("rSwitch") || 
                    			tmpCap.getDescription().getDescription().
                    			equals("nSwitch")) {
                    		
                    		/**
                    		 * Start up the OpenCV application.
                    		 */
                    		
                    		String sensNum = String.valueOf(Integer.
                    				parseInt(tmpSens.getName().
                    				substring(3, 4)) - 1);
                    		
                    		String [] cmd = {motionDetector, sensNum}; 
                    		
                    		try {
                    			
                    			if(tmpCap.getDescription().getDescription().
                            			equals("rSwitch")) {
                    			
									Process tmpProc = 
										Runtime.getRuntime().exec(cmd);
									
								/** 
								 * Sleep a while to make the external 
								 * process start up correctly. 
								 */
								
//								try {
//									Thread.sleep(5000);
//								} catch (InterruptedException e) {
//									e.printStackTrace();
//								}

//								BufferedReader read = 
//									new BufferedReader(
//											new InputStreamReader(tmpProc.
//													getInputStream()));
//								
//								System.err.println("Process says: " + 
//										read.readLine());
								
									processes.add(tmpProc);
								
                    			}
									
								// Try opening a socket to the sensor.
								
								while(!tmpSens.openSocket()) {
									
									System.err.println("Waiting for socket");
									
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}									
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						
                    	}
                    }                    
//                    Thread thr = new Thread(tmpSens);
//
//                    thr.start();
                }
            }
        } else {
            System.err.println("startSensors(): currEnvs.isEmpty()");
        }
    }

    public void startTimer(boolean emulation) {

        timer = new Timer(emulation);
    }

    public void stopSensors() {
        for(Sensor tmpSens : currEnvs.get(0).getTupleSources()) {

            tmpSens.stopSensor();
            tmpSens.closeFile();
        }
        
        for(Process tmpProc : processes) {
        	
        	tmpProc.destroy();
        }
    }

    public void storeCapabilities() throws IOException {

        FileWriter fOut = new FileWriter(new File(dataPrefix + 
        		"capabilities.dat"));

        BufferedWriter bOut = new BufferedWriter(fOut);

        bOut.write("capabilities " + getCapabilities().size() + "\n");

        for(Capability tmpCap : getCapabilities()) {

            bOut.write(tmpCap.getObject() + "\n");
        }

        bOut.close();
    }

    public void storeEnvironments() throws IOException {

        FileWriter fOut = new FileWriter(new File(dataPrefix + 
        		"environments.dat"));

        BufferedWriter bOut = new BufferedWriter(fOut);

        bOut.write("environments " + getEnvironments().size() + "\n");

        for(Environment tmpEnv : getEnvironments()) {

            bOut.write(tmpEnv.getEnvironment() + "\n");
        }

        bOut.close();
    }

    public void storeLoIs() throws IOException {

        FileWriter fOut = new FileWriter(new File(dataPrefix + "lois.dat"));

        BufferedWriter bOut = new BufferedWriter(fOut);

        bOut.write("lois " + this.getLoIs().size() + "\n");

        for(LocationOfInterest tmpLoI : getLoIs()) {

            bOut.write(tmpLoI.getObject() + "\n");
        }

        bOut.close();
    }

    public void storeObjects() throws IOException {

        FileWriter fOut = new FileWriter(new File(dataPrefix + "objects.dat"));

        BufferedWriter bOut = new BufferedWriter(fOut);

        bOut.write("object " + getObjects().size() + "\n");

        for(CommonSensObject tmpObj : getObjects()) {

            bOut.write(tmpObj.getObject() + "\n");
        }

        bOut.close();
    }

    public void storeSensors() throws IOException {

        FileWriter fOut = new FileWriter(new File(dataPrefix + "sensors.dat"));

        BufferedWriter bOut = new BufferedWriter(fOut);

        bOut.write("sensors " + this.getTupleSources().size() + "\n\n");

        for(Sensor tmpSens : getTupleSources()) {

            if(tmpSens instanceof PhysicalSensor) {
                bOut.write("PhysicalSensor ");
            } else if (tmpSens instanceof LogicalSensor) {
                bOut.write("LogicalSensor ");
            } else if (tmpSens instanceof ExternalSource) {
                bOut.write("ExternalSource ");
            }

            bOut.write(tmpSens.getObject() + "\n\n");
        }

        bOut.close();
    }

    @SuppressWarnings("unused")
	private void writeTable(ArrayList<ArrayList<String>> tTable) {
        try {
            BufferedWriter bOut = null;
            
            // Write this data to a LaTeX table:

            bOut = new BufferedWriter(new FileWriter(new File(expPrefix + 
            		"coverageTable.tex")));
            
            bOut.write("{\\small\n\\begin{table*}\\centering\n" +
            		"\\caption{Coverage of the areas.}\n\\begin{tabular}{");
            for (int i = 0; i < tTable.get(0).size(); i++) {
                bOut.write("|c");
            }
            bOut.write("|} \\hline\n");
            for (ArrayList<String> ts : tTable) {
                for (int i = 0; i < ts.size(); i++) {
                    
                    bOut.write(ts.get(i));
                    
                    if (i < ts.size() - 1) {
                        bOut.write("&");
                    } else {
                        bOut.write("\\\\ \\hline\n");
                    }
                }
            }
            bOut.write("\\hline\\end{tabular}\n\\label{tab:coverage}\n" +
            		"\\end{table*}}\n");

            bOut.close();
        } catch (IOException ex) {
            Logger.getLogger(CommonSens.class.getName()).
            	log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * From http://www.java-forums.org/advanced-java/4130-rounding-double-
     * two-decimal-places.html
     * 
     * @param d
     * @return
     */
    
    public static String roundTwoDecimals(double d) {
    	
    	DecimalFormat twoDForm = new DecimalFormat("#.##");
    	
    	return twoDForm.format(d).replace( ',', '.' );
    }
}


