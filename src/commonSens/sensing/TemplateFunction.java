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
public class TemplateFunction implements CustomFunction {

    Sensor belTS;
    ArrayList<Sensor> pullList;
    ConcurrentLinkedQueue<DataTuple> cepQueue;
    Capability capability;
    @SuppressWarnings("unused")
	private String tsName;

    public void addTupleSourcesToPull(ArrayList<Sensor> pullListToAdd) {

        if(this.pullList == null) {
            this.pullList = new ArrayList<Sensor>();
        }

        if(pullListToAdd != null)
            this.pullList.addAll(pullListToAdd);
    }

    public void pullDependedTupleSources(long timeSeconds, boolean simple) {

    }

    public void setBelongingTupleSource(Sensor belTS) {

        this.belTS = belTS;
        this.capability = belTS.getProvidedCapability("InTheKitchen");
        this.cepQueue = belTS.getQueue();
    }

    public void addQueue(ConcurrentLinkedQueue<DataTuple> cepQueue) {

        this.cepQueue = belTS.getQueue();
    }

    public void setDataTupleName(String name) {
        this.tsName = name;
    }
}