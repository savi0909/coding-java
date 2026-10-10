package arrays.ref;

import java.util.ArrayList;
import java.util.List;

/**
 * Fixed window (size |p|) + 26-slot count array, O(n) time / O(1) space (26 is constant).
 *
 * Critique:
 * - Correctness hinge: `adjust()` tracks a `satisfied` count of letters whose window
 *   count exactly equals the need count, rather than doing an O(26) Arrays.equals
 *   comparison every step — that's what keeps this truly O(n) instead of O(26n). This
 *   is the same "match-count" pattern reused in arrays.ref.MinimumWindowSubstring (B7).
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: p longer than s (empty result), s == p (single match), heavy
 *   repeats ("aaaaa" vs "aa" — every offset is a valid anagram start).
 * - Alternative: naive Arrays.equals(window, need) per step is simpler to read but
 *   O(26n) — fine at these constraints (3e4), not fine if the alphabet were large
 *   (e.g. full Unicode), where the match-count trick actually matters.
 */
public class FindAnagramIndices {

    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int n = s.length(), k = p.length();
        if (k > n) return result;

        int[] need = new int[26];
        for (char c : p.toCharArray()) need[c - 'a']++;
        int distinctNeeded = 0;
        for (int c : need) if (c > 0) distinctNeeded++;

        int[] window = new int[26];
        int satisfied = 0;

        for (int i = 0; i < n; i++) {
            int inIdx = s.charAt(i) - 'a';
            satisfied = adjust(window, need, inIdx, 1, satisfied);

            if (i >= k) {
                int outIdx = s.charAt(i - k) - 'a';
                satisfied = adjust(window, need, outIdx, -1, satisfied);
            }

            if (i >= k - 1 && satisfied == distinctNeeded) {
                result.add(i - k + 1);
            }
        }
        return result;
    }

    /** Applies delta to window[idx] and returns the updated satisfied-letters count. */
    private static int adjust(int[] window, int[] need, int idx, int delta, int satisfied) {
        boolean wasMatch = window[idx] == need[idx];
        window[idx] += delta;
        boolean isMatch = window[idx] == need[idx];
        if (wasMatch && !isMatch) satisfied--;
        if (isMatch && !wasMatch) satisfied++;
        return satisfied;
    }

    public static void main(String[] args) {
        check(findAnagrams("cbaebabacd", "abc"), List.of(0, 6));
        check(findAnagrams("abab", "ab"), List.of(0, 1, 2));
        check(findAnagrams("a", "ab"), List.of());          // p longer than s
        check(findAnagrams("abc", "abc"), List.of(0));       // s == p
        check(findAnagrams("aaaaa", "aa"), List.of(0, 1, 2, 3)); // heavy repeats
        System.out.println("arrays.ref.FindAnagramIndices: all checks passed");
    }

    private static void check(List<Integer> actual, List<Integer> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
