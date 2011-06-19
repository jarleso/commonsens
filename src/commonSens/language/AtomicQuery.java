/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

import com.seisw.util.geom.Poly;
import environment.LocationOfInterest;
import eventProcessor.DataTuple;
import eventProcessor.EvaluationTreeNode;
import eventProcessor.EventSet;
import eventProcessor.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import sensing.Capability;
import sensing.Sensor;

/**
 *
 * @author jarleso
 */
public class AtomicQuery extends QueryElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LocationOfInterest loi;
    Capability capability;
	ArrayList<?> values;
    String operator;
    Object value;
    Timestamp begin;
    Timestamp end;
    boolean isMin;
    double percentage;
        
    /**
     * The trees that form the N set.
     */
    
	HashMap<String, EvaluationTreeNode> trees;

    double falsePositives;

    HashMap<String, Sensor> isec;
    HashMap<String, Sensor> noIsec;
    HashMap<String, Sensor> devRep;
    @SuppressWarnings("unused")
	private Poly approximation;
    private double fPProb;

    /**
     * All the current possible events for the complex queries.
     */
    
    private EventSet<DataTuple> N;
    
    /**
     * All the current possible correct events from the complex queries.
     */
    
    private EventSet<DataTuple> M;

    
    public AtomicQuery(LocationOfInterest loi, Capability capability,
            String operator, Object value, Timestamp begin, Timestamp end,
            boolean isMin, double percentage,  boolean isNegation) {

        this.loi = loi;
        this.capability = capability;
        this.operator = operator;
        this.value = value;
        this.begin = begin;
        this.end = end;
        this.isMin = isMin;
        this.percentage = percentage;

        values = capability.getDescription().getValues();
        
        setIsNegation(isNegation);

    	trees = new HashMap<String, EvaluationTreeNode>();
        isec = new HashMap<String, Sensor>();
        noIsec = new HashMap<String, Sensor>();
        devRep = new HashMap<String, Sensor>();
    
        N = new EventSet<DataTuple>();
        M = new EventSet<DataTuple>();
    }

    public AtomicQuery(AtomicQuery atomicQuery) {

        this.loi = new LocationOfInterest(atomicQuery.getLoi());
        this.capability = new Capability(atomicQuery.getCapability());
        this.operator = new String(atomicQuery.getOperator());

        values = capability.getDescription().getValues();
        
        if(atomicQuery.getValue() instanceof Integer) {
            this.value = new Integer((Integer) atomicQuery.getValue());
        } else if(atomicQuery.getValue() instanceof Double) {
            this.value = new Double((Double) atomicQuery.getValue());
        } else if(atomicQuery.getValue() instanceof String) {
            this.value = new String((String) atomicQuery.getValue());
        }

        this.begin = new Timestamp(atomicQuery.getBegin());
        this.end = new Timestamp(atomicQuery.getEnd());
        this.isMin = new Boolean(atomicQuery.getIsMin());
        this.percentage = new Double(atomicQuery.getPercentage());
        this.isNegation = new Boolean(atomicQuery.getIsNegation());

    	trees = new HashMap<String, EvaluationTreeNode>();
        
        isec = new HashMap<String, Sensor>();

        Iterator<String> it = atomicQuery.isec.keySet().iterator();

        while(it.hasNext()) {

            String key = it.next();

            Sensor tmpS = atomicQuery.isec.get(key);

            isec.put(key, tmpS);
        }

        noIsec = new HashMap<String, Sensor>();

        it = atomicQuery.noIsec.keySet().iterator();

        while(it.hasNext()) {

            String key = it.next();

            Sensor tmpS = atomicQuery.noIsec.get(key);

            noIsec.put(key, tmpS);
        }

        devRep = new HashMap<String, Sensor>();

        it = atomicQuery.devRep.keySet().iterator();

        while(it.hasNext()) {

            String key = it.next();

            Sensor tmpS = atomicQuery.devRep.get(key);

            devRep.put(key, tmpS);
        }
    }

    public Timestamp getBegin() {
        return begin;
    }

    public Capability getCapability() {
        return capability;
    }

    public String getOperator() {
        return operator;
    }

    public Timestamp getEnd() {
        return end;
    }

    public LocationOfInterest getLoi() {
        return loi;
    }

    public Object getValue() {
        return value;
    }

    public String getFPProb(int num) {

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(num);
        nf.setMinimumFractionDigits(num);

        return nf.format(fPProb).toString() + " ";
    }

    @Override
    public String showStatementElement() {

        String retString = "";

        if (getIsNegation()) {
            retString += "! ";
        } 

        retString +=  "( ";

        retString += " " + capability.getName();
        
        retString += " " + operator + " ";

        if(value instanceof Integer) {
            retString += (Integer) value;
        } else if(value instanceof Double) {
            retString += (Double) value;
        } else if(value instanceof Float) {
            retString += (Float) value;
        } else if(value instanceof String) {
            retString += (String) value;
        }

        retString += ((loi == null)? " null " : " " + loi.getName()) + " ";

        retString += begin.getName()
            + " " + end.getName() + " ";

        retString += (isMin)? "min " : "max ";

        retString += percentage;

        retString += ") ";

        return retString;
    }

    public String showIsec() {

        String retString = "";

        Iterator<String> it = isec.keySet().iterator();

        while(it.hasNext()) {

            retString += it.next() + " ";
        }
        
        return retString;
    }

    public String showNoIsec() {

        String retString = "";

        Iterator<String> it = noIsec.keySet().iterator();

        while(it.hasNext()) {

            retString += it.next() + " ";
        }

        return retString;
    }

    public String showDevRep() {

        String retString = "";

        Iterator<String> it = devRep.keySet().iterator();

        while(it.hasNext()) {

            retString += it.next() + " ";
        }

        return retString;
    }

    @Override
    public String showStatementElementFile() {
    
        return showStatementElement();
    }

    public void setLoIApprox(Poly intersection) {
        this.approximation = intersection;
    }

    public void setFPProb(double d) {
        this.fPProb = 1 - d;
    }

    public void setIsec(ArrayList<Sensor> ec) {

//        System.err.println("Setting isec with size " + ec.size());

        for(Sensor tmpTS : ec) {

//            System.err.println("Putting " + tmpTS.getName());

            isec.put(tmpTS.getName(), tmpTS);
        }
    }

    public void setNoIsec(ArrayList<Sensor> ec) {

        for(Sensor tmpTS : ec) {

            noIsec.put(tmpTS.getName(), tmpTS);
        }
    }

    /**
     * Returning the tuple sources that are part of modelling the location
     * of interest. If the location of interest is null, all the sensors that
     * provide the capability in the atomic statement are put into the
     * isec set.
     *
     * @return
     */

    public HashMap<String, String> getIsecs() {

        HashMap<String, String> isecs = new HashMap<String, String>();

        Iterator<String> it = isec.keySet().iterator();

        while(it.hasNext()) {

            String tupleSource = it.next();

            isecs.put(tupleSource, tupleSource);
        }

        return isecs;
    }

    public HashMap<String, String> getNoIsecs() {

        HashMap<String, String> noIsecs = new HashMap<String, String>();

        Iterator<String> it = noIsec.keySet().iterator();

        while(it.hasNext()) {

            String tupleSource = it.next();

            noIsecs.put(tupleSource, tupleSource);
        }

        return noIsecs;
    }
    
    public HashMap<String, String> getDevReps() {

        HashMap<String, String> devReps = new HashMap<String, String>();

        Iterator<String> it = devRep.keySet().iterator();

        while(it.hasNext()) {

            String tupleSource = it.next();

            devReps.put(tupleSource, tupleSource);
        }

        return devReps;
    }

    /**
     * Tells if this atomic statement is set with a min or max.
     *
     * @return true: it is min, false: it is max
     */

    public boolean getIsMin() {
        return isMin;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isTimed() {
        return getBegin().getTimestamp() != -1 && getEnd().getTimestamp() != -1;
    }

    public boolean isDeltaTimed() {
        return getBegin().getTimestamp() != -1 && getEnd().getTimestamp() == -1;
    }

    /**
     * Only add those tuple sources that are not part of isec and noIsec.
     *
     * @param tmpTupleSource
     */

    public void addDevRep(Sensor tmpTupleSource) {

        if(!isec.containsKey(tmpTupleSource.getName())
                &&
                !noIsec.containsKey(tmpTupleSource.getName())) {

            System.err.println("Adding " + tmpTupleSource.getName() + 
            		" to DevRep in " + this.showStatementElement());

            devRep.put(tmpTupleSource.getName(), tmpTupleSource);
        }
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * Returns the length of the time window/duration. 
     * 
     * @return
     */
    
    public long getTimeWindow() {

        if(isTimed()) {
            return end.getTimestamp() - begin.getTimestamp();
        } 

    	return begin.getTimestamp();        
    }
    
    /**
     * 	Defines the N set of the atomic statement, i.e., the set that 
     * contains all the events that can be observed by this atomic
     * statement. For a an atomic statement, the N set is defined by
     * the LoI (Isec and NoIsec), the capability, and the timestamps.
     * 
     * 	The N set is represented by data tuples, since they provide relevant
     * event information, like the source, i.e., where was the event 
     * observed, the capability, the timestamps, the P-registration, 
     * and the sample rate of the sensor.
     * 
     *  
     */
    
    public void defineNset() {
    	
    	/**
    	 * Iterate through the Isec and NoIsec sets to find the correct 
    	 * sources. 
    	 */
    	
    	HashMap<String, Sensor> sources = 
    			new HashMap<String, Sensor>();
    	
    	sources.putAll(isec);
    	sources.putAll(noIsec);
    	
    	/**
    	 * For each of the sources, generate a set of the possible values
    	 * based on the capability in the statement. 
    	 */

    	/**
    	 * Sets up a permutation of allowed events based on the temporal
    	 * specifications. We restrict this to all events that are inside 
    	 * the duration and P-registration.   
    	 * 
    	 * Get the max depth of the tree.
    	 */

    	int maxDepth = getMaxDepth();

    	EvaluationTreeNode root = new EvaluationTreeNode();

    	/**
         * Since each tuple source might behave differently, e.g. have different
         * samplingFrequency values, we create one tree per tuple source. 
         */

    	Iterator<String> it = sources.keySet().iterator();
    	
    	while(it.hasNext()) {
    		
    		
    	}
    }
    
    /**
     * Gets the maximum depth of the N/M tree, i.e., the length of the 
     * evaluation window/duration based on the average sample rate of the 
     * sensor in the Isec and NoIsec sets. 
     */
    
    private int getMaxDepth() {
    	
    	double averageHz = this.getAverageHz();
    	
    	return (int) Math.round(this.getTimeWindow() * averageHz);
    }
    
    /**
     * Returns the average samplingFrequency from the tuple sources in the Isec and
     * NoIsec sets. 
     * 
     * @return
     */
    
    private double getAverageHz() {

    	double averageHz = 0;
    	
    	HashMap<String, Sensor> sensors = 
    		new HashMap<String, Sensor>();
    	
    	sensors.putAll(isec);
    	sensors.putAll(noIsec);
    	
    	Iterator<String> it = sensors.keySet().iterator();
    	
    	while(it.hasNext()) {
    	
    		Sensor calcTS = sensors.get(it.next());
    		
    		averageHz += calcTS.getHz();
    	}
    	
    	averageHz = averageHz/sensors.size();
    	
    	return averageHz;
    }
    
    /**
     * Creates a tree with the possible events. When the evaluation of an
     * atomic statement is performed, just follow the tree of possibilities
     * until the statement is matched or until there is a deviation.
     *  
     * The method runs recursively and stops when deviations have been made
     * or when the statement is fulfilled. 
     * 
     * @param tmpTS
     * @return
     */
    
    @Deprecated    
    private EvaluationTreeNode createPermutations(int depth, 
    		EvaluationTreeNode node, Sensor currTS) {
    
    	if(depth < getMaxDepth()) {
  		    	
	    	/**
	    	 * For each of the values in <tt>values</tt>, create the possible 
	    	 * permutations. All the possible values of a data tuple resemble
	    	 * a possible event.
	    	 */
	    	
	    	for(Object tmpVal : values) {
	    			
	    		//DataTuple tmpTuple = new DataTuple(tmpName, capability, value);
	    		
	    		
	    	}
    	}
	    
		/**
		 * Stop here.
		 */
    	
    	return null;
    }
    
    public void defineMset() {

    	/**
    	 * Sets up a permutation of allowed events given the temporal
    	 * specifications.   
    	 */

    	
    }

    /**
     * Returns the N set, i.e., all events that somehow related to the
     * query.
     * 
     * @return
     */
    
	public EventSet<DataTuple> getNset() {
		return null;
	}
	
    /**
     * Returns the M set, i.e., all events that are matched by the query.
     * 
     * @return
     */
    
	public EventSet<DataTuple> getMset() {
		return null;
	}

	/**
	 * Returns the deviation set, i.e., N \ M.
	 * @return
	 */
	
	public EventSet<DataTuple> getDeviations() {
		return (EventSet<DataTuple>) EventSet.difference(N, M);
	}
}
