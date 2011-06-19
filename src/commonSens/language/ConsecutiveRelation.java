package language;

public class ConsecutiveRelation extends QueryElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5035776952065123394L;

	public ConsecutiveRelation () {
    }

    @Override
    public String showStatementElement() {
        return "-> ";
    }

    @Override
    public String showStatementElementFile() {
        return showStatementElement();
    }

}

