package arrays.ref;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Fixed window + monotonic deque of negative indices, O(n) time / O(k) space worst case
 * (all-negative window). Each index enters and leaves the deque at most once.
 *
 * Critique:
 * - Correctness hinge: the deque only ever needs a front-eviction check
 *   (`peekFirst() <= i - k`), not a full scan, because it's built in increasing index
 *   order — the front is always the oldest surviving negative, i.e. the leftmost one
 *   still in the window, which is exactly "the first negative in the window".
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: no negatives (all zeros out), all negatives (front of deque is
 *   always the answer), k == 1 (window is a single element, deque holds at most one index).
 * - Alternative: an O(n·k) rescan-per-window is simpler but quadratic; not viable at
 *   n=1e5 — this is the actual reason a deque is worth reaching for here.
 */
public class FirstNegativeInEachWindow {

    public static int[] firstNegativeEachWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> negIndices = new ArrayDeque<>(); // indices of negatives, oldest first

        for (int i = 0; i < n; i++) {
            if (nums[i] < 0) negIndices.addLast(i);

            if (i >= k - 1) {
                while (!negIndices.isEmpty() && negIndices.peekFirst() <= i - k) {
                    negIndices.pollFirst();
                }
                result[i - k + 1] = negIndices.isEmpty() ? 0 : nums[negIndices.peekFirst()];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        check(firstNegativeEachWindow(new int[]{-8, 2, 3, -6, 10}, 2),
                new int[]{-8, 0, -6, -6});
        check(firstNegativeEachWindow(new int[]{12, -1, -7, 8, -15, 30, 16, 28}, 3),
                new int[]{-1, -1, -7, -15, -15, 0});
        check(firstNegativeEachWindow(new int[]{1, 2, 3, 4}, 2),
                new int[]{0, 0, 0}); // no negatives at all
        check(firstNegativeEachWindow(new int[]{-1, -2, -3}, 2),
                new int[]{-1, -2}); // all negatives
        check(firstNegativeEachWindow(new int[]{1, -2, 3}, 1),
                new int[]{0, -2, 0}); // k == 1
        System.out.println("arrays.ref.FirstNegativeInEachWindow: all checks passed");
    }

    private static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected)
                    + " but got " + Arrays.toString(actual));
        }
    }
}
