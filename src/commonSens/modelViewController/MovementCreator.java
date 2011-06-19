/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author jarleso
 */
public class MovementCreator extends javax.swing.JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6173907108766002170L;
	JFrame frame;
    JButton button;
    Core creator;

    public MovementCreator(Core creator) {

        this.creator = creator;
        creator.setMovementCreatorOn(false, this);

        frame = new JFrame("");
        frame.add(this);

        button = new JButton();
        button.setText("Pick first point");
        button.setEnabled(false);
        button.addActionListener(this);
        frame.add(button);

        frame.setBounds(this.getBounds());
        frame.setLocationRelativeTo(creator.getPanel());
        frame.setAlwaysOnTop(true);
        frame.pack();
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        creator.getPanel().getJButton7().setEnabled(true);
        creator.getPanel().getJButton8().setEnabled(true);
        creator.getPanel().getJButton9().setEnabled(true);
        frame.dispose();
    }

    public void started() {
        
        button.setText("Done");
        button.setEnabled(true);
        frame.repaint();
    }

}
