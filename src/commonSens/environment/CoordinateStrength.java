/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

/**
 * For each coordinate in the ray, there is a strength value 
 * associated with it. This is handled in this class.
 *
 * @author jarleso
 */
class CoordinateStrength {

    Triple triple;
    double strength;

    CoordinateStrength(Triple triple, double val) {

        this.triple = triple;
        this.strength = val;
    }

    public Triple getTriple() {
        return triple;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double newStrength) {
        strength = newStrength;
    }
}
