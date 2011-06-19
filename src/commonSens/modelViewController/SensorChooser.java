package modelViewController;

import environment.CommonSensObject;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import sensing.ExternalSource;
import sensing.LogicalSensor;
import sensing.PhysicalSensor;
import sensing.Sensor;

/* ListDemo.java requires no other files. */
public class SensorChooser extends JPanel
                      implements ListSelectionListener, ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3497804146428404396L;
	private JList list;
    private DefaultListModel listModel;

    private static final String chooseString = "Done";
    private JButton choiceButton;

    ArrayList<Sensor> sensors;
    EnvironmentCreator creator = null;
    CommonSensObject object = null;
    JFrame frame;

    public SensorChooser(EnvironmentCreator creator,
            ArrayList<Sensor> sensors) {
        super(new BorderLayout());

        this.creator = creator;
        startUp(sensors);
    }

    public SensorChooser(EnvironmentCreator creator, 
    		CommonSensObject currObject,
            ArrayList<Sensor> sensors) {

        super(new BorderLayout());

        this.object = currObject;
        this.creator = creator;
        startUp(sensors);
    }

    private void startUp(ArrayList<Sensor> sensors) {

        frame = new JFrame("Sensors");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        this.sensors = sensors;

        listModel = new DefaultListModel();

        for(Sensor tmpSens : sensors) {
            listModel.addElement(tmpSens.getName());
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

            for(Sensor tmpSens : sensors) {

                if(tmpSens.getName().equals(tmpName)) {

                    if(tmpSens instanceof PhysicalSensor)
                        tmpSens = new PhysicalSensor((PhysicalSensor) tmpSens);

                    else if(tmpSens instanceof LogicalSensor)
                        tmpSens = new LogicalSensor((LogicalSensor) tmpSens);

                    else
                        tmpSens = new ExternalSource((ExternalSource) tmpSens);

                    creator.addSensor(tmpSens);

                    tmpSens.setName(tmpSens.getType() + "_"
                            + creator.getDrawPanel().getCurrEnv().
                            getTupleSources().size());

                    if(object != null && tmpSens instanceof PhysicalSensor) {
                        ((PhysicalSensor) tmpSens).addToObject(object);
                        object.addSensor((PhysicalSensor)tmpSens);

                        System.err.println(object.getName() + " added to " + 
                        		tmpSens.getName());
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
        creator.getDrawPanel().repaint();
        frame.dispose();
    }
}