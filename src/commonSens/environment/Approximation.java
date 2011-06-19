package environment;

import java.util.ArrayList;

import sensing.Sensor;

public class Approximation {
	
	double fPProb;
	ArrayList<Sensor> isec; 
	ArrayList<Sensor> noIsec;
	
	Approximation(double fPProb, ArrayList<Sensor> isec, 
			ArrayList<Sensor> noIsec) {
	
		this.fPProb = fPProb;
		this.isec = isec;
		this.noIsec = noIsec;		
	}

	public double getfPProb() {
		return fPProb;
	}

	public ArrayList<Sensor> getIsec() {
		return isec;
	}

	public ArrayList<Sensor> getNoIsec() {
		return noIsec;
	}	
}
