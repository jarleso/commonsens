/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package environment;

import java.util.ArrayList;

import modelViewController.CommonSens;
import sensing.SignalType;

/**
 * A helper class for the reduction of a signal. Contains all the methods that
 * do the reduction.
 *
 * @author jarleso
 */
public class Ray {

    ArrayList<CoordinateStrength> triples;
    //ArrayList<CoordinateStrength> reduced;
    Triple from, to; // Tells only the distance.

    double length;
    int numElements;

    Ray(Triple from, Triple to) {

        this.from = from;
        this.to = to;

//        System.err.println("Creates ray from " + from.getObject()
//                + " to "
//                + to.getObject());

        instantiate(from, to);
    }

    private void instantiate(Triple from, Triple to) {

        // Set the number of elements.

        length = Math.sqrt(Math.pow((from.getX() - to.getX()), 2)
                + Math.pow((from.getY() - to.getY()), 2)
                + Math.pow((from.getZ() - to.getZ()), 2));

        numElements = (int) Math.round(length/CommonSens.GRANULARITY);

        //reduced = new ArrayList<CoordinateStrength>(numElements);

        //System.err.println("Ray instantiated with length: " + numElements);

        triples = new ArrayList<CoordinateStrength>(numElements);

        if(numElements > 0) {

            triples = new ArrayList<CoordinateStrength>(numElements);

            triples.add(new CoordinateStrength(from, 1));

            for(int i = 1; i < numElements; i++) {
                triples.add(new CoordinateStrength(to, 1));
            }

            // Recursively create all the midpoints:

            createNewList(triples, 0, triples.size() - 1);

        }

//        System.err.println("The elements in ray: " + length);
//
//        for(int i = 0; i < numElements; i++) {
//
//            System.err.println("\t" + boundary.get(i).getTriple().getObject());
//
//        }
    }

    public Triple getFrom() {
        return from;
    }

    public Triple getTo() {
        return to;
    }

    Ray(Ray tmpRay) {
        instantiate(tmpRay.getFrom(), tmpRay.getTo());
    }

    private void createNewList(ArrayList<CoordinateStrength> list, int start, int stop) {

        if(start > stop || start == stop || start + 1 == stop) {

            //System.err.println("createNewList returns = " + start + " " + stop);
            return;
        }

        Triple midPoint = new Triple(
                (int) ((list.get(start).getTriple().getX()
                    + list.get(stop).getTriple().getX())/2),
                (int) ((list.get(start).getTriple().getY()
                    + list.get(stop).getTriple().getY())/2),
                (int) ((list.get(start).getTriple().getZ()
                    + list.get(stop).getTriple().getZ())/2));

        int middle = stop - (stop - start)/2;

        list.set(middle, new CoordinateStrength(midPoint, 1));

        //System.err.println("createNewList(tmpList, " + start + ", " + middle);

        createNewList(list, start, middle);

        //System.err.println("createNewList(tmpList, " + middle + ", " + stop);

        createNewList(list, middle, stop);

    }

    private double perm(Environment tmpEnv, Triple tmpTrip,
            SignalType signalType) {

        double val = 0;

        // Get the objects that are located at this coordinate.

        if(tmpEnv == null) {
            //System.err.println("perm: tmpEnv == null...");
            //System.exit(-1);
        }

        ArrayList<CommonSensObject> objects = tmpEnv.getObjects(tmpTrip);

        // Find the one with the lowest permeability value for this
        // signalType.

        //System.err.println("PERM: Objects.size() = " + objects.size());

        for(CommonSensObject tmpObj : objects) {

            for(Permeability tmpPerm : tmpObj.getPermeabilities()) {

//                System.err.println(tmpPerm.getSignalType().getText() + " vs. "
//                        + signalType.getText());

                if(tmpPerm.getSignalType().getText()
                        .equals(signalType.getText())) {

                    //System.err.println("val = " + val + ", tmpPerm.getValue() = " + tmpPerm.getValue());

                    if(val == 0 || tmpPerm.getValue() < val) {
                        val = tmpPerm.getValue();
                    }
                }
            }
        }

        //System.err.println("perm found " + val + " and recalculates to " + (-1/(val - 1)));

        if(val == 0) return 0;

        return val;//(-1/(val - 1));
    }

    /**
     * The old way of reducing the range of the rays.
     * 
     * @param tmpEnv
     * @param signalType
     * @return
     */
    
    @Deprecated
    public Triple reduceRay_old(Environment tmpEnv, SignalType signalType) {

        double strength = 1;
        int range = numElements;
        double beta, betaCurr, betaPrev;
        beta = betaCurr = betaPrev = betaInit();
        int i = 0;
        int r = 0;

        while(strength > CommonSens.THRESHOLD && i < range) {

            betaCurr = getBeta(tmpEnv, signalType, i, beta);

            if(betaCurr < betaPrev) {

                r = skip(i, strength, betaCurr);
            }

            betaPrev = betaCurr;

            strength = 1/(1 + betaCurr * Math.pow(r, 2));

            i += 1;
            r += 1;

            //System.err.println("Range = " + range + " Strength = " + strength + " i = " + i);
        }

        if(i == range)
            return triples.get(range - 1).getTriple();

        return triples.get(i).getTriple();
    }

	/**
	 * Reduces the ray due to objects the ray meets. Currently it uses the Ding
	 * algorithm.
	 * 
	 * @param tmpEnv
	 * @param signalType
	 * @return The new boundary triple
	 */

	public Triple reduceRay(Environment tmpEnv, SignalType signalType) {

		int d0 = CommonSens.ANTENNA_LENGTH; 
		int range = numElements;
		double p0 = CommonSens.INITIAL_SIGNAL_STRENGTH;
		double m = CommonSens.THRESHOLD;
		double betaAir = Math.log(p0 / m)
				/ Math.log(((double) range) / (double) d0);
		double strength = p0;
		int i = 0;
		int r = 0;
		double prevPerm = betaAir;
		double currPerm = betaAir;

		while (strength > m && r < range) {
			currPerm = perm(tmpEnv, triples.get(r).getTriple(), signalType);
			if (prevPerm != currPerm) {
				p0 = strength;
				i = d0;
				prevPerm = currPerm;
			}
			strength = strength(tmpEnv, signalType, d0, p0, r, i);
			r += 1;
			i += 1;
		}

		if (r == range)
			return triples.get(range - 1).getTriple();

		return triples.get(r).getTriple();
	}

    double strength(Environment tmpEnv, SignalType signalType,
            int d0, double p0, int r, int i) {

        if(i < d0) {

            //System.err.println("i = " + i + ", d0 = " + d0 + " and returns " + p0);

            return p0;
        }

        double tmpPerm = perm(tmpEnv, triples.get(r).getTriple(), signalType);

        if(tmpPerm == 0) {
            return 0;
        }

        // Calculate beta. This is done as described in the EiMM10 paper.

        double retVal = p0/Math.pow(((double) i/(double) d0),(1 - tmpPerm));

        //System.err.println("strength returns " + retVal + " with tmpPerm = " + tmpPerm);

        return retVal;
    }

    double logB(double base, double n) {

        return Math.log(n)/Math.log(base);
    }

    private double betaInit() {

        //System.err.println("length = " + length);

        return (CommonSens.THRESHOLD - 1)/(-CommonSens.THRESHOLD * length);
    }

    private double getBeta(Environment tmpEnv, SignalType signalType, int i, double beta) {

        double ret = (CommonSens.THRESHOLD - 1)/(-CommonSens.THRESHOLD
                * Math.pow((perm(tmpEnv, triples.get(i).getTriple(), signalType) * length),2));//beta * perm(tmpEnv, boundary.get(i).getTriple(), signalType);

//        if(perm(tmpEnv, boundary.get(i).getTriple(), signalType) != 1) {
//            System.err.println("beta = "+ beta +" ret = " + ret);
//        }

        return ret;
    }

    private int skip(int i, double prevStrength, double currBeta) {

        return (int) Math.round(Math.sqrt((prevStrength - 1)/(-prevStrength * currBeta)));
    }
}
