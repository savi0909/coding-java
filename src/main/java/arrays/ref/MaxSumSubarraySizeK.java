package arrays.ref;

/**
 * Fixed-window sum, O(n) time / O(1) space, single pass — slide by subtracting the
 * element leaving and adding the one entering, never re-summing the whole window.
 *
 * Critique:
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: k == nums.length (one window, no slide), all-negative values
 *   (works fine since we track running sum, not a max-so-far-must-be-positive heuristic),
 *   single element.
 * - No better asymptotic alternative exists for a fixed window — O(n) is optimal since
 *   every element must be read at least once.
 */
public class MaxSumSubarraySizeK {

    public static int maxSubarraySum(int[] nums, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) windowSum += nums[i];
        int best = windowSum;
        for (int i = k; i < nums.length; i++) {
            windowSum += nums[i] - nums[i - k];
            best = Math.max(best, windowSum);
        }
        return best;
    }

    public static void main(String[] args) {
        check(maxSubarraySum(new int[]{2, 1, 5, 1, 3, 2}, 3), 9);
        check(maxSubarraySum(new int[]{2, 3, 4, 1, 5}, 2), 7);
        check(maxSubarraySum(new int[]{-4, -2, -7}, 2), -6);
        check(maxSubarraySum(new int[]{5}, 1), 5);
        check(maxSubarraySum(new int[]{1, 2, 3}, 3), 6); // k == nums.length
        System.out.println("arrays.ref.MaxSumSubarraySizeK: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
