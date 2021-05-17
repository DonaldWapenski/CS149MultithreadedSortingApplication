package cs149project;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Performs a quicksort on an array using RecursiveAction
 * @author Don
 */
public class QuickSorter extends RecursiveAction {
    private int begin, end, thresh;
    protected int[] arr;
    
    //FOR TESTING PURPOSES ONLY. If true, causes a few extra statements to print showing threads.
    protected static boolean testMultiThreading;
    
    /**
     * Constructor.
     * @param begin the starting index to sort from
     * @param end the ending index to stop sorting inclusive
     * @param arr the array to sort
     * @param threshold the threshold to apply a simpler sorting algorithm
     */
    public QuickSorter(int begin, int end, int[] arr, int threshold) {
        this.begin = begin;
        this.end = end;
        this.arr = arr;
        this.thresh = threshold;
        if (this.thresh < 1) {
            this.thresh = 1;
        }
    }
    
    /**
     * Overridden from RecursiveAction, performs the actual quicksort,
     * recursively forking threads in the process. Basic structure of
     * code is taken from page 183 of the textbook:
     * Operating System Concepts, by Silberchatz, Galvin, and Gagne
     * 10th Edition, Wiley, ISBN: 978-1119456339
     */
    @Override
    protected void compute() {
        if (testMultiThreading) {
            System.out.println("Thread " + Thread.currentThread().getId() + " sorting from " + this.begin + " to " + this.end);
        }
        if (end - begin < thresh) {
            selectionSort();
            if (testMultiThreading) {
                System.out.println("Thread " + Thread.currentThread().getId() + " finished sorting from " + this.begin + " to " + this.end);
            }
        } else {
            int pivot = quicksort(choosePivot());
            QuickSorter qs1 = new QuickSorter(begin, pivot-1, arr, thresh);
            QuickSorter qs2 = new QuickSorter(pivot+1, end, arr, thresh);
            if (testMultiThreading) {
                System.out.println("Thread " + Thread.currentThread().getId() + " finished sorting from " + this.begin + " to " + this.end);
            }
            qs1.fork();
            qs2.fork();
            qs1.join();
            qs2.join();
        }
        
    }
    
    /**
     * Partitions a list based on the value of the pivot
     * @param pivot the VALUE of the pivot, not the index
     * @return the INDEX the pivot is placed in after the quicksort
     */
    private int quicksort(int pivot) {
        //First, find index of pivot value
        int pivotIndex = begin;
        while (arr[pivotIndex] != pivot) { pivotIndex++; }
        
        //Next, partition the array. Start by swapping pivot to end
        swap(pivotIndex, end);
        int endOfLowerPart = begin;
        for (int i=endOfLowerPart; i<end; i++) {
            if (arr[i] <= pivot) {
                swap(endOfLowerPart, i);
                endOfLowerPart++;
            }
        }
        swap(endOfLowerPart, end); //undo initial action swapping pivot to end
        return endOfLowerPart;
    }
    
    /**
     * Calculates the pivot based on an estimation of the median. Estimation uses
     * the first, middle, and last values of the array and finds the median
     * of those three values.
     * @return the VALUE of the pivot
     */
    private int choosePivot() {
        int first = arr[begin];
        int last = arr[end];
        int mid = arr[(end - begin)/2];
        if ((first < mid && mid < last) ||
                (last < mid && mid < first)) {
            return mid; //mid is median
        } else if ((first < last && last < mid) ||
                (mid < last && last < first)) {
            return last; //last is median
        } else {
            return first; //first is median
        }
    }
    
    /**
     * Swaps two elements in the array
     * @param first the index of the first element to swap
     * @param second the index of the second element to swap
     */
    private void swap(int first, int second) {
        int temp = arr[first];
        arr[first] = arr[second];
        arr[second] = temp;
    }
    
    /**
     * Performs an iterative selection sort
     */
    private void selectionSort() {
        for (int i=begin; i<end; i++) {
            int min = i;
            for (int j=i+1; j<=end; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            swap(i, min);
        }
    }
}
