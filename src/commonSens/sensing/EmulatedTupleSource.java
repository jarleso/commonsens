/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sensing;

import environment.Environment;
import eventProcessor.DataTuple;
import eventProcessor.Timer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author jarleso
 */
public class EmulatedTupleSource extends Sensor {

    Sensor sensor = null;
    ArrayList<DataTuple> trace;

    int globalTraceCounter = 0;

    /**
     * The object inherits the attributes of the tuple source
     * it emulates.
     *
     * @param sensor
     */

    public EmulatedTupleSource(Sensor sensor) {

        this.sensor = sensor;
    }

    public Sensor getTupleSource() {
        return sensor;
    }

    @Override
    public ArrayList<Capability> getProvidedCapabilities() {
        return sensor.getProvidedCapabilities();
    }

    @Override
    public void setProvidedCapabilities(ArrayList<Capability> capabilities) {
        sensor.setProvidedCapabilities(capabilities);
    }

    @Override
    public void addProvidedCapability(Capability tmpCap) {
        sensor.addProvidedCapability(tmpCap);
    }

    @Override
    public boolean getIsCapabilityProvided(String name) {
        return sensor.getIsCapabilityProvided(name);
    }

    @Override
    public Capability getProvidedCapability(String name) {

        return sensor.getProvidedCapability(name);
    }

    @Override
    public void setOutPrefix(String outPrefix) {

        sensor.setOutPrefix(outPrefix);
    }

    @Override
    public void setupFile(int runNumber) {
        sensor.setupFile(runNumber);
    }

    @Override
    public void closeFile() {
        sensor.closeFile();
    }

    @Override
    protected void writeToFile(String text) {
        sensor.writeToFile(text);
    }

    @Override
    protected void writeToPullFile(String text) {
        sensor.writeToPullFile(text);
    }

    @Override
    public String getName() {
    	
//    	if(sensor == null) {
//    		return "";
//    	}
    	
        return sensor.getName();
    }

    @Override
    public void setName(String name) {
        sensor.setName(name);
    }

    @Override
    public String getType() {
        return sensor.getType();
    }

    @Override
    public void setType(String type) {
        sensor.setType(type);
    }

    @Override
    public void setCurrEnv(Environment currEnv) {
        sensor.setCurrEnv(currEnv);
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<DataTuple> cepQueue) {
        sensor.setQueue(cepQueue);
    }

    @Override
    public ConcurrentLinkedQueue<DataTuple> getQueue() {
        return sensor.getQueue();
    }

    @Override
    public void setGlobalTime(Timer timer) {
        sensor.setGlobalTime(timer);
    }

    @Override
    public void stopSensor() {
        sensor.stopSensor();
    }

    @Override
    public void startSensor(Environment currEnv, Timer timer, ConcurrentLinkedQueue<DataTuple> cepQueue, String expPrefix, int runNumber) {

        globalTraceCounter = 0;

        sensor.startSensor(currEnv, timer, cepQueue, expPrefix, runNumber);
    }

    @Override
    public String getCapabilitiesText() {
        return sensor.getCapabilitiesText();
    }

    @Override
    public String getObject() {

        return sensor.getObject();
    }

    /**
     * When the sensor is pulled, timeSeconds implies the current timestamp.
     * The method finds the data tuples that matches the timeSeconds
     * timestamp. If there are several alternative data tuples that match
     * the timestamp, i.e., several data tuples belong to the same second,
     * the method simply chooses the one that is closest. The closest
     * timestamp is the one with the smalles millisecond.
     *
     * @param timeSeconds
     * @param simple
     */

    @Override
    public void pullThisSensor(long timeMilliseconds, boolean simple) {

        System.err.println("inside pullThisSensor in EmulatedTupleSource" +
        		" with globalTraceCounter = "
                	+ globalTraceCounter);

        // Correct the timing. 

        System.err.println("EmulatedTupleSource.pullThisSensor trace.size() = " + trace.size());

        while((globalTraceCounter < trace.size() - 1) &&
                trace.get(globalTraceCounter).getStartTimeSecondsRounded()
                < timeMilliseconds) {

            globalTraceCounter++;
        }

//        System.err.println("globalTraceCounter has increased to " + globalTraceCounter);
//        System.err.println("timeMilliseconds = " + timeMilliseconds);

        if(trace.get(globalTraceCounter).getStartTimeSecondsRounded() == timeMilliseconds) {

            if(getQueue() != null) {
            	
            	System.err.println("Adding data tuple: " + trace.get(globalTraceCounter).getObject());
            	
                getQueue().add(trace.get(globalTraceCounter));
            } else {
                System.err.println("pullThisSensor(): dataTupleQueue == null");
            }
        } else {
            System.err.println("ERROR: " + trace.get(globalTraceCounter).getStartTimeSecondsRounded()
                     + " != " + timeMilliseconds);
        }
    }

    public void run() {
        throw new UnsupportedOperationException("NotOperator supported yet.");
    }

    public void addTrace(ArrayList<DataTuple> trace) {
        this.trace = trace;
    }
}
