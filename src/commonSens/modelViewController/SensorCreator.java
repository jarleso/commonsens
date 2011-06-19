/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;


import environment.Shape;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import sensing.Capability;
import sensing.PhysicalSensor;
import sensing.SignalType;

class SensorCreator extends JPanel implements ActionListener,
        MouseMotionListener, MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7994539278932914195L;
	private final JPanel mainPanel;
    private final JPanel sidePanel;
    private final JPanel drawPanel;
    private JLabel signalType;
    private JButton signalTypeChooser;
    private JLabel signalTypeTextField;
    private JLabel Hz;
    private JTextField HzTextField;
    private JLabel capabilitiesProvides;
    private JButton capabilityChooser;
    @SuppressWarnings("unused")
	private JLabel capabilityText;
    private JButton doneButton;
    private final MainView creator;
    private PhysicalSensor currSensor;
    private final JButton coverageAreaButton;
    JFrame f;
    private final JButton nameChooser;

    public SensorCreator(MainView creator) {

        this.creator = creator;
        currSensor = new PhysicalSensor();

        f = new JFrame("Physical Sensor Creator");
        f.add(this, BorderLayout.CENTER);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.setPreferredSize(new Dimension(400, 400));
        mainPanel.addMouseListener(this);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        drawPanel = new JPanel();
        drawPanel.setLayout(new BoxLayout(drawPanel, BoxLayout.Y_AXIS));
        //drawPanel.setPreferredSize(new Dimension(400, 400));

        nameChooser = new JButton();
        nameChooser.setText("Set Name");
        nameChooser.setEnabled(true);
        nameChooser.addActionListener(this);
        sidePanel.add(nameChooser);

        signalType = new JLabel("Signal type");
        sidePanel.add(signalType);
        signalTypeTextField = new JLabel(currSensor.getSignalTypeText());
        sidePanel.add(signalTypeTextField);

        signalTypeChooser = new JButton();
        signalTypeChooser.setText("Set Signal CoordinateStrength");
        signalTypeChooser.setEnabled(true);
        signalTypeChooser.addActionListener(this);
        sidePanel.add(signalTypeChooser);

        Hz = new JLabel("samplingFrequency");
        sidePanel.add(Hz);
        HzTextField = new JTextField(String.valueOf(currSensor.getHz()));
        sidePanel.add(HzTextField);

        capabilitiesProvides = new JLabel("Capabilities");
        sidePanel.add(capabilitiesProvides);

        capabilityChooser = new JButton();
        capabilityChooser.setText("Add Capability");
        capabilityChooser.setEnabled(true);
        capabilityChooser.addActionListener(this);
        sidePanel.add(capabilityChooser);

        capabilityText = new JLabel(currSensor.getCapabilitiesText());

        coverageAreaButton = new JButton();
        coverageAreaButton.setText("Add Shape");
        coverageAreaButton.setEnabled(true);
        coverageAreaButton.addActionListener(this);
        sidePanel.add(coverageAreaButton);

        doneButton = new JButton();
        doneButton.setText("Done");
        doneButton.setEnabled(true);
        doneButton.addActionListener(this);
        sidePanel.add(doneButton);

        mainPanel.add(sidePanel);

        JSeparator jSep = new JSeparator();
        jSep.setOrientation(SwingConstants.VERTICAL);

        //mainPanel.add(jSep);
        //mainPanel.add(drawPanel);
        f.add(mainPanel);

        f.setBounds(this.getBounds());
        f.setLocationRelativeTo(creator.getParent());
        f.setAlwaysOnTop(false);
        f.pack();
        f.setVisible(true);

        setForeground(Color.BLACK);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300,300);
    }

   /*
    * Paint when the AWT tells us to...
    */
//    @Override
//    public void paint(Graphics g) {
//
//        g.setColor(Color.black);
//
//        Shape shape = currSensor.getCoverageArea();
//
//        if(shape != null) {
//
//            int max = shape.getTripleSize();
//
//            shape.setStartPoint(new Triple(100, 100, 0));
//
//            int i = 0;
//
//            for(; i < max - 1; i++) {
//                g.drawLine(shape.getX(i), shape.getY(i),
//                    shape.getX(i + 1), shape.getY(i + 1));
//            }
//
//            g.drawLine(shape.getX(i), shape.getY(i),
//                    shape.getX(0), shape.getY(0));
//        }
//    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {

        System.out.println("x = " + e.getX() + " y = " + e.getY());
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Set Signal CoordinateStrength")) {

            currSensor.setSignalType(new SignalType(JOptionPane.showInputDialog("Please write signal type:")));

        } else if(e.getActionCommand().equals("Set Name")) {

            String name = JOptionPane.showInputDialog("Please input name.");

            currSensor.setName(name);

        } else if(e.getActionCommand().equals("Add Capability")) {

            new CapabilityChooser(creator, creator.getMainClass().getCapabilities());

        } else if(e.getActionCommand().equals("Add Shape")) {
            new ShapeCanvas(this.creator, null);

        } else if(e.getActionCommand().equals("Done")) {

            if(creator.getMainClass().addTupleSource(currSensor))
                f.dispose();
        }

        repaint();
    }

    void setShape(Shape shape) {

        if(currSensor != null) {

            currSensor.setCoverageArea(shape);

        } else {
            System.err.println("physSensor == null in PhysSCreator.java");
            System.exit(-1);
        }
    }

    void addCapability(Capability tmpCap) {

        currSensor.addProvidedCapability(tmpCap);

    }
}