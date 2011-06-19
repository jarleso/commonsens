package language;

public class Strict extends QueryElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5616964827531600125L;

	public Strict () {
    }

    @Override
    public String showStatementElement() {
        return "=> ";
    }

    @Override
    public String showStatementElementFile() {
        return showStatementElement();
    }

}

