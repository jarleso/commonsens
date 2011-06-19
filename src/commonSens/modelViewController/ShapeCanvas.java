package modelViewController;

import environment.Shape;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * A simple canvas for generating shapes.
 * 
 * @author jarleso
 *
 */

class ShapeCanvas extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3126214892759533349L;
	private final JPanel mainPanel;
    private final JPanel sidePanel;
    private final DrawPanel drawPanel;
    private final JButton polygonButton;
    private final JButton circleButton;
    private final JButton doneButton;
    private final MainView creator;
    private final JButton listButton;
    private final JLabel nameLabel;
    private final JTextField nameTextField;
    JFrame f;

    public ShapeCanvas(MainView creator, Shape shape) {

        this.creator = creator;

        f = new JFrame("Shape Creator");
        f.add(this, BorderLayout.CENTER);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        drawPanel = new DrawPanel(creator, shape);

        listButton = new JButton();
        listButton.setText("Show Shapes");
        listButton.setEnabled(true);
        listButton.addActionListener(this);
        sidePanel.add(listButton);

        nameTextField = new JTextField(10);
        sidePanel.add(nameTextField);

        if(shape != null) {
            nameTextField.setText(shape.getName());
        }
        
        nameLabel = new JLabel("Name");
        nameLabel.setLabelFor(nameTextField);

        polygonButton = new JButton();
        polygonButton.setText("Rectangle");
        polygonButton.setEnabled(true);
        polygonButton.addActionListener(this);
        sidePanel.add(polygonButton);

        circleButton = new JButton();
        circleButton.setText("Circle");
        circleButton.setEnabled(true);
        circleButton.addActionListener(this);
        sidePanel.add(circleButton);

        doneButton = new JButton();
        doneButton.setText("Done");
        doneButton.setEnabled(true);
        doneButton.addActionListener(this);
        sidePanel.add(doneButton);

        mainPanel.add(sidePanel);

        JSeparator jSep = new JSeparator();
        jSep.setOrientation(SwingConstants.VERTICAL);

        mainPanel.add(jSep);
        mainPanel.add(drawPanel);
        f.add(mainPanel);

        //f.setBounds(this.getBounds());
        f.setLocationRelativeTo(creator.getParent());
        f.setAlwaysOnTop(false);

        setForeground(Color.BLACK);
        f.pack();
        f.setVisible(true);

    }

    /**
     * Creates a circle with the given parameters. 
     * 
     * @param degrees
     * @param circleRadius
     * @return
     */
    
    private Shape createCircle(float degrees, float circleRadius) {

        double longitude = 0;//-85.578852;
        double latitude = 0;//38.215601;

        double points = 360; // Instead of 64.
        double maxPoints;
        double radius = 100;

        double rLatitude = Math.toDegrees(circleRadius/radius);
        double rLongitude = rLatitude / Math.cos(Math.toRadians(latitude));

        /**
         * The function will always make a circle, and "points" denotes the
         * granularity that applies to all circles. To calculate the number of
         * points to run through, we find the proportion like this:
         *
         * (degrees/360) = (num/points)
         *
         * ->
         *
         * (degrees*points)/360 = num
         *
         */

        if(degrees != 360) {

            // Add 1...

            maxPoints = Math.round(degrees); //1 + (degrees*points)/360;

        } else {

            maxPoints = points;
        }

        int[] cX = new int[(int) maxPoints];
        int[] cY = new int[(int) maxPoints];

        int i = 0;

        System.err.println("Max points = " + maxPoints);

        for(; i < (int) maxPoints - 1; i++) {

            double theta = Math.PI * (i / (points / 2));

            double tX = longitude + (rLongitude * Math.cos(theta));
            double tY = latitude + (rLatitude * Math.sin(theta));

            cX[i] = (int) Math.round(tX);
            cY[i] = (int) Math.round(tY);

            System.out.println(cX[i] + " " + cY[i]);
        }

        if(degrees != 360) {

            // Create the 0 point:

            cX[i] = 0;
            cY[i] = 0;
        }


        return new Shape(cX, cY);
    }

    private Shape createRectangle(float width, float height) {

        int[] cX = new int[4];
        int[] cY = new int[4];

        cX[0] = -Math.round(width)/2;
        cY[0] = -Math.round(height)/2;

        cX[1] = Math.round(width)/2;
        cY[1] = -Math.round(height)/2;

        cX[2] = Math.round(width)/2;
        cY[2] = Math.round(height)/2;

        cX[3] = -Math.round(width)/2;
        cY[3] = Math.round(height)/2;

        return new Shape(cX, cY);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getActionCommand().equals("Show Shapes")) {
            @SuppressWarnings("unused")
			ShapeChooser cEPShapeChooser = 
				new ShapeChooser(this, creator.getMainClass().getShapes());
            
        } else if(e.getActionCommand().equals("Circle")) {

            float degrees = Float.parseFloat(JOptionPane.showInputDialog("Please input degrees."));
            float radius = Float.parseFloat(JOptionPane.showInputDialog("Please input radius."));

            drawPanel.setShape(createCircle(degrees, radius));
            drawPanel.getShape()
                    .setName(JOptionPane.showInputDialog("Please write name."));
            
        } else if(e.getActionCommand().equals("Rectangle")) {

            float width = Float.parseFloat(JOptionPane.showInputDialog("Please input width."));
            float height = Float.parseFloat(JOptionPane.showInputDialog("Please input height."));
            
            drawPanel.setShape(createRectangle(width, height));
            drawPanel.getShape()
                    .setName(JOptionPane.showInputDialog("Please write name."));

        } else if(e.getActionCommand().equals("Done")) {

            if(!drawPanel.getOnlyShow()) {
                creator.getMainClass().addShape(drawPanel.getShape());
                creator.getCore().setShape(drawPanel.getShape());
            }

            f.dispose();
        }

        if(drawPanel.getShape() != null) {
            nameTextField.setText(drawPanel.getShape().getName());
        }
        
        drawPanel.repaint();
    }

    public void setShape(Shape shape) {
        drawPanel.setShape(shape);
    }


}
