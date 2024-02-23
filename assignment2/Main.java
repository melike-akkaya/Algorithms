import java.io.*;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        // read file
        String file =  args[0];              //get file name as an argument
        Task[] tasks = parseJSON(file);

        // The tasks list must be ordered according to the finishing order of the tasks in order to apply the weighted interval scheduling algorithm.
        // The task objects have the necessary method to understand how they will be sorted.
        // Hence, the Arrays.sort method (which is easy to use and satisfying with complexity) can be used.
        // Arrays.sort uses two sorting algorithms. One is a modification of Quicksort named dual-pivot quicksort,
        // the other an adaptation of MergeSort named Timsort. Both have a time complexity of O(n log n).
        Arrays.sort(tasks);

        Planner planner = new Planner(tasks);
        // dynamic solution
        planner.planDynamic();
        System.out.println();
        // greedy solution
        planner.planGreedy();
    }

    public static Task[] parseJSON(String filename) throws FileNotFoundException {

        /* JSON parsing operations here */
        Gson gson = new Gson();
        JsonReader jr = new JsonReader(new FileReader(filename));
        return gson.fromJson(jr, Task[].class);
    }
}