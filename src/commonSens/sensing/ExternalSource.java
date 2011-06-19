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
public class ExternalSource extends Sensor implements Runnable {


    public ExternalSource(ExternalSource tmpSource) {

        providedCapabilities = new ArrayList<Capability>();

        this.type = tmpSource.getType();
        this.name = tmpSource.getName();

        for(Capability tmpCap : tmpSource.getProvidedCapabilities()) {

            providedCapabilities.add(new Capability(tmpCap));
        }
    }

    public ExternalSource() {

        providedCapabilities = new ArrayList<Capability>();
    }

    @Override
    public String getCapabilitiesText() {

        String ret = "";

        ret += "capabilities " + providedCapabilities.size() + "\n";

        for(Capability cap : providedCapabilities) {

            ret += cap.getName() + " ";
        }

        ret += "type " + type + " ";
        ret += "name " + name + " ";

        return ret;
    }

    @Override
    public String getObject() {

        String tmpString = "";

        tmpString += "capabilities " + providedCapabilities.size() + "\n";

        for(Capability tmpCap : providedCapabilities) {

            tmpString += tmpCap.getObject();
        }

        tmpString += "type " + type + " ";
        tmpString += "name " + name + " ";

        return tmpString;
    }

    @Override
    public void pullThisSensor(long timeSeconds, boolean simple) {
    }

    public void run() {
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<DataTuple> cepQueue) {
        super.dataTupleQueue = cepQueue;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
