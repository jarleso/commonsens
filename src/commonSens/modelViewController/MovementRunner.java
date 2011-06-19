/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.Triple;
import environment.Person;

import java.util.ArrayList;

/**
 *
 * @author jarleso
 */
public class MovementRunner extends Thread {

    EnvironmentPanel creator;
    ArrayList<Triple> currPath;
    Person person;
    int speed;
    int currIndex;

    /** Creates new form MovementRunner */
    MovementRunner(EnvironmentPanel creator, ArrayList<Triple> currPath,
            Person person, int speed) {

        this.creator = creator;
        this.currPath = currPath;
        this.person = person;
        this.speed = speed;
        this.currIndex = 0;
    }

    @Override
    public void run() {

        for(Triple point : currPath) {

//            System.err.println("Moving to point (" + (int) point.getX()
//                    + ", " + (int) point.getY() + ")");

            person.setStartPoint(point.getX(), point.getY(), point.getZ());

            creator.repaint();

            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
            }
        }

        creator.getCreator().stopAll(-1);
    }

    /**
     * Returns true if done, false if not done.
     *
     * @param numSteps
     * @return
     */

    public boolean step(int numSteps) {

        currIndex = currIndex + numSteps;

        //System.err.println("step #: " + currIndex);

        if(currIndex >= currPath.size()) return true;

        Triple point = currPath.get(currIndex);

        //System.err.println(point.getX() + "\t" + point.getY() + "\t" + point.getZ());

        person.setStartPoint(point.getX(), point.getY(), point.getZ());

        //System.err.println("Repainting in MovementRunner.step");

        creator.repaint();

        return false;
    }

    public void setCurrPath(ArrayList<Triple> currPath) {

        this.currPath = currPath;
    }

    void reset() {
        currIndex = 0;
    }
}
