/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sensing;

/**
 *
 * @author jarleso
 */
public class SignalType {

    String signalType;

    public SignalType(String signalType) {
        this.signalType = signalType;
    }

    SignalType(SignalType signalType) {
        this.signalType = signalType.getText();
    }

    public String getText() {
        return signalType;
    }

    public String getObject() {
        return getText() + " ";
    }

}
