/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import com.seisw.util.geom.Poly;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import sensing.Capability;
import sensing.Sensor;

/**
 *
 * @author jarleso
 */
public class LocationOfInterest {

    String name;
    protected Shape shape;
    ArrayList<Capability> capabilities;
    private Poly model;
    private double error = 1;

    // This can not be used for anything else than showing in papers.

    HashMap<String, Sensor> isec;
    HashMap<String, Sensor> noIsec;
    
    public LocationOfInterest() {

        setupLoI();

    }

    public LocationOfInterest(String name) {

        this.name = name;

        setupLoI();
    }

    public LocationOfInterest(LocationOfInterest loi) {

        if(loi != null) {

            this.name = loi.getName();
            this.shape = new Shape(loi.getShape());

            setupLoI();

            for(Capability tmpCap : loi.getCapabilities()) {
                this.addCapability(tmpCap);
            }

            Iterator<String> it = loi.isec.keySet().iterator();

            while(it.hasNext()) {

                String key = it.next();

                Sensor tmpS = loi.isec.get(key);

                isec.put(key, tmpS);
            }

            it = loi.noIsec.keySet().iterator();

            while(it.hasNext()) {

                String key = it.next();

                Sensor tmpS = loi.noIsec.get(key);

                noIsec.put(key, tmpS);
            }
        } else {

            setupLoI();
        }
    }

    public void addCapability(Capability tmpCap) {
        capabilities.add(tmpCap);
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getObject() {

        String ret = "";

        ret += "capabilities " + capabilities.size() + " ";

        for(Capability cap : capabilities) {

            ret += cap.getObject();
        }

        ret += "shape " + shape.getObject();

        ret += "name " + name + " ";

        return ret;
    }

    public Shape getShape() {
        return shape;
    }

    public ArrayList<Capability> getCapabilities() {
        return capabilities;
    }

    public void setLoIApprox(Poly intersection) {
        this.model = intersection;
    }

    public Polygon getModel() {

        Polygon retPolygon = new Polygon();

        if(model != null) {
            for(int i = 0; i < model.getNumPoints(); i++) {

                retPolygon.addPoint(Math.round((float)model.getX(i)),
                        Math.round((float)model.getY(i)));
            }

            return retPolygon;
        }

        return null;
    }

    /**
     * Only for showing in papers.
     *
     * @param d
     */

    public void setError(double d) {
        this.error = 1 - d;

        //System.err.println("Error set to " + error + " for " + this.getName());

    }

    public double getError() {
        return error;
    }

//    public void setSensorsCoveringOutsideIntersection(ArrayList<PhysicalSensor> sensorsCoveringOutsideIntersection) {
//
//        this.noIsec = sensorsCoveringOutsideIntersection;
//    }

//    public ArrayList<PhysicalSensor> getSensorsCoveringOutsideIntersection() {
//        return noIsec;
//    }

//    public ArrayList<PhysicalSensor> getSensorsCoveringOutsideIntersection(Capability cap) {
//
//        ArrayList<PhysicalSensor> retArray = new ArrayList<PhysicalSensor>();
//
//        for(PhysicalSensor tmpSens : noIsec) {
//
//            if(tmpSens.getIsCapabilityProvided(cap.getName())) {
//                retArray.add(tmpSens);
//            }
//        }
//
//        return retArray;
//    }

//    public void setSensorsCoveringThis(ArrayList<PhysicalSensor> actualSensors) {
//
//        isec = actualSensors;
//    }

//    public ArrayList<PhysicalSensor> getSensorsCoveringThis(Capability cap) {
//
//        ArrayList<PhysicalSensor> retArray = new ArrayList<PhysicalSensor>();
//
//        for(PhysicalSensor tmpSens : isec) {
//
//            if(tmpSens.getIsCapabilityProvided(cap.getName())) {
//                retArray.add(tmpSens);
//            }
//        }
//
//        return retArray;
//    }

//    public ArrayList<PhysicalSensor> getSensorsCoveringThis() {
//
//        return isec;
//    }

    public void setIsec(ArrayList<Sensor> ec) {

        for(Sensor tmpTS : ec) {

            isec.put(tmpTS.getName(), tmpTS);
        }

    }
    
    public HashMap<String, Sensor> getIsec() {
    	return isec;
    }

    public void setNoIsec(ArrayList<Sensor> ec) {


        for(Sensor tmpTS : ec) {

            noIsec.put(tmpTS.getName(), tmpTS);
        }
    }

    public HashMap<String, Sensor> getNoIsec() {
    	return noIsec;
    }
    
    private void setupLoI() {

        capabilities = new ArrayList<Capability>();

        isec = new HashMap<String, Sensor>();
        noIsec = new HashMap<String, Sensor>();
    }
}
