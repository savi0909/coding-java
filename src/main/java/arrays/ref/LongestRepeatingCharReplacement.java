package arrays.ref;

/**
 * Variable window with a "replacement budget" of k, O(n) time / O(1) space (26-slot array).
 *
 * Critique:
 * - Correctness hinge (see inline comment): `maxFreqInWindow` is allowed to go stale
 *   after a shrink. This works because the window's length is monotonically
 *   non-decreasing across the whole run — once `best` records a length, the window
 *   never needs to produce anything shorter again, so an overestimated maxFreq just
 *   delays a shrink that wouldn't have improved `best` anyway. This is the single
 *   trickiest correctness point in the whole practice set — easy to "fix" into a wrong,
 *   slower solution by resetting maxFreq on every shrink.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: k == 0 (longest existing same-char run), single distinct char,
 *   k >= s.length (whole string).
 */
public class LongestRepeatingCharReplacement {

    public static int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0, maxFreqInWindow = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            int idx = s.charAt(right) - 'A';
            freq[idx]++;
            maxFreqInWindow = Math.max(maxFreqInWindow, freq[idx]);
            // window only ever grows or holds steady: shrinking would just re-derive
            // a size we've already recorded in `best`, so a stale maxFreqInWindow is fine.
            while ((right - left + 1) - maxFreqInWindow > k) {
                freq[s.charAt(left) - 'A']--;
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(characterReplacement("ABAB", 2), 4);
        check(characterReplacement("AABABBA", 1), 4);
        check(characterReplacement("ABCDE", 0), 1);   // k == 0 -> longest existing run
        check(characterReplacement("AAAA", 2), 4);    // single distinct char
        check(characterReplacement("ABCDE", 10), 5);  // k >= s.length
        System.out.println("arrays.ref.LongestRepeatingCharReplacement: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
