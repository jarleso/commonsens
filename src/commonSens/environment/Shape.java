/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import com.seisw.util.geom.Poly;
import com.seisw.util.geom.PolyDefault;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import sensing.SignalType;

/**
 * Implements all the properties and methods related to the shapes. 
 *
 * @author jarleso
 */
public class Shape {

    ArrayList<Triple> boundary;
    ArrayList<Triple> boundaryReduced;
    
    /**
     * All boundary are relative to the start point. Currently this is 
     * defined as the middle point in a shape. This contradicts the 
     * formal definition of a shape, where all the points are relative
     * to a start point that is part of the shape. 
     */
    
    private Triple startPoint;
    String name;
    private Polygon realCoveragePolygon;

    public Shape(String name) {

        this.name = name;

        startPoint = new Triple(0,0,0);

        boundary = new ArrayList<Triple>();
    }

    Shape(int numTriplets) {

        boundary = new ArrayList<Triple>(numTriplets);
    }

    public Shape(int[] cX, int[] cY) {

        startPoint = new Triple(0,0,0);

        setupShape(cX, cY);
    }

    /**
     * Copy another shape.
     * 
     * @param shape
     */
    
    public Shape(Shape shape) {

        this.boundary = new ArrayList<Triple>();

        for(Triple tmpTrip : shape.getTriples()) {

            boundary.add(new Triple(tmpTrip));
        }

        startPoint = new Triple(shape.getStartPoint());

        name = new String(shape.getName());
    }

    public void moveHoriz(int points) {

        int newPoint = startPoint.getX() + points;

        startPoint.setX(newPoint);
    }

    public void moveVertic(int points) {

        int newPoint = startPoint.getY() + points;

        startPoint.setY(newPoint);
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getX(int i) {
          return boundary.get(i).getX() + startPoint.getX();
    }

    synchronized public int getXReduced(int i) {

        if(boundaryReduced.get(i) != null)
            return boundaryReduced.get(i).getX();// + startPoint.getX();

        //System.err.println("getReducedX returns " + retInt);

        return getStartPoint().getX();
    }

    public int getY(int i) {
          return boundary.get(i).getY() + startPoint.getY();
    }

    synchronized public int getYReduced(int i) {

        if(boundaryReduced.size() > i)
            return boundaryReduced.get(i).getY();// + startPoint.getY();

        //System.err.println("getReducedY returns " + retInt);
        
        return getStartPoint().getY();
    }


    public Triple getStartPoint() {
        return startPoint;
    }

    public int[] getRaysX() {

        int []tmpInt = new int[boundary.size()];

        for(int i = 0; i < tmpInt.length; i++) {


            tmpInt[i] = getX(i);
        }

        return tmpInt;
    }

    public int[] getOuterBoundsX() {

        int size = boundary.size();

        int []tmpInt = new int[size];

        for(int i = 0; i < size; i++) {


            tmpInt[i] = getX(i);
        }

        return tmpInt;
    }

    public int[] getOuterBoundsXReduced() {

        int size = boundaryReduced.size();

        int []tmpInt = new int[size];

        for(int i = 0; i < size; i++) {

            tmpInt[i] = getXReduced(i);
        }

        return tmpInt;
    }

    public int[] getOuterBoundsY() {

        int size = boundary.size();

        int[] tmpInt = new int[size];

        for(int i = 0; i < size; i++) {


            tmpInt[i] = getY(i);
        }

        return tmpInt;
    }

    public int[] getOuterBoundsYReduced() {

        int size = boundaryReduced.size();

        int[] tmpInt = new int[size];

        for(int i = 0; i < size; i++) {


            tmpInt[i] = getYReduced(i);
        }

        return tmpInt;
    }

    public void setStartPoint(Triple triple) {

        this.startPoint = triple;
    }

    public int getTripleSize() {
        return boundary.size();
    }

    public void addTriple(Triple tmpTriple) {

        boundary.add(tmpTriple);
    }

    public ArrayList<Triple> getTriples() {
        return boundary;
    }

    public String getName() {
        return name;
    }

    public String getObject() {

        String retString = "";

        retString += this.name + " " + boundary.size()
                + " " + startPoint.getObject() + " ";

        for(Triple tmpTrip : boundary) {

            retString += tmpTrip.getObject() + " ";
        }

        return retString;
    }

    public Poly getPoly() {

        Poly retPoly = new PolyDefault();
        Polygon tmpPolygon = this.getPolygon();

        for(int i = 0; i < tmpPolygon.npoints; i++) {

            retPoly.add(new Point(tmpPolygon.xpoints[i],
                    tmpPolygon.ypoints[i]));
        }

        return retPoly;
    }

    public Poly getPolyReduced(Environment envir,
            SignalType signalType) {

        Poly retPoly = new PolyDefault();
        Polygon tmpPolygon = this.getPolygonReduced(envir, signalType);

        for(int i = 0; i < tmpPolygon.npoints; i++) {

            retPoly.add(new Point(tmpPolygon.xpoints[i],
                    tmpPolygon.ypoints[i]));
        }

        return retPoly;
    }

    public Polygon getPolygon() {

        return new Polygon(getOuterBoundsX(), getOuterBoundsY(),
                getOuterBoundsX().length);
    }

    public Polygon getPolygonReduced(Environment envir,
            SignalType signalType) {

        // Go through all the boundary and change the coordinate
        // according to the reduced coverage area.

        // For each triple, create a ray from the startPoint to
        // the triple.

        boundaryReduced = new ArrayList<Triple>(boundary.size());

        for(int i = 0; i < boundary.size(); i++) {

            Ray tmpRay = new Ray(getStartPoint(),
                    boundary.get(i).getRelative(getStartPoint()));

            Triple tmpTriple;

            if(tmpRay.numElements > 0) {

                tmpTriple = tmpRay
                        .reduceRay(envir, signalType);
            } else {

                // The mid point is also a triple, and reducing
                // that does not give any meaning.

                tmpTriple = new Triple(getStartPoint());
            }

//            System.err.println("Adds " + tmpTriple.getObject()
//                    + " to boundaryReduced");

            boundaryReduced.add(tmpTriple);

            //System.err.println(" to " + boundary.get(i).getObject());
        }

        int length = getOuterBoundsXReduced().length;

        realCoveragePolygon = new Polygon(getOuterBoundsXReduced(), getOuterBoundsYReduced(),
                length);

        return realCoveragePolygon;
                
    }

    public Polygon getRealCovereagePolygon() {
        return realCoveragePolygon;
    }

    public Poly getRealCovereagePoly() {

        Poly retPoly = new PolyDefault();

        for(int i = 0; i < realCoveragePolygon.npoints; i++) {

            retPoly.add(new Point(realCoveragePolygon.xpoints[i],
                    realCoveragePolygon.ypoints[i]));
        }

        return retPoly;
    }

    public void setStartPoint(int x, int y, int z) {

        this.startPoint.setX(x);
        this.startPoint.setY(y);
        this.startPoint.setZ(z);
    }

    public void changeDimRect(float width, float height) {

        int[] cX = new int[4];
        int[] cY = new int[4];

        cX[0] = -Math.round(width)/2;
        cY[0] = -Math.round(height)/2;

        cX[1] = Math.round(width)/2;
        cY[1] = -Math.round(height)/2;

        cX[2] = Math.round(width)/2;
        cY[2] = Math.round(height)/2;

        cX[3] = -Math.round(width)/2;
        cY[3] = Math.round(height)/2;

        setupShape(cX, cY);
    }

    private void setupShape(int[] cX, int[] cY) {

        boundary = new ArrayList<Triple>(cX.length);

        for(int i = 0; i < cX.length; i++) {

            Triple tmpTriple = new Triple(cX[i], cY[i], 0);

            boundary.add(tmpTriple);
        }
    }

    public void rotate(float degrees) {

        // Go through all boundary:

        float rads = (float) Math.toRadians(degrees);

        for(Triple tmpTrip : boundary) {

            int x = tmpTrip.getX();
            int y = tmpTrip.getY();

            int newX = (int) (Math.cos(rads) * x - Math.sin(rads) * y);
            int newY = (int) (Math.sin(rads) * x + Math.cos(rads) * y);

            tmpTrip.setX(newX);
            tmpTrip.setY(newY);
        }
    }

    // return area of polygon
    public double area() { return Math.abs(signedArea()); }

    // return signed area of polygon
    public double signedArea() {
        double sum = 0.0;
        for (int i = 0; i < getTripleSize(); i++) {
            sum = sum + (getX(i) * getY((i < getTripleSize() - 1)? i+1:0)
                    - (getY(i) * getX((i < getTripleSize() - 1)? i+1:0)));
        }
        return 0.5 * sum;
    }
}
