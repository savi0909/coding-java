package arrays.sliding;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/*
 * CRITIQUE (maxIndexPerWindow)
 *
 * Problem: for every window of size k, return the INDEX of the window's maximum.
 * Output length is n-k+1 (one index per window), not n.
 *
 * Why this is not MaxSum with `max` swapped in:
 *   Sum is reversible — the element sliding out can be subtracted, so O(1) per step.
 *   Max is NOT reversible. If arr[i-k] was the max, subtracting it tells you nothing
 *   about the new max, and you'd have to rescan the window -> O(n*k). The monotonic
 *   deque is what buys back O(1) amortized.
 *
 * Invariant: the deque holds indices of the current window whose values are strictly
 * decreasing front->back. Anything smaller than a later-arriving element can never be
 * the max again (it is both smaller AND expires earlier), so it is safe to discard.
 * Therefore the front is always the answer for the current window.
 *
 * Complexity: O(n) time — each index is offered exactly once and polled at most once,
 * so the inner while loop is amortized O(1) despite looking quadratic. O(k) extra space
 * for the deque, plus O(n-k+1) for the result.
 *
 * Edge cases an interviewer probes:
 *   - null / empty / k <= 0 / k > length -> returns int[0]. Chosen over MaxSum's {-1,-1}
 *     sentinel because "no windows exist" is naturally the empty result, not a magic value.
 *     Still an unstated contract — an interviewer may prefer IllegalArgumentException for
 *     k > length (caller error) while keeping empty for a genuinely empty array.
 *   - k == 1 -> every index is its own window max; deque never holds more than one entry.
 *   - k == arr.length -> single window, one output.
 *   - ties -> pop uses STRICT `<`, so an equal-valued earlier index is never evicted and
 *     stays ahead in the deque. Result: the EARLIEST max index wins, matching MaxSum's
 *     tie rule. Flipping to `<=` would give the latest index — say which one you chose.
 *   - all negatives / all equal -> fine; no sentinel like 0 or Integer.MIN_VALUE is seeded.
 *   - duplicates of the max -> only the earliest survives in the deque prefix, so no
 *     double-reporting.
 *   - overflow -> none. Only comparisons on values and index arithmetic; nothing accumulates.
 *
 * Concurrency: pure function over a caller-owned array; the deque is a local, so it is
 * thread-confined. Safe from multiple threads as long as no one mutates arr concurrently
 * (no defensive copy). ArrayDeque is deliberately non-thread-safe and non-synchronized —
 * correct choice here, and faster than LinkedList (contiguous, no node allocation).
 *
 * At scale: for a true stream you cannot random-access arr[i-k], but this algorithm never
 * needs to — it only needs the value at the indices it stored. Keep the k-element window in
 * a ring buffer alongside the deque, or store (index, value) pairs in the deque; memory
 * stays O(k) regardless of stream length. Sharding across windows is trivially parallel
 * since windows overlap by at most k-1 (split at boundaries, recompute the seam).
 *
 * Better alternative: the block/prefix-suffix-max method (partition into blocks of k, take
 * prefix max within blocks and suffix max backwards, then answer each window with one
 * comparison). Same O(n) but two flat array passes and zero allocation per element —
 * better cache behaviour. Cost: two O(n) scratch arrays instead of O(k), and it is far less
 * obvious to a reader. The deque is the right default; mention the other if asked to squeeze.
 */
public class MaxIndexPerWindow {

    public static void main(String[] args) {

        // windows: [2,1,5] [1,5,1] [5,1,3] [1,3,2] -> 5@2, 5@2, 5@2, 3@4
        test(new int[]{2, 1, 5, 1, 3, 2}, 3, new int[]{2, 2, 2, 4});
        test(new int[]{5, 1, 1, 1}, 1, new int[]{0, 1, 2, 3});      // k = 1
        test(new int[]{1, 2, 3}, 3, new int[]{2});                  // k == length
        test(new int[]{-4, -1, -5, -2}, 2, new int[]{1, 1, 3});     // all negative
        test(new int[]{4, 4, 4}, 2, new int[]{0, 1});               // ties -> earliest index
        test(new int[]{9, 1, 1, 1, 1}, 3, new int[]{0, 1, 2});      // max expires out of window
        test(new int[]{1, 2, 3, 4, 5}, 2, new int[]{1, 2, 3, 4});   // strictly increasing
        test(new int[]{5, 4, 3, 2, 1}, 2, new int[]{0, 1, 2, 3});   // strictly decreasing
        test(new int[]{}, 2, new int[]{});                          // empty
        test(null, 2, new int[]{});                                 // null
        test(new int[]{1, 2}, 0, new int[]{});                      // k <= 0
        test(new int[]{1, 2}, 5, new int[]{});                      // k > length
    }

    static void test(int[] arr, int k, int[] expected) {

        int[] actual = maxIndexPerWindow(arr, k);

        if (Arrays.equals(actual, expected)) {
            System.out.println("PASS  " + Arrays.toString(arr) + " k=" + k
                    + " -> " + Arrays.toString(actual));
        } else {
            System.out.println("FAIL  " + Arrays.toString(arr) + " k=" + k);
            System.out.println("Expected : " + Arrays.toString(expected));
            System.out.println("Actual   : " + Arrays.toString(actual));
        }
    }

    /*
        1. Slide a window of size k across arr.
        2. For each window, record the INDEX of its maximum element.
        3. On ties, keep the earliest index.
     */
    static int[] maxIndexPerWindow(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return new int[0];
        }

        int[] result = new int[arr.length - k + 1];
        // indices only; values are decreasing front -> back
        Deque<Integer> window = new ArrayDeque<>();

        for (int i = 0; i < arr.length; i++) {

            // evict the front once it falls out of [i-k+1, i]
            if (!window.isEmpty() && window.peekFirst() <= i - k) {
                window.pollFirst();
            }

            // anything smaller than arr[i] is dominated: smaller value AND expires sooner.
            // strict `<` keeps an equal earlier index -> earliest-index tie rule
            while (!window.isEmpty() && arr[window.peekLast()] < arr[i]) {
                window.pollLast();
            }

            window.offerLast(i);

            // first full window ends at index k-1
            if (i >= k - 1) {
                result[i - k + 1] = window.peekFirst();
            }
        }

        return result;
    }
}
