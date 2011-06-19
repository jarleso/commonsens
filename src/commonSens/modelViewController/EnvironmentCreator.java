/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.Environment;
import environment.LocationOfInterest;
import environment.CommonSensObject;
import environment.Shape;
import environment.Triple;
import environment.Person;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import sensing.Capability;
import sensing.ExternalSource;
import sensing.LogicalSensor;
import sensing.PhysicalSensor;
import sensing.Sensor;

/**
 * 
 */
public class EnvironmentCreator extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5069486356468065177L;
	private final JPanel mainPanel;
    private final JPanel sidePanel;
    private final EnvironmentPanel drawPanel;
    private Core creator;
    private final JButton objectAdder;
    private final JButton loiAdder;
    private final JButton doneButton;
    private final JButton sizeChanger;
    private final JButton rotater;
    private final Frame f;
    private JButton sensorAdder;
    private LoICreator loiCreator;
    private JButton loiCoverer;
    private final JButton personAdder;
    private JButton calculator;
    private boolean loiCreatorOn = false;
    private final JButton sensorToObjectAdder;
    private Environment currEnvironment;
    private final JButton toggle;
    private Person currPerson = null;

    public EnvironmentCreator(Core creator, Environment currEnv) {

        this.creator = creator;

        if(currEnv != null) {
            combineObjectsAndSensors(currEnv);
        }
        
        f = new JFrame("Environment Creator");
        f.add(this, BorderLayout.CENTER);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        //mainPanel.setPreferredSize(new Dimension(400, 400));

        sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(0,1));

        drawPanel = new EnvironmentPanel(creator, currEnv);
        
        JScrollPane drawPanelScroller = new JScrollPane(drawPanel);
        
        //drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
        //drawPanel.setPreferredSize(new Dimension(300, 400));

        objectAdder = new JButton();
        objectAdder.setText("Add Object");
        objectAdder.setEnabled(true);
        objectAdder.addActionListener(this);
        sidePanel.add(objectAdder);

//        sensorAdder = new JButton();
//        sensorAdder.setText("Add Capability in Environment");
//        sensorAdder.setEnabled(true);
//        sensorAdder.addActionListener(this);
//        sidePanel.add(sensorAdder);

        sensorToObjectAdder = new JButton();
        sensorToObjectAdder.setText("Add Sensor to Current Object");
        sensorToObjectAdder.setEnabled(true);
        sensorToObjectAdder.addActionListener(this);
        sidePanel.add(sensorToObjectAdder);

        personAdder = new JButton();
        personAdder.setText("Add Person");
        personAdder.setEnabled(true);
        personAdder.addActionListener(this);
        sidePanel.add(personAdder);

        loiAdder = new JButton();
        loiAdder.setText("Add LoI");
        loiAdder.setEnabled(true);
        loiAdder.addActionListener(this);
        sidePanel.add(loiAdder);

        sizeChanger = new JButton();
        sizeChanger.setText("Change Size");
        sizeChanger.setEnabled(true);
        sizeChanger.addActionListener(this);
        sidePanel.add(sizeChanger);

//        loiCoverer = new JButton();
//        loiCoverer.setText("Cover Current LoI");
//        loiCoverer.setEnabled(true);
//        loiCoverer.addActionListener(this);
//        sidePanel.add(loiCoverer);

        rotater = new JButton();
        rotater.setText("Rotate");
        rotater.setEnabled(true);
        rotater.addActionListener(this);
        sidePanel.add(rotater);

//        calculator = new JButton();
//        calculator.setText("Calculate FPProb");
//        calculator.setEnabled(true);
//        calculator.addActionListener(this);
//        sidePanel.add(calculator);

        toggle = new JButton();
        toggle.setText("Toggle coverage");
        toggle.setEnabled(true);
        toggle.addActionListener(this);
        sidePanel.add(toggle);

        doneButton = new JButton();
        doneButton.setText("Done");
        doneButton.setEnabled(true);
        doneButton.addActionListener(this);
        sidePanel.add(doneButton);

        mainPanel.add(sidePanel);

        JSeparator jSep = new JSeparator();
        jSep.setOrientation(SwingConstants.VERTICAL);

        mainPanel.add(jSep);
        mainPanel.add(drawPanelScroller);

        f.add(mainPanel);

        f.setBounds(this.getBounds());
        f.setLocationRelativeTo(creator.getPanel().getParent());
        f.setAlwaysOnTop(false);

        f.pack();

        if(creator.isRunningExperiment()) {
            f.setVisible(false);
        } else {
            f.setVisible(true);
        }

        setForeground(Color.BLACK);

        //drawPanel.setInforArea();
    }

    public Core getCreator() {
        return creator;
    }

    public EnvironmentPanel getDrawPanel() {
        return drawPanel;
    }

    public Environment getCurrEnvironment() {

        this.currEnvironment = drawPanel.getCurrEnv();

        return this.currEnvironment;
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Add Object")) {

            System.err.println("num objects: " + creator.getMainClass().getObjects().size());

            new ObjectChooser(this, creator.getMainClass().getObjects());

        } else if(e.getActionCommand().equals("Add Capability in Environment")) {

            new CapabilityChooser(this, creator.getMainClass().getCapabilities());

        } else if(e.getActionCommand().equals("Add Sensor to Current Object")) {

            new SensorChooser(this, drawPanel.getCurrObject(), creator.getMainClass().getTupleSources());

        } else if(e.getActionCommand().equals("Add Name")) {

            drawPanel.getCurrEnv().setName(JOptionPane.showInputDialog("Please write name:"));

        } else if(e.getActionCommand().equals("Add Person")) {

            String personName = JOptionPane.showInputDialog("Please write name:");

            drawPanel.getCurrEnv().addPerson(new Person(personName,
                    new Triple(drawPanel.getWidth()/2,
                    drawPanel.getHeight()/2, 0), 
                    creator.getMainClass().getCEPQueue()));

        } else if(e.getActionCommand().equals("Add LoI")) {

            loiCreator = new LoICreator(this);//, creator.getMainClass().getLoIs());
            loiCreatorOn = true;

        } else if(e.getActionCommand().equals("Change Size")) {

            float width = 0;
            float height = 0;

            if(drawPanel.getCurrObject() != null ||
                    drawPanel.getCurrLoI() != null) {

                width = (drawPanel.getCurrObject() != null)?
                    drawPanel.getCurrObject().getShape()
                        .getPolygon().getBounds().width
                    :
                    drawPanel.getCurrLoI().getShape()
                        .getPolygon().getBounds().width;

                height = (drawPanel.getCurrObject() != null)?
                    drawPanel.getCurrObject().getShape()
                        .getPolygon().getBounds().height
                    :
                    drawPanel.getCurrLoI().getShape()
                        .getPolygon().getBounds().height;

                width = Float.parseFloat(JOptionPane.showInputDialog("Please input width (currently: " + width + ")."));
                height = Float.parseFloat(JOptionPane.showInputDialog("Please input height (currently: " + height + ")."));
            }

            if(width != 0 && height != 0) {

                if(drawPanel.getCurrObject() != null) {

                    drawPanel.getCurrObject().getShape().changeDimRect(width, height);
                } else if(drawPanel.getCurrLoI() != null) {

                    drawPanel.getCurrLoI().getShape().changeDimRect(width, height);
                }
            }

        } else if(e.getActionCommand().equals("Rotate")) {

            float degrees = Float.parseFloat(JOptionPane.showInputDialog("Please input degrees."));

            if(drawPanel.getCurrObject() != null) {
                drawPanel.getCurrObject().getShape().rotate(degrees);
            } else if(drawPanel.getCurrSens() != null) {
                drawPanel.getCurrSens().getShape().rotate(degrees);
            }

        } else if(e.getActionCommand().equals("Cover Current LoI")) {

            if(drawPanel.getCurrLoI() != null) {

                boolean done = false;

                for(Capability tmpCap :
                    drawPanel.getCurrLoI().getCapabilities()) {

                    for(Sensor tmpSensor :
                        creator.getMainClass().getTupleSources()) {

                        if(tmpSensor.getIsCapabilityProvided(tmpCap.getName())) {

                            Sensor newSens;

                            if(tmpSensor instanceof PhysicalSensor)
                                newSens = new PhysicalSensor((PhysicalSensor) tmpSensor);

                            else if(tmpSensor instanceof LogicalSensor)
                                newSens = new LogicalSensor((LogicalSensor) tmpSensor);

                            else
                                newSens = new ExternalSource((ExternalSource) tmpSensor);

                            // Place it correctly.

                            if(newSens instanceof PhysicalSensor) {
                                if(coverLoI(drawPanel.getCurrLoI(), (PhysicalSensor) newSens)) {
                                    drawPanel.getCurrEnv().addTupleSource(
                                        newSens);
                                }
                            }

                            done = true;
                            break;
                        }
                    }
                    if(done) break;
                }
            }

        } else if(e.getActionCommand().equals("Calculate FPProb")) {

            System.err.println("This methond is no longer used.");
        	
            //currEnvironment.calculateErrors();

        } else if(e.getActionCommand().equals("Toggle coverage")) {

            drawPanel.toggleCoverage();

        } else if(e.getActionCommand().equals("Done")) {

            if(creator.getMainClass().addEnvironment(drawPanel.getCurrEnv()))
                f.dispose();
        }

        drawPanel.repaint();
    }

    void addObject(CommonSensObject tmpObject) {

        drawPanel.getCurrEnv().addCEPObject(tmpObject);
    }

    void addLoI(LocationOfInterest loi) {

        drawPanel.getCurrEnv().addCEPLoI(loi);
    }

    void addCapability(Capability tmpCap) {

        Sensor addTS;

        if(loiCreator != null) {

            loiCreator.getCurrLoI().addCapability(tmpCap);
        } else {

            // Find a sensor that provides the capability.

            for(Sensor tmpTS : creator.getMainClass().getTupleSources()) {

                System.err.println("Checking " + tmpTS.getName());

                if(tmpTS.getIsCapabilityProvided(tmpCap.getName())) {

                    // Capability match

                    System.err.println("Capability match.");

                    if(tmpTS instanceof PhysicalSensor) {

                        addTS = new PhysicalSensor((PhysicalSensor) tmpTS);

                        addTS.setName(addTS.getName() + "_"
                                + getCurrEnvironment().getTupleSources().size());

                        getCurrEnvironment().addTupleSource((PhysicalSensor) addTS);
                    
                    } else if(tmpTS instanceof ExternalSource) {

                        addTS = new ExternalSource((ExternalSource) tmpTS);

                        addTS.setName(addTS.getName() + "_"
                                + getCurrEnvironment().getTupleSources().size());

                        getCurrEnvironment().addTupleSource((ExternalSource) addTS);

                    } else {

                        // Continue search.
                    }
                }
            }
        }
    }

    void addSensor(Sensor tmpSens) {
        drawPanel.getCurrEnv().addTupleSource(tmpSens);

//        if(tmpSens instanceof PhysicalSensor)
//            drawPanel.getCurrEnv().setShowCapability(((PhysicalSensor)tmpSens).getProvidedCapabilities().get(0));
    }

    LoICreator getLoICreator() {
        return loiCreator;
    }

    private boolean coverLoI(LocationOfInterest currLoI, PhysicalSensor newSens) {

        // Multiplies with 2 so that it can be covered by half the
        // coverage area.

        if(currLoI.getShape().area()*2 > newSens.getShape().area()) {
            return false;
        }

        // Awful assumption...

//        Triple loiCenter = new Triple(drawPanel.getCurrLoI()
//                .getShape().getStartPoint());

        boolean goOn = false;
//        boolean fix = false;

        while(goOn) {

            for(Triple tmpTrip : drawPanel.getCurrLoI()
                    .getShape().getTriples()) {
                
                Point tmpPoint = new Point(tmpTrip.getX(), tmpTrip.getY());
                Shape tmpShape = newSens.getShape();
                Polygon tmpPoly = newSens.getPolygonReduced(drawPanel.getCurrEnv(),
                        newSens.getSignalType());

                while(!tmpPoly.contains(tmpPoint)) {

                    tmpShape.moveHoriz(-1);
                }

                goOn = false;
            }
        }

        return true;
    }

    void setMovementCreatorOn(boolean movementCreatorOn) {
        drawPanel.setMovementCreatorOn(movementCreatorOn);
    }

    void setMovementCreator(MovementCreator movementCreator) {
        drawPanel.setMovementCreator(movementCreator);
    }

    void setCurrPos(ArrayList<Triple> arrayList) {
        drawPanel.setCurrPos(arrayList);
    }

    public boolean getLoICreatorOn() {
        return loiCreatorOn;
    }

    public void setLoICreatorOn(boolean what) {
        loiCreatorOn = what;
    }

    /**
     * If physical sensors in the environment are attached to objects in
     * the environment, this method performs the actual merging of these
     * two. 
     *
     * @param currEnv
     */

    private void combineObjectsAndSensors(Environment currEnv) {

        for(Sensor tmpSens : currEnv.getTupleSources()) {

            if(tmpSens instanceof PhysicalSensor) {

                PhysicalSensor sens = (PhysicalSensor) tmpSens;

                if(sens.getObjectCoveredName() != null) {

                    CommonSensObject tmpObject = currEnv.
                            getObject(sens.getObjectCoveredName());

                    if(tmpObject == null) {

                        // Check persons:

                        System.err.println("sens.getObjectCoveredName() = " +
                                sens.getObjectCoveredName());

                        tmpObject = currEnv.
                                getPerson(sens.getObjectCoveredName());

                        if(tmpObject == null) {

                            System.err.println("tmpObject == null. currEnv." + 
                                    "Persons().size = " +
                                    currEnv.getPersons().size());
                            System.exit(-1);
                        }
                    }

                    sens.addToObject(tmpObject);
                    tmpObject.addSensor(sens);

                }
            }
        }
    }

    public void close() {
        f.dispose();
    }

    public void repaintDrawPanel() {
        getDrawPanel().repaint();
    }



    Person getCurrPerson() {
        return currPerson;
    }
}
