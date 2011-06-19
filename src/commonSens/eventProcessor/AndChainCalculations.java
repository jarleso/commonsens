/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

import language.AtomicQuery;
import language.QueryElement;

/**
 * The AndOperator chains have similar timestamps and this class keeps count of all
 * the matches and tuples that match. It also contains the deadline for
 * the chain if it is delta-timed and followed by a timed query.
 *
 * @author jarleso
 */
public class AndChainCalculations {

    int matchesCounter = 0;
    int tuplesCounter = 0;
    int numberOfTimeStepsMatched = 0;
    private boolean isUnderEvaluation = false;
    private int currCondition;
    private int id;

    /**
     * A representative element for the OrOperator chain. Used for getting
     * the timing.
     */

    AtomicQuery reprStatement;

    /**
     * Delta-timed queries that are followed by timed queries have a
     * deadline for when they should occur. This is set as part of 
     * the instantiation. 
     */
    
    private Timestamp deadline;
    
    AndChainCalculations(QueryElement theElement) {

        if(theElement instanceof AtomicQuery) {
            reprStatement = (AtomicQuery) theElement;


        } else {

            reprStatement = null;

            System.err.println("WARNING: theElement in " +
                    "CEPAndChainCalculations is not AtomicQuery.");
        }

        currCondition = DataTupleFilter.M_NO_TRIGGER;
        
        id = theElement.showStatementElement().hashCode();
        
        System.err.println("AndChainCalculations created with id " + id);
    }
    
    /**
     * Used for debugging. The id is a hash based on one of the statements
     * that this object handles.
     * 
     * @return
     */
    
    public int getId() {
    	return id;
    }

    /**
     * The two following methods update the counters. This also applies to
     * the filter, since there are timing in the filter as well.
     *
     * @param filter
     */

    public void updateMatchesCounter(DataTupleFilter filter) {

        matchesCounter += 1;

//        System.err.println("AndChainCalculations.updateMatchesCounter " + id +
//                " increased to " + matchesCounter);

        if(filter != null) {
            filter.updateMatchesCounter(1);
        }
    }

    public void updateTupleCounter(DataTupleFilter filter) {

        tuplesCounter += 1;

//        System.err.println("AndChainCalculations.updateTupleCounter " + id + 
//                " increased to " + tuplesCounter);

        if(filter != null) {
            filter.updateTupleCounter(1);
        }

//        System.err.println("AndChainsCalculations.updateTupleCounter(): " +
//                "tuplesCounter = " + tuplesCounter + ", " +
//                "matchesCounter = " + matchesCounter + ", " +
//                "numberOfTimeStepsMatched = " + numberOfTimeStepsMatched);
    }

    public void updateNumberOfTimeStepsMatched(DataTupleFilter filter) {

        numberOfTimeStepsMatched += 1;

//        System.err.println("AndChainCalculations." +
//        		"updateNumberOfTimeStepsMatched " +
//        		id +
//                " increased to " + numberOfTimeStepsMatched);

        if(filter != null) {
            filter.updateNumberOfTimeStepsMatched(1);
        }
    }

    public int getNumberOfTimeStepsMatched() {
        return numberOfTimeStepsMatched;
    }

    double getNumTuples() {
        return tuplesCounter;
    }

    double getNumMatches() {
        return matchesCounter;
    }

    /**
     * Returns the number of matching time steps compared to the size of
     * the window. Note that this a simple solution in our simulated
     * environment, since there is an update every time step.
     *
     * If <tt>timeUsed</tt> is -1, the method only returns the ratio between
     * <tt>matchesCounter</tt> and <tt>tuplesCounter</tt>. Otherwise, the
     * method returns the aforementioned ratio as a ratio of the time that
     * has elapsed during the evaluation of the current AndOperator chain.
     *
     * @param timeUsed
     * @return
     */
    public double getMatchRate(double timeUsed) {

        /**
         * Avoid dividing on 0.
         */

        if(tuplesCounter == 0) {
            return 0;
        }

        /**
         * jarleso 4.11.10: Use this one to get correct ratio. I don't know
         * 					why I have implemented the other stuff... However,
         * 					this solution assumes that samplingFrequency = 1000. Note that 
         * 					we have to multiply with 10,000 to obtain real
         * 					percentage since the time window is given in
         * 					milliseconds.
         */
        
        if(timeUsed == -1) {

//        	System.err.println("AndChainCalculation.getMatchRate: " + 
//        			"matchesCounter = " + matchesCounter + ", " + 
//        			"reprStatement.getTimeWindow() = " + 
//        			reprStatement.getTimeWindow());
        	
        	return (((double) matchesCounter)/reprStatement.getTimeWindow()) *
        			100000;
        	
            //return (((double) matchesCounter)/((double) tuplesCounter));
        }

        // TODO: Fix the 1000 so that it dynamically is determined by
        // the samplingFrequency of the current sensor.

        return (((double) matchesCounter)/((double) tuplesCounter)) *
                ((timeUsed + 1000)/reprStatement.getTimeWindow());
    }

    void setIsUnderEvaluation(boolean what, DataTupleFilter filter) {
        isUnderEvaluation = what;

        if(filter != null) {
            filter.setIsUnderEvalutaion(what);
        }
    }

    boolean getIsUnderEvaluation() {
        return isUnderEvaluation;
    }

    AtomicQuery getReprStatement() {
        return reprStatement;
    }

    public void setCurrCondition(int newCondition) {
        currCondition = newCondition;
    }

    public int getCurrCondition() {
        return currCondition;
    }
    
    /**
     * Sets the deadline. This is done when the state machine is created.
     * 
     * @param deadline
     */
    
    public void setDeadline(Timestamp deadline) {
    	
    	this.deadline = deadline;
    }
    
    /**
     * What do you think this method does? 
     * 
     * @return
     */
    
    public Timestamp getDeadline() {
    	return deadline;
    }
    
    public boolean isDeltaTimed() {
    	
    	return reprStatement.isDeltaTimed();
    }
    
    public boolean isTimed() {
    	
    	return reprStatement.isTimed();
    }
}
