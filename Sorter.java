package cs149project;

import java.util.Arrays;
import java.util.Random;

/**
 * Creates an array and sorts it separately with QuickSort and MergeSort
 * @author Don
 */
public class Sorter {
    private int thresh;
    private int[] arr;
    
    private QuickSorter qs;
    private MergeSorter ms;
    private long startTime, endTime, elapsedTimeQS, elapsedTimeMS;
    
    private Random rand;
    
    /**
     * Constructor
     * @param arraySize the size of the array to create
     * @param threshold the threshold value to use
     */
    public Sorter(int arraySize, int threshold) {
        arr = new int[arraySize];
        this.thresh = threshold;
        rand = new Random();
    }
    
    /**
     * Runs and times sorts for the same array using quicksort and mergesort
     */
    public void run() {
        //First generate random values for array. Range from 0 to 10*size, duplicates allowed
        for (int i=0; i<arr.length; i++) {
            arr[i] = rand.nextInt(arr.length*10);
        }
        
        //Run and time the quicksort
        startTime = System.nanoTime();
        qs = new QuickSorter(0, arr.length-1, arr.clone(), thresh);
        qs.fork();
        qs.join();
        //System.out.println("Quicksort result: " + Arrays.toString(qs.arr));
        endTime = System.nanoTime();
        elapsedTimeQS = endTime - startTime;
        //System.out.println("");
        //Run and time the mergesort
        startTime = System.nanoTime();
        ms = new MergeSorter(0, arr.length-1, arr.clone(), thresh);
        ms.fork();
        ms.join();
        //System.out.println("Mergesort result: " + Arrays.toString(ms.arr));
        endTime = System.nanoTime();
        elapsedTimeMS = endTime - startTime;
    }
    
    /**
     * Gets the time taken for quicksort in ms
     * @return the time taken for quicksort in ms
     */
    public long getQSTime() {
        return elapsedTimeQS;
    }
    
    /**
     * Gets the time taken for mergesort in ms
     * @return the time taken for mergesort in ms
     */
    public long getMSTime() {
        return elapsedTimeMS;
    }
    
    /**
     * Verifies that the quicksort was done correctly
     * @return true if the quicksort was correct
     */
    public boolean verifyQS() {
        return verifySort(qs.arr);
    }
    
    /**
     * Verifies that the mergesort was done correctly
     * @return true if the mergesort was correct
     */
    public boolean verifyMS() {
        return verifySort(ms.arr);
    }
    
    /**
     * Verifies that an array was sorted correctly in ascending order.
     * @param arrCheck The array to verify
     * @return true if the sort was correct
     */
    private boolean verifySort(int[] arrCheck) {
        boolean sorted = true;
        int prev = arrCheck[0];
        for (int i=1; i>arrCheck.length; i++) {
            if (prev > arrCheck[i]) {
                sorted = false;
            }
            prev = arrCheck[i];
        }
        return sorted;
    }
}
