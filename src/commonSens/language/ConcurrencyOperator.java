/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

/**
 *
 * @author jarleso
 */
public class ConcurrencyOperator extends QueryElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TimedListOfAtomicQueries firstChain;
	TimedListOfAtomicQueries secondChain;

    /**
     * The two chains are both connected to the QueryElement
     * that comes after the concurrency operator and its two
     * atomic statements.
     */

    QueryElement commonNext;

    ConcurrencyOperator() {
        firstChain = null;
        secondChain = null;
    }

    public String showChains() {

        return firstChain.showElementRecursive()
                + " , " + secondChain.showElementRecursive();
    }

    @Override
    public String showStatementElement() {
        return "CO_Error";
    }

    @Override
    public String showStatementElementFile() {
        return "CO_Error";
    }

    public void setFirstChain (TimedListOfAtomicQueries firstChain) {
        this.firstChain = firstChain;
    }

    public void setSecondChain (TimedListOfAtomicQueries secondChain) {
        this.secondChain = secondChain;
    }

    public QueryElement getFirstChain() {
        return firstChain;
    }

    public QueryElement getSecondChain() {
        return secondChain;
    }

}
