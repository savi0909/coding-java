package arrays.ref;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * D3. Top K frequent elements. int[] topKFrequent(int[] nums, int k)
 * [1,1,1,2,2,3], k=2 -> [1,2] (order within the result doesn't matter in general;
 * these test cases are chosen to have no frequency ties, so output order is deterministic here).
 */
/**
 * Critique:
 * - Complexity: O(n) to build the frequency map + O(d log k) for the heap (d = distinct
 *   values, d <= n), so O(n log k) overall, O(d) space.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: single element, k == distinct count (heap ends up holding
 *   every entry). Ties in frequency are deliberately excluded from the test cases —
 *   the problem allows any order among tied frequencies, so a hardcoded exact-array
 *   assertion would be flaky; `check()` here compares sorted arrays to stay
 *   order-independent regardless.
 * - Alternative (better complexity): bucket sort by frequency (bucket index = count,
 *   0..n) gets true O(n) — no log factor — since frequency is bounded by n. Worth
 *   naming as the "if you need it truly linear" answer; skipped here since O(n log k)
 *   is simpler to write correctly under interview pressure and k is usually << n.
 */
public class TopKFrequentElements {

    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);

        PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        for (Map.Entry<Integer, Integer> e : freq.entrySet()) {
            minHeap.offer(e);
            if (minHeap.size() > k) minHeap.poll();
        }

        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll().getKey();
        }
        return result;
    }

    public static void main(String[] args) {
        check(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2), new int[]{1, 2});
        check(topKFrequent(new int[]{1}, 1), new int[]{1});
        check(topKFrequent(new int[]{1, 2, 3}, 3), new int[]{3, 2, 1}); // k == distinct count
        System.out.println("arrays.ref.TopKFrequentElements: all checks passed");
    }

    private static void check(int[] actual, int[] expected) {
        int[] sortedActual = actual.clone();
        int[] sortedExpected = expected.clone();
        Arrays.sort(sortedActual);
        Arrays.sort(sortedExpected);
        if (!Arrays.equals(sortedActual, sortedExpected)) {
            throw new AssertionError("expected " + Arrays.toString(expected)
                    + " but got " + Arrays.toString(actual));
        }
    }
}
