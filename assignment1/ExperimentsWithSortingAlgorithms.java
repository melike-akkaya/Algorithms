import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ExperimentsWithSortingAlgorithms {

    // the following 3 functions calculate the average time spent for sorting on the input size given for different sorting algorithms
    public static void calculationForSelectionSort(int[] data, int inputSize, ArrayList<Double> times) {
        double average = 0;
        for (int i = 0; i < 10; i++) {
            int[] dataForSelectionSort = Arrays.copyOf(data, inputSize);
            long start = System.nanoTime();
            SelectionSort.sort(dataForSelectionSort, inputSize);
            long end = System.nanoTime();
            double duration = (end - start) / 1e6;
            average += duration;
        }
        times.add(average / 10);

        System.out.println("select sort for " + inputSize + " = " + average);
    }

    public static void calculationForQuickSort(int[] data, int inputSize, ArrayList<Double> times) {
        double average = 0;
        for (int i = 0; i < 10; i++) {
            int[] dataForQuickSort = Arrays.copyOf(data, inputSize);
            long start = System.nanoTime();
            QuickSort.sort(dataForQuickSort, inputSize - 1, 0);
            long end = System.nanoTime();
            double duration = (end - start) / 1e6;
            average += duration;
        }
        times.add(average / 10);

        System.out.println("quick sort for " + inputSize + " = " + average);
    }

    public static void calculationForBucketSort(int[] data, int inputSize, ArrayList<Double> times) {
        double average = 0;
        for (int i = 0; i < 10; i++) {
            int[] dataForBucketSort = Arrays.copyOf(data, inputSize);
            long start = System.nanoTime();
            BucketSort.sort(dataForBucketSort);
            long end = System.nanoTime();
            double duration = (end - start) / 1e6;
            average += duration;
        }
        times.add(average / 10);

        System.out.println("bucket sort for " + inputSize + " = " + average);
    }

    // used to calculate duration for each input size and also create a list of data to use when plotting the graph
    public static void listingTime (int[] data, int[] inputSizes, ArrayList<Double> durationForSelectionSort,
                                    ArrayList<Double> durationForQuickSort, ArrayList<Double> durationForBucketSort, double[][] yAxis) {
        System.out.println("SELECTION SORT");
        for (int size : inputSizes)
            calculationForSelectionSort(data, size, durationForSelectionSort);
        System.out.println("-------------------------");

        System.out.println("QUICK SORT");
        for (int size : inputSizes)
            calculationForQuickSort(data, size, durationForQuickSort);
        System.out.println("-------------------------");

        System.out.println("BUCKET SORT");
        for (int size : inputSizes)
            calculationForBucketSort(data, size, durationForBucketSort);

        for (int i = 0; i < 10; i++) {
            yAxis[0][i] = durationForSelectionSort.get(i);
            yAxis[1][i] = durationForQuickSort.get(i);
            yAxis[2][i] = durationForBucketSort.get(i);
        }
    }

    // used to get reversed ordered array
    public static void reverse (int[] array) {
        for(int i = 0; i < array.length / 2; i++) {
            // swapping the elements
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    // plots related graphs with sorting algorithms
    public static void calculateDataForSorting(int[] data, int[] inputSizes) throws IOException {
        //random input
        ArrayList<Double> selectionSort = new ArrayList<>();
        ArrayList<Double> quickSort = new ArrayList<>();
        ArrayList<Double> bucketSort = new ArrayList<>();

        double[][] randomYAxis = new double[3][10];
        System.out.println("FOR RANDOM DATA:");
        listingTime(data, inputSizes, selectionSort, quickSort, bucketSort, randomYAxis);
        Main.showAndSaveChart("Test on Random Data", inputSizes, randomYAxis);

        //sorted input
        selectionSort.clear();
        quickSort.clear();
        bucketSort.clear();
        Arrays.sort(data);
        double[][] sortedYAxis = new double[3][10];
        System.out.println("FOR SORTED DATA:");
        listingTime(data, inputSizes, selectionSort, quickSort, bucketSort, sortedYAxis);
        Main.showAndSaveChart("Test on Sorted Data", inputSizes, sortedYAxis);

        //reverse sorted input
        selectionSort.clear();
        quickSort.clear();
        bucketSort.clear();
        reverse(data);
        double[][] reverseSortedYAxis = new double[3][10];
        System.out.println("FOR REVERSE SORTED DATA:");
        listingTime(data, inputSizes, selectionSort, quickSort, bucketSort, reverseSortedYAxis);
        Main.showAndSaveChart("Test on Reverse Sorted Data", inputSizes, reverseSortedYAxis);
    }
}
