/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.Approximation;
import environment.Environment;
import environment.LocationOfInterest;
import environment.CommonSensObject;
import environment.Shape;
import environment.Triple;
import environment.Person;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import sensing.PhysicalSensor;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import sensing.Sensor;

/**
 *
 * @author jarleso
 */
class EnvironmentPanel extends JPanel implements MouseMotionListener,
        MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8683675020484685832L;
	private CommonSensObject currObject;
    private Environment currEnv;
    private PhysicalSensor currSens;
    private static final int WSIZE = 1000;     // Size of paint area.
    private static final int HSIZE = 600;     // Size of paint area.
    private LocationOfInterest currLoI;
    private boolean movementCreatorOn = false;
    private MovementCreator movementCreator;
    private Core creator;
//    private int RADIUS = 1;
    private ArrayList<Triple> currPath;
    private ArrayList<Triple> statList;
//    private long oldTime;
//    private boolean isExperiment;
    private MovementRunner movementRunner;
    private long sec1, sec2;
    private long currTime = 0;
    private boolean toggleCoverage = false;
    private Person currPerson;
    private static final boolean showOff = false;

    public EnvironmentPanel(Core creator, Environment currEnv) {

        this.creator = creator;

        if(currEnv == null) {
            this.currEnv = new Environment(creator);
        } else {
            this.currEnv = currEnv;
        }

        setPreferredSize(new Dimension(WSIZE, HSIZE));
        setBackground(Color.white);

        addMouseListener(this);
        addMouseMotionListener(this);

        //
        sec1 = sec2 = System.currentTimeMillis()/100;

        movementRunner = null;
    }

    public Environment getCurrEnv() {
        return this.currEnv;
    }

    public Person getCurrPerson() {
        return currEnv.getCurrPerson();
    }

    /**
    * Paint when the AWT tells us to...
    */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setColor(Color.white);

        g.setColor(Color.black);

        //System.err.println("Goes through obj: " + currEnv.getObjects().size());

        g.setColor(Color.black);
                g.drawString("Time: " +
                        String.valueOf(getTime()), 100, 25);

        for(CommonSensObject tmpObj : currEnv.getObjects()) {

            //System.err.println("Draws object " + tmpObj.getName());

            if(currObject != null &&
                    tmpObj.equals(currObject)) {

                g.setColor(Color.yellow);
            } else {
                g.setColor(Color.black);
            }

            Shape shape = tmpObj.getShape();

            if(shape != null) {

                g.drawPolygon(tmpObj.getShape().getPolygon( ));

//                Color tmpCol = g.getColor();
                
//                if(!tmpObj.getPermeabilities().isEmpty()
//                        &&
//                        !tmpObj.getName().equals("Room_2")) { // Hot fix...
//                    g.setColor(Color.LIGHT_GRAY);
//
//                    g.fillPolygon(tmpObj.getShape().getPolygon( ));
//
//                    g.setColor(tmpCol);
//                }
            }
        }

        Color tmpCol = g.getColor();

        for(Sensor tmpSource : currEnv.getTupleSources()) {

            if(tmpSource instanceof PhysicalSensor) {

                PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;

                if(currSens != null &&
                        tmpSens.equals(currSens)) {

                    g.setColor(Color.green);
                } else {
                    g.setColor(Color.red);
                }

                if(showOff) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.BLACK);
                }


                Shape shape = tmpSens.getShape();

                if(shape != null) {

                    //System.err.println("draws reduced polygon.");

                    Polygon tmpPolygon;

                    if(toggleCoverage) {
                        tmpPolygon = tmpSens.getPolygonReduced(currEnv,
                                tmpSens.getSignalType());
                    } else {
                        tmpPolygon = tmpSens.getShape().getPolygon();
                    }

                    g.drawPolygon(tmpPolygon);

                    // Write the name.

                    String name = tmpSens.getName();

                    g.setColor(Color.BLACK);

                    g.drawString(name, shape.getStartPoint().getX(),
                            shape.getStartPoint().getY() - 10);
                }
            }
        }

        g.setColor(tmpCol);

        tmpCol = g.getColor();

        for(LocationOfInterest tmpLoI : currEnv.getLoIs()) {

            Shape shape = tmpLoI.getShape();

            if(shape != null) {

                // Check for error intersection

                Polygon model = tmpLoI.getModel();

                if(model != null) {

                    //g.setColor(Color.PINK);

                    g.fillPolygon(model);

                }

                if(currLoI != null &&
                    tmpLoI.equals(currLoI)) {

                    g.setColor(Color.blue);
                } else {
                    g.setColor(Color.ORANGE);
                }

                if(showOff) {
                    g.setColor(Color.ORANGE);
                } else {
                    g.setColor(Color.BLACK);
                }

                Polygon poly = tmpLoI.getShape().getPolygon();

                g.drawPolygon(poly);

                g.setColor(Color.white);

                g.fillRect(0, 0, 250, 10);

                g.setColor(Color.black);
//                g.drawString("Error: " +
//                        String.valueOf(tmpLoI.getError()), 100, 10);

                Rectangle polyRec = poly.getBounds();

                String name = tmpLoI.getName();

                drawCenteredString(name, polyRec.x,
                        polyRec.y, (int) polyRec.getWidth(), 
                        (int) polyRec.getHeight(), g);
            }
        }

        g.setColor(tmpCol);

        for(Person tmpPerson : currEnv.getPersons()) {

            g.setColor(Color.blue);

            g.drawPolygon(tmpPerson.getShape().getPolygon());

            // Draw movement pattern.

            if(showOff) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.BLACK);
            }

            if(statList != null) {
                for(int i = 0; i < statList.size() - 1; i++) {

                    g.drawLine(statList.get(i).getX(),
                            statList.get(i).getY(),
                            statList.get(i + 1).getX(),
                            statList.get(i + 1).getY());

                }
            }
        }
    }

    /**
     * From http://www.java2s.com/Tutorial/Java/
     * 	0261__2D-Graphics/Centertext.htm
     *
     * Some modifications by jarleso.
     *
     * @param s
     * @param x
     * @param y
     * @param w
     * @param h
     * @param g
     */

    public void drawCenteredString(String s, int x, int y,
            int w, int h, Graphics g) {

        FontMetrics fm = g.getFontMetrics();

        int xLocal = (w - fm.stringWidth(s)) / 2;
        int yLocal = (fm.getAscent() + 
        		(h - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x + xLocal, y + yLocal);
    }

    /**
     * Runs the movement pattern as a thread.
     * 
     * @param speed
     * @param exp
     */
    
    void runMovement(int speed, boolean exp) {

//        isExperiment = exp;
//
//        if(exp) {
//            oldTime = getTime(true);
//        }

        if(currPath != null) {

            new MovementRunner(this, currPath,
                    currPerson, speed).start();
        }
    }

    boolean stepMovement(int steps) {

        int speed = 0;

        if(movementRunner == null)
            movementRunner = new MovementRunner(this, currPath,
                    getCurrPerson(), speed);

        return movementRunner.step(steps);
    }

    public void resetMovement() {

        int speed = 0;

        if(movementRunner == null) {
            movementRunner = new MovementRunner(this, currPath,
                    getCurrPerson(), speed);
        } else {

            movementRunner.reset();
            movementRunner.setCurrPath(currPath);
        }
    }

    /**
     * Sets the radius of the semi-random movement patterns. This is not
     * how to create workloads anymore.
     *     
     * @param newRad
     */
    
    @Deprecated
    public void setRadius(int newRad) {
    	
    	System.err.println("NOTE: setRadius() does not do anything.");
    	
//        RADIUS = newRad;
    }

    public void mouseDragged(MouseEvent e) {

        if(currObject != null) {

            currObject.getShape().setStartPoint(e.getX(), e.getY(), 0);            
        }

        else if(currSens != null) {

            if(currSens.getObjectConnectedTo() != null) {
                currSens.getShape().setStartPoint(currSens.getObjectConnectedTo().getShape().getStartPoint());
            } else {
                currSens.getShape().setStartPoint(e.getX(), e.getY(), 0);
            }

            currSens.setHasBeenMoved(true);
        }

        else if(currLoI != null) {

            currLoI.getShape().setStartPoint(e.getX(), e.getY(), 0);
        }

        else if(currPerson != null) {

            Triple tmpTriple = new Triple(e.getX(), e.getY(), 0);

            if(currEnv.checkTripleWithObjects(tmpTriple)) {

                getCurrPerson().setStartPoint(e.getX(), e.getY(), 0);

                // Check all the sensors and see if it is connected to the
                // object.
               
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {

        if(sec1 != sec2) {

            creator.getPanel().getJTextArea1().setText("");

            Point tmpPoint = e.getPoint();

            /**
             * Add coordinate at top.
             */
            
            creator.getPanel().getJTextArea1().append(tmpPoint.getX() + " " +
            		tmpPoint.getY() + "\n");
            
            for(CommonSensObject tmp : currEnv.getObjects()) {

                if(tmp.getShape().getPolygon().contains(tmpPoint)) {

                    creator.getPanel().getJTextArea1().append(tmp.getName() + 
                    		"\n");
                }
            }

            for(Sensor tmpSource : currEnv.getTupleSources()) {

                if(tmpSource instanceof PhysicalSensor) {

                    PhysicalSensor tmp = (PhysicalSensor) tmpSource;

                    if(toggleCoverage) {

                        if(tmp.getPolygonReduced(currEnv, tmp.getSignalType())
                                .contains(tmpPoint)) {

                            creator.getPanel().getJTextArea1().
                            	append(tmp.getName() + "\n");
                        }
                    } else {
                        if(tmp.getShape().getPolygon().contains(tmpPoint)) {

                            creator.getPanel().getJTextArea1().
                            	append(tmp.getName() + "\n");
                        }
                    }
                }
            }

            for(Person tmp : currEnv.getPersons()) {

                if(tmp.getShape()
                        .getPolygon()
                        .contains(tmpPoint)) {

                    creator.getPanel().getJTextArea1().
                    	append(tmp.getName() + "\n");
                }
            }

            for(LocationOfInterest tmp : currEnv.getLoIs()) {

                if(tmp.getShape()
                        .getPolygon()
                        .contains(tmpPoint)) {

                	long time = System.currentTimeMillis();
                	
                	Approximation approx = currEnv.calculateError(tmp);
                	
                	time = System.currentTimeMillis() - time;
                	
                    creator.getPanel().getJTextArea1().
                    	append(tmp.getName() + " " + approx.getfPProb() + "\n");
                    
                    creator.getPanel().getJTextArea1().
                		append("Isec:\n");
                    
                    for(Sensor tmpSens : approx.getIsec()) {

                        creator.getPanel().getJTextArea1().
                			append("\t" + tmpSens.getName() + "\n");
                    }
                    
                    creator.getPanel().getJTextArea1().
            			append("NoIsec:\n");
                
                    for(Sensor tmpSens : approx.getNoIsec()) {

	                    creator.getPanel().getJTextArea1().
	            			append("\t" + tmpSens.getName() + "\n");
                    }
                    
                    // Show processing time.
                    
                    creator.getPanel().getJTextArea1().
        				append("Processing time (ms) = " + time);
                    
                }
            }
            
            sec2 = sec1;

        } else {
            sec1 = System.currentTimeMillis()/100;
        }
    }

    public void mouseClicked(MouseEvent e) {

        if(movementCreatorOn) {

            // Store the point in the arrayList

            Triple tmpTriple = new Triple(e.getX(), e.getY(), 0);

            if(currEnv.checkTripleWithObjects(tmpTriple)) {
                currEnv.getCurrPos().add(tmpTriple);

                movementCreator.started();

                // Put person there, for show:

                if(this.currEnv != null) {
                    currPerson = currEnv.getPersons().get(0);
                    currPerson.getShape().setStartPoint(e.getX(), e.getY(), 0);
                }
            } else {

                JOptionPane.showMessageDialog(null, "Person can not be " +
                		"placed there!", "alert", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    public void mousePressed(MouseEvent e) {

        // Check for objects that the point might be within.
        // Use a stupid algorithm that goes through all the
        // objects and finds the one that covers the point.

        //System.err.println("mousePressed: " + e.getX() + " " + e.getY());

        Point tmpPoint = new Point(e.getX(), e.getY());

        for(CommonSensObject tmpObj : currEnv.getObjects()) {

            //System.err.println("Investigates object " + tmpObj.getName());

            if(tmpObj.getShape().getPolygon().contains(tmpPoint)) {

                //System.err.println("Point is inside!");

                currLoI = null;
                currSens = null;
                currObject = tmpObj;
                currPerson = null;
                break;
            } else {
                //System.err.println("Point is NotOperator inside! :(");
            }
        }

        for(Sensor tmpSource : currEnv.getTupleSources()) {

            if(tmpSource instanceof PhysicalSensor) {

                PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;

    //            if(creator.getEnvironmentCreator().getLoICreatorOn()) {
    //
    //                getIntersection(new Triple(e.getX(), e.getY(), 0),
    //                        creator.getEnvironmentCreator()
    //                        .getLoICreator().getCurrLoI());
    //
    //            } else {
//                    System.err.println("Investigates sensor " + 
//                    		tmpSens.getName());

                    if(toggleCoverage) {

                        if(tmpSens.getPolygonReduced(currEnv, 
                        		tmpSens.getSignalType())
                                .contains(tmpPoint)) {

                            currLoI = null;
                            currSens = tmpSens;
                            currObject = null;
                            currPerson = null;
                            break;
                        }
                    } else {
                        if(tmpSens.getShape().getPolygon().
                        		contains(tmpPoint)) {

                            currLoI = null;
                            currSens = tmpSens;
                            currObject = null;
                            currPerson = null;
                            break;
                        }
                    }
            }
        }

        for(LocationOfInterest tmpLoI : currEnv.getLoIs()) {

            if(tmpLoI.getShape().getPolygon().contains(tmpPoint)) {

                currLoI = tmpLoI;
                currSens = null;
                currObject = null;
                currPerson = null;

                //currEnv.calculateError(tmpLoI);

                break;
            } 
        }

        for(Person person : currEnv.getPersons()) {

            if(person.getShape().getPolygon().contains(tmpPoint)) {

                currLoI = null;
                currSens = null;
                currObject = person;
                currPerson = person;

                break;
            }
        }


        repaint();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    CommonSensObject getCurrObject() {
       return currObject;
    }

    PhysicalSensor getCurrSens() {
        return currSens;
    }

    LocationOfInterest getCurrLoI() {
        return currLoI;
    }

    void setMovementCreatorOn(boolean movementCreatorOn) {
        this.movementCreatorOn = movementCreatorOn;
    }

    void setCurrPos(ArrayList<Triple> arrayList) {

        currEnv.setCurrPos(arrayList);

        creator.getPanel().getJButton9().setEnabled(true);
    }

    void setMovementCreator(MovementCreator movementCreator) {
        this.movementCreator = movementCreator;
    }

    public void showMovementPattern() {

        statList = new ArrayList<Triple>();

        int i = 0;

        Triple tmpTriple = null;

        if(currEnv.getCurrPos() != null && 
        		!currEnv.getCurrPos().isEmpty()) {

            for(Triple triple : currEnv.getCurrPos()) {

                // Make sure the person does not crash into walls.

                boolean ok = false;

                while(!ok) {

//                    int randSX = (int) (Math.random() * 100);
//                    int randSY = (int) (Math.random() * 100);

//                    int randX = (int) randSX%(RADIUS*2);
//                    int randY = (int) randSY%(RADIUS*2);

                    int newX = triple.getX(); 
                    //(int) Math.round(triple.getX() - RADIUS + randX);
                    int newY = triple.getY(); 
                    //(int) Math.round(triple.getY() - RADIUS + randY);
                    int newZ = 0;

                    tmpTriple = new Triple(newX, newY, newZ);

                    if(currEnv.checkTripleWithObjects(tmpTriple)) {
                        ok = true;
                    }
                }

                statList.add(i, tmpTriple);

                i++;
            }

            // If we want to have a smoother movement pattern, use this
            // function:

            // setMovement(statList);

            // If setMovement is commented out, currPath has to be set:

            currPath = currEnv.getCurrPos();

            //

            if(currPerson == null) {
                currPerson = currEnv.getPersons().get(0);
            }

            currPerson.getShape().setStartPoint((int) statList.get(0).getX(),
                    (int) statList.get(0).getY(), 0);
        }

        repaint();
    }

    /**
     * Calculates the std. err. for each point and creates the
     * path in which the object should move.
     *
     * @param currPos
     */

    void setMovement(ArrayList<Triple> currPos) {

        ArrayList<Triple> newPoints = new ArrayList<Triple>();

        repaint();

        for(int i = 0; i < currPos.size() - 1; i++) {

            // Find the length between the two points:

            Triple one = new Triple(currPos.get(i));
            Triple two = new Triple(currPos.get(i + 1));

            int length = (int) (Math.sqrt(Math.pow(two.getX() - 
            		one.getX(), 2) +
                    Math.pow(two.getY() - one.getY(), 2)));

            // Create new arraylist with this amount of points:

            //System.err.println("Length = " + length);

            if(length > 0) {

                Triple [] tmpList = new Triple[length];

                tmpList[0] = one;

                tmpList[length - 1] = two;

                // Recursively create all the midpoints:

                createNewList(tmpList, 0, length - 1);

                for(int y = 0; y < length; y++) {
                    newPoints.add(tmpList[y]);
                }
            }
        }

        currPath = newPoints;


    }

    /**
     * Recursively generates a smoother movement pattern by finding all 
     * the points in the lines that make up the movement pattern.
     * 
     * @param tmpList
     * @param start
     * @param stop
     */
    
    private void createNewList(Triple [] tmpList, int start, int stop) {

        if(start > stop || start == stop || start + 1 == stop) {

//            System.err.println("createNewList returns = " + start + 
//            		" " + stop);
            return;
        }

        Triple midPoint = new Triple((int) ((tmpList[start].getX() + 
        		tmpList[stop].getX())/2),
                (int) ((tmpList[start].getY() + tmpList[stop].getY())/2), 0);

        int middle = stop - (stop - start)/2;

        tmpList[middle] = midPoint;

        //System.err.println("createNewList(tmpList, " + start + ", " + middle);

        createNewList(tmpList, start, middle);

        //System.err.println("createNewList(tmpList, " + middle + ", " + stop);

        createNewList(tmpList, middle, stop);

    }

    @SuppressWarnings("unused")
	private synchronized long getTime(boolean emulation) {

        return creator.getMainClass().getTime(emulation);
    }

    public Core getCreator() {
        return creator;
    }

    void setTime(long currentSecond) {
        currTime = currentSecond;
    }

    public long getTime() {
        return currTime;
    }

    /**
     * Turns on/off coverage calculation since calculation takes a while
     * when moving sensors around in the environment.
     */
    
    void toggleCoverage() {
        this.toggleCoverage ^= true;
    }
}
