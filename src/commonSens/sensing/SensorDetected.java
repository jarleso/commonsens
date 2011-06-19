/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sensing;

import java.util.ArrayList;

/**
 *
 * @author jarleso
 */
public class SensorDetected extends CapabilityDescription {

    int from;
    String to;
    

    public SensorDetected(String description, int from, String to) {
    	this.description = description;
        this.from = from;
        this.to = to;
    }
    
    public int getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String getObject() {
        return getDescription() + " = {" + from + ", " + to + "}";
    }

	@Override
	public ArrayList<?> getValues() {
		ArrayList<String> ret = new ArrayList<String>();
		
		ret.add(String.valueOf(from));
		ret.add(to);
		
		return ret;
	}
}
