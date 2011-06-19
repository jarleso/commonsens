/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

import environment.Environment;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelViewController.Core;

/**
 *
 * @author jarleso
 */
public class MainDataTupleFilter {

    ArrayList<DataTupleFilter> currentFilters;
    ConcurrentLinkedQueue<DataTuple> dataTupleQueue;
    private Environment currEnvir;
    private final ArrayList<DataTuple> batch;

    private BufferedWriter outTupleSources;
    private BufferedWriter outMeasurements;
    private BufferedWriter outStateChanges;

	private int averageCounter = 0;
    private double averageProcessingTime = -1;
    private long evaluateBatchStart, evaluateBatchStop;
    private long queuePullingStart, queuePullingStop;
    private long /*stateEvaluationStart,*/ stateEvaluationStop;
    private long getTupleSourcesToPullStart, getTupleSourcesToPullStop;
    private final Timer globalTime;
    private long batchTime = -1; // NotOperator really needed.
    private int numberOfTupleSourcesToPull = 0;
    private String outPrefix;

    /**
     * endMessage is initiated with DataTupleFilter.M_NO_TRIGGER, since
     * this means that non of the filters are
     * triggered.
     */

    private int endMessage = DataTupleFilter.M_NO_TRIGGER;

    public MainDataTupleFilter(ConcurrentLinkedQueue<DataTuple> cepQueue,
            Environment currEnvir, Timer globalTime, int runNumber,
            int numSensors, int numAnds, String outPrefix) {

        currentFilters = new ArrayList<DataTupleFilter>();

        this.dataTupleQueue = cepQueue;
        this.currEnvir = currEnvir;
        this.globalTime = globalTime;
        this.outPrefix = outPrefix;

        batch = new ArrayList<DataTuple>();

        setupFiles(runNumber, numSensors, numAnds);
    }

    /**
     * Go through all the currentFilters and get a list is all the
     * tuple sources that should be pulled. All the tuple sources
     * that should be pulled are obtained as HashMap to avoid that
     * the same tuple source is pulled twice. 
     *
     * @param currentTime
     * @param numSensors
     */

    HashMap<String, String> toPull = new HashMap<String, String>();;
    Iterator<String> it;

    private void pullSensors(long currentTimeMilliseconds, 
    		int numSensors) {

        toPull.clear();

//        System.err.println("MainDataTupleFilter: Going through currentFilters with size " + 
//        		currentFilters.size());

        for(DataTupleFilter tmpFilter : currentFilters) {

            toPull.putAll(tmpFilter.getSensorsToPull(/*currentTime, 
            												numSensors*/));
        }

        // Run through the tuple sources.

        it = toPull.keySet().iterator();

//        System.err.println("About to pull sensors from toPullAndEvaluate " + 
//        		"with size " + toPullAndEvaluate.size());

        while (it.hasNext()) {

            String sensorName = it.next();

//            System.err.println("Pulling " + sensorName);

            // Only "really" pull the sensors that end with _0
            // to save time. This is done in the DEBS experiments so that
            // all tuple sources that provide the same data do not have to
            // be pulled. We can do this because we do not measure the time
            // it takes to pull the sensors. This is fixed in getBatch, so
            // that the state receives the correct amount of data tuples.

            if (sensorName.matches(".+_0$") || !sensorName.matches(".+_.+$")) {

//                System.err.println("Pulling " + sensorName);

//                System.err.println("The sensors in current env: ");
//
//                for(Sensor ts : getCurrEnvironment().getTupleSources()) {
//                    System.err.println(ts.getName());
//                }

                getCurrEnvironment().getTupleSource(sensorName).
                						pullThisSensor(currentTimeMilliseconds, 
                								true);
            }
        }
    }

    /**
     * Pulls and evaluates all the statements that are running in the system.
     * Returns the endMessage from the last of the statements that were
     * evaluated.
     * 
     * The method avoids redundancy by by not pulling the same sensor twice.
     *
     * @param currentTime
     * @param numTuples
     * @param numSensors
     * @return
     */

    HashMap<String, DataTuple> toPullAndEvaluate =
            new HashMap<String, DataTuple>();
    ArrayList<DataTupleFilter> toBeRemoved = new ArrayList<DataTupleFilter>();
    HashMap<String, String> relevantTSs = new HashMap<String, String>();
    int retValue;

    public int pullAndEvaluate(long currentTimeMilliseconds, int numTuples, 
    		int numSensors) {

        /**
         * Pull the correct tuple sources. dataTupleQueue is then filled with
         * data tuples.
         */

        getTupleSourcesToPullStart = System.currentTimeMillis();

        pullSensors(currentTimeMilliseconds, numSensors);

        getTupleSourcesToPullStop = System.currentTimeMillis() - 
        								getTupleSourcesToPullStart;

        toPullAndEvaluate.clear();

        numTuples = 0;

        queuePullingStart = System.currentTimeMillis();

        evaluateBatchStart = System.currentTimeMillis();

        while (!dataTupleQueue.isEmpty()) {

            DataTuple tmpTuple = dataTupleQueue.poll();

            if (tmpTuple != null) {

                // Run simulation. Note that we set <= since we want the first
                // sensors to run as well.

                for (int i = 0; i <= numSensors; i++) {

                    // Copy the tuple and send it.

                    DataTuple newTuple = new DataTuple(tmpTuple);

                    // Extract the prefix from the name.

                    String sensName = newTuple.getSensorName();

                    newTuple.setSensorName(sensName.replaceAll("_0$", "_" + i));

//                    System.err.println("MainDataTupleFilter: Putting " + 
//                    		newTuple.getSensorName() + 
//                    		" in toPullAndEvaluate");

                    toPullAndEvaluate.put(newTuple.getSensorName(), newTuple);

                    //System.err.println("Tuple : " + newTuple.showTuple());

                    numTuples += 1;
                }
            } else {
            }
        }

        /**
         * toPullAndEvaluate now contains all the data tuples from the correct
         * data sources.
         *
         * For each of the filters, find the relevant tuple sources and
         * send the matching data tuple to the filter as ArrayList<DataTuple>.
         */

        relevantTSs.clear();

        toBeRemoved.clear();

        for(DataTupleFilter tmpFilter : currentFilters) {

            batch.clear();
            relevantTSs.clear();
            
            relevantTSs.putAll(tmpFilter.getSensorsToPull(null,
                    currentTimeMilliseconds, true));

            Iterator<String> relTSsIterator = relevantTSs.keySet().iterator();

            while(relTSsIterator.hasNext()) {

                String currRelevantTS = relTSsIterator.next();

                DataTuple tmpTuple = toPullAndEvaluate.get(currRelevantTS);

//                System.err.println("MainDataTupleFilter.pullAndEvaluate " +
//                		"adding to batch:" + currRelevantTS);

                if(tmpTuple != null) {

                    //System.err.println("\t" + tmpTuple.showTuple());

                    batch.add(tmpTuple);
                } else {
                    //System.err.println("\tIt was null.");
                }
            }

            /**
             * The batch should now contain all the relevant data tuples. 
             * Currently the tuple processing is not threaded, so we do not 
             * have to copy the batch.
             */

//            if(batch.isEmpty()) {
//                System.err.println("MainFilter.pullAndEvaluate is sending" +  
//            		" empty batch to tmpFilter");
//            }

            /**
             * If retValue is any
             */

            tmpFilter.getBatch(batch);

            if(tmpFilter.isFinished()) {

            	System.err.println("pullAndEvaluate: tmpFilter is finished");
            	
                toBeRemoved.add(tmpFilter);
            }
        }

        for(DataTupleFilter tmpFilter : toBeRemoved) {

//            System.err.println("MainDataTupleFilter setting endMessage to " +
//                    tmpFilter.getEndMessage());

            endMessage = tmpFilter.getEndMessage();

            currentFilters.remove(tmpFilter);
        }

        evaluateBatchStop = System.currentTimeMillis() - evaluateBatchStart;

        queuePullingStop = System.currentTimeMillis() - queuePullingStart;

//        System.err.println(getTime() + "\t" + batchTime + "\t"
//                + batch.size() + "\t" + evaluateBatchStop + "\t"
//                + queuePullingStop + "\t" + stateEvaluationStop + "\t" + 
//        			numberOfTupleSourcesToPull + "\n");

        writeMeasurementsFile(this.getTime() + "\t" + batchTime + "\t"
                + batch.size() + "\t" + evaluateBatchStop + "\t"
                + queuePullingStop + "\t" + stateEvaluationStop + "\t"
                + getTupleSourcesToPullStop + "\t" + 
                numberOfTupleSourcesToPull + "\n");

        this.addToAverageProcessingTime(evaluateBatchStop);
        
        evaluateBatchStop = 0;
        queuePullingStop = 0;
        stateEvaluationStop = 0;
        getTupleSourcesToPullStop = 0;

        if(currentFilters.isEmpty()) {

            /**
             * All the filters are finished. Close the files and signal to
             * MainPanel that the experiment is finished.
             */

            // Commented out for investigation

            //closeFile();

//        	System.err.println("MainDataTupleFilter.pullAndEvaluate returns " +
//        			"DataTupleFilter.M_FINISHED");
        	
        	// NotOperator so nice approach, but ok for now.
        	
        	// endMessage = DataTupleFilter.M_FINISHED;
        	
            return endMessage;// DataTupleFilter.M_FINISHED;
        }

//    	System.err.println("MainDataTupleFilter.pullAndEvaluate returns " +
//    			"DataTupleFilter.M_NO_TRIGGER");
        
        /**
         * One or more of the filters are not finished. 
         */
        
        return DataTupleFilter.M_NO_TRIGGER;
    }

    public void addDataTupleFilter(DataTupleFilter sigmaFilter) {

        currentFilters.add(sigmaFilter);
    }

    public void removeSigmaFilter(DataTupleFilter sigmaFilter) {

        if(!currentFilters.remove(sigmaFilter))
            System.err.println("MainFilter does not contain sigmaFilter " +
            		"to be removed.");
    }

    /**
     * For now the method sends the end message from the first filter.
     *
     * @return
     */

    public int getEndMessage() {

        return endMessage;
    }

    private Environment getCurrEnvironment() {
        return currEnvir;
    }

    // Logging:

     /**
     * Setting up the files that show what is going on in the system. For 
     * DEBS10, the outMeasurements BufferedWriter is used.
     *
     * @param runNumber
     * @param numSensors
     * @param numAnds
     */
    private void setupFiles(int runNumber, int numSensors, int numAnds) {
        try {
            outTupleSources = new BufferedWriter(new FileWriter(outPrefix + 
            		"/" + runNumber + "_" + numSensors + "_" + numAnds + 
            		"_TupleSources.dat"));

            System.err.println("Sigma filter setting up measurement file " + 
            		outPrefix + "/" + runNumber + "_" + numSensors + "_" + 
            		numAnds + "_Measurements.dat");

            outMeasurements = new BufferedWriter(new FileWriter(outPrefix + 
            		"/" + runNumber + "_" + numSensors + "_" + numAnds + 
            		"_Measurements.dat"));
            outStateChanges = new BufferedWriter(new FileWriter(outPrefix + 
            		"/" + runNumber + "_" + numSensors + "_" + numAnds + 
            		"_StateChanges.dat"));
        } catch (IOException ex) {
            Logger.getLogger(DataTupleFilter.class.getName()).
        		log(Level.SEVERE, null, ex);
        }
    }

    public void closeFile() {

        System.err.println("Files are closing.");

        try {
            outTupleSources.close();
            outMeasurements.close();
            outStateChanges.close();
        } catch (IOException ex) {
            Logger.getLogger(QueryPoolElement.class.getName()).
            	log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unused")
    private final void writeToTupleSourcesFile(String text) {
        try {
            outTupleSources.write(text);
        } catch (IOException ex) {
            // out is closed. End experiment.
        }
    }

    private void writeMeasurementsFile(String text) {
        try {

            outMeasurements.write(text);
//            System.err.println("Has written " + text + " to outMeasurements.");
        } catch (IOException ex) {

            System.err.println("writeMeasurementsFile: outMeasurements " +
            		"is closed.");
        }
    }
    
	public void writeStateChangesFile(String text) {
        try {
            outStateChanges.write(text);
        } catch (IOException ex) {
            // out is closed. End experiment.
        }
    }

    private long getTime() {

        return globalTime.getCurrentMillisecond(Core.Hz);
    }

    /**
     * Gives the average processing time in milliseconds.
     * @return
     */
    
	public double getAverageProcessingTime() {
		return averageProcessingTime;
	}
	
	public void addToAverageProcessingTime(double value) {

		averageCounter   += 1;
		
		averageProcessingTime = ((averageProcessingTime * 
				(averageCounter - 1)) + 
				value) / (averageCounter);

	}
}
