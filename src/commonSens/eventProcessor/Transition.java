/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

/**
 *
 * @author jarleso
 */
class Transition {

    private boolean isStrict;
    private Box nextBox;

    public Transition(boolean isStrict) {

        this.isStrict = isStrict;
    }

    public boolean getIsStrict() {
        return isStrict;
    }

    public Box getNextBox() {
        return nextBox;
    }

    public void setNextBox(Box next) {
        this.nextBox = next;
    }

    public String getTransition() {

        if(isStrict) return "=> ";

        return "-> ";
    }
}
