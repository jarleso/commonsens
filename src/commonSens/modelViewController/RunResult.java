package modelViewController;

public class RunResult {

	int result = -1;
	long endTime = -1;
	double averageProcessingTime = -1;
	
	public void setResult(int result) {
		this.result = result;
	}
	
	public int getResult() {
		return result;
	}

	public void setEndTime(long currentSecond) {
		this.endTime = currentSecond;
	}

	public void setAvgProcessingTime(double averageProcessingTime) {
		this.averageProcessingTime = averageProcessingTime;
		
	}

	public long getEndTime() {
		return endTime;
	}

	public double getAvgProcessingTime() {
		return averageProcessingTime;
	}
	

}
