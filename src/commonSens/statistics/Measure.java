/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package statistics;

/**
 *
 * @author jarleso
 */
class Measure {

    int num;
    long time;
    double batchTime, batchSize, evaluateBatch, queuePulling,
            stateEvaluation, timeTupleSourcesToPull, getTupleSourcesToPull;

    double maxBatchTime, maxBatchSize, maxEvaluateBatch, maxQueuePulling,
            maxStateEvaluation, maxTimeTupleSourcesToPull, maxGetTupleSourcesToPull;

    double minBatchTime, minBatchSize, minEvaluateBatch, minQueuePulling,
            minStateEvaluation, minTimeTupleSourcesToPull, minGetTupleSourcesToPull;

    Measure(long time, double batchTime, double batchSize,
             double evaluateBatch, double queuePulling, double
            stateEvaluation, double timeTupleSourcesToPull, double getTupleSourcesToPull) {

        this.time = time;
        this.batchTime = batchTime;
        this.batchSize = batchSize;
        this.evaluateBatch = evaluateBatch;
        this.queuePulling = queuePulling;
        this.stateEvaluation = stateEvaluation;
        this.timeTupleSourcesToPull = timeTupleSourcesToPull;
        this.getTupleSourcesToPull = getTupleSourcesToPull;

        this.maxBatchTime = batchTime;
        this.maxBatchSize = batchSize;
        this.maxEvaluateBatch = evaluateBatch;
        this.maxQueuePulling = queuePulling;
        this.maxStateEvaluation = stateEvaluation;
        this.maxTimeTupleSourcesToPull = timeTupleSourcesToPull;
        this.maxGetTupleSourcesToPull = getTupleSourcesToPull;

        this.minBatchTime = batchTime;
        this.minBatchSize = batchSize;
        this.minEvaluateBatch = evaluateBatch;
        this.minQueuePulling = queuePulling;
        this.minStateEvaluation = stateEvaluation;
        this.minTimeTupleSourcesToPull = timeTupleSourcesToPull;
        this.minGetTupleSourcesToPull = getTupleSourcesToPull;

        num = 1;
    }

    /**
     * Calculates the average values and the variance.
     * 
     * @param batchTime
     * @param batchSize
     * @param evaluateBatch
     * @param queuePulling
     * @param stateEvaluation
     * @param timeTupleSourcesToPull
     * @param getTupleSourcesToPull
     */
    
    public void addLine(double batchTime, double batchSize,
             double evaluateBatch, double queuePulling, double
            stateEvaluation, double timeTupleSourcesToPull, double getTupleSourcesToPull) {

        this.batchSize = ((this.batchSize * num) +  batchSize)/(num + 1);
        this.batchTime = ((this.batchTime * num) +  batchTime)/(num + 1);
        this.evaluateBatch = ((this.evaluateBatch * num) +  evaluateBatch)/(num + 1);
        this.getTupleSourcesToPull = ((this.getTupleSourcesToPull * num) +
                getTupleSourcesToPull)/(num + 1);
        this.queuePulling = ((this.queuePulling * num) +
                queuePulling)/(num + 1);
        this.timeTupleSourcesToPull = ((this.timeTupleSourcesToPull * num) +
                timeTupleSourcesToPull)/(num + 1);
        this.stateEvaluation = ((this.stateEvaluation * num) +
                stateEvaluation)/(num + 1);

        if(this.maxBatchTime < batchTime) this.maxBatchTime = batchTime;
        if(this.maxBatchSize < batchSize) this.maxBatchSize = batchSize;
        if(this.maxEvaluateBatch < evaluateBatch) this.maxEvaluateBatch = evaluateBatch;
        if(this.maxQueuePulling < queuePulling) this.maxQueuePulling = queuePulling;
        if(this.maxStateEvaluation < stateEvaluation) this.maxStateEvaluation = stateEvaluation;
        if(this.maxTimeTupleSourcesToPull < timeTupleSourcesToPull) this.maxTimeTupleSourcesToPull = timeTupleSourcesToPull;
        if(this.maxGetTupleSourcesToPull < getTupleSourcesToPull) this.maxGetTupleSourcesToPull = getTupleSourcesToPull;

        if(this.minBatchTime > batchTime) this.minBatchTime = batchTime;
        if(this.minBatchSize > batchSize) this.minBatchSize = batchSize;
        if(this.minEvaluateBatch > evaluateBatch) this.minEvaluateBatch = evaluateBatch;
        if(this.minQueuePulling > queuePulling) this.minQueuePulling = queuePulling;
        if(this.minStateEvaluation > stateEvaluation) this.minStateEvaluation = stateEvaluation;
        if(this.minTimeTupleSourcesToPull > timeTupleSourcesToPull) this.minTimeTupleSourcesToPull = timeTupleSourcesToPull;
        if(this.minGetTupleSourcesToPull > getTupleSourcesToPull) this.minGetTupleSourcesToPull = getTupleSourcesToPull;

        num += 1;
    }

    public String getLine() {

        return time + "\t" + batchTime + "\t" + minBatchTime + "\t" + maxBatchTime
                + "\t" + batchSize + "\t" + minBatchSize + "\t" + maxBatchSize
                + "\t" + evaluateBatch + "\t" + minEvaluateBatch + "\t" + maxEvaluateBatch
                + "\t" + queuePulling + "\t" + minQueuePulling + "\t" + maxQueuePulling
                + "\t" + stateEvaluation + "\t" + minStateEvaluation + "\t" + maxStateEvaluation
                + "\t" + getTupleSourcesToPull + "\t" + minGetTupleSourcesToPull + "\t" + maxGetTupleSourcesToPull
                
                /* Line added 11.6.10 to get the EiMM10 results. */
                
                + "\t" + timeTupleSourcesToPull + "\t" + minTimeTupleSourcesToPull + "\t" + maxTimeTupleSourcesToPull;
    }
}
