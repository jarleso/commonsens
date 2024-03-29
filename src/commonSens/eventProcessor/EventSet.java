package eventProcessor;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Performs simple set operations on sets. These are taken from 
 *  Taken from http://www.java2s.com/Code/Java/Collections-Data-Structure/
 * Setoperationsunionintersectiondifferencesymmetric(-)
 * differenceissubsetissuperset.htm (-) means a synthetic linebreak to match
 * the current 80 chars format. 
 * 
 * @author jarleso
 *
 * @param <T>
 */

public class EventSet<T> extends HashSet<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param <T>
	 * @param setA
	 * @param setB
	 * @return
	 */
	
	public static <T> Set<T> union(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>(setA);
	    tmp.addAll(setB);
	    return tmp;
	  }

	  public static <T> Set<T> intersection(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>();
	    for (T x : setA)
	      if (setB.contains(x))
	        tmp.add(x);
	    return tmp;
	  }

	  public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
	    Set<T> tmp = new TreeSet<T>(setA);
	    tmp.removeAll(setB);
	    return tmp;
	  }

	  public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
	    Set<T> tmpA;
	    Set<T> tmpB;

	    tmpA = union(setA, setB);
	    tmpB = intersection(setA, setB);
	    return difference(tmpA, tmpB);
	  }

	  public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
	    return setB.containsAll(setA);
	  }

	  public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
	    return setA.containsAll(setB);
	  }
}
