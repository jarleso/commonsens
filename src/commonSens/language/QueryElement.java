package language;

import sensing.CapabilityDescription;
import eventProcessor.EventSet;


/**
 *  @author jarleso
 */
public abstract class QueryElement implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3534905877132793491L;

	protected String eventString = null;

    protected QueryElement nextQueryElement = null;

    protected QueryElement previousQueryElement = null;

    protected boolean isNegation;

    private EventSet<CapabilityDescription> Ncq;
    private EventSet<CapabilityDescription> Mcq;
    
    public abstract String showStatementElement ();
    public abstract String showStatementElementFile ();

    public String showElementRecursive () {

        String returnString = this.showStatementElement();

        if(getNext() != null) {

            returnString += getNext().showElementRecursive();
        }

        return returnString;
    }

    public String showElementsRecursiveFile() {

        //System.err.println("Showing element");

        if(this.nextQueryElement != null) {

            //System.err.println("The next element is " + this.statementElement.showStatementElement());

            return showStatementElementFile() + nextQueryElement.showElementsRecursiveFile();
        }

        return showStatementElementFile();
    }

    public QueryElement getElementsRecursive () {
        return null;
    }

    public QueryElement getLastElement () {

        QueryElement tmpElement = this.getNext();

        if(tmpElement == null) return this;

        while(tmpElement.getNext() != null) {

            tmpElement = tmpElement.getNext();
        }

        return tmpElement;
    }

    public QueryElement getFirstElement () {

        QueryElement tmpElement = this.getPrevious();

        //System.err.println("Finding first element: " + tmpElement);

        if(tmpElement == null) return this;

        while(tmpElement.getPrevious() != null) {

            tmpElement = tmpElement.getPrevious();
        }

        return tmpElement;
    }

    public String getEventString () {
        return eventString;
    }

    /**
     *  Sets the next QueryElement and the previous QueryElement in the next QueryElement.
     *       
     *       @param nextQueryElement
     */
    public void setNext (QueryElement nextEvent) {

        //System.err.println("Setting next = " 
    	//+ nextQueryElement.showStatementElement());

        this.nextQueryElement = nextEvent;
        if(nextEvent != null) {
            nextEvent.setPreviousN(this);
        }
    }

    /**
     *  Sets the next QueryElement. This method is called by the 
     *  previous QueryElement in order to avoid a recursive loop.
     *       
     *       @param nextQueryElement
     */
    public void setNextN (QueryElement nextEvent) {
        this.nextQueryElement = nextEvent;
    }

    /**
     *  @return the next QueryElement
     */
    public QueryElement getNext () {
        return nextQueryElement;
    }

    /**
     *  Sets the previous QueryElement and the next QueryElement in the previous QueryElement.
     *       
     *       @param previousQueryElement
     */
    public void setPrevious (QueryElement previousEvent) {
        this.previousQueryElement = previousEvent;
        if(previousEvent != null) {
            previousEvent.setNextN(this);
        }
    }

    /**
     *  Sets the previous QueryElement. This method is called by the next QueryElement
     *       in order to avoid a recursive loop.
     *       
     *       @param previousQueryElement
     */
    public void setPreviousN (QueryElement previousEvent) {
                this.previousQueryElement = previousEvent;
    }

    /**
     *  @return the previous QueryElement
     */
    public QueryElement getPrevious () {
        return previousQueryElement;
    }

    public void setEventString (String val) {
        this.eventString = val;
    }

    public String getAttribute() {

        return "object";
    }

    public void setIsNegation(boolean nextIsNot) {

        //System.err.println("Setting negation to " + nextIsNot);

        this.isNegation = nextIsNot;
    }

    public boolean getIsNegation() {
        return isNegation;
    }
}

