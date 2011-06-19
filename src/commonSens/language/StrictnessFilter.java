/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package language;

import java.util.ArrayList;

/**
 *
 * @author jarleso
 */
public class StrictnessFilter {

    int key;

    ArrayList<QueryElement> conditions;

    public StrictnessFilter(int key) {
        this.key = key;

        conditions = new ArrayList<QueryElement>();
    }

    public void addConditions(QueryElement tmpElem) {

        conditions.add(tmpElem);
    }

    public int getKey() {
        return key;
    }
}
