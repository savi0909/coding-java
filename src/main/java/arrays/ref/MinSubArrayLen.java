package arrays.ref;

/**
 * Variable window, O(n) time / O(1) space. Grows right pointer unconditionally, then
 * shrinks left as far as possible while the invariant (sum >= target) still holds.
 *
 * Critique:
 * - Correctness hinge: relies on all values being positive — that's what guarantees
 *   the window sum is monotonic as left/right move, so greedily shrinking from the
 *   left never skips a better answer. Breaks immediately with negative values (see
 *   arrays.ref.SubarraySumEqualsK, which needs prefix-sum + hashmap instead for that reason).
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: single element == target, single element < target (returns 0),
 *   whole array required.
 * - Amortized O(n): left never moves backward, so total shrink-steps across the whole
 *   run are bounded by n even though there's a nested while loop.
 */
public class MinSubArrayLen {

    public static int minSubArrayLen(int target, int[] nums) {
        int left = 0, sum = 0, best = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= nums[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    public static void main(String[] args) {
        check(minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}), 2);
        check(minSubArrayLen(4, new int[]{1, 4, 4}), 1);
        check(minSubArrayLen(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1}), 0);
        check(minSubArrayLen(5, new int[]{5}), 1);      // single element == target
        check(minSubArrayLen(6, new int[]{5}), 0);      // single element < target
        check(minSubArrayLen(15, new int[]{1, 2, 3, 4, 5}), 5); // whole array needed
        System.out.println("arrays.ref.MinSubArrayLen: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
