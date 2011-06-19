package eventProcessor;

import modelViewController.CommonSens;
import modelViewController.Core;
import modelViewController.EnvironmentCreator;
import modelViewController.MainView;

public class MonitoringRunner implements Runnable {

	private MainDataTupleFilter mainFilter;
    private EnvironmentCreator environmentCreator;
	
	CommonSens mainClass;
	MainView panel;
	Core core;
	boolean emulation;
	int numRuns; 
	int numSensors;
	int numAnds;
	int option; 
	int runNumber;
	boolean reEval;
	
	public MonitoringRunner(CommonSens mainClass, MainView panel, 
			EnvironmentCreator environmentCreator, Core core, boolean emulation, 
			int numRuns, int numSensors, int numAnds, int option, 
			int runNumber, boolean reEval) {
		this.mainClass = mainClass;
		this.panel = panel;
		this.environmentCreator = environmentCreator;
		this.core = core;
		this.emulation = emulation;
		this.numRuns = numRuns; 
		this.numSensors = numSensors;
		this.numAnds = numAnds;
		this.option = option; 
		this.runNumber = runNumber;
		this.reEval = reEval;
	}

	@Override
	public void run() {
		
		/**
    	 * Set up main filter.
    	 */
    	
    	mainFilter = core.setupEventProcessingPhase(emulation, runNumber, numSensors,
                numAnds, option, reEval);

        if (mainFilter != null) {
        

	        /**
	         * At this point all the sensors are set up. The emulation is
	         * done very simple by pulling all the tuple sources once
	         * every second. 
	         * 
	         * Correction:
	         * For the EiMM-paper, we have set the sampling rate to 
	         * 333 milliseconds, since this matches the time it takes
	         * to sample the 3 sensors better.  
	         * 
	         */
	        
//	        long checkTime = mainClass.getTimer().
//	        					getCurrentMillisecondRounded();	        

	        long checkTime = mainClass.getTimer().
	        					getCurrentMillisecond(Core.Hz);	        

	        
	        while (true) {
	        	
	        	/**
	        	 * Spin/sleep-loop until next second or until the user has 
	        	 * pushed the stop button.
	        	 */
	        	
        		// Check if the stop button has been pushed
            
	        	if(panel.getStopPushed()) {
	        		
	        		panel.setStopPushed(false);
	        		
	        		break;
	        		
	        	}
	        	
	        	while (mainClass.getTimer().
	        			getCurrentMillisecond(Core.Hz) == checkTime) {
	        		
	        		try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}                		
	        	}
	        	
	        	checkTime = mainClass.getTimer().
	        					getCurrentMillisecond(Core.Hz);
	        	
	        	if (mainFilter != null) {
	
	                /** 
	                 * Pull and evaluate the tuple sources for all the 
	                 * running statements.
	                 */
	
	                int retVal = mainFilter.pullAndEvaluate(mainClass.
	                		getTimer().getCurrentMillisecond(Core.Hz), 0,
	                        numSensors);
	
	            	System.err.println("Has pulled and evaluated at " +
	            			"time " + checkTime + " with retVal = " +
	            			retVal);
	                
	            	/**
	            	 * For EiMM10:
	            	 * 
	            	 * For some strange reason, something happens with the
	            	 * socket connection after 1 minute. Therefore, we stop
	            	 * the experiment at one minute. 
	            	 * 
	            	 * For real-time evaluation of the system we have not 
	            	 * experienced any issues related to this, so the code
	            	 * is uncommented. 
	            	 */
	            	
	            	/*
	            	if(mainClass.getTimer().getCurrentMillisecond(Core.Hz) == 
	            		60000) {
	            		break;
	            	}
	            	*/
	            	
	                if (retVal == DataTupleFilter.M_FINISHED) {
	                	
	                		
	                	if(!reEval) {
	                	
		                	/** 
		                	 * Stop the evaluation when the filer tells that the
		                	 * system has finished.
		                	 */
		                	
		                    break;
	                	}
	                }
	            }
	        	
	        	panel.showTime(mainClass.getTimer());
	
	            if(environmentCreator != null) {
	                environmentCreator.repaintDrawPanel();
	            }                	
	        }
        }
        
        mainClass.stopSensors();

        mainFilter.closeFile();

        MainView.printEndMessage(mainFilter.getEndMessage());
		
	}

	/**
	 * Returns the mainFilter.
	 * 
	 * @return
	 */
	
	public MainDataTupleFilter getMainDataTupleFilter() {
		return mainFilter;
	}

}
