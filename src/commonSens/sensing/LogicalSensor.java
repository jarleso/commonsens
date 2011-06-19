/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensing;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import eventProcessor.DataTuple;

/**
 *
 * @author jarleso
 */
public class LogicalSensor extends Sensor implements Runnable {

    double Hz;
    ArrayList<Capability> dependedCapabilities;

    CustomFunction customFunction;

    public LogicalSensor() {

        dependedCapabilities = new ArrayList<Capability>();
    }

    public LogicalSensor(LogicalSensor tmpSensor) {

        for (Capability tmpCap : tmpSensor.getProvidedCapabilities()) {

            providedCapabilities.add(new Capability(tmpCap));
        }

        dependedCapabilities = new ArrayList<Capability>();

        for (Capability tmpCap : tmpSensor.getCapabilitiesNeeded()) {

            dependedCapabilities.add(new Capability(tmpCap));
        }

        this.Hz = tmpSensor.getHz();
    }

    public void run() {

        if (currEnv != null) {

            while (!stop) {

                pullThisSensor(-1, false);

                try {
                    Thread.sleep((long) (1000 / getHz()));
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public double getHz() {
        return Hz;
    }

    public void setHz(double Hz) {
        this.Hz = Hz;
    }

    public String getCapabilitiesText() {

        String ret = "providedCapabilities " + providedCapabilities.size() + "\n";

        for (Capability cap : providedCapabilities) {

            ret += cap.getName() + " ";
        }

        ret += "dependedCapabilities " + dependedCapabilities.size() + "\n";

        for (Capability cap : dependedCapabilities) {

            ret += cap.getName() + " ";
        }

        return ret;
    }

    public ArrayList<Capability> getCapabilitiesNeeded() {
        return dependedCapabilities;
    }

    public String getObject() {

        String tmpString = "";

        tmpString += "samplingFrequency " + Hz + " ";

        tmpString += getCapabilitiesText();

        tmpString += "type " + type + " ";
        tmpString += "name " + name + " ";

        return tmpString;
    }

    public void addDependedCapability(Capability tmpCap) {
        this.dependedCapabilities.add(tmpCap);
    }

    public DataTuple pull(Capability cap, Object value, long currentSecond) {

        DataTuple retTuple;

        if (currentSecond == -1) {

            retTuple = new DataTuple(getName(), cap,
                    value, timer.getCurrentSecond(),
                    timer.getCurrentSecond());
        } else {
            retTuple = new DataTuple(getName(), cap,
                    value, currentSecond,
                    currentSecond);
        }

        writeToPullFile(retTuple.showTuple());

        return retTuple;
    }

    /**
     * Calls customFunction.pullDependedTupleSources(currentSecond, simple),
     * which performs the computation and puts the data tuple in the
     * queue.
     *
     * @param currentSecond
     * @param simple
     */

    public void pullThisSensor(long currentSecond, boolean simple) {

        //System.err.println("LogicalSensor.pullThisSensor()");

        customFunction.pullDependedTupleSources(currentSecond, simple);
    }

    public void setFunction(CustomFunction customFunction) {

        this.customFunction = customFunction;

        customFunction.setBelongingTupleSource(this);
    }

    public void addTupleSourcesToPull(ArrayList<Sensor> pullList) {

        customFunction.addTupleSourcesToPull(pullList);
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<DataTuple> cepQueue) {
        this.dataTupleQueue = cepQueue;

        // Add queue to the custom function as well.

        customFunction.addQueue(cepQueue);
    }

    @Override
    public void setName(String name) {
        this.name = name;

        customFunction.setDataTupleName(name);
    }
}
