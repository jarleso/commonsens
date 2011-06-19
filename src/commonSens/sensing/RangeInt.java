/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sensing;

import java.util.ArrayList;

/**
 * Describes a range of integers [from, ..., to], i.e., including from and to.
 *
 * @author jarleso
 */
public class RangeInt extends CapabilityDescription {

    int from, to;

    public RangeInt(int from, int to) {

        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public String getObject() {
    	
        return "{" + getFrom() + 
        	((getFrom() + 1 == getTo())? ", " : ", ..., ") + 
        	getTo() + "}";
    }

	@Override
	public ArrayList<Integer> getValues() {
		
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		for(int i = from; i <= to; i++) {
			values.add(i);
		}
		
		return values;
	}
}
