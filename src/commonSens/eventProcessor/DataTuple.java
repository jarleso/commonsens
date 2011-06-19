/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

import sensing.Capability;

/**
 *
 * @author jarleso
 */
public class DataTuple {

    String sensorName;
    Capability capability;
    Object value;
    long startTime;
    long stopTime;

    public DataTuple(String sensorName, Capability capability, Object value, 
    		long startTime, long stopTime) {

        this.sensorName = sensorName;
        this.capability = capability;
        this.value = value;
        this.startTime = startTime;
        this.stopTime = stopTime;
    }

    DataTuple(DataTuple tmpTuple) {
        this.sensorName = new String(tmpTuple.getSensorName());
        this.capability = new Capability(tmpTuple.getCapability());

        if(tmpTuple.getValue() instanceof Integer) {
            this.value = new Integer((Integer) tmpTuple.getValue());
        } else if(tmpTuple.getValue() instanceof Double) {
            this.value = new Double((Double) tmpTuple.getValue());
        } else if(tmpTuple.getValue() instanceof String) {
            this.value = new String((String) tmpTuple.getValue());
        }

        this.startTime = tmpTuple.startTime;
        this.stopTime = tmpTuple.stopTime;
    }

    public DataTuple(String sensorName, Capability capability, Object value) {

    	this.sensorName = sensorName;
        this.capability = capability;
        this.value = value;
        this.startTime = -1;
        this.stopTime = -1;
	}

	public String getSensorName() {
        return sensorName;
    }

    public Capability getCapability() {
        return capability;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStartTimeSecondsRounded() {

//        System.err.println("getStartTimeSecondsRounded changes "
//                + getStartTime() + " to " +
//                ((getStartTime()/1000) * 1000));

        return (getStartTime()/1000) * 1000;
    }

    public long getStopTime() {
        return stopTime;
    }

    public Object getValue() {
        return value;
    }

    /**
     * For compatibility.
     * 
     * @return
     */
    
    public String getObject() {
    	return showTuple();
    }
    
    public String showTuple() {

        String retTuple = "";

        retTuple += getSensorName() + " ";
        retTuple += getCapability().getName() + " ";

        Object tmpValue = getValue();

        if(tmpValue instanceof Integer) {
            retTuple += (Integer) tmpValue + " ";
        } else if(tmpValue instanceof Double) {
            retTuple += (Double) tmpValue + " ";
        } else if(tmpValue instanceof String) {
            retTuple += (String) tmpValue + " ";
        }

        retTuple += getStartTime() + " " + getStopTime();
        
        return retTuple;
    }

    public void setSensorName(String sensorName) {

        this.sensorName = sensorName;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }
}
