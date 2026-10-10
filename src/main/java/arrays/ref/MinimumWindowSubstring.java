package arrays.ref;

/**
 * Variable window + match-count tracking (same "satisfied" pattern as
 * arrays.ref.FindAnagramIndices/A2), O(n + m) time (m = |t|), O(1) space (128-slot arrays).
 *
 * Critique:
 * - Correctness hinge for duplicates: `window[c] == need[c]` only fires `satisfied++`
 *   the moment a letter's count first reaches what's needed — extra occurrences beyond
 *   that don't re-trigger it, and shrinking only decrements `satisfied` once the count
 *   drops *below* need. That's what makes "AABC"-style duplicate requirements in t work
 *   correctly instead of over- or under-counting matches.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: duplicate chars in t (forces the window to span every
 *   occurrence of the scarce letter — see the ADOBECODEBANC/AABC test), t longer than s
 *   (short-circuits to ""), answer is the whole string.
 * - This is the "hard" closer for a reason: it's B7 in the practice set specifically
 *   because it combines A2's match-counting with B1's shrink-while-valid loop — good
 *   synthesis check that both patterns actually clicked.
 */
public class MinimumWindowSubstring {

    public static String minWindow(String s, String t) {
        if (t.isEmpty() || s.length() < t.length()) return "";

        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int required = 0;
        for (int c : need) if (c > 0) required++;

        int[] window = new int[128];
        int satisfied = 0;
        int left = 0, bestLen = Integer.MAX_VALUE, bestStart = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            window[c]++;
            if (window[c] == need[c]) satisfied++;

            while (satisfied == required) {
                if (right - left + 1 < bestLen) {
                    bestLen = right - left + 1;
                    bestStart = left;
                }
                char lc = s.charAt(left);
                window[lc]--;
                if (window[lc] < need[lc]) satisfied--;
                left++;
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestStart, bestStart + bestLen);
    }

    public static void main(String[] args) {
        check(minWindow("ADOBECODEBANC", "ABC"), "BANC");
        check(minWindow("a", "a"), "a");
        check(minWindow("a", "aa"), "");
        check(minWindow("aa", "aa"), "aa");                 // answer is the whole string
        // duplicate chars in t: only two 'A's exist (idx 0 and 10), so any valid window
        // must span both -> forced to s.substring(0, 11).
        check(minWindow("ADOBECODEBANC", "AABC"), "ADOBECODEBA");
        check(minWindow("a", "b"), "");                     // t longer/absent
        System.out.println("arrays.ref.MinimumWindowSubstring: all checks passed");
    }

    private static void check(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("expected \"" + expected + "\" but got \"" + actual + "\"");
        }
    }
}
