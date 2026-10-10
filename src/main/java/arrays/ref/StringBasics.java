package arrays.ref;

import java.util.Objects;

/*
 * CRITIQUE
 * - Time/space: O(n) in name.length() for trim()/chars()/allMatch/concat, O(n) space
 *   for the trimmed copy + result string. No regex compilation/backtracking cost now
 *   (was matches() before) — allMatch also short-circuits on the first bad char instead
 *   of scanning the whole string.
 * - Concurrency: N/A, no shared/mutable state.
 * - Edge cases covered: null -> guard clause; empty/whitespace-only -> "" after trim;
 *   pure-symbol input ("!!!", "   ") -> fails char check; unicode letters (e.g. "María")
 *   PASS since Character.isLetterOrDigit is Unicode-aware; spaces are a valid char, so
 *   "John Doe" and "John  Doe" (double space) both PASS — the function only trims outer
 *   whitespace and validates characters, it does not normalize/collapse internal
 *   whitespace, so whatever survives trim() is echoed back exactly as given. NOT
 *   covered: chars() streams UTF-16 code units, not code points, so characters outside
 *   the BMP (surrogate pairs) would be checked half at a time and likely fail —
 *   irrelevant for real names. Still rejects hyphens/apostrophes ("O'Brien") — decide
 *   if that's intended.
 * - At scale: irrelevant here (single string, no I/O) — this isn't a scale problem.
 */
class StringBasics {
    // Complete this function to return either
    // "Hello, [name]!" or "Hello there!"
    // based on the input

    /*
     * CRITIQUE
     * - Time/space: O(n) time, O(n) space for the new StringBuilder/String — can't
     *   reverse a String in place since Java strings are immutable.
     * - Concurrency: N/A, no shared/mutable state.
     * - Edge cases: null -> "" (silent default, mirrors sayHello's null-guard style
     *   rather than throwing — debatable: a reverse() utility swallowing null into ""
     *   can mask a caller bug that IllegalArgumentException would surface). Empty ->
     *   "" (no-op, correct). Single char -> unchanged (correct). Surrogate pairs
     *   (e.g. emoji) are handled correctly — StringBuilder.reverse() is documented to
     *   keep high/low surrogate order intact rather than reversing them individually,
     *   unlike the chars()-based check in sayHello above which does NOT have that
     *   guarantee.
     * - At scale: irrelevant — single string, no I/O.
     * - Alternative: none needed; StringBuilder.reverse() is the standard idiom and
     *   already handles the one subtlety (surrogate pairs) that a hand-rolled
     *   two-pointer swap would need to special-case.
     */
    public static String reverse(String str) {
        if (str == null) return "";
        return new StringBuilder(str).reverse().toString();
    }


    public static String sayHello(String name) {
        if (Objects.isNull(name)) {
            return "Hello there!";
        }

        String trimmed = name.trim();
        if (trimmed.isEmpty() || !trimmed.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == ' ')) {
            return "Hello there!";
        }

        return "Hello, " + trimmed + "!";
    }

}