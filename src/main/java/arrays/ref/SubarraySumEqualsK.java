package arrays.ref;

import java.util.HashMap;
import java.util.Map;

/**
 * Prefix-sum + hashmap, O(n) time / O(n) space.
 *
 * Critique:
 * - Why not sliding window (the trap this problem is designed to expose): with
 *   negative values, growing the window doesn't monotonically increase the sum, so
 *   there's no valid rule for when to shrink from the left — the window invariant
 *   that B1/B4/B5/B6 all depend on (sum/product only grows as you add elements)
 *   simply doesn't hold. Prefix sums sidestep this: subarray(i+1..j) sums to k iff
 *   prefix[j] - prefix[i] == k, which needs no monotonicity assumption at all.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: k == 0 with zeros present, all-negative-plus-one-positive
 *   values, single element equal to k.
 * - The `prefixCount.put(0, 1)` seed handles subarrays starting at index 0 — without
 *   it, a prefix sum that itself equals k would never be counted.
 */
public class SubarraySumEqualsK {

    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);
        int sum = 0, count = 0;
        for (int num : nums) {
            sum += num;
            count += prefixCount.getOrDefault(sum - k, 0);
            prefixCount.merge(sum, 1, Integer::sum);
        }
        return count;
    }

    public static void main(String[] args) {
        check(subarraySum(new int[]{1, 1, 1}, 2), 2);
        check(subarraySum(new int[]{1, 2, 3}, 3), 2);
        check(subarraySum(new int[]{1, -1, 0}, 0), 3);
        check(subarraySum(new int[]{-1, -1, 1}, 0), 1); // all negatives (+ one positive)
        check(subarraySum(new int[]{5}, 5), 1);          // single element equal to k
        System.out.println("arrays.ref.SubarraySumEqualsK: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
