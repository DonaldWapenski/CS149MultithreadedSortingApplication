package cs149project;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Performs a mergesort on an array using RecursiveAction
 * @author Don
 */
public class MergeSorter extends RecursiveAction {
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
    public MergeSorter(int begin, int end, int[] arr, int threshold) {
        this.begin = begin;
        this.end = end;
        this.arr = arr;
        this.thresh = threshold;
        if (this.thresh < 1) {
            this.thresh = 1;
        }
    }
    
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
            //Begin by doing "sort" portion of mergesort by splitting array in half
            int mid = (end - begin)/2+begin;
            MergeSorter ms1 = new MergeSorter(begin, mid, arr, thresh);
            MergeSorter ms2 = new MergeSorter(mid+1, end, arr, thresh);
            ms1.fork();
            ms2.fork();
            ms1.join();
            ms2.join();
            
            //Finish by merging the two halves back together
            int[] tempArr = new int[end-begin+1]; //temporary array to store results of merge
            int half1 = begin; //tracks where we are in the first half of the array
            int half2 = mid+1; //same as above but for second half
            for (int i=0; i<tempArr.length; i++) {
                if (half1 == mid+1) {
                    tempArr[i] = arr[half2];
                    half2++;
                } else if (half2 == end+1) {
                    tempArr[i] = arr[half1];
                    half1++;
                } else if (arr[half1] < arr[half2]) {
                    tempArr[i] = arr[half1];
                    half1++;
                } else { //arr[half2] < arr[half1]
                    tempArr[i] = arr[half2];
                    half2++;
                }
            }
            //Write the merged temporary array to the real array
            for (int i=0; i<tempArr.length; i++) {
                arr[begin+i] = tempArr[i];
            }
            if (testMultiThreading) {
                System.out.println("Thread " + Thread.currentThread().getId() + " finished sorting from " + this.begin + " to " + this.end);
            }
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
