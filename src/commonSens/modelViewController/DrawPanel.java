/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import environment.Shape;
import environment.Triple;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/**
 *
 * Called by ShapeCanvas to show the shapes.
 *
 * @author jarleso
 */
class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7151205788460340696L;
	Shape shape;
    MainView creator;
    private boolean onlyShow;
    private static final int SIZE = 300;     // Size of paint area.

    public DrawPanel(MainView creator, Shape shape) {

        setPreferredSize(new Dimension(SIZE, SIZE));
        setBackground(Color.white);
        
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.creator = creator;

        if(shape != null) {
            onlyShow = true;
            this.shape = shape;
        } else {
            onlyShow = false;
        }
    }

    public void setShape(Shape shape) {

        this.shape = shape;
    }

    /*
    * Paint when the AWT tells us to...
    */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.setColor(Color.black);

        if(shape != null) {

            int max = shape.getTripleSize();

            shape.setStartPoint(new Triple(50, 50, 0));

            int i = 0;

            for(; i < max - 1; i++) {
                g.drawLine(shape.getX(i), shape.getY(i),
                    shape.getX(i + 1), shape.getY(i + 1));
            }

            g.drawLine(shape.getX(i), shape.getY(i),
                    shape.getX(0), shape.getY(0));
        }
    }

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

    public Shape getShape() {
        return shape;
    }

    boolean getOnlyShow() {
        return onlyShow;
    }

}
