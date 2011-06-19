package eventProcessor;

import java.util.ArrayList;
import javax.swing.tree.TreeNode;

public class EvaluationTreeNode {

	String setType;
	DataTuple currentDataTuple;
	ArrayList<DataTuple> children;
	
	public EvaluationTreeNode() {
		children = new ArrayList<DataTuple>();
	}
	
	public EvaluationTreeNode(DataTuple currentDataTuple, String setType) {
		
		this.currentDataTuple = currentDataTuple;
		this.setType = setType;
		
		children = new ArrayList<DataTuple>();
	}
	
	/**
	 * Checks if an incoming data tuple matches the current data tuple.
	 * 
	 * @return
	 */
	
	public boolean equals(DataTuple incoming) {
		
		return incoming.getObject().equals(currentDataTuple.getObject());
	}
	
	/**
	 * When an event is detected, its duration can be specified. This 
	 * simplifies the evaluation of the data tuple. The method sets the
	 * timestamp of the current data tuple, investigates the sample 
	 * frequency and increases the timestamp so that it matches the next
	 * expected events. 
	 * 
	 * The method runs recursively until it matches stopTime + 1 so that we
	 * make sure that M \subset N. 
	 * 
	 * @param beginTime
	 */

	@Deprecated
	public void updateTiming(Timestamp beginTime, Timestamp stopTime) {
		
	}
	
	public void addChild(DataTuple child) {
		
		children.add(child);
	}
	
	public boolean compare(DataTuple tmpTuple) {
		
		return false;
	}
	
	public ArrayList<DataTuple> children() {
		return children;
	}

	public int getChildCount() {
		return children.size();
	}

	public TreeNode getParent() {
		return null;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}
}
