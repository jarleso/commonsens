/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import sensing.PhysicalSensor;

/**
 *
 * @author jarleso
 */
public class CommonSensObject {

	String name;
	protected Shape shape;
    HashMap<String,Permeability> permeabilityMap;
    ArrayList<PhysicalSensor> sensorsAttached;

    public CommonSensObject(String name) {

        this.name = name;

        permeabilityMap = new HashMap<String,Permeability>();
        sensorsAttached = new ArrayList<PhysicalSensor>();
    }

    public CommonSensObject(Shape shape) {

        permeabilityMap = new HashMap<String,Permeability>();
        sensorsAttached = new ArrayList<PhysicalSensor>();

        setShape(shape);
    }

    public CommonSensObject(Permeability permeability) {

        permeabilityMap = new HashMap<String,Permeability>();
        sensorsAttached = new ArrayList<PhysicalSensor>();

        addPerm(permeability);
    }

    public CommonSensObject(CommonSensObject tmpObject) {

        this.shape = new Shape(tmpObject.getShape());

        permeabilityMap = new HashMap<String, Permeability>();
        sensorsAttached = new ArrayList<PhysicalSensor>();

        for(Permeability tmpPerm : tmpObject.getPermeabilities()) {

            permeabilityMap.put(tmpPerm.getSignalTypeString(),
                    new Permeability(tmpPerm.getSignalType(),
                        Double.valueOf(tmpPerm.getValue())));
        }

//        for(PhysicalSensor sensor : tmpObject.getSensors()) {

        	name = new String(tmpObject.getName());
//        }

    }

    public Permeability getPerm(String signalType) {
        return permeabilityMap.get(signalType);
    }

    public void addPerm(Permeability perm) {
        permeabilityMap.put(perm.getSignalTypeString(), perm);
       
    }

    public String getObject() {

        String retString = "";

        retString += name + " ";
        retString += "shape " + shape.getObject();
        retString += "permeabilities " + permeabilityMap.size() + " ";

        Iterator<String> it = permeabilityMap.keySet().iterator();

        while(it.hasNext()) {

            retString += permeabilityMap.get(it.next()).getObject() + " ";
        }

        return retString;
    }

    public ArrayList<Permeability> getPermeabilities() {

        ArrayList<Permeability> tmpPerm = new ArrayList<Permeability>();
        Iterator<String> it = permeabilityMap.keySet().iterator();

        while(it.hasNext()) {
            tmpPerm.add(permeabilityMap.get(it.next()));
        }

        return tmpPerm;
    }

    public ArrayList<PhysicalSensor> getSensors() {
        return this.sensorsAttached;
    }

    public void addSensor(PhysicalSensor sensor) {
        sensorsAttached.add(sensor);
    }

    public void setStartPoint(int x, int y, int i) {

        getShape().setStartPoint(x, y, i);

        for(PhysicalSensor sens : sensorsAttached) {
            sens.getShape().setStartPoint(x, y, i);
        }
    }
    
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    
    public Shape getShape() {
        return shape;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
