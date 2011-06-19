/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eventProcessor;

import environment.Environment;
import java.util.ArrayList;
import java.util.HashMap;
import language.QueryElement;
import language.AtomicQuery;
import modelViewController.QueryParser;
import sensing.PhysicalSensor;
import sensing.Sensor;

/**
 *
 * @author jarleso
 */
class Box {

    /** 
     * Each of the array lists in matches and noMatches symbolize the
     * "or" operator.
     */
     
    private ArrayList<ArrayList<QueryElement>> elements;
    private ArrayList<AndChainCalculations> andLists;
    boolean hasStarted;
    
    Transition nextBoxTransition;
    //Transition selfTransition;
    //Transition deviationTransition;
    Environment cEnvironment;

    HashMap<String, Sensor> devRep;
    int orCounter = 0;
    private ArrayList<HashMap<String, String>> iSecCheck;
    private double maxPercentage = 0;
    
    public Box() {
        elements = new ArrayList<ArrayList<QueryElement>>();
        devRep = new HashMap<String, Sensor>();
        iSecCheck = new ArrayList<HashMap<String, String>>();
        andLists = new ArrayList<AndChainCalculations>();
    }

    public void newOr() {
        orCounter += 1;
    }

    /**
     * tElement is simply added to the andChain for the
     * current elements number.
     *
     * @param tElement
     * @param cEnvironment
     * @param isNegation
     */

    public void addQueryElement(QueryElement tElement,
            Environment cEnvironment, boolean isNot) {

        this.cEnvironment = cEnvironment;
        hasStarted = false;

        /** 
         * Check if the new percentage value is higher than the
         * maximum.
         */

        if (tElement instanceof AtomicQuery) {
            if (((AtomicQuery) tElement).getPercentage() > maxPercentage) {
                maxPercentage = ((AtomicQuery) tElement).getPercentage();
            }
        }

        // Add statement element.

        try {
            elements.get(orCounter);

        } catch (IndexOutOfBoundsException e) {

            elements.add(orCounter, new ArrayList<QueryElement>());
            andLists.add(orCounter, new AndChainCalculations((AtomicQuery) tElement));
        }

        elements.get(orCounter).add(tElement);

    }

    public void addDevRep(PhysicalSensor sens) {

        devRep.put(sens.getName(), sens);
    }

    public void setDevRep(ArrayList<Sensor> ec) {

        for (Sensor tmpTS : ec) {

            devRep.put(tmpTS.getName(), tmpTS);
        }
    }

    public String showState() {

        String tmpString = "";

        for (int iLocal = 0; iLocal < elements.size(); iLocal++) {

            tmpString += "ELEMENTS: ";

            for (QueryElement tmpElem : elements.get(iLocal)) {

                tmpString += tmpElem.showStatementElement();

                tmpString += "Isec: " + ((AtomicQuery) tmpElem).
                	showIsec();
                tmpString += "NoIsec: " + ((AtomicQuery) tmpElem).
                	showNoIsec();
                tmpString += "DevRep: " + ((AtomicQuery) tmpElem).
                	showDevRep();
                tmpString += "FPProb: " + ((AtomicQuery) tmpElem).
                	getFPProb(2);

            }

            if (iLocal < elements.size() - 1) {
                tmpString += "OrOperator ";
            } else {

                if(nextBoxTransition != null) {

                    tmpString += "TRANSITION ";

                    tmpString += nextBoxTransition.getTransition();
                }
            }
        }

        return tmpString;
    }

    void addTransition(String string) {

        if (string.equals("ConsecutiveRelation")) {

            nextBoxTransition = new Transition(false);

        } else {

            nextBoxTransition = new Transition(true);

            // Add more to the match and noMatch.

        }
    }

    Transition getTransition() {
        return nextBoxTransition;
    }
    /**
     * Finds the correct tuple sources to pull. If the relation is => 
     * (isStrict == true), for those atomic statements that are still
     * not under evaluation or not finished evaluated, the DevRep set is
     * also included. The method therefore investigates the previous
     * state and goes through its andLists list.
     *
     * Currently, if there are elements in one of the DevRep sets that are
     * also part of one of the Isec sets, they should not be added.
     *
     * @param currentTime
     * @param isStrict
     * @return
     */
    int getTSIter = 0;
    ArrayList<QueryElement> getTSList;
    AndChainCalculations getTSCurrCalc;

    HashMap<String, String> getSensorsToPull(Box prevState,
            long currentTime, boolean isStrict) {

//        System.err.println("Inside getTupleSourcesToPull in Box " +
//        		this.showState() +        		        		
//        		" with elements.size() = " + elements.size());

        HashMap<String, String> tupleSources = new HashMap<String, String>();

        for (getTSIter = 0; getTSIter < elements.size(); getTSIter++) {

            getTSList = elements.get(getTSIter);
            getTSCurrCalc = andLists.get(getTSIter);

            for (QueryElement se : getTSList) {

                if ((((AtomicQuery) se).isTimed()
                        && currentTime >= ((AtomicQuery) se).getBegin().
                        getTimestamp())
                        || !((AtomicQuery) se).isTimed()) {

//                    System.err.println("Box: Adding Isecs for " + 
//                    		se.showStatementElement());

                    tupleSources.putAll(((AtomicQuery) se).getIsecs());

//                    System.err.println("Box: Adding NoIsecs for " + 
//                    		se.showStatementElement());

                    tupleSources.putAll(((AtomicQuery) se).getNoIsecs());

                    if (isStrict) {

                        if (!getTSCurrCalc.getIsUnderEvaluation()) {

//                            System.err.println("Adding DevReps for " + 
//                        	se.showStatementElement());

                            tupleSources.putAll(((AtomicQuery) se).
                            		getDevReps());
                        }
                    }
                }
            }
        }

        return tupleSources;
    }

    int getElementsSize() {
        return elements.size();
    }

    AtomicQuery tmpAS = null;
    boolean jumpToNextOr = false;
    int elementsSize;
    int j;
    int size;
    int i;
    AtomicQuery stA;
    private ArrayList<QueryElement> evaluationList;
    private AndChainCalculations currAndList = null;

    /**
     * The function receives a batch from the tuple filter. If this is the
     * first batch that should be evaluated in this state, it means that it
     * is an evaluation against the transitions to the state. If the state 
     * evaluation has already started, the batch is evaluated against 
     * (in this order):
     *
     * (1) the self-transition,
     * (2) the transition to the next state
     *
     * If the state is satisfied, only the transition to the next state is
     * evaluated. If => is used, DevDet is updated for the previous state.
     *
     * If any of the statements in the chain have negations in front of them
     * and there is no corresponding data tuple, this has to be reported as a
     * positive response. If there exists a data tuple from the data source, 
     * a positive response is reported if the condition is not met.
     *
     * @param batch
     * @return
     */

    public int evaluateBatch(ArrayList<DataTuple> batch, 
    		DataTupleFilter filter) {

        tmpAS = null;
        jumpToNextOr = false;
        iSecCheck.clear();

        // Only investigate if the batch is not empty.

        if (!batch.isEmpty()) {

            /**
             * Go through all the atomic statements in 'elements' and
             * compare the batch with the isec and noisec sets
             * for all the atomic statements. Note that that all the atomic
             * statements in one element list have to be matched.
             * 
             * TODOs: Fix so that the system allows the P-registered ANDed
             * statements.
             */

            elementsSize = elements.size();
            j = 0;

            for (; j < elementsSize; j++) {

                /**
                 * We clear the iSecCheck since this is only relevant for
                 * checking the ands. elementsSize indicates the number of
                 * ORs in the statement.
                 */
                iSecCheck.clear();

                size = elements.get(j).size();
                i = 0;

                for (; i < size; i++) {

//                    System.err.println("Box.evaluateBatch: " +
//                    		"Evaluating anded statement number " + 
//                            (i + 1) + " of " + (size));

                    tmpAS = (AtomicQuery) elements.get(j).get(i);

//                    System.err.println("Box.evaluateBatch: statement: " + 
//                    		tmpAS.showStatementElement());
                    
                    /**
                     * Adding all the isecs to the current anded statement. 
                     * When a data tuple matches one of the elements in the 
                     * isec set, this element is removed from the isec set. 
                     * Only when iSecCheck is empty, it means that all the 
                     * conditions in the and list have been matched correctly.
                     *
                     * If the current statement is a negation, we do not add the
                     * elements in the isec set, since it will be impossible to
                     * know which elements to remove when we get a match. Note
                     * that for negated statements, a match happens if there is
                     * a data tuple that do not match the conditions, or that
                     * there is no data tuple.
                     */

                    if (!tmpAS.getIsNegation()) {
                        iSecCheck.add(i,
                                new HashMap<String, String>(tmpAS.getIsecs()));
                    }

                    /** 
                     * The batch only contains only one data tuple from each
                     * tuple source.
                     */

//                    System.err.println("batch size is " + batch.size());

                    for (DataTuple tmpTup : batch) {

//                        System.err.println("evaluating " + 
//                        		tmpTup.getObject());

                        /**
                         * In the cases where the location of interest is empty,
                         * the isecs, noisecs, etc. are empty. These statements
                         * have to be evaluated differently.
                         */

                        if(!tmpAS.getIsecs().isEmpty()) {

                            if (tmpAS.getIsecs().containsKey(tmpTup.
                            		getSensorName())) {

//                                System.err.println(tmpTup.getSensorName() + 
//                                		" is in getIsecs");

                                /** 
                                 * Evaluate the data tuple with the conditions 
                                 * in atomic statement.
                                 */

                                if (!evaluateDataTuple(tmpTup, tmpAS)) {

//                                	System.err.println("Condition is not " + 
//                                			"matched");
                                	
                                    jumpToNextOr = true;

                                } else {
                                    /** 
                                     * A match. Remove from iSecCheck. Only if
                                     * iSecCheck is empty, all the tuple 
                                     * sources in isec are matched.
                                     */

                                    iSecCheck.get(i).remove(tmpTup.
                                    		getSensorName());

                                    if (iSecCheck.get(i).size() ==
                                            tmpAS.getIsecs().size()) {
//                                        System.err.println("ERROR: " + 
//                                        		"iSecCheck.size() " +
//                                                "== tmpPS.getIsecs().size()");
                                    } else {
//                                        System.err.println("iSecCheck.get(" +
//                                        		 i + ")"+
//                                        		".size() = " + 
//                                        		iSecCheck.get(i).size());
                                    }
                                }

                            } else if (tmpAS.getNoIsecs().
                                    containsKey(tmpTup.getSensorName())) {

                                //System.err.println(tmpTup.getSensorName() + 
                            	//" is in getNoIsecs");

                                if (evaluateDataTuple(tmpTup, tmpAS)) {
                                    jumpToNextOr = true;
                                }

                            } else if (tmpAS.getDevReps().
                            		containsKey(tmpTup.getSensorName())) {

                                /** 
                                 * If the tuple matches this condition, it 
                                 * is a strictness deviation.
                                 */

                                //System.err.println(tmpTup.getSensorName() + 
                            	//" is in getDevReps");

                                if (evaluateDataTuple(tmpTup, tmpAS)) {
                                    return DataTupleFilter.
                                    M_STRICTNESS_DEVIATION;
                                }

                            } else if (tmpAS.getIsNegation()) {

                                /**
                                 * The data tuple did not match any of the 
                                 * other conditions but tmpAS is a negation. 
                                 * Hence, it is correct behaviour
                                 * that "there is a match".
                                 */

                                //System.err.println("tmpAS is a negation.");

                            } else {

                                //System.err.println("The data tuple does " + 
                            	//"not match.");
                                jumpToNextOr = true;
                            }
                        } else {

                            /**
                             * Evaluate the data tuple for the atomic
                             * statement that does not have a location of
                             * interest.
                             */

                            if(!evaluateDataTuple(tmpTup, tmpAS)) {
                                jumpToNextOr = true;
                            }
                        }
                    }

                    if (jumpToNextOr) {
                        jumpToNextOr = false;
                        break;
                    }
                }

                //System.err.println("i = " + i + ", size = " + size
                 //       + ", isEmpty(iSecCheck) = " + isEmpty(iSecCheck));

                if (i == size && isEmpty(iSecCheck)) {

                    // All conditions in andList have been matched correctly.

//                    System.err.println("All conditions in andList have " + 
//                    		"been matched correctly.");

                    evaluationList = elements.get(j);

                    currAndList = andLists.get(j);

                    if (!currAndList.getIsUnderEvaluation()) {

                        stA = (AtomicQuery) evaluationList.get(0);
                        
                    	if(stA.isTimed() && batch.get(0).
                        		getStartTime() < stA.getBegin().
                        		getTimestamp()) { 

                    		/**
                    		 * The code should not go into this place.
                    		 */
                    		
//                    		System.err.println("evaluateBatch: ERROR 12... ;)");
                    		
                    	} else {
                    	
//	                    	System.err.println("evalBatch: Setting the " + 
//	                    			"currAndList to underEvaluation.");
	                    	
	                        currAndList.setIsUnderEvaluation(true, filter);
	
	                        /**
	                         * Update the timing for the state. Since all the 
	                         * timing in the evaluation list have to be 
	                         * similar, we just adapt the values from the first 
	                         * statement.
	                         */
	
	                        if (stA.isDeltaTimed()) {
	
	                            /**
	                             * Instantiate the timing.
	                             */
	                            stA.setEnd(new Timestamp(batch.get(0).
	                            		getStartTime() + 
	                            		stA.getBegin().getTimestamp()));
	
	                            stA.setBegin(new Timestamp(batch.get(0).
	                            		getStartTime()));
	                        }
                    	}
                    }
                    
                    /** 
                     * Now, the statistics have to be updated as well.
                     */
    
                    if(currAndList.getIsUnderEvaluation()) {
                    	updateCurrAndChainStatistics(currAndList, filter);
                    }

                } else /* !(i == size && isEmpty(iSecCheck)) */ {

//                	System.err.println("Box.evaluateBatch: Conditions " +
//                			"are not matched.");
                	
                    /**
                     * One of the conditions in the AndOperator chain did not match
                     * the condition. The tuple counter has to be updated.
                     * This also applies to the case where the system only
                     * reads from trace files.
                     * 
                     * jarleso: CORRECTION 4.11.10: WE DO NOT UPDATE THE
                     *          TUPLE COUNTER BECAUSE UPDATING IT CHANGES
                     *          THE MATCH RATE.
                     *            
                     */

                	/**
                	 * Remember to update to the correct currAndList.
                	 */
                	
                    currAndList = andLists.get(j);
                	
                    if (currAndList.getIsUnderEvaluation()) {

//                        System.err.println("evaluateBatch updates the " +
//                                "tuple counter with wrong condition " +
//                                "match.");

                        /**
                         * When there is no match, the tuple counter still
                         * has to be updated for currAndList. 
                         */

                        //currAndList.updateTupleCounter(filter);
                    }
                }
            }

//            System.err.println("j == " + j + " elementsSize == " +
//            		"" + elementsSize);

            if (j == elementsSize) {
            	
//            	System.err.println("evalBatch returns NO_MATCHES and " +
//            			"currAndList.getIsUnderEvaluation() = " + 
//            			currAndList.getIsUnderEvaluation());
            	
                return QueryPoolElement.NO_MATCHES;
            }

//            System.err.println("evalBatch returns MATCH");
            
            return QueryPoolElement.MATCH;

        } else /* the batch is empty */ {

//        	System.err.println("evaluateBatch: The batch is empty.");
        	
            /**
             * Satisfy or start evaluation of all the statements with 
             * negations.
             */

            elementsSize = elements.size();
            j = 0;

            for (; j < elementsSize; j++) {

                currAndList = andLists.get(j);

                size = elements.get(j).size();
                i = 0;

                for (; i < size; i++) {

                    tmpAS = (AtomicQuery) elements.get(j).get(i);

                    /**
                     * If the AndOperator chain is not under evaluation, and there
                     * exists a negation in the chain, there is no reason to
                     * start the evaluation, since there has not been any 
                     * tuples yet to start the evaluation. The only chains 
                     * to be evaluated are those that only consist of negated
                     * statements.
                     */

                    if(!tmpAS.getIsNegation()) {

                        break;
                    }
                }

                if(i == size) {

                    /**
                     * All the statements are negations. TODOs: Investigate
                     * that the timing is correct as well, i.e., if the
                     * statement is a negation, but the timing is not matching,
                     * the statement should be set under evaluation.
                     */

                    if (!currAndList.getIsUnderEvaluation()) {
                        currAndList.setIsUnderEvaluation(true, filter);
                    }

                    updateCurrAndChainStatistics(currAndList, filter);
                }
            }
        }

        /**
         * For all the states that are already under evaluation and there are 
         * no matches, we have to increase the number of tuples that should
         * have been evaluated. Otherwise, the system can not correctly
         * detect the timing deviation.
         *
         * However, if the system reads from trace files, and these trace files
         * show that the sampling rate is irregular, it is hard to force the
         * behaviour of the system into our sampling frequency regime. 
         */
        for (AndChainCalculations tmpCal : andLists) {
            if (tmpCal.getIsUnderEvaluation() &&
                    !cEnvironment.getIsTraceFiles()) {
                tmpCal.updateTupleCounter(filter);
            } else {
//                System.err.println("evaluateBatch DOES NotOperator" +
//                		" update the tuple counter.");
            }
        }

        //System.err.println("Returning NO_MATCHES from Box.evaluateBatch");

        return QueryPoolElement.NO_MATCHES;
    }

    public ArrayList<AndChainCalculations> getAndChains() {
        return andLists;
    }

    /**
     * Evaluates one single data tuple. Since the values are all
     * Object, the main part of the method is to investigate the instance
     * that the value is and evaluate it thereafter.
     *
     * @param dataTuple
     * @param stA
     * @return
     */
    boolean retVal;
    Object value;
    String operator;
    boolean isNegation;

    public boolean evaluateDataTuple(DataTuple dataTuple, 
    		AtomicQuery stA) {

        retVal = true;

        value = QueryParser.getCorrectObjectType(dataTuple.getValue());
        operator = stA.getOperator();
        isNegation = stA.getIsNegation();

        if (value instanceof Integer) {

            int tupVal = (Integer) value;
            int condVal = (Integer) stA.getValue();

            if (operator.equals(">")) {
                retVal = tupVal > condVal;
            } else if (operator.equals(">=")) {
                retVal = tupVal >= condVal;
            } else if (operator.equals("<")) {
                retVal = tupVal < condVal;
            } else if (operator.equals("<=")) {
                retVal = tupVal <= condVal;
            } else if (operator.equals("==")) {
                retVal = tupVal == condVal;
            } else if (operator.equals("!=")) {
                retVal = tupVal != condVal;
            } else {

                //System.err.println("Unknown operator: " + operator);
                return false;
            }
        } else if (value instanceof Double) {

            double tupVal = (Double) value;
            double condVal = (Double) stA.getValue();

            if (operator.equals(">")) {
                retVal = tupVal > condVal;
            } else if (operator.equals(">=")) {
                retVal = tupVal >= condVal;
            } else if (operator.equals("<")) {
                retVal = tupVal < condVal;
            } else if (operator.equals("<=")) {
                retVal = tupVal <= condVal;
            } else if (operator.equals("==")) {
                retVal = tupVal == condVal;
            } else if (operator.equals("!=")) {
                retVal = tupVal != condVal;
            } else {

                System.err.println("Unknown operator: " + operator);
                return false;
            }
        } else if (value instanceof Float) {

            float tupVal = (Float) value;
            float condVal = (Float) stA.getValue();

            if (operator.equals(">")) {
                retVal = tupVal > condVal;
            } else if (operator.equals(">=")) {
                retVal = tupVal >= condVal;
            } else if (operator.equals("<")) {
                retVal = tupVal < condVal;
            } else if (operator.equals("<=")) {
                retVal = tupVal <= condVal;
            } else if (operator.equals("==")) {
                retVal = tupVal == condVal;
            } else if (operator.equals("!=")) {
                retVal = tupVal != condVal;
            } else {

                //System.err.println("Unknown operator: " + operator);
                return false;
            }
        } else if (value instanceof String) {

            String tupVal = (String) value;
            String condVal = (String) stA.getValue();

            if (operator.equals("==")) {
                retVal = tupVal.equals(condVal);
            } else if (operator.equals("!=")) {
                retVal = !tupVal.equals(condVal);
            } else {

                //System.err.println("Unknown operator: " + operator);
                return false;
            }
        }

        if (isNegation) {
            return !retVal;
        }

        return retVal;
    }

    /**
     * Returns an evaluation of the statement. If both timestamps are
     * -1 it means that the statement is not timed, which again means that
     * if the state is satisfied only once, that is sufficient.
     *
     * @param st
     * @return
     */
    static boolean isTimed(AtomicQuery st) {
        return st.getBegin().getTimestamp() != -1
                && st.getEnd().getTimestamp() != -1;
    }

    private boolean isEmpty(ArrayList<HashMap<String, String>> iSecCheck) {

        for (HashMap<String, String> hm : iSecCheck) {
            if (!hm.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    double getMaxPercentage() {
        return maxPercentage;
    }

    public ArrayList<ArrayList<QueryElement>> getElements() {
        return elements;
    }

    private void updateCurrAndChainStatistics(AndChainCalculations currAndChain,
            DataTupleFilter filter) {

        currAndChain.updateNumberOfTimeStepsMatched(filter);
        currAndChain.updateMatchesCounter(filter);
        currAndChain.updateTupleCounter(filter);
    }
}
