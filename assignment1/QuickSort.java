public class QuickSort {
    public static void swap (int[] numbers, int firstElement, int secondElement) {
        int temp = numbers[firstElement];
        numbers[firstElement] = numbers [secondElement];
        numbers[secondElement] = temp;
    }

    // used to divide the array into two parts
    public static int partition (int[] numbers, int low, int high) {
        int pivot = numbers[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (numbers[j] <= pivot) {
                i++;
                swap (numbers, i, j);
            }
        }
        swap (numbers, i+1, high);
        return i+1;
    }
    public static void sort (int[] numbers, int high, int low) {
        int stackSize = high - low + 1;
        int[] stack = new int[stackSize];

        int top = -1;

        stack[++top] = low;
        stack[++top] = high;

        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];
            int pivot = partition(numbers, low, high);

            if (pivot - 1 > low) {
                stack[++top] = low;
                stack[++top] = pivot - 1;
            }
            if (pivot + 1 < high) {
                stack[++top] = pivot + 1;
                stack[++top] = high;
            }
        }
    }
}