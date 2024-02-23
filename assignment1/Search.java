import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Search {
    public static int binarySearch(int[] numbers, int value) {
        int low = 0;
        int high = numbers.length - 1;

        while (high - low > 0) {
            int mid = (high + low) / 2;

            if (numbers[mid] == value)
                return mid;

            else if (numbers[mid] < value)
                low = mid + 1;

            else
                high = mid - 1;
        }
        return - 1; //not found
    }

    public static int linearSearch(int[] numbers, int value, int size) {
        for (int i = 0; i < size; i++) {
            if (value == numbers[i]) {
                return i;
            }
        }
        return -1; //not found
    }

    // the following 2 functions calculate the average time spent for sorting on the input size given for different searching algorithms
    public static void calculationForBinarySearch(int[] data, int inputSize, ArrayList<Double> times) {
        double average = 0;
        for (int i = 0; i < 1000; i++) {
            // to randomly select the number to search
            Random random = new Random();
            int randomIndex = random.nextInt(inputSize);

            long start = System.nanoTime();
            binarySearch(data, data[randomIndex]);
            long end = System.nanoTime();
            double duration = (end - start);
            average += duration;
        }
        times.add(average / 1000);

        System.out.println("binary search " + inputSize + " = " + average);
    }

    public static void calculationForLinearSearch(int[] data, int inputSize, ArrayList<Double> times) {
        double average = 0;
        for (int i = 0; i < 1000; i++) {
            // to randomly select the number to search
            Random random = new Random();
            int randomIndex = random.nextInt(inputSize);

            long start = System.nanoTime();
            linearSearch(data, data[randomIndex],inputSize);
            long end = System.nanoTime();
            double duration = (end - start);
            average += duration;
        }
        times.add(average / 1000);

        System.out.println("linear search " + inputSize + " = " + average);
    }

    // used to calculate duration for each input size and also create a list of data to use when plotting the graph
    public static void listingTime (int[] data, int[] inputSizes, ArrayList<Double> durationForLinearSearchWithRandomData,
                                    ArrayList<Double> durationForLinearSearchWithSortedData, ArrayList<Double> durationForBinarySearch, double[][] yAxis) {
        System.out.println("LINEAR SEARCH WITH RANDOM DATA");
        for (int size : inputSizes)
            calculationForLinearSearch(data, size, durationForLinearSearchWithRandomData);
        System.out.println("-------------------------");

        Arrays.sort(data);

        System.out.println("LINEAR SEARCH WITH SORTED DATA");
        for (int size : inputSizes)
            calculationForLinearSearch(data, size, durationForLinearSearchWithSortedData);
        System.out.println("-------------------------");

        System.out.println("BINARY SEARCH WITH RANDOM DATA");
        for (int size : inputSizes)
            calculationForBinarySearch(data, size, durationForBinarySearch);

        for (int i = 0; i < 10; i++) {
            yAxis[0][i] = durationForLinearSearchWithRandomData.get(i);
            yAxis[1][i] = durationForLinearSearchWithSortedData.get(i);
            yAxis[2][i] = durationForBinarySearch.get(i);
        }
    }

    // plots related graphs with sorting algorithms
    public static void calculateDataForSearching(int[] data, int[] inputSizes) throws IOException {
        //random input
        ArrayList<Double> randomLinearSearch = new ArrayList<>();
        ArrayList<Double> sortedLinearSearch = new ArrayList<>();
        ArrayList<Double> binarySearch = new ArrayList<>();
        double[][] yAxis = new double[3][10];
        listingTime(data, inputSizes, randomLinearSearch, sortedLinearSearch, binarySearch, yAxis);
        Main.showAndSaveChart("Searching Test Analysis", inputSizes, yAxis);
    }
}
