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
public abstract class CapabilityDescription {

	String description;
	
	public String getDescription() {
    	return description;
    }
	
    abstract public String getObject();

	abstract public ArrayList<?> getValues();

}
