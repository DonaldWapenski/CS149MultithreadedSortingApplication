package cs149project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * Unit test for MergeSorter to ensure accuracy of test
 * @author Don
 */
public class MergeSorterTest {
    /**
     * Test of compute method, of class MergeSorter.
     */
    @Test
    public void testCompute() {
        System.out.println("MergeSorter compute");
        final int[] testThresh = new int[] {0, 1, 2, 3, 4};
        final int[][] testCases = new int[][] {
            {91, 178, 12, 132, 48, 38, 147, 172, 145, 60, 188, 6, 194, 43, 35, 46, 24, 32, 97, 13},
            {157, 177, 59, 80, 76, 132, 138, 78, 186, 72, 137, 34, 190, 100, 45, 193, 33, 31, 51, 11},
            {51, 174, 3, 37, 112, 10, 130, 59, 25, 122, 33, 20, 195, 104, 68, 61, 44, 34, 46, 92},
            {40, 137, 157, 21, 67, 125, 18, 123, 38, 169, 76, 168, 200, 65, 166, 116, 115, 107, 56, 52},
            {166, 125, 9, 61, 2, 85, 14, 168, 183, 136, 37, 151, 101, 96, 146, 11, 45, 177, 119, 97},
            {-166, -125, -9, -61, -2, -85, -14, -168, -183, -136, -37, -151, -101, -96, -146, -11, -45, -177, -119, -97},
            {-166, -125, -9, 61, -2, -85, -14, 168, -0, 136, 37, -151, -101, 96, -146, 11, -45, -177, -119, -97},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
            {20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, -1, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20},
            {1},
            {}
        };
        int[][] expectedResults = new int[testCases.length][];
        for (int i=0; i<testCases.length; i++) {
            expectedResults[i] = testCases[i].clone();
            Arrays.sort(expectedResults[i]);
        }
        
        //The test itself. Test each threshold value for each array
        for (int i=0; i<testCases.length; i++) {
            System.out.println("Input array: " + Arrays.toString(testCases[i]));
            System.out.println("Expected output: " + Arrays.toString(expectedResults[i]));
            for (int j=0; j<testThresh.length; j++) {
                MergeSorter instance = new MergeSorter(0, testCases[i].length-1, testCases[i].clone(), testThresh[j]);
                instance.compute();
                assertArrayEquals(expectedResults[i], instance.arr);
                System.out.println("    Results with threshold " + testThresh[j] + ": " + Arrays.toString(instance.arr));
            }
            //Also test with threshold size equal to the array and greater than the array
            MergeSorter instance = new MergeSorter(0, testCases[i].length-1, testCases[i].clone(), testCases[i].length);
            instance.compute();
            assertArrayEquals(expectedResults[i], instance.arr);
            System.out.println("    Results with threshold " + testCases[i].length + ": " + Arrays.toString(instance.arr));
            
            instance = new MergeSorter(0, testCases[i].length-1, testCases[i].clone(), testCases[i].length+1);
            instance.compute();
            assertArrayEquals(expectedResults[i], instance.arr);
            System.out.println("    Results with threshold " + (testCases[i].length+1) + ": " + Arrays.toString(instance.arr));
            System.out.println();
        }
        System.out.println("MergeSorter compute complete\n");
        
        System.out.println("MergeSorter multithreaded test");
        int[] multithreadTestCase = new int[] {5, 1, 5, 2, 7, 2, 10, 23, 15, 11, 21, 19, 6, 30, 26, 17, 13, 29, 6, 14};
        MergeSorter instance = new MergeSorter(0, multithreadTestCase.length-1, multithreadTestCase, 2);
        MergeSorter.testMultiThreading = true;
        instance.fork();
        instance.join();
        System.out.println("MergeSorter multithreaded complete");
    }
}
