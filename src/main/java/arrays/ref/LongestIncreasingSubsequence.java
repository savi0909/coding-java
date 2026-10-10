package arrays.ref;

import java.util.Arrays;

/**
 * Longest strictly-increasing subsequence — length only (patience sorting / binary search).
 * tails[i] = smallest possible tail value of an increasing subsequence of length i+1
 * seen so far. tails is always sorted, so each element does one binary search to find
 * where it belongs: extend tails (new longest length) or replace the first tail >= it
 * (keeps future extensions easier without changing the length found so far).
 */
/**
 * Critique:
 * - Complexity: O(n log n) time (n elements, each one binary search over tails),
 *   O(n) space for tails.
 * - Concurrency: N/A — no shared mutable state, single-threaded pure function.
 * - Edge cases covered: empty array, single element, all duplicates (strict LIS ==
 *   1 since equal values don't extend), strictly decreasing input (LIS == 1),
 *   all equal to a non-decreasing run (still 1, since we require strict increase).
 * - What breaks at scale: none here — n log n is already the optimal known
 *   complexity for this problem; large n is fine as long as it fits in memory.
 * - This gives *length* only. Reconstructing an actual subsequence requires an
 *   extra parent-pointer array recorded alongside tails (not implemented here —
 *   ask for the "+ reconstruct" variant if needed).
 */
public class LongestIncreasingSubsequence {


    /**
     * Correct now that the maxLength/result comparison moved inside the loop (runs every
     * iteration instead of only at the break). See getStartIndexOptimized below for a
     * cleaner O(n)/O(1) rewrite without the found-flag indirection.
     */
    public int getStartIndex(int[] sale){
        if(sale.length <= 1) return -1;
        int result = -1;
        int currentResult = 0;
        int maxLength = 1;
        int currentMaxLength =1;
        for(int i = 0; i < sale.length-1; i++){
            if(sale[i] < sale[i+1]){
                currentMaxLength++;
            }
            else{
                currentMaxLength = 1;
                currentResult = i+1;
            }

            if(currentMaxLength > maxLength){
                maxLength = currentMaxLength;
                result = currentResult;
            }
        }

        return result;

    }

    /**
     * Better alternative: same O(n) time / O(1) space as the fixed getStartIndex, but
     * tracks "current run start + length" directly instead of a found-flag plus a
     * separate currentResult — one branch per element instead of two nested ones.
     * Ties: keeps the first run seen (strict '>' on the bestLen update), matching
     * getStartIndex's tie-breaking behavior.
     */
    public static int getStartIndexOptimized(int[] sale) {
        if (sale.length <= 1) return -1;
        int bestStart = -1, bestLen = 1;
        int curStart = 0, curLen = 1;
        for (int i = 1; i < sale.length; i++) {
            if (sale[i] > sale[i - 1]) {
                curLen++;
            } else {
                curStart = i;
                curLen = 1;
            }
            if (curLen > bestLen) {
                bestLen = curLen;
                bestStart = curStart;
            }
        }
        return bestStart;
    }

    public static int lengthOfLIS(int[] nums) {
        if (nums.length == 0) return 0;
        int[] tails = new int[nums.length];
        int size = 0;
        for (int num : nums) {
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (tails[mid] < num) lo = mid + 1;
                else hi = mid;
            }
            tails[lo] = num;
            if (lo == size) size++;
        }
        return size;
    }

    public static void main(String[] args) {
        check(lengthOfLIS(new int[]{}), 0); // empty
        check(lengthOfLIS(new int[]{7}), 1); // single element
        check(lengthOfLIS(new int[]{4, 4, 4, 4}), 1); // all duplicates
        check(lengthOfLIS(new int[]{5, 4, 3, 2, 1}), 1); // strictly decreasing
        check(lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}), 4);
        check(lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}), 4);
        check(lengthOfLIS(new int[]{1, 2, 3, 4, 5}), 5); // already increasing

        System.out.println("arrays.ref.LongestIncreasingSubsequence: all checks passed");

        LongestIncreasingSubsequence solver = new LongestIncreasingSubsequence();
        int[][] samples = {
                {1, 2, 3, 2, 3, 4, 5},
                {5, 4, 3, 2, 1},
                {7},
                {1, 2, 3, 4},
                {1, 2, 2, 3, 4, 1, 2}
        };
        for (int[] sample : samples) {
            int a = solver.getStartIndex(sample);
            int b = getStartIndexOptimized(sample);
            check(a, b);
            System.out.println("getStartIndex" + Arrays.toString(sample) + " = " + a
                    + "  (getStartIndexOptimized agrees: " + b + ")");
        }
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual + Arrays.toString(new int[]{actual, expected}));
        }
    }
}
