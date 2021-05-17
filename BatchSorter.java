package cs149project;

import java.util.ArrayList;

/**
 * Sets up and runs Sorters and aggregates statistics
 * @author Don
 */
public class BatchSorter {
    private int iterations, sortsPerIt, arraySize, arrayInc, thresh, threshInc, threshIt;

    protected ArrayList<Statistics> statistics;
    
    //Default parameters. Lowest allowable values.
    final public int DEF_ITERATIONS = 1, DEF_SORTSPERIT = 1, DEF_ARRAYSIZE = 1, 
            DEF_ARRAYINC = 0, DEF_THRESH = 1, DEF_THRESHINC = 0, DEF_THRESHIT = 0;
    
    /**
     * Default constructor.
     */
    public BatchSorter() {
        this.iterations = DEF_ITERATIONS;
        this.sortsPerIt = DEF_SORTSPERIT;
        this.arraySize = DEF_ARRAYSIZE;
        this.arrayInc = DEF_ARRAYINC;
        this.thresh = DEF_THRESH;
        this.threshInc = DEF_THRESHINC;
        this.threshIt = DEF_THRESHIT;
        this.statistics = new ArrayList<>();
    }
    
    /**
     * Constructor with input parameters. If parameters passed in are unallowable values,
     * they will be set to the default value.
     * @param iterations Number of iterations to perform
     * @param sortsPerIt Number of arrays to sort per iteration
     * @param arraySize Initial size of array to sort
     * @param arrayInc Size to increase array per iteration
     * @param thresh Initial threshold to switch to a simpler sorting algorithm
     * @param threshInc How much to increase the threshold size by each iteration
     * @param threshIt Number of iterations to test threshold, multiplies number of overall iterations
     */
    public BatchSorter(int iterations, int sortsPerIt, int arraySize, int arrayInc, int thresh, int threshInc, int threshIt) {
        load(iterations, sortsPerIt, arraySize, arrayInc, thresh, threshInc, threshIt);
    }
    
    /**
     * Loads values given to it, similar to constructor.
     * @param iterations Number of iterations to perform
     * @param sortsPerIt Number of arrays to sort per iteration
     * @param arraySize Initial size of array to sort
     * @param arrayInc Size to increase array per iteration
     * @param thresh Initial threshold to switch to a simpler sorting algorithm
     * @param threshInc How much to increase the threshold size by each iteration
     * @param threshIt Number of iterations to test threshold, multiplies number of overall iterations
     */
    public void load(int iterations, int sortsPerIt, int arraySize, int arrayInc, int thresh, int threshInc, int threshIt) {
        if (iterations < DEF_ITERATIONS) {
            this.iterations = DEF_ITERATIONS;
        } else {
            this.iterations = iterations;
        }
        if (sortsPerIt < DEF_SORTSPERIT) {
            this.sortsPerIt = DEF_SORTSPERIT;
        } else {
            this.sortsPerIt = sortsPerIt;
        }
        if (arraySize < DEF_ARRAYSIZE) {
            this.arraySize = DEF_ARRAYSIZE;
        } else {
            this.arraySize = arraySize;
        }
        if (arrayInc < DEF_ARRAYINC) {
            this.arrayInc = DEF_ARRAYINC;
        } else {
            this.arrayInc = arrayInc;
        }
        if (thresh < DEF_THRESH) {
            this.thresh = DEF_THRESH;
        } else {
            this.thresh = thresh;
        }
        if (threshInc < DEF_THRESHINC) {
            this.threshInc = DEF_THRESHINC;
        } else {
            this.threshInc = threshInc;
        }
        if (threshIt < DEF_THRESHIT) {
            this.threshIt = DEF_THRESHIT;
        } else {
            this.threshIt = threshIt;
        }
        this.statistics = new ArrayList<>();
    }
    
    /**
     * Sets up, runs, and collects statistics for each sort
     */
    public void run() {
        for (int i=0; i<iterations; i++) {
            System.out.println("Iteration " + (i+1));
            for (int j=0; j<threshIt; j++) {
                Sorter sorter = new Sorter(arraySize+i*arrayInc, thresh+j*threshInc);
                
                //Trackers for average run-time for each algorithm
                double avgTimeQS = 0;
                double avgTimeMS = 0;
                
                //Paranoid checks for incorrect sorting. Should stay 0 by end of execution.
                int failedQS = 0;
                int failedMS = 0;
                for (int s=0; s<sortsPerIt; s++) {
                    sorter.run();
                    avgTimeQS += sorter.getQSTime() / sortsPerIt;
                    avgTimeMS += sorter.getMSTime() / sortsPerIt;
                    if (!sorter.verifyQS()) {
                        failedQS++;
                    }
                    if (!sorter.verifyMS()) {
                        failedMS++;
                    }
                }
                
                //Record statistics
                avgTimeQS /= 1000000; //convert ns to ms
                avgTimeMS /= 1000000;
                statistics.add(new Statistics(arraySize+i*arrayInc, thresh+j*threshInc, avgTimeQS, avgTimeMS, failedQS, failedMS));
            }
        }
    }
    
    public int getIterations() {
        return iterations;
    }

    public int getSortsPerIt() {
        return sortsPerIt;
    }

    public int getArraySize() {
        return arraySize;
    }

    public int getArrayInc() {
        return arrayInc;
    }

    public int getThresh() {
        return thresh;
    }

    public int getThreshInc() {
        return threshInc;
    }

    public int getThreshIt() {
        return threshIt;
    }
}
