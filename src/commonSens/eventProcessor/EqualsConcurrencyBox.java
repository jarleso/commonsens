/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

/**
 *
 * @author jarleso
 */
public class EqualsConcurrencyBox extends ConcurrencyBox {

    /**
     * The idea with the concurrency operator is that the two chains should
     * be evaluated concurrently and they should start and stop
     * at the same time.
     *
     * Hence, the method returns a deviation message if the two values
     * are different.
     *
     *
     *
     * @param firstMachineResult
     * @param secondMachineResult
     */

    @Override
    int evaluateResults(int firstMachineResult, int secondMachineResult) {

        if(firstMachineResult != secondMachineResult) {

            return DataTupleFilter.M_CONCURRENCY_DEVIATION;
        }

        return firstMachineResult;
    }
}
