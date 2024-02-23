public class SelectionSort {
    // this function finds and returns the index of the smallest member of the array
    public static int findMin (int[] numbers, int startIndex) {
        int minIndex = startIndex;

        for (int i = startIndex; i < numbers.length; i++) {
            if (numbers[i] < numbers[minIndex]) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    // this function uses the findMin function and detects the smallest element in the list.
    // then places the smallest element in the right place.
    public static void sort(int[] numbers, int size) {
        for (int i = 0; i < size; i++) {
            int minIndex = findMin(numbers, i);

            int temp = numbers[minIndex];
            numbers[minIndex] = numbers[i];
            numbers[i] = temp;
        }
    }
}
