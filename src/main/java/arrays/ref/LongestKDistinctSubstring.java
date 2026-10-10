package arrays.ref;

import java.util.HashMap;
import java.util.Map;

/**
 * Variable window + frequency map, O(n) amortized time (each char enters/leaves the
 * map O(1) times), O(k) space for the map.
 *
 * Critique:
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: k == 0 (short-circuits to 0 before touching the string),
 *   k >= distinct count (window never needs to shrink, answer is full length),
 *   empty string (loop body never runs, returns 0).
 * - Same shape as arrays.ref.FruitIntoBaskets (B3), which is this problem with k hardcoded to 2 —
 *   worth recognizing that pattern reuse live rather than re-deriving from scratch.
 * - Alternative: a 128/26-slot int[] instead of HashMap<Character,Integer> avoids
 *   boxing and is faster in practice; skipped here since k can exceed 26 as a generic
 *   parameter and Unicode input isn't excluded by the constraints.
 */
public class LongestKDistinctSubstring {

    public static int longestKDistinct(String s, int k) {
        if (k == 0) return 0;
        Map<Character, Integer> freq = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            freq.merge(s.charAt(right), 1, Integer::sum);
            while (freq.size() > k) {
                char c = s.charAt(left);
                int remaining = freq.get(c) - 1;
                if (remaining == 0) freq.remove(c); else freq.put(c, remaining);
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(longestKDistinct("eceba", 2), 3);
        check(longestKDistinct("aa", 1), 2);
        check(longestKDistinct("abcadcacacaca", 3), 11);
        check(longestKDistinct("abc", 0), 0);
        check(longestKDistinct("abc", 5), 3); // k >= distinct count -> full length
        check(longestKDistinct("", 2), 0);
        System.out.println("arrays.ref.LongestKDistinctSubstring: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
