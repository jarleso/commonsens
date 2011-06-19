package modelViewController;

import environment.Shape;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

/* ListDemo.java requires no other files. */
public class ShapeChooser extends JPanel
                      implements ListSelectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -287159630206730169L;
	private JList list;
    private DefaultListModel listModel;

    private static final String chooseString = "Done";
    private JButton choiceButton;

    ArrayList<Shape> shapes;
    ShapeCanvas creator;
    JFrame frame;

    public ShapeChooser(ShapeCanvas creator, ArrayList<Shape> shapes) {
        super(new BorderLayout());

        frame = new JFrame("Shapes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        this.creator = creator;
        this.shapes = shapes;

        listModel = new DefaultListModel();

        for(Shape tmpShape : shapes) {
            listModel.addElement(tmpShape.getName());
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
        choiceButton.addActionListener(new ChoiceListener());

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

    class ChoiceListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            //listModel.remove(index);

            if(index != -1) {
                String tmpName = (String) listModel.get(index);

                for(Shape tmpShape : shapes) {

                    if(tmpShape.getName().equals(tmpName)) {

                        creator.setShape(tmpShape);
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
        }
    }
}