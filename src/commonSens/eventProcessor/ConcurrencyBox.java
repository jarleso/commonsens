/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

import java.util.HashMap;

/**
 *
 * @author jarleso
 */
public abstract class ConcurrencyBox extends Box {

    QueryPoolElement firstBoxList;
    QueryPoolElement secondBoxList;

    void setFirstBoxList(QueryPoolElement firstBoxList) {
        this.firstBoxList = firstBoxList;
    }

    void setSecondBoxList(QueryPoolElement secondBoxList) {
        this.secondBoxList = secondBoxList;
    }

    QueryPoolElement getFirstBoxList() {
        return firstBoxList;
    }

    QueryPoolElement getSecondBoxList() {
        return secondBoxList;
    }

    @Override
    HashMap<String, String> getSensorsToPull(Box prevState, 
    		long currentTime, boolean isStrict) {

//        System.err.println("Inside getTupleSourcesToPull in ConcurrencyBox.");

        HashMap<String, String> toPull = new HashMap<String, String>();

        if(firstBoxList != null) {
        	
//        	System.err.println("\tPulling firstBoxList");
        	
            toPull.putAll(firstBoxList.getDataTupleFilter().
            		getSensorsToPull(prevState, currentTime, isStrict));
        } else {
//        	System.err.println("\tfirstMachine == null...");
        }

        if(secondBoxList != null) {
        	
//        	System.err.println("\tPulling secondBoxList");
        	
            toPull.putAll(secondBoxList.getDataTupleFilter().
            		getSensorsToPull(prevState, currentTime, isStrict));
        }else {
//        	System.err.println("\tsecondMachine == null...");
        }
        
        return toPull;
    }

    /**
     * Depending on the concurrency class, this method evaluates the results
     * from the two concurrent chains.
     *
     * @param firstMachineResult
     * @param secondMachineResult
     */

    abstract int evaluateResults(int firstMachineResult, int secondMachineResult);
}
