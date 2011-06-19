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
public class InTheKitchenFunction implements CustomFunction {

    Sensor belTS;
    ArrayList<Sensor> pullList;
    ConcurrentLinkedQueue<DataTuple> cepQueue;
    Capability capability;
    private boolean hasSentDataTuple;
    private String tsName;

    public void addTupleSourcesToPull(ArrayList<Sensor> pullListToAdd) {

        if(this.pullList == null) {
            this.pullList = new ArrayList<Sensor>();
        }

        if(pullListToAdd != null)
            this.pullList.addAll(pullListToAdd);
    }

    /**
     * Pulls the tuple sources in pullListToAdd. For ByTheTelephoneFunction, it is
     * only sufficient to pullDependedTupleSources the pullListToAdd and if any of the tuple sources
     * return a data tuple, the system turns this data tuple into a data
     * tuple that is returned from this tuple source.
     *
     * @return
     */

    public void pullDependedTupleSources(long timeSeconds, boolean simple) {

        hasSentDataTuple = false;

//        System.err.println("ByTheTelephoneFunction.pull()");

        for(Sensor tmpTS : pullList) {

            tmpTS.pullThisSensor(timeSeconds, simple);
        }

        /**
         * Pop the data tuples from the shared queue. In situations where there
         * are concurrent pulling of the queue, there might be other data tuples
         * in the queue. The method takes these out of the queue and puts them
         * back in afterwards.
         *
         * The queue is emptied and only one of the real tuples - the one that
         * gives the result from this logical sensor - is put back. The tuples
         * that does not have anything to do with this method are also put back.
         *
         * Note that in a multi-threaded system execution, with this solution,
         * it might happen that the data tuples that we need are not in the
         * queue since they are taken away with another similar function.
         */

        ArrayList<DataTuple> toBeReturned = new ArrayList<DataTuple>();

//        DataTuple tupleToSend = null;

        while(!cepQueue.isEmpty()) {

            DataTuple tmpTuple = cepQueue.poll();

            if(isInPullList(tmpTuple.getSensorName())) {

                if(!hasSentDataTuple) {

//                    tupleToSend = tmpTuple;

                    tmpTuple.setCapability(capability);
                    tmpTuple.setSensorName(tsName);

                    // The capabilities have the same functionality (switch),
                    // so we do not change anymore.

                    toBeReturned.add(tmpTuple);

                    hasSentDataTuple = true;
                }

            } else {
                toBeReturned.add(tmpTuple);
            }
        }

        // Put the data tuples in toBeReturned on the queue.

        for(DataTuple tmpTuple : toBeReturned) {

            System.err.println("Adding " + tmpTuple.getSensorName() + " to queue.");

            cepQueue.add(tmpTuple);
        }
    }

    private boolean isInPullList(String sensorName) {

        for(Sensor tmpTS : pullList) {

            if(tmpTS.getName().equals(sensorName)) return true;
        }

        return false;
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