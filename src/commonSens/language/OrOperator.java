package language;

public class OrOperator extends QueryElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7137209666085345305L;

	public OrOperator () {
    }

    @Override
    public String showStatementElement () {
        
        return "|| ";
    }

    @Override
    public String showStatementElementFile() {

        return "|| ";
    }

}

