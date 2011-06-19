/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

/**
 *
 * @author jarleso
 */
public class NotOperator extends QueryElement implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2759783552313048927L;

	@Override
    public String showStatementElement() {

        return "! ";
    }

    @Override
    public String showStatementElementFile() {

        return "! ";
    }
}
