/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import sensing.SignalType;

/**
 *
 * @author jarleso
 */
public class Permeability {

    double permVal = 1;
    SignalType signalType;

    public Permeability (SignalType signalType, double permVal) {

        this.signalType = signalType;
        this.permVal = permVal;
     
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public String getSignalTypeString() {
        return signalType.getText();
    }

    public String getValueString() {
        return String.valueOf(permVal);
    }

    public double getValue() {
        return permVal;
    }

    public String getObject() {
        return signalType.getObject() + " " + permVal;
    }
}
