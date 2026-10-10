package arrays.ref;

/**
 * Build each row via repeatCapitalized(c, count) — first occurrence uppercase,
 * remaining lowercase — appending "-" unconditionally after every row, then
 * trim the one trailing separator instead of branching on "is this the first
 * row". O(n^2) time / O(n^2) space (row i is length i+1, total output length
 * is triangular in n), StringBuilder throughout so no quadratic string-concat
 * blowup on top of that.
 *
 * Critique:
 * - Time/space: O(n^2) is inherent to the output size itself, not an
 *   inefficiency to fix — can't do better than the size of what you emit.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: null input, empty string (result stays length 0, the
 *   `result.length() > 0` guard skips setLength(-1) — without that guard an
 *   empty input throws StringIndexOutOfBoundsException), non-letter
 *   characters (digits, symbols, whitespace) -> all return "" per spec.
 *   Single-char input reduces to just the uppercase letter (row 0, zero
 *   extra copies). repeatCapitalized(c, 1) is the degenerate case: uppercase
 *   only, loop body never runs.
 * - What breaks at scale: not a real concern here, n is a short username-like
 *   string in every realistic use of this kata; the O(n^2) output length
 *   itself would be the limiting factor long before the loop logic.
 * - Alternative: could validate with a regex ("^[a-zA-Z]*$") instead of a
 *   manual scan — same complexity, marginally less code, but a manual loop
 *   short-circuits on the first bad char instead of building a Pattern.
 *   "append then trim" trades one StringBuilder.setLength() call for
 *   eliminating the per-iteration `if (i > 0)` branch — a wash on
 *   complexity, mostly a readability/taste call.
 */
public class Accum {

    public static String accum(String s) {
        if (s == null) {
            return "";
        }
        for (char c : s.toCharArray()) {
            if (!Character.isLetter(c)) {
                return "";
            }
        }

        StringBuilder result = new StringBuilder();
        int count = 0;
        for (char c : s.toCharArray()) {
            count++;
            result.append(repeatCapitalized(c, count)).append("-");
        }
        if (result.length() > 0) {
            result.setLength(result.length() - 1); // drop the trailing "-"
        }
        return result.toString();
    }

    // First occurrence uppercase, remaining (count - 1) occurrences lowercase.
    private static String repeatCapitalized(char c, int count) {
        StringBuilder row = new StringBuilder();
        row.append(Character.toUpperCase(c));
        for (int j = 1; j < count; j++) {
            row.append(Character.toLowerCase(c));
        }
        return row.toString();
    }

    public static void main(String[] args) {
        check(accum("abcd"), "A-Bb-Ccc-Dddd");
        check(accum("RqaEzty"), "R-Qq-Aaa-Eeee-Zzzzz-Tttttt-Yyyyyyy");
        check(accum("cwAt"), "C-Ww-Aaa-Tttt");
        check(accum(null), "");
        check(accum(""), "");
        check(accum("a1b"), "");       // digit -> invalid
        check(accum("a b"), "");       // whitespace -> invalid
        check(accum("a"), "A");        // single char
        System.out.println("arrays.ref.Accum: all checks passed");
    }

    private static void check(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("expected \"" + expected + "\" but got \"" + actual + "\"");
        }
    }
}
