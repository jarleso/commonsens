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
public class RangeString extends CapabilityDescription {

    String from, to;

    public RangeString(String description, String from, String to) {

    	this.description = description;    	
        this.from = from;
        this.to = to;
    }
    
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String getObject() {

        return getDescription() + " = { " + getFrom() + " , " + getTo() + " }";
    }

	@Override
	public ArrayList<String> getValues() {

		ArrayList<String> values = new ArrayList<String>();
		values.add(from);
		values.add(to);
		
		return values;
	}
}