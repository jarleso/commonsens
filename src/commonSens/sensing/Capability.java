/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sensing;

/**
 *
 * @author jarleso
 */
public class Capability {

    String name;
    CapabilityDescription description;

    public Capability(String name, CapabilityDescription description) {

        this.name = name;
        this.description = description;
    }

    public Capability(Capability tmpCap) {
        this.name = tmpCap.getName();
        this.description = tmpCap.getDescription();
    }

    public String getName() {
        if(name != null) {
            return name;
        }
        return "";
    }

    public CapabilityDescription getDescription() {
        return description;
    }

    public String getObject() {
        return getName() + " " + getDescriptionString() + " ";
    }

    private String getDescriptionString() {

        if(description == null) return "";

        return description.getObject();

    }
}
