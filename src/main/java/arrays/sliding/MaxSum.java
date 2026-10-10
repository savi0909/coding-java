package arrays.sliding;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

/*
 * CRITIQUE (windowMax + test harness)
 *
 * Bug fixed: test() took `int expected` and compared it to the `int[]` returned by
 * windowMax with `==`. Two defects in one line:
 *   1. `int[] == int` is a compile error (incomparable types) — the file never built.
 *   2. Even with matching types, `==` on arrays compares references, not contents,
 *      so it would print FAIL for every correct answer. Use Arrays.equals.
 *   Printing `actual` directly would also have dumped `[I@1b6d3586`; use Arrays.toString.
 *
 * windowMax itself is correct: O(n) time, O(1) extra space (the returned int[2] aside).
 * Fixed-size sliding window — the prefix loop seeds the first window, then each step
 * does one subtract + one add instead of re-summing k elements (naive is O(n*k)).
 *
 * Edge cases an interviewer probes:
 *   - null / empty array, k <= 0, k > length  -> guarded, returns {-1,-1}. Sentinel is
 *     weak API design; Optional<int[]> or an exception states the contract better.
 *   - k == arr.length -> second loop never runs, returns the whole-array sum at start 0. OK.
 *   - all negatives -> works (maxSum seeded from the real first window, not 0).
 *   - ties -> `sum > maxSum` keeps the EARLIEST start index. That's the conventional
 *     choice but it is an unstated contract; say it out loud in an interview.
 *   - overflow -> k large ints near Integer.MAX_VALUE silently wrap. Use long for sum
 *     and maxSum if the range isn't bounded; costs nothing here.
 *
 * Concurrency: none — pure function over a caller-owned array. Safe to call from
 * multiple threads provided no one mutates arr concurrently (no defensive copy is made).
 *
 * At scale: streaming input that doesn't fit in memory breaks the random-access
 * `arr[i-k]` lookback. Standard fix: keep a k-element ring buffer (ArrayDeque) of the
 * live window and evict from the head — O(k) memory instead of O(n).
 *
 * Better alternative: returning int[2] is positional and untyped — the caller has to
 * remember [0]=sum, [1]=start. A `record Window(int sum, int start)` costs one extra
 * allocation but makes misuse impossible. Prefer it outside of hot loops.
 */
public class MaxSum {


    public static void main(String[] args) {

        test(new int[]{2, 1, 5, 1, 3, 2}, 3, new int[]{9, 2});
        test(new int[]{5, 1, 1, 1}, 1, new int[]{5, 0});          // k = 1
        test(new int[]{1, 2, 3}, 3, new int[]{6, 0});             // k == length
        test(new int[]{-4, -1, -5, -2}, 2, new int[]{-5, 0});     // all negative
        test(new int[]{1, 4, 1, 4}, 2, new int[]{5, 0});          // tie -> earliest start
        test(new int[]{}, 2, new int[]{-1, -1});                  // empty
        test(null, 2, new int[]{-1, -1});                         // null
        test(new int[]{1, 2}, 0, new int[]{-1, -1});              // k <= 0
        test(new int[]{1, 2}, 5, new int[]{-1, -1});              // k > length
    }

    static void test(int[] arr, int k, int[] expected) {

        int[] actual = windowMax(arr, k);

        if (Arrays.equals(actual, expected)) {
            System.out.println("PASS  " + Arrays.toString(arr) + " k=" + k
                    + " -> " + Arrays.toString(actual));
        } else {
            System.out.println("FAIL  " + Arrays.toString(arr) + " k=" + k);
            System.out.println("Expected : " + Arrays.toString(expected));
            System.out.println("Actual   : " + Arrays.toString(actual));
        }
    }


    static int[] maxInEveryWindow(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return new int[0];
        }

        int[] result = new int[arr.length - k + 1];

        Deque<Integer> deque = new LinkedList<>();

        // ------------------------------------------------
        // Build first window
        // ------------------------------------------------
        for (int i = 0; i < k; i++) {

            while (!deque.isEmpty() &&
                    arr[deque.peekLast()] <= arr[i]) {

                deque.pollLast();
            }

            deque.offerLast(i);
        }

        // ------------------------------------------------
        // Initialize answer
        // ------------------------------------------------
        result[0] = arr[deque.peekFirst()];

        int index = 1;

        // ------------------------------------------------
        // Slide window
        // ------------------------------------------------
        for (int i = k; i < arr.length; i++) {

            int windowStart = i - k + 1;

            // Remove expired indices
            while (!deque.isEmpty() &&
                    deque.peekFirst() < windowStart) {

                deque.pollFirst();
            }

            // Remove smaller elements
            while (!deque.isEmpty() &&
                    arr[deque.peekLast()] <= arr[i]) {

                deque.pollLast();
            }

            // Add new index
            deque.offerLast(i);

            // Update answer
            result[index++] = arr[deque.peekFirst()];
        }

        return result;
    }
    /*
        1. Find the max sum in the window size k within the array arr.
        2. Return the max sum.
        3. also return start index of the window with a max sum.
     */
    static int[] windowMax(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return new int[]{-1, -1};
        };
         int[] result = new int[2];
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        int maxSum = sum;
        int start = 0;
        for (int i = k; i < arr.length; i++) {
            sum = sum - arr[i - k] + arr[i];
            if (sum > maxSum) {
                maxSum = sum;
                start = i-k+1;

            }

        }

        result[0] = maxSum;
        result[1] = start;
        return result;
    }
}