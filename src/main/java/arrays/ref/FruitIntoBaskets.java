package arrays.ref;

import java.util.HashMap;
import java.util.Map;

/**
 * Same algorithm as arrays.ref.LongestKDistinctSubstring (B2) with k fixed at 2 — the "baskets"
 * framing is the interviewer testing whether you recognize the shape under new words.
 * O(n) time, O(1) space (map never holds more than 3 entries).
 *
 * Critique:
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: all-same value (never shrinks, answer is full length),
 *   strictly increasing (shrinks every step, answer is always 2), length-1 array.
 * - Alternative: since the cap is a fixed 2, two plain int variables (last-seen value +
 *   its run length, second-to-last value + its run length) replace the HashMap entirely
 *   and avoid boxing — a fair follow-up if asked to micro-optimize a k==2-specific case.
 */
public class FruitIntoBaskets {

    public static int totalFruit(int[] fruits) {
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < fruits.length; right++) {
            freq.merge(fruits[right], 1, Integer::sum);
            while (freq.size() > 2) {
                int f = fruits[left];
                int remaining = freq.get(f) - 1;
                if (remaining == 0) freq.remove(f); else freq.put(f, remaining);
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(totalFruit(new int[]{1, 2, 1}), 3);
        check(totalFruit(new int[]{0, 1, 2, 2}), 3);
        check(totalFruit(new int[]{1, 2, 3, 2, 2}), 4);
        check(totalFruit(new int[]{5, 5, 5, 5}), 4);       // all same value
        check(totalFruit(new int[]{1, 2, 3, 4, 5}), 2);    // strictly increasing
        check(totalFruit(new int[]{7}), 1);                // length 1
        System.out.println("arrays.ref.FruitIntoBaskets: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
