/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eventProcessor;

/**
 *
 * @author jarleso
 */
public class Timer implements Runnable {

    long startTime;
    private boolean emulation;
    long currentTime;

    /**
     * Starts timing. Time is always relative to
     * to the start time of the current experiment.
     */

    public Timer(boolean emulation) {

    	restart(emulation);
    }

    public long getCurrentSecond() {

        if(emulation)
            return (System.currentTimeMillis() - startTime)/1000;

        return currentTime/1000;
    }

    public long getCurrentMillisecond() {

        if(emulation)
            return (System.currentTimeMillis() - startTime);

        return currentTime;
    }

    public void increaseSecond() {

        currentTime += 1000;
    }

    public void increaseSeconds(long numSeconds) {

        currentTime += numSeconds;
    }

    public void increaseMilliseconds(long numMilliseconds) {

        currentTime += numMilliseconds;
    }

    public void run() {

        
    }

    public long getCurrentMillisecondRounded() {
    	
    	if(emulation)
            return ((System.currentTimeMillis() - startTime)/1000)*1000;
    	
        return (currentTime / 1000) * 1000;
    }

	public void restart(boolean emulation) {
		
        this.emulation = emulation;

        if(emulation)
            startTime = System.currentTimeMillis();
        else {
            startTime = 0;
            currentTime = 0;
        }
	}

	/**
	 * Returns the current part of the millisecond as divided by samplingFrequency. For 
	 * instance, samplingFrequency = 3 divides the second into three 333 parts. For any 
	 * second S, the value S.123 will return S, S.444 returns S.333 and
	 * S.998 return S.666. 
	 * 
	 * @param samplingFrequency
	 * @return the current time in seconds added to the correct part of the
	 * millisecond. -1 if error.
	 */
	
	public long getCurrentMillisecond(int Hz) {

		if(Hz == 1) {
			return getCurrentMillisecondRounded();
		}
		
		long millis = getCurrentMillisecond() - 
						getCurrentMillisecondRounded();
		
		int chunkSize = 1000 / Hz;
		
		for(int i = 0; i < (1000 - chunkSize); i += chunkSize) {
			
			if (millis <= (i + chunkSize)) {
				return getCurrentMillisecondRounded() + i;
			}
		}
		
		return -1;
	}
}
