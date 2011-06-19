/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * QueryParser.java
 *
 * Created on 20.apr.2009, 17:01:28
 */

package modelViewController;

import language.StrictnessFilter;
import language.ConditionalStrict;
import environment.LocationOfInterest;
import eventProcessor.Timestamp;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import language.AndOperator;
import language.QueryElement;
import language.TimedListOfAtomicQueries;
import language.AtomicQuery;
import language.ConOpDuring;
import language.ConOpEquals;
import language.ConcurrencyOperator;
import language.ConsecutiveRelation;
import language.OrOperator;
import language.Strict;
import sensing.Capability;
import sensing.Sensor;

/**
 * Originally, this was a GUI based setup where the user could create 
 * statements
 * by pushing buttons. Now, the functionality of the class is used, but not
 * the GUI part.
 *
 * @author jarleso
 */
public class QueryParser extends javax.swing.JPanel
        implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3134197928926826536L;
	Core creator = null;
    AtomicQuery currCondition = null;

    public void setCurrCondition(AtomicQuery currCondition) {
        this.currCondition = currCondition;
    }

    public Core getCreator() {
        return creator;
    }
    
    JFrame frame;

    /** Creates new form QueryParser */

    public QueryParser(Core creator, Scanner in, int numAnds) {

        this.creator = creator;

        initComponents();

        frame = new JFrame("Statement creator");

        frame.add(this);
        frame.setBounds(this.getBounds());
        frame.setLocationRelativeTo(creator.getPanel());
        frame.pack();

        if(in == null) {
            frame.setVisible(creator.getPanel().getDoShow());
        } else {

            frame.setVisible(false);

            QueryElement tmpStatement = readComplexStatement(in, numAnds);

            if(tmpStatement != null) {
                
                getCreator().getMainClass().addComplexStatement(tmpStatement);

                getCreator().getPanel().getJButton3().setEnabled(true);
                getCreator().getPanel().showStatements();
            } else {
                System.err.println("QueryParser: tmpStatement == null");
            }

            frame.dispose();
        }


        //getCreator().getMainClass().addComplexStatement(getCurrStatement());
    }

    public void enableRelations() {

        getJButton5().setEnabled(true);
        getJButton7().setEnabled(true);

    }

    public void disableRelations() {

        getJButton5().setEnabled(false);
        getJButton7().setEnabled(false);

    }

    public void toggleTemporalFeatures() {

//        getJButton8().setEnabled(getJButton8().isEnabled() ^ true);
//        getJButton9().setEnabled(getJButton9().isEnabled() ^ true);
    }

    public AtomicQuery getCurrCondition() {
        return currCondition;
    }

    @SuppressWarnings("unused")
	private void enableConditions() {

        getJButton1().setEnabled(true);
        getJButton2().setEnabled(true);
        getJButton3().setEnabled(true);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NotOperator modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();

        jLabel1.setText("Elements to add:");

        jButton1.setText("LoI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Sensor");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Capability");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel2.setText("Current statement:");

        jButton4.setText("Create statement");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setText("Relations:");

        jButton5.setText("->");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("=>");
        jButton7.setActionCommand("jButton7");
        jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel4.setText("Temporal constraints");

        jButton8.setText("Time");
        jButton8.setEnabled(false);

        jButton9.setText("Interval");
        jButton9.setEnabled(false);

        jButton10.setText("Set Condition");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel5.setText("Operators:");

        jButton6.setText("&&");

        jButton11.setText("||");

        jButton12.setText("!");

        jLabel6.setText("Concurrency Operators");

        jButton13.setText("Equals");

        jButton14.setText("Starts");

        jButton15.setText("Finishes");

        jButton16.setText("During");

        jButton17.setText("Overlaps");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButton1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton3)
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel1))
                            .addGap(143, 143, 143))
                        .addComponent(jLabel6)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButton5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton7))
                                .addComponent(jLabel3))
                            .addGap(26, 26, 26)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jButton6)
                                    .addGap(6, 6, 6)
                                    .addComponent(jButton11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton12))))
                        .addComponent(jButton10)
                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9))
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton17)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6)
                            .addComponent(jButton11)
                            .addComponent(jButton12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton14)
                    .addComponent(jButton15)
                    .addComponent(jButton16)
                    .addComponent(jButton17))
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );
    }// </editor-fold>

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {

        getJButton4().setEnabled(true);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        getJButton4().setEnabled(true);
    }
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {

        // ->

        frame.repaint();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {

        // =>

        frame.repaint();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {

        // Statement is finished.

        frame.dispose();
    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    public JButton getJButton4() {
        return jButton4;
    }
    // End of variables declaration

    public JButton getJButton2() {
        return jButton2;
    }

    public JButton getJButton3() {
        return jButton3;
    }
    // End of variables declaration

    public JButton getJButton8() {
        return jButton8;
    }

    public JButton getJButton9() {
        return jButton9;
    }
    // End of variables declaration

    public JButton getJButton5() {
        return jButton5;
    }

    public JButton getJButton7() {
        return jButton7;
    }
    // End of variables declaration

    public JButton getJButton1() {
        return jButton1;
    }
    // End of variables declaration

    public JTextArea getJTextArea1() {
        return jTextArea1;
    }

    boolean notInTupleSources(Sensor sensor, 
    		ArrayList<Sensor> sensors) {

        for(Sensor ts : sensors) {
            if(ts.equals(sensor)) return false;
        }

        return true;
    }

    /**
     * Creates a timed complex statement.
     *
     * @param in
     * @param numAnds
     * @return
     */

    private TimedListOfAtomicQueries 
    readTimedChainOfAtomicStatements(Scanner in, int numAnds) {

        String tmpString;

        TimedListOfAtomicQueries timedListOfAtomicQueries = null;

        QueryElement first = null;

        first = readAtomicStatementsChain(in, numAnds);

        // Finish off with the last part of the statement.

        String tmpTb = in.next();

//        System.err.println("tmpTb for complex statement = " + tmpTb);

        Timestamp tB = new Timestamp(tmpTb);

        String tmpTe = in.next();

        Timestamp tE = new Timestamp(tmpTe);

        tmpString = in.next();

        boolean isMin = false;
        double percentage = 0;

        // P-registered statement

        if(tmpString.equals("min")) {
            isMin = true;
        } else {
            isMin = false;
        }

        percentage = in.nextDouble();

//        System.err.println("isMin = " + isMin + 
//        		" percentage = " + percentage);

        timedListOfAtomicQueries = new TimedListOfAtomicQueries(first,
                tB, tE, isMin, percentage);

        //System.err.println("The statement: " + first.showElementRecursive());

        return timedListOfAtomicQueries;
    }

    /**
     * Reads and returns an atomic statement.
     *
     * @param in
     * @return
     */

    private AtomicQuery readOneAtomicStatement(Scanner in, 
    		boolean isNegation) {

        //System.err.println("Inside readOneAtomicStatement");

        String tmpCapability = in.next();

        /**
         * All capabilities have to be registered already as objects. Look
         * in the file capabilities.dat.
         */

        //System.err.println("tmpCapability = " + tmpCapability);

        Capability currCap = getCreator()
                .getMainClass().getCapability(tmpCapability);

        if(currCap == null) {

            JOptionPane.showMessageDialog(null, "Capability " + 
            		tmpCapability + " does not exist in the system!",
                    "Alert", JOptionPane.ERROR_MESSAGE);

            return null;

        }

        String tmpOperator = in.next();

        //System.err.println("tmpOperator " + tmpOperator);

        String val = in.next();

        //System.err.println("val = " + val);

        String tmpLoI = in.next();

        LocationOfInterest currLoI = null;

        if(!tmpLoI.equals("null")) {

            currLoI = getCreator().getMainClass()
                .getEnvironments().get(0).getLoI(tmpLoI);

//            System.err.println("Env = " + getCreator().getMainClass()
//                    .getEnvironments().get(0).getEnvironment());
            
            if(currLoI == null) {

                JOptionPane.showMessageDialog(null, "LoI " + tmpLoI + 
                		" does not exist in the environment!",
                        "Alert", JOptionPane.ERROR_MESSAGE);

                return null;
            }
        } else {
            
            // tmpLoI == null. 
            
        }

        String tmpTb = in.next();

        //System.err.println("tmpTb = " + tmpTb);

        Timestamp tB = new Timestamp(tmpTb);

        String tmpTe = in.next();

        //System.err.println("tmpTb = " + tmpTe);

        Timestamp tE = new Timestamp(tmpTe);

        String tmpString = in.next();

        boolean isMin = false;
        double percentage = 0;

        // P-registered statement

        if(tmpString.equals("min")) {
            isMin = true;
        } else {
            isMin = false;
        }

        percentage = in.nextDouble();

//        System.err.println("isMin = " + isMin + " percentage = " + 
//        		percentage);

        Object tmpValue = getCorrectObjectType(val);

        return new AtomicQuery(currLoI, currCap, tmpOperator,
                                  tmpValue, tB, tE, isMin, percentage,
                                  isNegation);
    }

    /**
     * For the DMSN10 experiments, numAnds only applies to the first chain. We
     * keep it this way for now, but this should be changes for future
     * experiments, since the second chain also contains possible complex
     * events.
     *
     * @param in
     * @param concOp
     * @param numAnds
     * @return
     */

    private QueryElement readConcurrencyClass(Scanner in,
            ConcurrencyOperator concOp, int numAnds) {

        //System.err.println("Reading concurrency class");

        concOp.setFirstChain((TimedListOfAtomicQueries) 
        		readAtomicStatementsChain(in, numAnds));

        // The first chain should have been stopped by ","

        concOp.setSecondChain((TimedListOfAtomicQueries)
        		readAtomicStatementsChain(in, 0 /* numAnds */));

        // The second chain should have been stopped by ")". in is therefore
        // pointing at the next nextQueryElement.
        
        return concOp;
    }

    private QueryElement readAtomicStatementsChain(Scanner in, 
    		int numAnds) {

        QueryElement first = null;
        QueryElement curr = null;

        boolean firstElement = true;
        boolean nextIsNot = false;

        String tmpString = in.next();

        while(!tmpString.equals(",") && !tmpString.equals(")")) {

//            System.err.println("tmpString = " + tmpString);

            if(tmpString.equals("(")) {

//                System.err.println("Reading atomic statement.");

                QueryElement tmpElem = null;
                QueryElement storeElem = null;

                tmpElem = readOneAtomicStatement(in, nextIsNot);

                // Read the last ")"

                tmpString = in.next();

                if(!tmpString.equals(")")) {

//                    System.err.println("Error in end of atomic statement: " 
//                    		+ tmpString);

                }

                // Reset nextIsNot

                nextIsNot = false;

                storeElem = tmpElem;

                // Now ")" should be the next in points to.

                /**
                 * In case there are several ANDs that have to be made.
                 */

                for(int i = 0; i <= numAnds; i++) {

                    if(firstElement) {

                        first = tmpElem;
                        curr = tmpElem;
                        firstElement = false;
                    } else {

                        curr.setNext(tmpElem);
                        curr = curr.getNext();
                    }

                    if(i < numAnds) {
                        tmpElem = new AndOperator();

                        curr.setNext(tmpElem);
                        curr = curr.getNext();

                        tmpElem = 
                        	new AtomicQuery((AtomicQuery) storeElem);
                    }
                }

            } else {

                String noneAtomicStatement = tmpString;

                QueryElement tmpElem = null;

//                System.err.println("noneAtomicStatement = " + 
//                		noneAtomicStatement);

                if(noneAtomicStatement.equals("->")) {

                    tmpElem = new ConsecutiveRelation();

                } else if(noneAtomicStatement.equals("=>")) {

                    tmpElem = new Strict();

                } else if(noneAtomicStatement.matches("\\=\\>\\d")) {

                    int key = Integer.
                    parseInt(noneAtomicStatement.substring(2));

                    tmpElem = new ConditionalStrict(key);

                } else if(noneAtomicStatement.equals("||")) {

                    tmpElem = new OrOperator();

                } else if(noneAtomicStatement.equals("&&")) {

                    tmpElem = new AndOperator();

                } else if(noneAtomicStatement.equals("!")) {

                    nextIsNot = true;

                    tmpString = in.next();

                    continue;

                } else if(noneAtomicStatement.equals("equals")) {

                    tmpString = in.next();

                    if(tmpString.equals("(")) {

                        tmpElem = readConcurrencyClass(in, new ConOpEquals(), 
                        		numAnds);
                    } else {

                        System.err.println("Parse error for equals: " + 
                        		tmpString);
                    }

                } else if(noneAtomicStatement.equals("during")) {

                    tmpString = in.next();

                    if(tmpString.equals("(")) {

                        tmpElem = readConcurrencyClass(in, new ConOpDuring(), 
                        		numAnds);
                    } else {

                        System.err.println("Parse error for equals: " + 
                        		tmpString);
                    }

                } else if(noneAtomicStatement.equals("[")) {

                    tmpElem = this.readTimedChainOfAtomicStatements(in, 
                    		numAnds);

                    tmpString = in.next();

                    if(!tmpString.equals("]")) {
                        JOptionPane.showMessageDialog(null, "Timed chain of " + 
                                " atomic statements are parsed incorrectly: " +
                                tmpString,
                            "Alert", JOptionPane.ERROR_MESSAGE);
                    }

                } else {

                    //System.err.println("Parse error: operatorOrRelation = "
                        //    + noneAtomicStatement);

                    return null;
                }

                if(firstElement) {

                    curr = tmpElem;

                    first = curr;
                    firstElement = false;
                } else {

//                    System.err.println("tmpElem = " + 
//                    		tmpElem.showStatementElement());

//                    System.err.println("curr = " + 
//                    		curr.showStatementElement());

                    curr.setNext(tmpElem);
                    curr = curr.getNext();
                }
            }

            if(in.hasNext()) {
                tmpString = in.next();
            } else {
                break;
            }
        }

//        System.err.println("Has left while-loop with tmpString = " + 
//        		tmpString);
//        
//        System.err.println("Query returned is " + first.showElementRecursive());

        return first;
    }

    private QueryElement readComplexStatement(Scanner in, int numAnds) {

        QueryElement first = null;
        QueryElement curr = null;
        QueryElement tmpElem = null;

        boolean firstElement = true;
//        boolean nextIsNot = false;

        while (in.hasNext()) {

            String tmpString = in.next();

            if (tmpString.equals("#")) {

                // A comment. Skip line.

                in.nextLine();

                continue;
            }

            if (tmpString.equals("[")) {

                tmpElem = readTimedChainOfAtomicStatements(in, numAnds);

                tmpString = in.next();

                if(!tmpString.equals("]")) {
                    JOptionPane.showMessageDialog(null, "Timed chain of " +
                    		"atomic statements are parsed incorrectly: " + 
                    		tmpString,
                        "Alert", JOptionPane.ERROR_MESSAGE);
                }

            } else if (tmpString.equals("->")) {

                tmpElem = new ConsecutiveRelation();

                if(firstElement) {
                    JOptionPane.showMessageDialog(null, "Statements can " +
                    		"not start with ->",
                        "Alert", JOptionPane.ERROR_MESSAGE);

                    return null;
                }

            } else if (tmpString.equals("=>")) {

                tmpElem = new Strict();

                if(firstElement) {
                    JOptionPane.showMessageDialog(null, "Statements can not " +
                    		"start with =>",
                        "Alert", JOptionPane.ERROR_MESSAGE);

                    return null;
                }

            } else if (tmpString.equals("filter")) {

                /**
                 * Read a filter and store it for the processing part.
                 */

                int key = in.nextInt();

                StrictnessFilter tmpFilter = readStrictnessFilter(in, numAnds);

                // Store the current filter globally.

                if (creator.getMainClass().addStricnessFilter(tmpFilter)) {

                } else {
                    JOptionPane.showMessageDialog(null, "Key " + key + 
                    		" for the stricness filter is already in use.",
                        "Alert", JOptionPane.ERROR_MESSAGE);
                }

            } else {

                JOptionPane.showMessageDialog(null, "Unknown tmpString = " + 
                		tmpString,
                        "Alert", JOptionPane.ERROR_MESSAGE);

            }

            if(firstElement) {

                first = tmpElem;
                curr = tmpElem;
                firstElement = false;
            } else {

                curr.setNext(tmpElem);
                curr = curr.getNext();
            }
        }

        return first;
    }

    /**
     * Checks if the format of the strig is similar to 2010-02-27,
     * i.e., [0-9]{4}\-[0-9]{1,2}\-[0-9]{1,2}
     */

    public static boolean isDate(String tmpTb) {

        return tmpTb.matches("[0-9]{4}\\-[0-9]{1,2}\\-[0-9]{1,2}");
    }

    private StrictnessFilter readStrictnessFilter(Scanner in, int numAnds) {

        int key = in.nextInt();

        StrictnessFilter tmpFilter = new StrictnessFilter(key);

        int numConditions = in.nextInt();

        for (int i = 0; i < numConditions; i++) {
        
            tmpFilter.addConditions(this.readComplexStatement(in, numAnds));
        }

        return tmpFilter;
    }

    /**
     * Returns the correct (Integer, Double, Float or String) type of
     * a value.
     *
     * @param val
     * @return
     */

    public static Object getCorrectObjectType(Object val) {

        /**
         * We use exceptions to find the correct type of value, since
         * tmpValue is Object (to be general).
         */

        Object tmpValue = null;

        if(!(val instanceof String)) {
            return val;
        }

        String sVal = (String) val;

        try {
            tmpValue = Integer.parseInt(sVal);
        } catch (NumberFormatException e1) {

//            System.err.println("readOneAtomicStatement: '" + sVal + 
//            		"' is not Integer.");

            try {
                tmpValue = Double.parseDouble(sVal);
            } catch (NumberFormatException e2) {

//                System.err.println("readOneAtomicStatement: '" + sVal + 
//                		"' is not Double.");

                try {
                    tmpValue = Float.parseFloat(sVal);
                } catch (NumberFormatException e3) {

//                    System.err.println("readOneAtomicStatement: '" + sVal + 
//                    		"' is not Float.");

                    try {
                        tmpValue = sVal;
                    } catch (NumberFormatException e4) {
                    }
                }
            }
        }

        return tmpValue;
    }
}
