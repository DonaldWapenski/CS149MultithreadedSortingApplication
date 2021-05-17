package cs149project;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Iterator;

/**
 * Main file for this project.
 * Sorts an array using two threads with Quicksort and Mergesort.
 * @author Don Wapenski 010535538
 */
public class CS149Project {
    final private static String parametersFileName = "parameters.properties";
    final private static String outputFileName = "output.csv";
    final private static String lineSeparator = System.lineSeparator();
    
    public static void main(String argv[]) {
        System.out.println("Program Starting");
        //Start by loading the input file
        Properties parameters;
        FileInputStream in;
        BatchSorter bs = new BatchSorter();
        try {
            parameters = new Properties();
            in = new FileInputStream(parametersFileName);
            parameters.load(in);
            
            //Read input. Set defaults to -1 since BatchSorter performs validation
            int it = Integer.parseInt(parameters.getProperty("iterations","-1"));
            int sorts_per_it = Integer.parseInt(parameters.getProperty("sorts_per_iteration","-1"));
            int array_size = Integer.parseInt(parameters.getProperty("array_size","-1"));
            int array_inc = Integer.parseInt(parameters.getProperty("array_increment","-1"));
            int thresh = Integer.parseInt(parameters.getProperty("threshold","-1"));
            int thresh_inc = Integer.parseInt(parameters.getProperty("threshold_increment","-1"));
            int thresh_it = Integer.parseInt(parameters.getProperty("threshold_iterations","-1"));
            in.close();
            
            bs.load(it, sorts_per_it, array_size, array_inc, thresh, thresh_inc, thresh_it);
        } catch (IOException e) {
            System.out.println("ERROR loading input file.");
            e.printStackTrace();
            System.exit(-1);
        }
        
        bs.run();
        
        //extract statics and write to csv
        try {
            FileWriter fw = new FileWriter(outputFileName);
            fw.write("PARAMETERS"+lineSeparator);
            fw.write("Iterations,"+bs.getIterations()+lineSeparator);
            fw.write("Sorts per Iteration,"+bs.getSortsPerIt()+lineSeparator);
            fw.write("Array Size,"+bs.getArraySize()+lineSeparator);
            fw.write("Array Increment,"+bs.getArrayInc()+lineSeparator);
            fw.write("Threshold,"+bs.getThresh()+lineSeparator);
            fw.write("Threshold Increment,"+bs.getThreshInc()+lineSeparator);
            fw.write("Threshold Iterations,"+bs.getThreshIt()+lineSeparator);
            fw.write(lineSeparator);
            fw.write("RESULTS"+lineSeparator);
            fw.write("Array Size,Threshold,QuickSort Average Time,MergeSort Average Time,FailedQuickSort,FailedMergeSort"+lineSeparator);
            Iterator<Statistics> it = bs.statistics.iterator();
            while (it.hasNext()) {
                Statistics stats = it.next();
                String s = String.format("%d,%d,%.3f,%.3f,%d,%d", stats.arraySize, stats.threshold, stats.avgTimeQS, stats.avgTimeMS, stats.failedQS, stats.failedMS);
                //String s = stats.arraySize+","+stats.threshold+","+stats.avgTimeQS+","+stats.avgTimeMS+","+stats.failedQS+","+stats.failedMS+lineSeparator;
                fw.write(s+lineSeparator);
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("ERROR writing output file.");
            e.printStackTrace();
        }
        System.out.println("Program Ending");
    }
}
