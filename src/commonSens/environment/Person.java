/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import eventProcessor.DataTuple;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author jarleso
 */
public class Person extends CommonSensObject {

    ConcurrentLinkedQueue<DataTuple> queue;

    public Person (String name, Triple initPos, 
    		ConcurrentLinkedQueue<DataTuple> queue) {

        super(name);
        this.queue = queue;

        shape = new Shape(name + "_Shape");

        shape.setStartPoint(initPos);
        shape.addTriple(new Triple(- 10, 10, 0));
        shape.addTriple(new Triple(10, 10, 0));
        shape.addTriple(new Triple(10, - 10, 0));
        shape.addTriple(new Triple(- 10, - 10, 0));

    }

    public Person (String name, Shape shape, 
    		ConcurrentLinkedQueue<DataTuple> queue) {

        super(name);
        this.shape = shape;
        this.queue = queue;
    }

    @Override
    public String getObject() {

        String retString = "";

        retString += getName() + " ";
        retString += "shape ";

        retString += shape.getObject();

        return retString;
    }
}
