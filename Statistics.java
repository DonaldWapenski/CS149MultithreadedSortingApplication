package cs149project;

/**
 * Provides a container for statistics of a sort.
 * @author Don
 */
public class Statistics {
    public int arraySize, threshold;
    public double avgTimeQS, avgTimeMS;
    public int failedQS, failedMS;
    
    /**
     * Constructor with parameters to initialize values.
     * @param arraySize Size of array that was sorted
     * @param threshold Threshold value used to switch to simpler algorithm
     * @param avgTimeQS Average run-time of quicksorts in ms
     * @param avgTimeMS Average run-time of mergesorts in ms
     * @param failedQS Number of incorrect quicksorts. Expected to be 0
     * @param failedMS Number of incorrect mergesorts. Expected to be 0
     */
    public Statistics(int arraySize, int threshold, double avgTimeQS, double avgTimeMS, int failedQS, int failedMS) {
        this.arraySize = arraySize;
        this.threshold = threshold;
        this.avgTimeQS = avgTimeQS;
        this.avgTimeMS = avgTimeMS;
        this.failedQS = failedQS;
        this.failedMS = failedMS;
    }
}
