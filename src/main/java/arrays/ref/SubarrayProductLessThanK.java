package arrays.ref;

/**
 * Variable window, O(n) time / O(1) space.
 *
 * Critique:
 * - Correctness hinge: `count += right - left + 1` counts every subarray ending at
 *   `right` and starting anywhere from `left` to `right` — all of them satisfy product
 *   < k because product only grows as elements are all >= 1, so if the widest such
 *   subarray qualifies, every narrower suffix of it does too.
 * - Depends on all values being positive (>= 1), same requirement as arrays.ref.MinSubArrayLen (B1)
 *   for the same reason: product/sum must be monotonic under insertion for the window
 *   to be shrinkable correctly.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: k <= 1 (short-circuits to 0, since every nums[i] >= 1 means no
 *   subarray can have product < 1), all-1s array, single element.
 */
public class SubarrayProductLessThanK {

    public static int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k <= 1) return 0; // every nums[i] >= 1, so no subarray can have product < 1
        long product = 1;
        int left = 0, count = 0;
        for (int right = 0; right < nums.length; right++) {
            product *= nums[right];
            while (product >= k) {
                product /= nums[left++];
            }
            count += right - left + 1;
        }
        return count;
    }

    public static void main(String[] args) {
        check(numSubarrayProductLessThanK(new int[]{10, 5, 2, 6}, 100), 8);
        check(numSubarrayProductLessThanK(new int[]{1, 2, 3}, 0), 0);
        check(numSubarrayProductLessThanK(new int[]{1, 2, 3}, 1), 0); // k <= 1
        check(numSubarrayProductLessThanK(new int[]{1, 1, 1}, 2), 6); // all 1s, k == 2
        check(numSubarrayProductLessThanK(new int[]{5}, 10), 1);      // single element
        System.out.println("arrays.ref.SubarrayProductLessThanK: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
