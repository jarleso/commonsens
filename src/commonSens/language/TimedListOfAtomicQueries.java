/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

import eventProcessor.Timestamp;

/**
 *
 * @author jarleso
 */
public class TimedListOfAtomicQueries extends QueryElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7468726484774429592L;
	private boolean isMin;
    private double percentage;
    private Timestamp begin;
    private Timestamp end;

    QueryElement theList = null;

    public TimedListOfAtomicQueries(
            QueryElement theList,
            Timestamp begin,
            Timestamp end,
            boolean isMin,
            double percentage) {

        this.theList = theList;
        this.begin = begin;
        this.end = end;
        this.isMin = isMin;
        this.percentage = percentage;
    }

    public QueryElement getTheChain() {
        return theList;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public boolean isIsMin() {
        return isMin;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public String showStatementElement() {
        return showStatementElementFile();
    }

    @Override
    public String showStatementElementFile() {

        return "[ " + getTheChain().showElementsRecursiveFile()
                + " , " + begin.getTimestamp() + " " + end.getTimestamp()
                + " " + ((isMin)? "min " : "max ") + percentage + " ] ";
    }
}
