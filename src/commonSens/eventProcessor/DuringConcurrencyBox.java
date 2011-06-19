/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eventProcessor;

/**
 *
 * @author jarleso
 */
public class DuringConcurrencyBox extends ConcurrencyBox {

	private boolean firstFinished = false;
	private boolean middleFinished = false;
	private boolean endFinished = false;
	
    /**
     * The first machine should start after the second machine and stop
     * before the second machine.
     * 
     * 1st         |---------------|
     * 2nd   |----------------------------|
     *
     * @param firstMachineResult
     * @param secondMachineResult
     * @return
     */
    @Override
    int evaluateResults(int firstMachineResult, int secondMachineResult) {

//        System.err.println("DuringConcurrencyBox.evaluateResults firstMachineResult " +
//                " = " + firstMachineResult + ", secondMachineResult = " +
//                secondMachineResult);

        if (firstMachineResult == DataTupleFilter.M_NO_TRIGGER
                && secondMachineResult == DataTupleFilter.M_CONTINUE) {
        	
        	/**
        	 * Only the second machine has started. This is always
        	 * good.
        	 */
        	
            return DataTupleFilter.M_CONTINUE;
        }

        if (firstMachineResult == DataTupleFilter.M_CONTINUE
                && secondMachineResult == DataTupleFilter.M_CONTINUE) {
        	
        	/**
        	 * Both machines have started.
        	 */
        	
        	firstFinished = true;
        	
            return DataTupleFilter.M_CONTINUE;
        }

        if (firstMachineResult == DataTupleFilter.M_FINISHED
                && secondMachineResult == DataTupleFilter.M_CONTINUE) {
        	
        	/**
        	 * The first machine is done and the second continues.
        	 */
        	
        	// Possible delete...
//        	if(firstFinished) {
//        		middleFinished = true;
//        	} else {
//        		return DataTupleFilter.M_CONCURRENCY_DEVIATION;
//        	}
        	
            return DataTupleFilter.M_CONTINUE;
        }

        if (firstMachineResult == DataTupleFilter.M_FINISHED
                && secondMachineResult == DataTupleFilter.M_FINISHED) {
            
            /**
             * Both claim to be finished.
             */
            
        	middleFinished = true;
            
            return DataTupleFilter.M_REWIND_BOTH;
        }
        
        /**
         * Now check the special cases that might cause deviations. However,
         * it might also be the case that they are not deviations.
         */

        if (firstMachineResult == DataTupleFilter.M_CONTINUE &&
        		secondMachineResult == DataTupleFilter.M_FINISHED) {
        	
        	if(middleFinished)
        		return DataTupleFilter.M_FINISHED;
        	
        	return DataTupleFilter.M_CONCURRENCY_DEVIATION;
        }

        if (firstMachineResult == DataTupleFilter.M_NO_TRIGGER &&
        		secondMachineResult == DataTupleFilter.M_FINISHED) {
        	
        	if(middleFinished) {
            	endFinished = true;
            	
        		return DataTupleFilter.M_FINISHED;
        	}
        	
        	return DataTupleFilter.M_REWIND_SECOND;
        	
        }
        
        if (firstMachineResult == DataTupleFilter.M_NO_TRIGGER &&
        		secondMachineResult == DataTupleFilter.M_NO_TRIGGER) {
        	
//        	System.err.println("Evaluating M_NO_TRIGGER for both machines " +
//        			"with endFinished = " + endFinished + ", " +
//        					"middleFinished = " + middleFinished +
//        					", and firstFinished = " + firstFinished);
        	
        	if(endFinished) {
        		return DataTupleFilter.M_FINISHED;
        	}
        	
        	if(middleFinished || firstFinished) {
        		return DataTupleFilter.M_CONCURRENCY_DEVIATION;
        	}
        	
        	return DataTupleFilter.M_NO_TRIGGER;
        }
        
        
        /**
         * It is unclear whether the following condition should result in
         * an accepted result, but by including this condition, we assure
         * that situations where only the second machine is satisfied do
         * not cause deviations.
         *
         * An alternative is to write (during(a, b) || b):
         *
         */

//        if (firstMachineResult == DataTupleFilter.M_NO_TRIGGER
//                && secondMachineResult == DataTupleFilter.M_FINISHED) {
//            return DataTupleFilter.M_FINISHED;
//        }

        /**
         * In case any of the machines has reported a deviation,
         * this should be prioritized.
         */
        if (firstMachineResult == DataTupleFilter.M_CONCURRENCY_DEVIATION
                || firstMachineResult == DataTupleFilter.M_STRICTNESS_DEVIATION
                || firstMachineResult == DataTupleFilter.M_TIMING_DEVIATION) {
            return firstMachineResult;
        }

        if (secondMachineResult == DataTupleFilter.M_CONCURRENCY_DEVIATION
                || secondMachineResult == DataTupleFilter.M_STRICTNESS_DEVIATION
                || secondMachineResult == DataTupleFilter.M_TIMING_DEVIATION) {
            return secondMachineResult;
        }

        return DataTupleFilter.M_CONCURRENCY_DEVIATION;

    }
}
