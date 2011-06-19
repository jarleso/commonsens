/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * getMainPanel.java
 *
 * Created on 15.apr.2009, 11:34:13
 */
package modelViewController;

import environment.LocationOfInterest;
import eventProcessor.DataTupleFilter;
import eventProcessor.MainDataTupleFilter;
import eventProcessor.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import language.AtomicQuery;
import language.QueryElement;
import statistics.PlotCreator;

/**
 * The view of the system. It shows the buttons the user can push.
 *
 * @author jarleso
 */
public class MainView extends javax.swing.JPanel implements ActionListener,
        MouseMotionListener, MouseListener, java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1124713892076470104L;
	public static int MEDIUM = 10;
    public static int RADIUS = 5;

    public static final String RUNTIME_DIR = "RuntimeData/";
	public static final String EXPERIMENTS_DIR = "Experiments/";
    
    private FileChooser jFileChooser1;
    private boolean doShow;
    private JButton newSensorButton;
    private boolean reEval;


    private JButton newEnvironmentButton;
    private JButton exitButton;

    private final JButton jButton14;

    private JButton jButton15;
    private final JButton jButton16;

    private final JButton jButton17;

    private MainDataTupleFilter globalFilter;

    private final JScrollPane condScroller;
    private JButton jButton18;
    private JButton calculateCoverageButton;
    private final JButton moreSensButton;

    private final JButton moreAndsButton;
    private final JButton gnuButton;
    private final JButton tracefileButton;
    private final JScrollPane statementsScroller;
    private final JButton overnightButton;



    public JFrame getFrame() {
        return frame;
    }
    private CommonSens mainClass;

    public CommonSens getMainClass() {
        return mainClass;
    }
    private JFrame frame;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;
    private JButton jButton7;
    private JButton jButton8;
    private JButton jButton9;
    private JButton jButton10;
    private JButton jButton11;
    private JButton jButton13;
    private JPanel buttonPanelPalette;
    private JButton openEnvironmentButton;
    private JButton saveEnvironmentButton;
    private JButton newObjectButton;
    private JTextArea currCondText;
    private JTextArea currSetsText;
    private JLayeredPane jLayeredPane1;
    private JLabel currCond;
    private JLabel currSets;
    private JLabel isRunning;
    private JLabel deviation;

    public JLayeredPane getJLayeredPane1() {
        return jLayeredPane1;
    }
    HashMap<String, JLabel> areas;
    private JTextArea jTextArea1;
    private JTextArea jTextArea2;
    private String expPrefix;
    JPanel topPanel;
    JPanel bottomPanel;


    Core core;
	private JButton increasingNumberRealSensors;
	private JButton realWorldPlot;
	private JScrollPane setScroller;
	private JButton binomButton;
	private JButton permButton;
	private boolean stopPushed;
	private int stepCount = 0;

    /** Creates new form getMainPanel */
    public MainView(CommonSens mainClass, boolean doShow, String expPrefix,
    		boolean palette, boolean reEval) {

        this.mainClass = mainClass;
        this.doShow = doShow;
        this.reEval = reEval;
        this.stopPushed = false;

        Dimension dim = new Dimension();

        setSize(dim);

        frame = new JFrame("CommonSens");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        bottomPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        topPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

//        jButton1 = new javax.swing.JButton();
//        jButton1.setText("Create statement");
//        jButton1.addActionListener(this);
//        buttonPanel.add(jButton1);

        jButton10 = new javax.swing.JButton();
        jButton10.setText("Open Query");
        jButton10.addActionListener(this);
        buttonPanel.add(jButton10);

//        jButton11 = new javax.swing.JButton();
//        jButton11.setText("Save statement");
//        jButton11.addActionListener(this);
//        buttonPanel.add(jButton11);

//        jButton13 = new javax.swing.JButton();
//        jButton13.setText("Check statement");
//        jButton13.addActionListener(this);
//        buttonPanel.add(jButton13);

        buttonPanel.add(new JSeparator());
        //buttonPanel.add(new JSeparator());
        //buttonPanel.add(new JSeparator());

        jButton5 = new javax.swing.JButton();
        jButton5.setText("Create movement pattern");
        jButton5.addActionListener(this);
        buttonPanel.add(jButton5);

//        jButton15 = new javax.swing.JButton();
//        jButton15.setText("Set radius");
//        jButton15.addActionListener(this);
//        buttonPanel.add(jButton15);

        jButton6 = new javax.swing.JButton();
        jButton6.setText("Open movement pattern");
        jButton6.addActionListener(this);
        buttonPanel.add(jButton6);

        jButton7 = new javax.swing.JButton();
        jButton7.setText("Save movement pattern");
        jButton7.addActionListener(this);
        jButton7.setEnabled(true);
        buttonPanel.add(jButton7);

        jButton8 = new javax.swing.JButton();
        jButton8.setText("Show movement pattern");
        jButton8.addActionListener(this);
        jButton8.setEnabled(true);
        buttonPanel.add(jButton8);

//        jButton18 = new javax.swing.JButton();
//        jButton18.setText("Show many movement patterns");
//        jButton18.addActionListener(this);
//        jButton18.setEnabled(true);
//        buttonPanel.add(jButton18);

        jButton9 = new javax.swing.JButton();
        jButton9.setText("Run movement pattern");
        jButton9.addActionListener(this);
        jButton9.setEnabled(true);
        buttonPanel.add(jButton9);

        buttonPanel.add(new JSeparator());

//        jButton2 = new javax.swing.JButton();
//        jButton2.setText("Pull");
//        jButton2.addActionListener(this);
//        buttonPanel.add(jButton2);

        buttonPanel.add(new JSeparator());

        jButton17 = new javax.swing.JButton();
        jButton17.setText("Set steps per timestamp");
        jButton17.addActionListener(this);
        jButton17.setEnabled(true);
        buttonPanel.add(jButton17);

        jButton3 = new javax.swing.JButton();
        jButton3.setText("Start Monitoring");
        jButton3.addActionListener(this);
        jButton3.setEnabled(true);
        buttonPanel.add(jButton3);

        jButton14 = new javax.swing.JButton();
        jButton14.setText("Start Simulation");
        jButton14.addActionListener(this);
        jButton14.setEnabled(true);
        buttonPanel.add(jButton14);

        jButton16 = new javax.swing.JButton();
        jButton16.setText("Step Simulation");
        jButton16.addActionListener(this);
        jButton16.setEnabled(true);
        buttonPanel.add(jButton16);

        jButton4 = new javax.swing.JButton();
        jButton4.setText("Stop Monitoring");
        jButton4.addActionListener(this);
        jButton4.setEnabled(true);
        buttonPanel.add(jButton4);

        topPanel.add(buttonPanel);

        JSeparator jSep = new JSeparator();
        jSep.setOrientation(SwingConstants.VERTICAL);

        topPanel.add(jSep);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel();
        infoLabel.setText("Info:");
        infoLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        infoPanel.add(infoLabel);

        jTextArea1 = new JTextArea();
        jTextArea1.setText("");
        jTextArea1.setPreferredSize(new Dimension(150, 50));
        jTextArea1.setLineWrap(true);
        jTextArea1.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        infoPanel.add(jTextArea1);

        topPanel.add(infoPanel);

        JSeparator jSep2 = new JSeparator();
        jSep2.setOrientation(SwingConstants.VERTICAL);

        topPanel.add(jSep2);

        JPanel stmtPanel = new JPanel();
        stmtPanel.setLayout(new BoxLayout(stmtPanel, BoxLayout.Y_AXIS));

        JLabel stmtLabel = new JLabel();
        stmtLabel.setText("Queries:");
        stmtLabel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        stmtPanel.add(stmtLabel);

        jTextArea2 = new JTextArea();
        jTextArea2.setLineWrap(true);
        jTextArea2.setText("");

        statementsScroller = new JScrollPane(jTextArea2);
        statementsScroller.setPreferredSize(new Dimension(400, 100));
        statementsScroller.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        stmtPanel.add(statementsScroller);

        currCond = new JLabel();
        currCond.setText("Current condition/state:");
        currCond.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        stmtPanel.add(currCond);

        currCondText = new JTextArea();
        currCondText.setLineWrap(true);
        currCondText.setText("");

        condScroller = new JScrollPane(currCondText);
        condScroller.setPreferredSize(new Dimension(50, 100));
        condScroller.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        stmtPanel.add(condScroller);

        currSets = new JLabel();
        currSets.setText("Current Message:");
        currSets.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        
        stmtPanel.add(currSets);

        currSetsText = new JTextArea();
        currSetsText.setLineWrap(true);
        currSetsText.setText("");

        setScroller = new JScrollPane(currSetsText);
        setScroller.setPreferredSize(new Dimension(50, 100));        
        setScroller.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        
        stmtPanel.add(setScroller);

        isRunning = new JLabel();
        isRunning.setBackground(Color.WHITE);
        isRunning.setForeground(Color.DARK_GRAY);
        isRunning.setText("Box:");
        isRunning.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        stmtPanel.add(isRunning);

        deviation = new JLabel();
        deviation.setForeground(Color.WHITE);
        deviation.setText("OK");
        deviation.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        stmtPanel.add(deviation);

        topPanel.add(stmtPanel);

        JSeparator jSep3 = new JSeparator();
        jSep3.setOrientation(SwingConstants.VERTICAL);

        topPanel.add(jSep3);

        JPanel probPanel = new JPanel();
        probPanel.setLayout(new BoxLayout(probPanel, BoxLayout.Y_AXIS));
        probPanel.setAlignmentX(SwingConstants.RIGHT);
        probPanel.setBorder(new TitledBorder("Prob"));
        probPanel.setSize(100, 100);

        // Go through the sub-areas and add these:

        JSeparator jSep4;
        jSep4 = new JSeparator();
        jSep4.setOrientation(SwingConstants.HORIZONTAL);

        // Create JLayeredPane for Area:

        jLayeredPane1 = new JLayeredPane();
        jLayeredPane1.setOpaque(true);
        jLayeredPane1.setPreferredSize(dim);
        //frame.getContentPane().add(jLayeredPane1);//, BorderLayout.SOUTH);
        jLayeredPane1.setAlignmentX(MainView.CENTER_ALIGNMENT);

        buttonPanelPalette = new JPanel();
        buttonPanelPalette.setLayout(new GridLayout(0,1));
        buttonPanelPalette.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        tracefileButton = new javax.swing.JButton();
        tracefileButton.setText("Load Tracefile");
        tracefileButton.addActionListener(this);
        buttonPanelPalette.add(tracefileButton);

        newEnvironmentButton = new javax.swing.JButton();
        newEnvironmentButton.setText("New Environment");
        newEnvironmentButton.addActionListener(this);
        buttonPanelPalette.add(newEnvironmentButton);

        openEnvironmentButton = new javax.swing.JButton();
        openEnvironmentButton.setText("Open Environment");
        openEnvironmentButton.addActionListener(this);
        buttonPanelPalette.add(openEnvironmentButton);

        saveEnvironmentButton = new javax.swing.JButton();
        saveEnvironmentButton.setText("Save Environment");
        saveEnvironmentButton.addActionListener(this);
        buttonPanelPalette.add(saveEnvironmentButton);

        newObjectButton = new javax.swing.JButton();
        newObjectButton.setText("New Object");
        newObjectButton.addActionListener(this);
        buttonPanelPalette.add(newObjectButton);

        newSensorButton = new javax.swing.JButton();
        newSensorButton.setText("New Physical Sensor");
        newSensorButton.addActionListener(this);
        buttonPanelPalette.add(newSensorButton);

        bottomPanel.add(buttonPanelPalette);
        
        JPanel buttonPanelPalette2 = new JPanel();
        buttonPanelPalette2.setLayout(new GridLayout(0,1));
        buttonPanelPalette2.setAlignmentX(JPanel.CENTER_ALIGNMENT);

//        calculateCoverageButton = new javax.swing.JButton();
//        calculateCoverageButton.setText("Calculate Coverage");
//        calculateCoverageButton.addActionListener(this);
//        buttonPanelPalette2.add(calculateCoverageButton);

        moreSensButton = new javax.swing.JButton();
        moreSensButton.setText("More sens per loi");
        moreSensButton.addActionListener(this);
        buttonPanelPalette2.add(moreSensButton);

        overnightButton = new javax.swing.JButton();
        overnightButton.setText("Run overnight");
        overnightButton.addActionListener(this);
        buttonPanelPalette2.add(overnightButton);

        moreAndsButton = new javax.swing.JButton();
        moreAndsButton.setText("More ands");
        moreAndsButton.addActionListener(this);
        buttonPanelPalette2.add(moreAndsButton);

        gnuButton = new javax.swing.JButton();
        gnuButton.setText("Call gnuplot");
        gnuButton.addActionListener(this);
        buttonPanelPalette2.add(gnuButton);
        
        exitButton = new javax.swing.JButton();
        exitButton.setText("Exit");
        exitButton.addActionListener(this);
        buttonPanelPalette2.add(exitButton);
        
        bottomPanel.add(buttonPanelPalette2);
        
        JPanel buttonPanelPalette3 = new JPanel();
        buttonPanelPalette3.setLayout(new GridLayout(0,1));
        buttonPanelPalette3.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        increasingNumberRealSensors = new javax.swing.JButton();
        increasingNumberRealSensors.setText("More real sensors");
        increasingNumberRealSensors.addActionListener(this);
        buttonPanelPalette3.add(increasingNumberRealSensors);
        
        realWorldPlot = new javax.swing.JButton();
        realWorldPlot.setText("Make real-world plot");
        realWorldPlot.addActionListener(this);
        buttonPanelPalette3.add(realWorldPlot);

        binomButton = new javax.swing.JButton(); 
        binomButton.setText("Calculate binominal coefficient");
        binomButton.addActionListener(this);
        buttonPanelPalette3.add(binomButton);
        
//        permButton = new javax.swing.JButton(); 
//        permButton.setText("Get permutations");
//        permButton.addActionListener(this);
//        buttonPanelPalette3.add(permButton);
        
        bottomPanel.add(buttonPanelPalette3);
        
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
              

        bottomPanel.add(jLayeredPane1);

        mainPanel.add(topPanel);
        mainPanel.add(jSep4);
        mainPanel.add(bottomPanel);

        frame.getContentPane().add(mainPanel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(doShow);

        core = new Core(mainClass, this);
    }

    public JButton getJButton7() {
        return jButton7;
    }

    public boolean getDoShow() {
        return doShow;
    }

    JButton getJButton3() {

        return jButton3;
    }

    JButton getJButton8() {

        return jButton8;
    }

    JButton getJButton9() {

        return jButton9;
    }

    public String getOutPrefix() {
        return this.expPrefix;
    }

    void showStatements() {

        jTextArea2.setText("");

        for (QueryElement stmt : getMainClass().getComplexStatements()) {
            jTextArea2.append(stmt.showElementRecursive() + "\n");
        }
    }
    
    public void actionPerformed(ActionEvent e) {

        jTextArea1.setText("");

        if (e.getActionCommand().equals("Pull")) {

            // Pull all the sensors.

            core.pullSensors(0);

        } else if (e.getActionCommand().equals("Open Query")) {
        	
            jFileChooser1 = new FileChooser(RUNTIME_DIR);

            jFileChooser1.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            int returnVal = jFileChooser1.showOpenDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                core.readStatementFile(jFileChooser1.getSelectedFile(), 0);

            } else {
                //System.err.println("What?!");
            }

        } else if (e.getActionCommand().equals("Save statement")) {

        	/**
        	 * Never used. Elements are obtained through the command line.
        	 */
        	
            jFileChooser1 = new FileChooser("./../Statements");

            jFileChooser1.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
            int returnVal = jFileChooser1.showSaveDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                PrintWriter out;

                try {
                    out = new PrintWriter(
                    		new BufferedWriter(
                    				new FileWriter(jFileChooser1.
                    						getSelectedFile())));
                    out.print(this.getMainClass().getComplexStatements().
                    		get(0).showElementRecursive());
                    out.close();
                } catch (IOException ex) {
                }
            }

        } else if (e.getActionCommand().equals("Check statement")) {

            // Assuming that the first element is a timed chain 
        	// of atomic statements.

            QueryElement tmpElem = getMainClass().getComplexStatements().
            get(0).getFirstElement();

            // Check the coverage for each LoI and report this

            currCondText.setText("");
//            String infoText = currCondText.getText();

            while (tmpElem != null) {

                if (tmpElem instanceof AtomicQuery) {

                    LocationOfInterest tmpLoI = ((AtomicQuery) tmpElem).
                    getLoi();

                    currCondText.setText(currCondText.getText() + "\n"
                            + tmpLoI.getName() + " error = " + tmpLoI.
                            getError());

                    currCondText.setText(expPrefix);

                }

                tmpElem = tmpElem.getNext();
            }

        } else if (e.getActionCommand().equals("Create movement pattern")) {

            core.createMovementPattern();

        } else if (e.getActionCommand().equals("Open movement pattern")) {
        	
            jFileChooser1 = new FileChooser(RUNTIME_DIR);
            jFileChooser1.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            int returnVal = jFileChooser1.showOpenDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                core.readMovementFile(jFileChooser1.getSelectedFile());

                getJButton7().setEnabled(true);
                getJButton8().setEnabled(true);
                getJButton9().setEnabled(true);

                setReset();
            }

            // Update the coordinates for the person.



        } else if (e.getActionCommand().equals("Save movement pattern")) {

            jFileChooser1 = new FileChooser(RUNTIME_DIR);
            jFileChooser1.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
            int returnVal = jFileChooser1.showSaveDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                FileWriter fOut = null;
                try {
                    fOut = new FileWriter(jFileChooser1.getSelectedFile());
                } catch (IOException ex) {
                    Logger.getLogger(MainView.class.getName()).log(Level.
                    		SEVERE, null, ex);
                }

                BufferedWriter bOut = new BufferedWriter(fOut);

                try {

                    bOut.write(core.getEnvironmentCreator().getDrawPanel().
                    		getCurrEnv().getCurrPosString());
                    bOut.close();
                } catch (IOException ex) {
                }
            }

        } else if (e.getActionCommand().equals("Set radius")) {

            int radius = Integer.parseInt(JOptionPane.
            		showInputDialog("Set radius"));

            setRadius(radius);

        } else if (e.getActionCommand().equals("Show movement pattern")) {

            showMovementPattern();

        } else if (e.getActionCommand().equals("Show many movement patterns")) {

            showMovementPattern();

        } else if (e.getActionCommand().equals("Run movement pattern")) {

            core.runMovement(false);

        } else if (e.getActionCommand().equals("Set steps per timestamp")) {

            core.setStepsPerTimestamp(Integer.parseInt(JOptionPane.
            		showInputDialog("Set steps per timestamp")));

        } else if (e.getActionCommand().equals("Start Monitoring")) {

            //globalFilter = setupExperiment();
            core.startEventProcessing(true, 1, 0, 0, core.getSimulationOption(), 0,
            		reEval);

        } else if (e.getActionCommand().equals("Start Simulation")) {

            //globalFilter = setupExperiment();

            core.startEventProcessing(false, 1, 0, 0, core.getSimulationOption(), 0,
            		false);

        } else if (e.getActionCommand().equals("Step Simulation")) {

            if (core.getFirstStep() == true) {
                core.setFirstStep(false);

                globalFilter = core.setupEventProcessingPhase(false, 0, 0, 0, 
                		core.getSimulationOption(), false);
            }

            core.stepEventProcessing(globalFilter);
            
            System.err.println("\n" + (stepCount++) + "\n");

        } else if (e.getActionCommand().equals("Stop Monitoring")) {

        	/**
        	 * First wait for the evaluation thread to stop.
        	 */

        	setStopPushed(true);
        	
        	/**
        	 * The thread reads getStopPushed() and when it finds out the
        	 * button is pushed it stops, but first it calls
        	 * setStopPushed(false). We take advantage of this...  
        	 */
        	
        	while(getStopPushed()) {
        		try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
        	}
        	
            core.setFirstStep(true);
            core.stopAll(-1);

        } else if (e.getActionCommand().equals("New Object")) {

            core.setEnvironmentCreatorOn(false);
            core.setSensorCreatorOn(false);
            core.setObjectCreatorOn(true);

            core.setObjectCreator(new ObjectCreator(this));

        } else if (e.getActionCommand().equals("New Physical Sensor")) {

            core.setEnvironmentCreatorOn(false);
            core.setSensorCreatorOn(true);
            core.setObjectCreatorOn(false);

            core.setPhysicalSensorCreator(new SensorCreator(this));

        } else if (e.getActionCommand().equals("New Capability")) {

//            core.setEnvironmentCreatorOn(false);
//            core.setSensorCreatorOn(false);
//            core.setObjectCreatorOn(false);
//
//            String name = JOptionPane.showInputDialog("Please write name:");
//            String description = JOptionPane.showInputDialog("Please write " +
//            		"description:");

            //Capability tmpCap = new Capability(name, description);

            //getMainClass().addProvidedCapability(tmpCap);

        } else if (e.getActionCommand().equals("Load Tracefile")) {

             core.setSimluationOption(chooseSimulationMode());

        } else if (e.getActionCommand().equals("New Environment")) {

            core.setEnvironmentCreatorOn(false);
            core.setSensorCreatorOn(false);
            core.setObjectCreatorOn(false);

            core.setEnvironmentCreator(new EnvironmentCreator(core, null));

        } else if (e.getActionCommand().equals("Open Environment")) {

            core.setEnvironmentCreatorOn(false);             
            core.setSensorCreatorOn(false);
            core.setObjectCreatorOn(false);

            jFileChooser1 = new FileChooser(RUNTIME_DIR);
            jFileChooser1.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            int returnVal = jFileChooser1.showOpenDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                core.readEnvironmentFile(jFileChooser1.getSelectedFile(), 1);
            }


        } else if (e.getActionCommand().equals("Save Environment")) {

            jFileChooser1 = new FileChooser(RUNTIME_DIR);
            jFileChooser1.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
            int returnVal = jFileChooser1.showSaveDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                FileWriter fOut = null;
                try {
                    fOut = new FileWriter(jFileChooser1.getSelectedFile());
                } catch (IOException ex) {
                    Logger.getLogger(MainView.class.getName()).log(
                    		Level.SEVERE, null, ex);
                }

                BufferedWriter bOut = new BufferedWriter(fOut);

                try {
                    //out = new ObjectOutputStream (
                	//new FileOutputStream (jFileChooser1.getSelectedFile()));

                    String envOut = core.getEnvironmentCreator().
                    getDrawPanel().getCurrEnv().getEnvironment();

                    System.err.println("envOut = " + envOut);

                    bOut.write(envOut);
                    //out.writeChars(envOut) ;
                    //out.close ();

                    bOut.close();

                } catch (IOException ex) {
                }
            }

        } else if (e.getActionCommand().equals("Calculate Coverage")) {

            core.calculateCoverageTime();

        } else if (e.getActionCommand().equals("More ands")) {

            core.moreSensPerLoI(mainClass.getEnvironmentFilename(),
                    mainClass.getMovementFileName(),
                    mainClass.getStatementFilename(), 1, -1, -1);

        } else if (e.getActionCommand().equals("More sens per loi")) {

            core.moreSensPerLoI(mainClass.getEnvironmentFilename(),
                    mainClass.getMovementFileName(),
                    mainClass.getStatementFilename(), -1, 0, -1);

        } else if (e.getActionCommand().equals("Run overnight")) {

            core.moreSensPerLoI(mainClass.getEnvironmentFilename(),
                    mainClass.getMovementFileName(),
                    mainClass.getStatementFilename(), 1, 5, -1);

            core.moreSensPerLoI(mainClass.getEnvironmentFilename(),
                    mainClass.getMovementFileName(),
                    mainClass.getStatementFilename(), 5, 0, -1);

        } else if (e.getActionCommand().equals("Call gnuplot")) {

            String expDir = JOptionPane.showInputDialog("Write directory:");
            int numRuns = Integer.parseInt(JOptionPane.
            		showInputDialog("numRuns:"));
            long numSecs = Long.parseLong(JOptionPane.
            		showInputDialog("numSecs:"));
            int numSens = Integer.parseInt(JOptionPane.
            		showInputDialog("numSens:"));
            int numAnds = Integer.parseInt(JOptionPane.
            		showInputDialog("numAnds:"));

            String tmpExp = this.mainClass.getExpPrefix();

            this.mainClass.setExpPrefix(EXPERIMENTS_DIR + expDir + "/");

            PlotCreator.createGnuplotTupleProcessing2D(numRuns, numSecs, 
            		numSens,
                    numAnds, this.mainClass.getExpPrefix());

            this.mainClass.setExpPrefix(tmpExp);
            
        } else if (e.getActionCommand().equals("More real sensors")) {
        	
        	int numSensors = Integer.parseInt(JOptionPane.
        			showInputDialog("What is the max number of" +
        					"sensors?"));
        	
        	Scanner [] scanners = new Scanner[numSensors];
        	
        	String [] filenames = new String[numSensors];
        	
        	for(int i = 0; i < numSensors; i++) {
        		
        		jFileChooser1 = new FileChooser(EXPERIMENTS_DIR);
                jFileChooser1.setDialogType(javax.swing.JFileChooser.
                		OPEN_DIALOG);
                int returnVal = jFileChooser1.showOpenDialog(MainView.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {

                    try {
						scanners[i] = new Scanner(jFileChooser1.
								getSelectedFile());
						
						filenames[i] = jFileChooser1.getSelectedFile().
											getPath();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
                }        		
        	}
        	
        	PlotCreator.genStatsForMoreRealSensors(scanners, filenames, 
        			mainClass.getExpPrefix());

        } else if (e.getActionCommand().equals("Make real-world plot")) {
        	
    		jFileChooser1 = new FileChooser(EXPERIMENTS_DIR);
    		jFileChooser1.setDialogTitle("Choose experiment directory.");
    		jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser1.setDialogType(javax.swing.JFileChooser.
            		OPEN_DIALOG);
            
            int returnVal = jFileChooser1.showOpenDialog(MainView.this);

            File dir = null;
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {

            	jFileChooser1.setCurrentDirectory(jFileChooser1.
						getSelectedFile());
            	
            	dir = jFileChooser1.getCurrentDirectory();
            }        		

            FilenameFilter filter1 = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith("0_0.dat");
                }
            };
            
            String[] sensorFiles = dir.list(filter1);
            
//            for(int i = 0; i < sensorFiles.length; i++) {
//            	System.err.println(sensorFiles[i]);
//            }

            Scanner[] scanners = new Scanner[sensorFiles.length];
            
            for(int i = 0; i < sensorFiles.length; i++) {

                try {
    				scanners[i] = new Scanner(new File(jFileChooser1.
    						getCurrentDirectory() + "/" + sensorFiles[i]));
    			} catch (FileNotFoundException e1) {
    				e1.printStackTrace();
    			}
            }
            
            FilenameFilter filter2 = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith("StateChanges.dat");
                }
            };

        	String[] smFile = dir.list(filter2);
            
        	if(smFile.length != 1) {
        		System.err.println("smFile.length != 1");
        	}
        	
			Scanner smScanner = null;
        	
			System.err.println("Creating scanner for " + jFileChooser1.
			getCurrentDirectory() + "/" + smFile[0]);
			
        	try {
				smScanner = new Scanner(new File(jFileChooser1.
						getCurrentDirectory() + "/" + smFile[0]));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
        	
			try {
				PlotCreator.createGnuplotFromRun(sensorFiles, scanners, 
						smFile, smScanner, mainClass.getExpPrefix(),
						dir.getPath());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		
        } else if (e.getActionCommand().equals("Calculate binominal " +
        		"coefficient")) {
        	
        	Object[] options = {"MAX", "MIN"};

        	int option = JOptionPane.showOptionDialog(frame,
	            "Choose MAX or MIN:",
	            "Choose MAX or MIN",
	            JOptionPane.YES_NO_CANCEL_OPTION,
	            JOptionPane.QUESTION_MESSAGE,
	            null,
	            options,
	            options[0]);
	        	
        	int samples = Integer.parseInt(JOptionPane.
            		showInputDialog("Number of samples:"));
        	
        	int pval = Integer.parseInt(JOptionPane.
            		showInputDialog("P-value:"));
        	
        	Core.binCoff(option == 0, samples, pval);

        } else if (e.getActionCommand().equals("Get permutations")) {
        	
        	int sN = Integer.parseInt(JOptionPane.
            		showInputDialog("Number of sensors:"));
        	

        	int tN = Integer.parseInt(JOptionPane.
            		showInputDialog("Time window:"));

        	Core.nPerm(tN, sN);
        	
        } else if (e.getActionCommand().equals("Exit")) {

            try {
                getMainClass().storeObjects();
                getMainClass().storeEnvironments();
                getMainClass().storeCapabilities();
                getMainClass().storeSensors();

            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).
                log(Level.SEVERE, null, ex);
            }

            frame.dispose();
        }        
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void changeCurrCond(String cond) {

    	//System.err.println("MainView:Setting currCondText to " + cond);
    	
        currCondText.setText(cond);
        frame.repaint();
    }

    /**
     * Sets information about the sets of information; the E, N, and M sets.
     * 
     * @param setInfo
     */
    
    public final synchronized void setCurrSets(String setInfo) {

        currSetsText.setText(setInfo);
        frame.repaint();
    }
    
    public final synchronized String getCurrSets() {
    	return currSetsText.getText();
    }

    public final synchronized void appendCurrSets(String setInfo) {
    	currSetsText.append(" " + setInfo);    	
    }
    
    //
    
    public final synchronized void setIsRunning() {

        isRunning.setForeground(Color.GREEN);
        isRunning.setText("Running");
        frame.repaint();
    }

    public final synchronized void unsetIsRunning() {
        isRunning.setForeground(Color.RED);
        isRunning.setText("Stopped");
        frame.repaint();
    }

    public final synchronized void setReset() {
        isRunning.setForeground(Color.YELLOW);
        isRunning.setText("Restarted");
        frame.repaint();
    }

    public synchronized void setDeviation() {

        deviation.setForeground(Color.RED);
        deviation.setText("DEVIATION!");
        System.out.print('\007');
        System.out.flush();
    }

    public final synchronized void updateArea(String id, double value) {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);     // don't group by threes
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        if (value != 0) {
            areas.get(id).setForeground(Color.BLUE);
        }

        areas.get(id).setText(String.valueOf(nf.format(value)));

        frame.repaint();
    }

    /**
     * NotOperator used anymore.
     * 
     * @param radius
     */
    
    @Deprecated
    public void setRadius(int radius) {

        if (core.getEnvironmentCreatorOn()) {

    		core.getEnvironmentCreator().getDrawPanel().setRadius(radius);
        }
    }

    public void showMovementPattern() {

        if (core.getEnvironmentCreatorOn()) {

            core.getEnvironmentCreator().getDrawPanel().showMovementPattern();
        }
    }

    JTextArea getJTextArea1() {
        return jTextArea1;
    }

    /**
     * Tells the DrawPanel object to draw the current time. If the DrawPanel
     * is null, i.e., due to e.g. running trace files, the method does
     * nothing.
     *
     * @param timer
     */

    public final void showTime(Timer timer) {

        if(core.getEnvironmentCreator() != null) {

            core.getEnvironmentCreator().getDrawPanel().
            	setTime(timer.getCurrentSecond());
        }
    }

    public static String printEndMessage(int endMessage) {

        if (endMessage == DataTupleFilter.M_FINISHED) {
            System.err.println("M : fulfilled");
            return "Fulfilled";

        } else if (endMessage == DataTupleFilter.M_NO_TRIGGER) {
            System.err.println("M : noTrigger");
            return "No Trigger";

        } else if (endMessage == DataTupleFilter.M_STRICTNESS_DEVIATION) {
            System.err.println("M : strictnessDeviation");
            return "Strictness Deviation";

        } else if (endMessage == DataTupleFilter.M_TIMING_DEVIATION) {
            System.err.println("M : timingDeviation");
            return "Timing Deviation";

        } else if (endMessage == DataTupleFilter.M_QUERY_TIMING_DEVIATION) {
            
        	System.err.println("M : holisticTimingDeviation");
            return "Holistic Timing Deviation";
        	
        } else if (endMessage == DataTupleFilter.M_CONCURRENCY_DEVIATION) {
            System.err.println("M : concurrencyDeviation");
            return "Concurrency Deviation";

        } else if (endMessage == DataTupleFilter.M_CONTINUE) {
            System.err.println("M : still under evaluation");
            return "Still Under Evaluation";

        } else if (endMessage == -1) {
            System.err.println("No filters present.");
            return "No Filters Present";

        } else {
            System.err.println("Unknown return statement from M : " + 
            		endMessage);

        }

        return "";
    }

    public final void closeEnvironmentCreatorPanel() {
        core.getEnvironmentCreator().close();
    }

    private final int chooseSimulationMode() {

        /**
         * Let the user choose from different simulation
         * modes.
         *
         * 1. From ailab.eecs.wsu.edu/casas. The user can choose a
         *    trace file.
         *
         */
        Object[] options = {"From ailab.eecs.wsu.edu/casas"};
                //, "Ordinary emulation mode"};

        int option = JOptionPane.showOptionDialog(frame,
                "Choose emulation mode:",
                "Choose emulation mode",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (option == 0) {

            jFileChooser1 = new FileChooser("./../../dataSets/");
            jFileChooser1.setDialogType(javax.swing.JFileChooser.OPEN_DIALOG);
            int returnVal = jFileChooser1.showOpenDialog(MainView.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                core.readDataFile(jFileChooser1.getSelectedFile(), option);
            }
        }

        return option;
    }

    public final Core getCore() {
        return core;
    }

	public boolean getStopPushed() {

		return stopPushed;
	}

	public void setStopPushed(boolean b) {

		stopPushed = b;
	}
}
