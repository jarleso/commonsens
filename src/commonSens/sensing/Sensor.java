/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sensing;

import environment.Environment;
import eventProcessor.DataTuple;
import eventProcessor.Timer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import modelViewController.CommonSens;

/**
 *
 * @author jarleso
 */
public abstract class Sensor {

    private String outPrefix;
    private BufferedWriter out;
    private BufferedWriter outPulled;
    protected String name;
    protected String type;
    protected Environment currEnv;
    protected ConcurrentLinkedQueue<DataTuple> dataTupleQueue;
    protected Timer timer;
    protected boolean stop;
    protected ArrayList<Capability> providedCapabilities;
    
    // For real-life sensors we use sockets to communicate.
    
    Socket socket;
    PrintWriter pullOut = null;
    BufferedReader in = null;

    public Sensor() {
        this.providedCapabilities = new ArrayList<Capability>();
    }

    abstract public String getCapabilitiesText();

    abstract public String getObject();

    abstract public void pullThisSensor(long timeSeconds, boolean simple);

    public ArrayList<Capability> getProvidedCapabilities() {
        return providedCapabilities;
    }

    public void setProvidedCapabilities(ArrayList<Capability> capabilities) {
        this.providedCapabilities = capabilities;
    }

    public void addProvidedCapability(Capability tmpCap) {
        this.providedCapabilities.add(tmpCap);
    }

    /**
     * Returns true if the capability is provided by this tuple source.
     *
     * @param name
     * @return
     */
    public boolean getIsCapabilityProvided(String name) {

        return getProvidedCapability(name) != null;
    }

    /**
     * Returns a capability. If name == null, the method returns the first
     * capability in the list of provided capabilities.
     *
     * @param name
     * @return
     */
    public Capability getProvidedCapability(String name) {

        if (name == null) {
            return this.providedCapabilities.get(0);
        }

        for (Capability caps : getProvidedCapabilities()) {

            if (caps.getName().equals(name)) {
                return caps;
            }
        }

        return null;
    }

    public void setOutPrefix(String outPrefix) {

        this.outPrefix = outPrefix;
    }

    public void setupFile(int runNumber) {

        try {
            out = new BufferedWriter(new FileWriter(outPrefix + "/" + getName() + "_" + runNumber + ".dat"));
        } catch (IOException ex) {
        }

        try {
            outPulled = new BufferedWriter(new FileWriter(outPrefix + "/" + getName() + "_pulled_" + runNumber + ".dat"));
        } catch (IOException ex) {
        }
    }

    public void closeFile() {
        try {
            out.close();
        } catch (IOException ex) {
        }

        try {
            outPulled.close();
        } catch (IOException ex) {
        }
    }

    protected void writeToFile(String text) {
        try {
            out.write(text + "\n");
        } catch (IOException ex) {
        }
    }

    protected void writeToPullFile(String text) {
        try {
            outPulled.write(text + "\n");
        } catch (IOException ex) {
        }
    }

    public String getName() {
        return name;
    }

    public abstract void setName(String name);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCurrEnv(Environment currEnv) {
        this.currEnv = currEnv;
    }

    public abstract void setQueue(ConcurrentLinkedQueue<DataTuple> cepQueue);

    public ConcurrentLinkedQueue<DataTuple> getQueue() {
        return dataTupleQueue;
    }

    public void setGlobalTime(Timer timer) {
        this.timer = timer;
    }

    public void stopSensor() {

        System.err.println(this.getName() + " is stopping.");

        stop = true;
    }

    /**
     * Instantiates and starts the tuple source. 
     * 
     * @param currEnv
     * @param timer
     * @param dataTupleQueue
     * @param expPrefix
     * @param runNumber
     */
    
    public void startSensor(Environment currEnv, Timer timer, ConcurrentLinkedQueue<DataTuple> cepQueue, String expPrefix, int runNumber) {

        setCurrEnv(currEnv);
        setGlobalTime(timer);
        setQueue(cepQueue);
        setOutPrefix(expPrefix);
        setupFile(runNumber);
    }

    /**
     * This method is custom built for the EiMM10 paper. 
     */
    
    public boolean openSocket() {
    		
    	System.err.println("openSocket() for sensor " + name);
    	
		/**
		 * Initiate the socket the first time. Note that this
		 * configuration is very static at the moment.
		 */
		
		int portNumber = Integer.parseInt(name.substring(3, 4)) + 
							8010;/*20000*/;
		
		System.err.println("pullThisSensor: portNumber = " + 
				portNumber);
		
		try {
			socket = new Socket(CommonSens.SENSOR_HOST, portNumber);
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
		
		try {
			pullOut = new PrintWriter(socket.getOutputStream(), 
					true);
		} catch (IOException e) {
			
			System.err.println("pullThisSensor: pullOut did " +
					"not inititate.");
			
			e.printStackTrace();
			return false;
		}
		
        try {
			in = new BufferedReader(new InputStreamReader(
			                            socket.getInputStream()));
		} catch (IOException e) {
			
			System.err.println("pullThisSensor: in did not " +
					"inititate.");
			
			e.printStackTrace();
			return false;
		}
		
		/**
		 * The first readings from the camera are sometimes wrong,
		 * so we pull the camera once just to remove this
		 * error.
		 */
		
		System.err.println("Sending correcting pull message.");
    	
    	pullOut.print("pull");
    	pullOut.flush();
    	
    	System.err.println("Waiting for result.");
    	
    	try {
			in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
    }

    /**
     * Returns the samplingFrequency. The default samplingFrequency is 1 unless overloaded.
     * 
     * @return
     */
    
	public double getHz() {
		return 1;
	}
}
