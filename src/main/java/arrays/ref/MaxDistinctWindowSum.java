package arrays.ref;

import java.util.HashMap;
import java.util.Map;

/**
 * Fixed window + duplicate counter, O(n) time / O(k) space — synthesis of A1 (fixed
 * window sliding sum) and the distinctness-tracking idea from B2/B3.
 *
 * Critique:
 * - Correctness hinge: `duplicates` counts how many distinct values currently have
 *   freq >= 2 in the window, not how many *extra* occurrences exist — it only needs to
 *   answer "is this window fully distinct" (duplicates == 0), so it increments once
 *   when a value's count crosses 1->2 and decrements once when it drops back 2->1,
 *   ignoring any further repeats beyond that.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: no valid distinct window exists (best stays 0, matching the
 *   "return 0 if none qualify" spec), every window distinct, k == 1 (every window is
 *   trivially distinct).
 * - Returns `long` (not `int`) because window sums can exceed Integer.MAX_VALUE at the
 *   stated constraints (k, nums.length up to 1e5) — a detail worth flagging since the
 *   given signature already has it, but an int overflow here is the kind of bug that
 *   only shows up with large adversarial inputs.
 */
public class MaxDistinctWindowSum {

    public static long maxDistinctWindowSum(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        long windowSum = 0, best = 0;
        int duplicates = 0;

        for (int i = 0; i < nums.length; i++) {
            int in = nums[i];
            windowSum += in;
            int newCount = freq.merge(in, 1, Integer::sum);
            if (newCount == 2) duplicates++;

            if (i >= k) {
                int out = nums[i - k];
                windowSum -= out;
                int remaining = freq.merge(out, -1, Integer::sum);
                if (remaining == 0) freq.remove(out);
                if (remaining == 1) duplicates--;
            }

            if (i >= k - 1 && duplicates == 0) {
                best = Math.max(best, windowSum);
            }
        }
        return best;
    }

    public static void main(String[] args) {
        check(maxDistinctWindowSum(new int[]{1, 5, 4, 2, 9, 9, 9}, 3), 15);
        check(maxDistinctWindowSum(new int[]{4, 4, 4}, 3), 0);
        check(maxDistinctWindowSum(new int[]{1, 2, 3}, 5), 0); // no window of size k exists
        check(maxDistinctWindowSum(new int[]{1, 2, 3, 4}, 2), 7); // every window distinct
        check(maxDistinctWindowSum(new int[]{5, 5, 5}, 1), 5);    // k == 1
        System.out.println("arrays.ref.MaxDistinctWindowSum: all checks passed");
    }

    private static void check(long actual, long expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
