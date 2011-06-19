/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelViewController;

import javax.swing.JFileChooser;

/**
 * A generic JFileChooser for our CEP system.
 *
 * @author jarleso
 */
public class FileChooser extends JFileChooser implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8536529556185183234L;

	FileChooser(String file) {

        super(file);
    }
}
