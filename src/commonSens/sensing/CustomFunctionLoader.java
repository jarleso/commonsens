/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sensing;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jarleso
 */
public class CustomFunctionLoader extends ClassLoader {

    public CustomFunctionLoader() {
    }

    /**
     * Dynamically loads the object given by <tt>name<\tt>. The method also
     * adds the String "sensing." in front of the name, since it is implied
     * in our system that all the dynamic classes with the custom functions
     * are part of the package <tt>sensing</tt>.
     *
     * @param name
     * @param resolve
     * @return
     */

    public Object getObject(String name, boolean resolve) {

        name = "sensing." + name;

        try {
            try {
                return loadClass(name, resolve).newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(CustomFunctionLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CustomFunctionLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomFunctionLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
	@Override
    public Class findClass(String name) {
         byte[] b = loadClassData(name);
         return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {

        // load the class data from the connection

        return name.getBytes();
    }
}
