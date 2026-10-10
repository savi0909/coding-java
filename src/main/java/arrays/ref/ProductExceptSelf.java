package arrays.ref;

import java.util.Arrays;

/**
 * Two passes (prefix products, then suffix products folded in-place), O(n) time,
 * O(1) extra space (output array doesn't count toward the "no extra space" bar).
 *
 * Critique:
 * - Correctness hinge: no division means each result[i] must be built as (product of
 *   everything before i) * (product of everything after i) rather than
 *   (total product) / nums[i] — division would also break on any zero in the array.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: exactly one zero (every index except the zero's own gets 0;
 *   the zero's own index gets the product of everything else), two-or-more zeros (all
 *   outputs 0, since every index's "everything else" now includes at least one zero),
 *   negatives (sign just falls out of the running product naturally).
 */
public class ProductExceptSelf {

    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= suffix;
            suffix *= nums[i];
        }
        return result;
    }

    public static void main(String[] args) {
        check(productExceptSelf(new int[]{1, 2, 3, 4}), new int[]{24, 12, 8, 6});
        check(productExceptSelf(new int[]{-1, 1, 0, -3, 3}), new int[]{0, 0, 9, 0, 0});
        check(productExceptSelf(new int[]{1, 0}), new int[]{0, 1});           // exactly one zero
        check(productExceptSelf(new int[]{0, 0, 2}), new int[]{0, 0, 0});     // two zeros
        check(productExceptSelf(new int[]{-2, 3}), new int[]{3, -2});         // negatives
        System.out.println("arrays.ref.ProductExceptSelf: all checks passed");
    }

    private static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected)
                    + " but got " + Arrays.toString(actual));
        }
    }
}
