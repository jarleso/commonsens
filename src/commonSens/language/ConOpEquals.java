/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

/**
 *
 * @author jarleso
 */
public class ConOpEquals extends ConcurrencyOperator {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5447069736273078803L;

	@Override
    public String showStatementElement() {
        return "equals ( " + showChains() + " ) ";
    }

    @Override
    public String showStatementElementFile() {
        return showStatementElement();
    }

}
