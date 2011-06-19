package modelViewController;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import sensing.Capability;

/* ListDemo.java requires no other files. */
public class CapabilityChooser extends JPanel
                      implements ListSelectionListener, ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7509559380755063202L;
	private JList list;
    private DefaultListModel listModel;

    private static final String chooseString = "Done";
    private JButton choiceButton;

    ArrayList<Capability> capabilities;
    MainView creator;
    EnvironmentCreator envCreator;
    JFrame frame;

    public CapabilityChooser(MainView creator,
            ArrayList<Capability> capabilities) {
        super(new BorderLayout());

        this.envCreator = null;
        this.creator = creator;
        this.capabilities = capabilities;

        setup((Component) creator);
    }

    CapabilityChooser(EnvironmentCreator envCreator, ArrayList<Capability> capabilities) {

        super(new BorderLayout());

        this.envCreator = envCreator;
        this.creator = null;
        this.capabilities = capabilities;

        setup((Component) creator);
    }

    public void setup(Component c) {

        frame = new JFrame("Objects");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);
        frame.setLocationRelativeTo(creator);

        listModel = new DefaultListModel();

        for(Capability tmpCap : capabilities) {
            listModel.addElement(tmpCap.getName());
        }

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        choiceButton = new JButton(chooseString);
        choiceButton.setActionCommand(chooseString);
        choiceButton.addActionListener(this);

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(choiceButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                choiceButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                choiceButton.setEnabled(true);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        //This method can be called only if
        //there's a valid selection
        //so go ahead and remove whatever's selected.
        int index = list.getSelectedIndex();
        //listModel.remove(index);

        if(index != -1) {
            String tmpName = (String) listModel.get(index);

            for(Capability tmpCap : capabilities) {

                if(tmpCap.getName().equals(tmpName)) {

                    if(envCreator != null) {

                        envCreator.addCapability(tmpCap);

                    } else {

                        creator.getCore().addCapability(tmpCap);

                    }

                    break;
                }
            }

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                choiceButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }

        if(envCreator != null) {

            envCreator.getDrawPanel().repaint();

        } else {

            creator.repaint();

        }

        frame.dispose();
    }
}