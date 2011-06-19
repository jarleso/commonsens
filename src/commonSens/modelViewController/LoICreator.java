/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.LocationOfInterest;
import environment.Shape;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author jarleso
 */
class LoICreator extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4959315500386094299L;
	EnvironmentCreator creator;
    JFrame f;
    JPanel mainPanel;
    private final JButton shapeAdder;
    private final JButton capabilityAdder;
    LocationOfInterest currLoI;
    private final JButton nameAdder;
    private final JButton done;

    public LoICreator(EnvironmentCreator creator) {
    
        this.creator = creator;
        currLoI = new LocationOfInterest();

        f = new JFrame("Environment Creator");
        f.add(this, BorderLayout.CENTER);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        //mainPanel.setPreferredSize(new Dimension(400, 400));

        nameAdder = new JButton();
        nameAdder.setText("Set Name");
        nameAdder.setEnabled(true);
        nameAdder.addActionListener(this);
        mainPanel.add(nameAdder);

        shapeAdder = new JButton();
        shapeAdder.setText("Set Shape");
        shapeAdder.setEnabled(true);
        shapeAdder.addActionListener(this);
        mainPanel.add(shapeAdder);
        
        capabilityAdder = new JButton();
        capabilityAdder.setText("Add Capability");
        capabilityAdder.setEnabled(true);
        capabilityAdder.addActionListener(this);
        mainPanel.add(capabilityAdder);

        done = new JButton();
        done.setText("Done");
        done.setEnabled(true);
        done.addActionListener(this);
        mainPanel.add(done);

        f.add(mainPanel);

        f.pack();
        f.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Set Shape")) {

            new ShapeCanvas(creator.getCreator().getPanel(), null);

        } else if(e.getActionCommand().equals("Add Capability")) {

            new CapabilityChooser(creator.getCreator().getPanel(),
                    creator.getCreator().getMainClass().getCapabilities());
        } else if(e.getActionCommand().equals("Set Name")) {

            currLoI.setName(JOptionPane.showInputDialog("Please input name."));

        } else if(e.getActionCommand().equals("Done")) {

            creator.setLoICreatorOn(false);

            creator.addLoI(currLoI);
            
            if(creator.getCreator().addLoI(currLoI))
                f.dispose();
        }
    }

    LocationOfInterest getCurrLoI() {
        return currLoI;
    }

    public void setShape(Shape shape) {

        System.err.println("Shape is added to " + currLoI.getName());

        currLoI.setShape(shape);
    }
}
