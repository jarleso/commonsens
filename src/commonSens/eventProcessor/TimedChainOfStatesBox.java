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
public class TimedChainOfStatesBox extends Box {

    QueryPoolElement theBoxList;

    void setTheStateMachine(QueryPoolElement theMachine) {
        this.theBoxList = theMachine;
    }

    QueryPoolElement getTheStateMachine() {
        return theBoxList;
    }

    @Override
    HashMap<String, String> getSensorsToPull(Box prevState, long currentTime, boolean isStrict) {

//        System.err.println("Inside getTupleSourcesToPull in Timed.");

        HashMap<String, String> toPull = new HashMap<String, String>();

        if(theBoxList != null) {
            toPull.putAll(theBoxList.getDataTupleFilter().getSensorsToPull(prevState, currentTime, isStrict));
        }

        return toPull;
    }

    @Override
    public String showState() {
    	
    	if(theBoxList.getCurrentBox() == null) {
    		return "TimedChainOfStatesBox.showState() -> " +
    				"theBoxList.getCurrentState() == null";
    	}
    	
        return theBoxList.getCurrentBox().showState();
    }
}
