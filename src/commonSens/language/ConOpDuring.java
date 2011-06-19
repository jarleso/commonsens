/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

/**
 *
 * @author jarleso
 */
public class ConOpDuring extends ConcurrencyOperator {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2803872662371971972L;

	@Override
    public String showStatementElement() {
        return "during ( " + showChains() + " ) ";
    }

    @Override
    public String showStatementElementFile() {
        return showStatementElement();
    }

}