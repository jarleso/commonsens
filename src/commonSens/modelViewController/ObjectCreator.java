/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.CommonSensObject;
import environment.Permeability;
import environment.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sensing.SignalType;

/**
 *
 * @author jarleso
 */
public class ObjectCreator extends javax.swing.JPanel implements ActionListener  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8514653027449243667L;
	JFrame frame;
    JLabel coordinates;
    JButton showShapeButton, permeabilityButton, doneButton;
    MainView creator;
    private CommonSensObject currObject;
    String signalType;
    double perm;
    private final JButton setShapeButton;
    private final JTextField permeabilitiesField;
    private final JButton nameButton;

    public MainView getCreator() {
        return creator;
    }
    JPanel mainPanel, sidePanel, drawPanel;

    ObjectCreator (MainView creator) {

        this.creator = creator;

        frame = new JFrame("Object Creator");
        frame.add(this);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        
        setShapeButton = new JButton();
        setShapeButton.setText("Set Shape");
        setShapeButton.setEnabled(true);
        setShapeButton.addActionListener(this);
        sidePanel.add(setShapeButton);

        showShapeButton = new JButton();
        showShapeButton.setText("Show Shape");
        showShapeButton.setEnabled(true);
        showShapeButton.addActionListener(this);
        sidePanel.add(showShapeButton);

        permeabilityButton = new JButton();
        permeabilityButton.setText("Add permeability");
        permeabilityButton.setEnabled(true);
        permeabilityButton.addActionListener(this);
        sidePanel.add(permeabilityButton);

        nameButton = new JButton();
        nameButton.setText("Set Name");
        nameButton.setEnabled(true);
        nameButton.addActionListener(this);
        sidePanel.add(nameButton);

        doneButton = new JButton();
        doneButton.setText("Done");
        doneButton.setEnabled(true);
        doneButton.addActionListener(this);
        sidePanel.add(doneButton);

        permeabilitiesField = new JTextField(10);

        if(this.currObject != null) {

            for(Permeability perm : currObject.getPermeabilities()) {

                permeabilitiesField.setText(permeabilitiesField.getText() + "\n" +
                        perm.getSignalType() + " " + perm.getValue() + "\n");

            }
        }

        sidePanel.add(permeabilitiesField);

        mainPanel.add(sidePanel);

        frame.add(mainPanel);

        frame.setBounds(this.getBounds());
        frame.setLocationRelativeTo(creator);
        frame.setAlwaysOnTop(false);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Set Shape")) {

        	System.err.println("NOTE: Set Shape: Nothing is done.");
        	
//            ShapeCanvas shapeCreator = new ShapeCanvas(creator, null);

        } else if(e.getActionCommand().equals("Show Shape")) {

            if(currObject != null) {
                if(currObject.getShape() != null) {
                    new ShapeCanvas(creator, currObject.getShape());
                }
            }

        } else if(e.getActionCommand().equals("Set Name")) {

            setName();

        } else if(e.getActionCommand().equals("Add permeability")) {

            signalType = JOptionPane.showInputDialog("Please input signal type.");
            perm = Float.parseFloat(JOptionPane.showInputDialog("Please input permeability value."));
            
            setPermeability(new Permeability(new SignalType(signalType),
                    perm));
            
            for(Permeability tmpPerm : currObject.getPermeabilities()) {

                permeabilitiesField.setText(permeabilitiesField.getText() + "\n" +
                        tmpPerm.getSignalType() + " " + tmpPerm.getValue() + "\n");

            }

        } else if(e.getActionCommand().equals("Done")) {

            if(creator.getMainClass().addObject(currObject))
                frame.dispose();
        }
    }

    void setName() {

        String name = JOptionPane.showInputDialog("Please input name.");

        if(currObject == null) {
            currObject = new CommonSensObject(name);
        } else {
            currObject.setName(name);
        }
    }

    void setShape(Shape shape) {

        if(currObject == null) {
            currObject = new CommonSensObject(shape);
        } else {
            currObject.setShape(shape);
        }
    }

    void setPermeability(Permeability permeability) {

        if(currObject == null) {
            currObject = new CommonSensObject(permeability);
        } else {
            currObject.addPerm(permeability);
        }
    }
}
