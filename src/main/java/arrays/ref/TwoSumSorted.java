package arrays.ref;

import java.util.Arrays;

/**
 * Two pointers on sorted input, O(n) time / O(1) space — no hashmap needed because
 * sortedness lets you decide which pointer to move without searching.
 *
 * Critique:
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: negatives, two-element input (single possible pair), duplicate
 *   values (works because pointers move on sum comparison, not value equality).
 * - Throws on no-solution rather than returning a sentinel like {-1,-1} — matches the
 *   problem's "exactly one solution" guarantee; would need to change contract if that
 *   guarantee didn't hold (e.g. return Optional<int[]> or empty array).
 * - Contrast with the unsorted two-sum: that one needs a hashmap for O(n) since you
 *   can't binary-search/two-pointer without order; sortedness is what buys O(1) space here.
 */
public class TwoSumSorted {

    /** Returns 1-based indices. Assumes exactly one solution exists. */
    public static int[] twoSumSorted(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            if (sum == target) return new int[]{left + 1, right + 1};
            if (sum < target) left++; else right--;
        }
        throw new IllegalArgumentException("no solution for target " + target);
    }

    public static void main(String[] args) {
        check(twoSumSorted(new int[]{2, 7, 11, 15}, 9), new int[]{1, 2});
        check(twoSumSorted(new int[]{2, 3, 4}, 6), new int[]{1, 3});
        check(twoSumSorted(new int[]{-3, 0, 3, 4}, 0), new int[]{1, 3}); // negatives
        check(twoSumSorted(new int[]{1, 2}, 3), new int[]{1, 2});       // two-element input
        check(twoSumSorted(new int[]{1, 1, 2, 3}, 2), new int[]{1, 2}); // duplicates
        System.out.println("arrays.ref.TwoSumSorted: all checks passed");
    }

    private static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected)
                    + " but got " + Arrays.toString(actual));
        }
    }
}
