/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import java.awt.Point;

/**
 *
 * @author jarleso
 */
public class Triple {

    int x, y, z;

    Triple() {

    }

    public Triple(int x, int y, int z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Triple(Triple tmpTrip) {
        this.x = tmpTrip.getX();
        this.y = tmpTrip.getY();
        this.z = tmpTrip.getZ();
    }

    public Triple getRelative(Triple startPoint) {

        return new Triple(getX() + startPoint.getX(),
                getY() + startPoint.getY(),
                getZ() + startPoint.getZ());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {

//        if(this.x != x) {
//            System.err.println("Sets x from " + this.x
//                + " to " + x);
//        }

        this.x = x;
    }

    public int getY() {

        return y;
    }

    public void setY(int y) {

//        if(this.y != y) {
//            System.err.println("Sets y from " + this.y
//                + " to " + y);
//        }
        
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    String getObject() {
        return getX() + " " + getY() + " " + getZ() + " ";
    }

    Point toPoint() {
        return new Point(getX(), getY());
    }
}
