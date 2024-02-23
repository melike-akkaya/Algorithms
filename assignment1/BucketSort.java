import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BucketSort {
    public static void sort(int[] numbers) {
        int numberOfBuckets = (int) Math.ceil(Math.sqrt(numbers.length));

        List<Integer>[] buckets = new List[numberOfBuckets];

        for (int i = 0; i < numberOfBuckets; i++) {
            buckets[i] = new ArrayList<>();
        }

        int max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }

        for (int k : numbers) {
            int bucketIndex = hash(k, max, numberOfBuckets);
            buckets[bucketIndex].add(k);
        }

        for (int i = 0; i < numberOfBuckets; i++) {
            Collections.sort(buckets[i]);
        }

        int index = 0;
        for (int i = 0; i < numberOfBuckets; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                numbers[index++] = buckets[i].get(j);
            }
        }
    }

    public static int hash(int i, int max, int numberOfBuckets) {
        return (int) Math.floor(i * 1.0 / max * (numberOfBuckets - 1));
    }
}
