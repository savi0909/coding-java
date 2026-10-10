package arrays.ref;

/**
 * Variable window tracking a zero-budget, O(n) time / O(1) space.
 *
 * Critique:
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: k == 0 (degenerates to "longest existing run of 1s"),
 *   k >= zero count (whole array qualifies), all zeros with k == 0 (answer 0).
 * - Same window-with-a-budget shape as arrays.ref.LongestRepeatingCharReplacement (B5) — the
 *   "budget" is zeros-to-flip here vs characters-to-replace there.
 * - No stronger alternative: O(n) is optimal since every element must be inspected.
 */
public class MaxConsecutiveOnesIII {

    public static int longestOnes(int[] nums, int k) {
        int left = 0, zeros = 0, best = 0;
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) zeros++;
            while (zeros > k) {
                if (nums[left] == 0) zeros--;
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(longestOnes(new int[]{1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0}, 2), 6);
        check(longestOnes(new int[]{0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1}, 3), 10);
        check(longestOnes(new int[]{1, 0, 1, 0, 1}, 0), 1); // k == 0 -> longest existing run
        check(longestOnes(new int[]{0, 0, 0}, 3), 3);       // k >= zero count -> whole array
        check(longestOnes(new int[]{0, 0, 0}, 0), 0);       // all zeros, no flips allowed
        System.out.println("arrays.ref.MaxConsecutiveOnesIII: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
