/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import com.seisw.util.geom.Poly;


import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import language.AtomicQuery;
import language.QueryElement;
import modelViewController.Core;
import sensing.Capability;
import sensing.EmulatedTupleSource;
import sensing.ExternalSource;
import sensing.LogicalSensor;
import sensing.PhysicalSensor;
import sensing.Sensor;

/**
 *
 * @author jarleso
 */
public class Environment {

    ArrayList<CommonSensObject> objects;
    String name = "created_environment";
    ArrayList<Sensor> sensors;
    private ArrayList<LocationOfInterest> lois;
    private ArrayList<Person> persons;
    private ArrayList<Triple> currPos;

    private Person currPerson;
    private final Core core;
    private boolean isTraceFiles = false;

//    private Capability someCap;

    public Environment(Core panel) {

        lois = new ArrayList<LocationOfInterest>();
        objects = new ArrayList<CommonSensObject>();
        sensors = new ArrayList<Sensor>();
        persons = new ArrayList<Person>();

        this.core = panel;
    }

    public void addCEPObject(CommonSensObject object) {

        //object.setName(object.getName() + "_" + objects.size());

        objects.add(object);
    }

    public ArrayList<CommonSensObject> getObjects() {
        return objects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnvironment() {

        String retString = "";

        retString += name + " " + objects.size() + "\n\n";

        for(CommonSensObject tmpObj : objects) {

            retString += tmpObj.getObject() + "\n\n";
        }

        retString += "sensors" + " " + sensors.size() + "\n\n";

        for(Sensor tmpSens : sensors) {

            if(tmpSens instanceof PhysicalSensor)
                retString += "PhysicalSensor ";

            else if(tmpSens instanceof LogicalSensor)
                retString += "LogicalSensor ";

            else
                retString += "ExternalSource ";

            retString += tmpSens.getObject() + "\n\n";
        }

        retString += "lois " + lois.size() + "\n\n";

        for(LocationOfInterest loi : lois) {
            retString += loi.getObject() + "\n\n";
        }

        retString += "persons " + persons.size() + "\n\n";

        for(Person person : persons) {
            retString += person.getObject() + "\n\n";
        }

        if(currPos != null) {

            retString += getCurrPosString();
        }

        return retString;
    }

    public String getCurrPosString() {

        String retString = "currPos " + currPos.size() + " ";

            for(Triple tmpTrip : currPos) {

                retString += tmpTrip.getObject();
            }

            return retString;
    }

    public void addTupleSource(Sensor tmpTS) {

        if(tmpTS instanceof PhysicalSensor) {

//           System.err.println("Adding physical sensor " + tmpTS.getName());

//           setShowCapability(((PhysicalSensor) tmpTS).getProvidedCapabilities().get(0));
        } else if(tmpTS instanceof LogicalSensor) {

//            System.err.println("Adding logical sensor.");

        } else if(tmpTS instanceof ExternalSource) {
            
        } else if(tmpTS instanceof EmulatedTupleSource) {

            /**
             * If this is an emulated tuple source, we set this as a variable
             * in this class. The reason for this that the reading of the
             * trace files is different than from reading an environment.
             */

            setIsTraceFiles(true);
        }

        sensors.add(tmpTS);
    }

    public void addPerson(Person tmpPers) {

        persons.add(tmpPers);
    }

    public ArrayList<Sensor> getTupleSources() {
        return sensors;
    }

    ArrayList<CommonSensObject> getObjects(Triple tmpTrip) {

        ArrayList<CommonSensObject> tmpObjects = new ArrayList<CommonSensObject>();

        Point tmpPoint = tmpTrip.toPoint();

        for(CommonSensObject tmpObj : objects) {

            if(tmpObj.getShape().getPolygon().contains(tmpPoint)) {
                tmpObjects.add(tmpObj);
            }
        }
        
        return tmpObjects;
    }

    public void addCEPLoI(LocationOfInterest loi) {

//    	System.err.println("Adding loi " + loi.getName() + " in environment " +
//    			this.getEnvironment());
    	
        lois.add(loi);
    }

    public ArrayList<LocationOfInterest> getLoIs() {
        return lois;
    }

    public LocationOfInterest getLoI(String actionCommand) {

    	//System.err.println("lois.size() = " + lois.size());
    	
        for(LocationOfInterest loi : lois) {

//        	System.err.println("loi = " + loi.getName());
        	
            if(loi.getName().equals(actionCommand)) {
                return loi;
            }
        }

        return null;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void setCurrPos(ArrayList<Triple> arrayList) {
        currPos = arrayList;
    }

    public ArrayList<Triple> getCurrPos() {

        return currPos;
    }

    public CommonSensObject getObject(String objectName) {

        for(CommonSensObject tmpObj : this.getObjects()) {

            if(tmpObj.getName().equals(objectName))
                return tmpObj;
        }

        return null;
    }

    public CommonSensObject getPerson(String objectCoveredName) {

        for(Person tmpPers : this.getPersons()) {

            //System.err.println("tmpPers.getName() = " + tmpPers.getName());

            if(tmpPers.getName().equals(objectCoveredName))
                return tmpPers;
        }

        return null;
    }

    public Sensor getTupleSource(String tsName) {

        for(Sensor ts : getTupleSources()) {

            if(ts.getName().equals(tsName)) {
                return ts;
            }
        }

        return null;
    }

    public Person getCurrPerson() {
        return persons.get(0);
    }

    /**
     * Returns FPProb.
     */
    
    public Approximation calculateError(LocationOfInterest tmpLoI) {

        ArrayList<Sensor> isec =
            new ArrayList<Sensor>();

        ArrayList<Sensor> noIsec =
            new ArrayList<Sensor>();

        Poly intersection = null;

        ArrayList<Sensor> providesCapability = sensors;
    	
        for(Sensor tmpSource : providesCapability) {
        	
            if(tmpSource instanceof PhysicalSensor) {

                PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;

                intersection = tmpLoI.getShape().getPoly()
                    .intersection(tmpSens.getPolyReduced(this,
                        tmpSens.getSignalType()));

                if(intersection.getArea() == tmpLoI.getShape()
                        .getPoly().getArea()) {

                    // The LoI is covered by this tmpSens.

               		isec.add(tmpSens);
                }
            }
        }

//        System.err.println("isec.size() = " + isec.size());
        
        // Create the actual intersection.

        if(!isec.isEmpty()) {
            intersection = ((PhysicalSensor) isec.get(0)).getPolyReduced(this,
                    ((PhysicalSensor) isec.get(0)).getSignalType());

            for(int i = 1; i < isec.size(); i++) {

                intersection = ((PhysicalSensor) isec.get(i))
                        .getPolyReduced(this, ((PhysicalSensor) isec.get(i))
                        .getSignalType()).intersection(intersection);
            }

            // Add the sensors that have an intersection with the "intersection"
            // but not the LoI (noIsec).

            for(Sensor tmpSource : providesCapability) {

                if(tmpSource instanceof PhysicalSensor) {

                    PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;
                    
                        Poly tmpIntersection = tmpSens
                                .getPolyReduced(this, tmpSens.getSignalType())
                                .intersection(intersection);

                        Poly testIntersection = tmpSens
                                .getPolyReduced(this, tmpSens.getSignalType())
                                .intersection(tmpLoI.getShape().getPoly());

                        if(tmpIntersection.getArea() != 0
                                &&
                                testIntersection.getArea() == 0) {

                            // The LoI is not covered, but the intersection is.

                            //System.err.println("Adding " + tmpSens.getName());

                            noIsec.add(tmpSens);
                        }
                    }
                }

                // Run an XOR on the intersection and the elements in
                // noIsec.

                for(Sensor tmpTS : noIsec) {

                    PhysicalSensor tmpSens = (PhysicalSensor) tmpTS;
                    
                    Poly tmpIntersection = intersection.intersection(tmpSens
                            .getPolyReduced(this, tmpSens.getSignalType()));

                    intersection = intersection.xor(tmpIntersection);
                }

            	return new Approximation((1 - (tmpLoI.getShape()
                            .getPoly().getArea()/intersection.getArea())),
                            isec,
                            noIsec);

        } else /* isec was empty. */ {

            return new Approximation(1D, 
            		new ArrayList<Sensor>(), 
            		new ArrayList<Sensor>());
        }
    }
    
	/**
	 * Calculates the area of all the intersections of the sensors that cover 
	 * a LoI in the AtomicQuery tmpAQuery. If the LoI == null, all the sensors 
	 * in the environment that provide the capability have to be included.
	 */    

	public void calculateError(AtomicQuery tmpAQuery) {

		LocationOfInterest tmpLoI = tmpAQuery.getLoi();
		Capability capability = tmpAQuery.getCapability();
		ArrayList<Sensor> isec = new ArrayList<Sensor>();
		ArrayList<Sensor> noIsec = new ArrayList<Sensor>();
		Poly intersection = null;

		// Obtain all the tuple sources that provide the capability.

		ArrayList<Sensor> providesCapability = findSensor(capability);

		if (providesCapability.isEmpty()) {
			return;
		}

		// Add the sensors that have an intersection with LoI.

		if (tmpLoI != null) {
			for (Sensor tmpSource : providesCapability) {
				if (tmpSource instanceof PhysicalSensor) {
					PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;
					intersection = tmpLoI.getShape().getPoly().intersection(
							tmpSens.getPolyReduced(this, tmpSens
									.getSignalType()));
					if (intersection.getArea() == tmpLoI.getShape().getPoly()
							.getArea()) {
						isec.add(tmpSens);
					}
				}
			}

			// Create the actual intersection.

			if (!isec.isEmpty()) {
				intersection = ((PhysicalSensor) isec.get(0)).getPolyReduced(
						this, ((PhysicalSensor) isec.get(0)).getSignalType());
				for (int i = 1; i < isec.size(); i++) {
					intersection = ((PhysicalSensor) isec.get(i))
							.getPolyReduced(
									this,
									((PhysicalSensor) isec.get(i))
											.getSignalType()).intersection(
									intersection);
				}

				// Add the sensors that have an intersection with the
				// "intersection" but not the LoI (noIsec).

				for (Sensor tmpSource : providesCapability) {
					if (tmpSource instanceof PhysicalSensor) {
						PhysicalSensor tmpSens = (PhysicalSensor) tmpSource;
						if (tmpSens.getIsCapabilityProvided(capability
								.getName())) {
							Poly tmpIntersection = tmpSens.getPolyReduced(this,
									tmpSens.getSignalType()).intersection(
									intersection);
							Poly testIntersection = tmpSens.getPolyReduced(
									this, tmpSens.getSignalType())
									.intersection(tmpLoI.getShape().getPoly());
							if (tmpIntersection.getArea() != 0
									&& testIntersection.getArea() == 0) {

								// The LoI is not covered, but the
								// intersection is.

								noIsec.add(tmpSens);
							}
						}
					}
				}

				// Run an XOR on the intersection and the elements in
				// noIsec.

				for (Sensor tmpTS : noIsec) {
					PhysicalSensor tmpSens = (PhysicalSensor) tmpTS;
					Poly tmpIntersection = intersection.intersection(tmpSens
							.getPolyReduced(this, tmpSens.getSignalType()));
					intersection = intersection.xor(tmpIntersection);
				}
				tmpAQuery.setLoIApprox(intersection);
				tmpAQuery.setFPProb(tmpLoI.getShape().getPoly().getArea()
						/ intersection.getArea());
				tmpAQuery.setIsec(isec);
				tmpAQuery.setNoIsec(noIsec);
			} else /* isec was empty. */{
				tmpAQuery.setLoIApprox(null);
				tmpAQuery.setFPProb(0);
			}
		} else /* tmpLoI == null */{
			tmpAQuery.setLoIApprox(null);
			tmpAQuery.setFPProb(0);
			tmpAQuery.setIsec(providesCapability);
		}
	}


    /**
     * Currently, this only investigates the current setup and returns a
     * set of sensors that provide a given capability.
     *
     * The method first investigates the current environment to see if
     * there exists tuple sources in the environment that provide the
     * capability.
     *
     * If there are no tuple sources in the environment that provide the
     * capability, the method investigates the sensor repository to find
     * a matching sensor. The tuple source is then added to the
     * environment.
     *
     * @param capability
     * @return
     */

    private ArrayList<Sensor> findSensor(Capability capability) {

        ArrayList<Sensor> retList = new ArrayList<Sensor>();
        ArrayList<Sensor> pullList = new ArrayList<Sensor>();

        if(capability == null) {
            System.err.println("findTS: capability == null");
        }

        for(Sensor tmpTS : getTupleSources()) {

//            System.err.println("Checking for capability " + capability.getName()
//                    + " in " + tmpTS.getName());

            if(tmpTS.getIsCapabilityProvided(capability.getName())) {

//                System.err.println("Capability is provided!");

                if(tmpTS instanceof LogicalSensor) {

                    // NotOperator yet implemented.
                }

                retList.add(tmpTS);
            }
        }

        if(retList.isEmpty()) {

//            System.err.println("Checking the repository.");

            /**
             * Go through the tuple sources in the repository. This is e.g.
             * typical for logical sensors and external sources, since they
             * seldomly are located in a newly instantiated environment.
             */

            ArrayList<Sensor> repository = core.getMainClass()
                    .getTupleSources();

            for(Sensor tmpTS : repository) {

                if(tmpTS == null) {
                    System.err.println("Environment. findTS: tmpTS == null.");
                }

                if(tmpTS.getIsCapabilityProvided(capability.getName())) {

//                    System.err.println("Capability is provided!");

                    if(tmpTS instanceof LogicalSensor) {

                        LogicalSensor lSens = (LogicalSensor) tmpTS;

                        /**
                         * Investigate the capabilities this logical sensor
                         * depends on and find the corresponding tuple
                         * sources.
                         */
                    
                        for(Capability tmpCapability :
                            lSens.getCapabilitiesNeeded()) {

                            pullList.addAll(findSensor(tmpCapability));
                        }

                        /**
                         * retList now contains all the physical sensors or
                         * external sources that provide the needed capability.
                         *
                         * Add these tuple sources to the logical sensor. 
                         */

                         lSens.addTupleSourcesToPull(pullList);
                    }

                    retList.add(tmpTS);

                    addTupleSource(tmpTS);
                }
            }
        }

        return retList;
    }

    /**
     * Add the sensors that have the same capabilities than prevST and
     * currST in currST.NoIsec. Fill the devRep list in all the elements
     * in both lists with the tuple sources that provide the same capabilities
     * and that are not in Isec and NoIsec.
     *
     * In cases where several AndOperator chains are connected by ORs, it is important
     * that none of the DevRep sets contain tuple sources that belong to any
     * of the other statements' Isec sets, since this would lead to an
     * inconsistent situation.
     *
     * @param prevST
     * @param currST
     */

    public void addStrict(ArrayList<QueryElement> previousList,
            ArrayList<QueryElement> currentList) {

        HashMap<String, String> notToDevRep = new HashMap<String, String>();

// All tuple sources in previousList are now added to the DevRep set. Which of
// them to pull needs to be decided during eventProcessor.

        for(QueryElement tmpSTA : previousList) {

            if(tmpSTA instanceof AtomicQuery)
                notToDevRep.putAll(((AtomicQuery) tmpSTA).getIsecs());
        }

        for(QueryElement tmpSTA : currentList) {

            if(tmpSTA instanceof AtomicQuery)
                notToDevRep.putAll(((AtomicQuery) tmpSTA).getIsecs());
        }

        for(QueryElement tmpSTB : currentList) {

            ArrayList<Sensor> thisTupleSources = findSensor(((AtomicQuery) tmpSTB).getCapability());

            if(tmpSTB instanceof AtomicQuery) {

                for(Sensor tmpTupleSource : thisTupleSources) {

                    /**
                     * If the tmpTupleSource is not in the isec set, add it to
                     * devRep.
                     */

                    if(!notToDevRep.containsKey(tmpTupleSource.getName()))
                        ((AtomicQuery) tmpSTB).addDevRep(tmpTupleSource);
                }
            }
        }
    }

    public boolean checkTripleWithObjects(Triple tmpTriple) {

        if(currPerson == null) {

            currPerson = getPersons().get(0);
        }

        Triple currStartPoint = new Triple(currPerson.getShape()
                .getStartPoint());

        // Temporarly set the person to the tmpTriple position

        currPerson.getShape().setStartPoint(tmpTriple);

        int numCount = 0;

        for(CommonSensObject tmpObject : getObjects()) {

            Poly tmpPoly = tmpObject.getShape().getPoly()
                    .intersection(currPerson.getShape().getPoly());

            if(!tmpPoly.isEmpty()) {
                numCount += 1;
            }
        }

        currPerson.getShape().setStartPoint(currStartPoint);

        //System.err.println("numCount = " + numCount);

        if(numCount == 2) return false;

        return true;
    }

    public void setIsTraceFiles(boolean b) {
        isTraceFiles = b;
    }

    public boolean getIsTraceFiles() {
        return isTraceFiles;
    }

//    public void calculateErrors() {
//
//        for(LocationOfInterest tmpLoI : getLoIs()) {
//
//            calculateError(tmpLoI,
//                    getShowCapability());
//        }
//    }
}

