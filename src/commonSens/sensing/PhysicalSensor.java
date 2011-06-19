package sensing;

import com.seisw.util.geom.Poly;
import environment.Environment;
import environment.CommonSensObject;
import environment.Shape;
import environment.Person;
import eventProcessor.DataTuple;

import java.awt.Point;
import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jarleso
 */
public class PhysicalSensor extends Sensor implements Runnable {

    SignalType signalType;
    
    // One of these are used for cov.
    
    Shape coverageArea;
    CommonSensObject objectCovered;
    
    //
    
    double samplingFrequency;
    
    private String objectCoveredName = null;
    private boolean hasBeenMoved = true; /** 
    									  * Initialized to true to force 
    									  * calculation first time.
    									  */
    private boolean doEvaluate = false;
    
    public PhysicalSensor() {
        
        super.providedCapabilities = new ArrayList<Capability>();
    }

    public PhysicalSensor(PhysicalSensor tmpSensor) {

        super.providedCapabilities = new ArrayList<Capability>();
        coverageArea = new Shape(tmpSensor.getShape());
        this.samplingFrequency = tmpSensor.getHz();
        this.type = tmpSensor.getType();
        this.name = tmpSensor.getName();
        this.signalType = new SignalType(tmpSensor.getSignalType());
        
        for(Capability tmpCap : tmpSensor.getProvidedCapabilities()) {
         
            super.getProvidedCapabilities().add(new Capability(tmpCap));
        }

        //System.err.println("NEW PHYSICAL SENSOR = " + this.getObject());
    }

    public void addToObject(CommonSensObject objectCovered) {
        this.objectCovered = objectCovered;

        coverageArea.setStartPoint(objectCovered.getShape().getStartPoint());
    }

    public double getHz() {
        return samplingFrequency;
    }

    public void setHz(double Hz) {
        this.samplingFrequency = Hz;
    }

    public String getCapabilitiesText() {

        String ret = "";

        for(Capability cap : super.getProvidedCapabilities()) {

            ret += cap.getName() + " ";
        }

        return ret;
    }

    public Shape getCoverageArea() {
        return coverageArea;
    }

    public void setCoverageArea(Shape coverageArea) {
        this.coverageArea = coverageArea;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {

        this.signalType = signalType;
    }

    public String getSignalTypeText() {

        if(getSignalType() != null) {

            return getSignalType().getText();
        }

        return "";
    }

    public String getObject() {

        String tmpString = "";

        tmpString += "signalType " + signalType.getObject();
        tmpString += "coverageArea " + coverageArea.getObject();
        tmpString += "samplingFrequency " + samplingFrequency + " ";

        tmpString += "capabilities " + super.getProvidedCapabilities().size() + 
        "\n";

        for(Capability tmpCap : super.getProvidedCapabilities()) {

            tmpString += tmpCap.getObject();
        }

        tmpString += "type " + type + " ";
        tmpString += "name " + name + " ";
        tmpString += "object " + ((getObjectConnectedTo() == null)? "null " : 
        	(getObjectConnectedTo().getName()) + " ");

        return tmpString;
    }

    public Shape getShape() {
        return getCoverageArea();
    }

    /**
     * Currently, the physical sensor investigates the coverage area. If there
     * are any other physical sensors within its coverage area, the physical
     * sensor sends information about that current sensor.
     */

    public void run() {

        // Check all the sensors and persons inside the coverage
        // area.

        if(currEnv != null) {
     
            while(!stop) {

                pullThisSensor(-1, false);

                try {
                    Thread.sleep((long) (1000 / getHz()));
                } catch (InterruptedException ex) {
                    Logger.getLogger(PhysicalSensor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Returns a tuple to the shared queue. Also sends the providedCapabilities so that
     * the sigma filter can investigate if this sensor has the providedCapabilities
     * we are interested in.
     *
     * @param value
     * @return
     */

    public DataTuple pull(Capability cap, Object value, long currentSecond) {

        DataTuple retTuple;

        if(currentSecond == -1) {

            retTuple = new DataTuple(getName(), cap,
                    value, timer.getCurrentMillisecondRounded(),
                    timer.getCurrentMillisecondRounded());
        } else {
            retTuple = new DataTuple(getName(), cap,
                    value, currentSecond,
                    currentSecond);
        }

        this.writeToPullFile(retTuple.showTuple());

        return retTuple;
    }

    public CommonSensObject getObjectConnectedTo() {
        return this.objectCovered;
    }

    public void setObjectConnectedToString(String objectName) {

        this.objectCoveredName = objectName;
    }

    public String getObjectCoveredName() {
        return this.objectCoveredName;
    }

    /**
     * Since this is an emulated environment, the system has to investigate
     * all the physical sensors and persons that are located in the environment.
     * If this other physical sensor or a person is within the coverage area
     * of the physical sensor, and the capabilities match, a data tuple
     * with the information is created.
     *
     * @param currentMillisecond
     * @param simple States if only the first of many possible 'equal' physical
     * sensors should be pulled. This is part of the experiments where we
     * increase the number of similar sensors that are placed at the same
     * location. In an emulated environment we do not measure the time it takes
     * to pull the sensors. Therefore, we only pull one of the sensors and
     * generate one data tuple. MainDataTupleFilter.pullAndEvaluate() then
     * copies the current data tuple into the number of data tuples that
     * matches the number of emulated tuple sources.
     */

    public void pullThisSensor(long currentMillisecond, boolean simple) {

        DataTuple sendTuple = null;
        
        String result = "";

        for(Capability cap : super.getProvidedCapabilities()) {

            if(cap.getName().equals("LocateRFIDActiveTag")) {

                for(Sensor tmpSens : currEnv.getTupleSources()) {

                    if(tmpSens.getName().matches(".+_0$")) {

                        if(tmpSens instanceof PhysicalSensor) {

                            PhysicalSensor phySens = (PhysicalSensor) tmpSens;

                            Point tmpPoint =
                                    new Point(phySens.getShape().getStartPoint().getX(),
                                        phySens.getShape().getStartPoint().getY());

                            if(getPolygonReduced(currEnv, signalType)
                                    .contains(tmpPoint)
                                    &&
                                    tmpSens.getType().equals("RFIDActiveTag")) {

                                //System.err.println("Has a match!");

                                String personName = phySens.getObjectConnectedTo().getName();

                                sendTuple = new DataTuple(getName(), cap,
                                    personName, timer.getCurrentMillisecondRounded(),
                                    timer.getCurrentMillisecondRounded());//phySens.pull(cap, (Object) tmpSens.getName(), currentSecond);

                            } else {

                                //System.err.println("Has not a match!");

                                //sendTuple = new DataTuple(getName(), cap,
                                //    0, timer.getCurrentSecond(),
                                //    timer.getCurrentSecond());
                            }

                            //System.err.println("Sent tuple " + sendTuple.showTuple());

                            sendTuple(currentMillisecond, sendTuple);
                        }
                    }
                }
            } else if(cap.getName().equals("DetectPerson")) {


                if(getType().equals("RFIDReader")) {

                    /**
                     * Iterate through all the sensors in the current
                     * environment. If any of the tuple sources in the coverage
                     * area is an RFIDActiveTag, a data tuple is generated
                     * by the information from the tag.
                     */

                    for(Sensor tmpSens : currEnv.getTupleSources()) {

                        if(simple) {

                            // I don't know why this is done here. Sorry...

                            doEvaluate = tmpSens.getName().matches(".+_10$");
    //                        doEvaluate = tmpSens.getName().matches(".+_0$");

    //                        System.err.println("doEvaluate = " + doEvaluate
    //                                + " from sensor " + tmpSens.getName()
    //                                + ", " + tmpSens.getName().matches(".+_10$"));

                        } else {
                            doEvaluate = true;
                        }

                        if(doEvaluate) {

                            if(tmpSens instanceof PhysicalSensor) {

                                PhysicalSensor phySens = (PhysicalSensor) tmpSens;

                                Point tmpPoint =
                                        new Point(phySens.getShape().getStartPoint().getX(),
                                            phySens.getShape().getStartPoint().getY());

                                if(getPolygonReduced(currEnv, signalType)
                                        .contains(tmpPoint)
                                        &&
                                        tmpSens.getType().equals("RFIDActiveTag")) {

                                    //System.err.println("Has a match!");

                                    String personName = phySens.getObjectConnectedTo().getName();

                                    sendTuple = new DataTuple(getName(), cap,
                                        personName, timer.getCurrentMillisecondRounded(),
                                        timer.getCurrentMillisecondRounded());//phySens.pull(cap, (Object) tmpSens.getName(), currentSecond);

                                } else {

                                    //System.err.println("Has not a match!");

                                    //sendTuple = new DataTuple(getName(), cap,
                                    //    0, timer.getCurrentSecond(),
                                    //    timer.getCurrentSecond());
                                }

                                //System.err.println("Sent tuple " + sendTuple.showTuple());

                                sendTuple(currentMillisecond, sendTuple);
                            }
                        }
                    }
                } else if (getType().equals("camera")) {

                    /**
                     * Iterate through all the persons in the environment. If
                     * the person is inside the coverage area of the camera,
                     * generate a data tuple with the information about the
                     * person. Note that in this abstraction, we assume that
                     * the person is recognized as long as the person is
                     * inside the coverage area. 
                     */

                    Person tmpPerson = currEnv.getCurrPerson();

                    Point tmpPoint =
                        new Point(tmpPerson.getShape().getStartPoint().getX(),
                            tmpPerson.getShape().getStartPoint().getY());

                    if(getPolygonReduced(currEnv, signalType)
                            .contains(tmpPoint)) {

                        sendTuple = new DataTuple(getName(), cap,
                                        tmpPerson.getName(),
                                        currentMillisecond,
                                        currentMillisecond)
                                        /**                                                                                
                                        timer.getCurrentMillisecondRounded(),
                                        timer.getCurrentMillisecondRounded())
                                        */;
                    }

                    sendTuple(currentMillisecond, sendTuple);

                } else {
                    //System.err.println("cap.getName() = " + cap.getName());
                }
            } else if(cap.getName().equals("DetectMotion")) {
            	
            	/**
            	 * This uses real cameras that use OpenCV to detect motion.
            	 * Is used in the EiMM10 paper and for the real world
            	 * experiments. 
            	 */
            	
            	// Pull the socket
            	
            	System.err.println(getName() + " sends pull message.");
            	
            	pullOut.print("pull");
            	pullOut.flush();
            	
            	System.err.println("Waiting for result.");
            	
            	try {
					result = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(result.equals("1")) {
					result = "true";
				} else {
					result = "false";
				}
				
				sendTuple = new DataTuple(getName(), cap,
                        result, currentMillisecond,
                        currentMillisecond);
				
				sendTuple(currentMillisecond, sendTuple);
            }
        }
    }

    public void setHasBeenMoved(boolean b) {
        hasBeenMoved = true;
    }

    public Poly getPolyReduced(Environment envir, SignalType signalType) {

        if(hasBeenMoved) {
 
            setHasBeenMoved(false);
        
            return getShape().getPolyReduced(envir, signalType);
        }

        return getShape().getRealCovereagePoly();
    }
        
    public Polygon getPolygonReduced(Environment envir, SignalType signalType) {

        if(hasBeenMoved) {
 
            setHasBeenMoved(false);
        
            return getShape().getPolygonReduced(envir, signalType);
        }

        return getShape().getRealCovereagePolygon();
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<DataTuple> cepQueue) {
        super.dataTupleQueue = cepQueue;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private void sendTuple(long currentSecond, DataTuple sendTuple) {

        if(sendTuple != null) {

            this.writeToFile(currentSecond + "\t" + sendTuple.showTuple());

//            System.err.println("PhysicalSensor.sendTuple = " + sendTuple.showTuple());
            
            dataTupleQueue.add(sendTuple);

        } else {
            this.writeToFile(String.valueOf(currentSecond));
        }
    }
}
