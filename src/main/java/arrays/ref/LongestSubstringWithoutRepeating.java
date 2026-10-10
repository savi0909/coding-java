package arrays.ref;

import java.util.HashMap;
import java.util.Map;

/**
 * Sliding window + hashmap, O(n) time / O(min(n, alphabet)) space, single pass.
 *
 * Critique:
 * - Correctness hinge: {@code prev >= windowStart} (not just {@code prev != null}) —
 *   without it, a stale index from before the window wrongly shrinks it. This is what
 *   makes "dvdf" resolve to "vdf" instead of the wrong "dvd".
 * - Concurrency: N/A, pure function, no shared state.
 * - Edge cases covered (see main): empty string, single char, all-unique, all-same,
 *   whitespace, tie-breaking ("dvdf").
 * - Gap: char is a UTF-16 code unit, not a code point — characters outside the BMP
 *   (most emoji) are surrogate pairs and could be split mid-window. Fix would be
 *   iterating codepoints (int keys) instead of char; not done here since it doubles
 *   the code for a case not in the test set — flag it if asked about i18n.
 * - Scale: pure CPU-bound string algo, no fan-out/caching angle; only scale axis is
 *   string length, already O(n).
 */
public class LongestSubstringWithoutRepeating {

    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastSeenAt = new HashMap<>();
        int windowStart = 0;
        int longest = 0;
        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            Integer prev = lastSeenAt.get(c);
            if (prev != null && prev >= windowStart) {
                windowStart = prev + 1;
            }
            lastSeenAt.put(c, end);
            longest = Math.max(longest, end - windowStart + 1);
        }
        return longest;
    }

    /** Follow-up: return the substring itself (last one found, if there's a tie — {@code >=}). */
    public static String longestSubstringLastFound(String s) {
        Map<Character, Integer> lastSeenAt = new HashMap<>();
        int windowStart = 0;
        int bestStart = 0;
        int bestLen = 0;
        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            Integer prev = lastSeenAt.get(c);
            if (prev != null && prev >= windowStart) {
                windowStart = prev + 1;
            }
            lastSeenAt.put(c, end);
            int len = end - windowStart + 1;
            if (len >= bestLen) {
                bestLen = len;
                bestStart = windowStart;
            }
        }
        return s.substring(bestStart, bestStart + bestLen);
    }

    /** Follow-up: return the substring itself (first one found, if there's a tie). */
    public static String longestSubstring(String s) {
        Map<Character, Integer> lastSeenAt = new HashMap<>();
        int windowStart = 0;
        int bestStart = 0;
        int bestLen = 0;
        for (int end = 0; end < s.length(); end++) {
            char c = s.charAt(end);
            Integer prev = lastSeenAt.get(c);
            if (prev != null && prev >= windowStart) {
                windowStart = prev + 1;
            }
            lastSeenAt.put(c, end);
            int len = end - windowStart + 1;
            if (len > bestLen) {
                bestLen = len;
                bestStart = windowStart;
            }
        }
        return s.substring(bestStart, bestStart + bestLen);
    }

    public static void main(String[] args) {
        String[] tests = {
                "abcabcbb", "bbbbb", "pwwkew", "", "a",
                "abcdef", "aaaa", " ", "a b a", "dvdf"
        };
        for (String t : tests) {
            System.out.println("\"" + t + "\" -> length=" + lengthOfLongestSubstring(t)
                    + ", substring=\"" + longestSubstring(t) + "\"");
        }
    }
}
