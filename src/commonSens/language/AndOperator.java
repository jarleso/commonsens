package language;

public class AndOperator extends QueryElement implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AndOperator () {
    }

    public String showStatementElement () {

        return "&& ";
    }
   
    @Override
    public String showStatementElementFile() {
        return "&& ";
    }

}

