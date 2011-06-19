/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

/**
 *
 * @author jarleso
 */
public class ConditionalStrict extends Strict {

	StrictnessFilter filter;
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 592782079399294362L;
	int key;

    public ConditionalStrict(int key) {
        this.key = key;
    }
    
    public ConditionalStrict(StrictnessFilter filter) {
    	this.filter = filter;
    }
}
