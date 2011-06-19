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
public interface CustomFunction {

    public void setBelongingTupleSource(Sensor belTS);

    public void addTupleSourcesToPull(ArrayList<Sensor> pullList);

    public void pullDependedTupleSources(long timeSeconds, boolean simple);

    public void addQueue(ConcurrentLinkedQueue<DataTuple> cepQueue);

    public void setDataTupleName(String name);

}
