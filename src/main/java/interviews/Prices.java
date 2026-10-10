package interviews;

import java.util.LinkedHashMap;
import java.util.Map;

public class Prices {

    public static void main(String[] args) {
         System.out.println(new Prices().solution("abacabad"));

     }

    /*
     * First non-repeating character (LC 387).
     *
     * THE PASS-2 COST IS THE WHOLE DESIGN DECISION. Both passes are "O(n)" on paper, but
     * they are not the same shape, and at n = 10,000,000 the difference dominates:
     *
     *   pass 1 - unavoidably touches all n characters, whatever the structure.
     *   pass 2 - only has to inspect DISTINCT characters (k <= 65536, k = 26 for lowercase
     *            ASCII). Any pass 2 that re-walks the string is doing up to n lookups to
     *            extract at most k facts.
     *
     * An intermediate version of this file re-walked the string in pass 2 with a HashMap
     * get per character. On a 10M-char string that is ~10M hash lookups plus ~10M
     * Character boxes to answer a question with 26 possible answers - a real regression,
     * not a wash. Insertion order is exactly what buys pass 2 back down to k, which is why
     * the LinkedHashMap here is load-bearing rather than decoration.
     *
     * The map value encodes both facts in one int - first index if seen once, -1 once seen
     * twice - so pass 2 reads entrySet() directly and performs ZERO map lookups, and the
     * surviving value IS the answer's index. `merge(c, i, (seen, dup) -> -1)` passes i as
     * the merge value, which also removes the `int finalI = i` capture the older `compute`
     * form needed.
     *
     * Complexity: O(n) pass 1 + O(k) pass 2, O(k) space. Beyond asymptotics, the constant
     * that matters is one hash + one box per character in pass 1 (see firstUniqueIndexFast
     * for the version that removes even that).
     *
     * Concurrency: pure function over an immutable String, all state method-local -> safe
     * from any number of threads. The streaming variant described below would NOT be: it
     * carries state across calls and needs external synchronization.
     *
     * Edge cases an interviewer would probe:
     *   - null / "" -> -1 ('_' via solution())
     *   - all repeating "aabb" -> -1
     *   - answer at the last index "aabbc" -> 'c'
     *   - single char "a" -> 'a'
     *   - index 0 is a valid answer, so pass 2 must test `!= -1`, never truthiness of the
     *     value; a `> 0` test would silently drop the answer for "abb".
     *   - THE CHAR SENTINEL IS LOSSY: solution("_") returns '_', indistinguishable from
     *     "not found". Any char sentinel has this flaw, which is why firstUniqueIndex()
     *     returning -1 is the real API and solution() is only the signature-preserving shim.
     *   - Boxed-Integer identity: `e.getValue() != -1` unboxes to an int compare. Comparing
     *     two boxed Integers with != would pass for values <= 127 and break past the Integer
     *     cache - i.e. it would work until the string got longer than 128 chars.
     *   - Unicode: charAt() walks UTF-16 code units, so an emoji (surrogate pair) counts as
     *     two "characters" and a lone occurrence returns an unpaired surrogate - garbage.
     *     Fix if it matters: iterate s.codePoints() into a Map<Integer,Integer>.
     *
     * What breaks at scale: for a String in memory, nothing - but see firstUniqueIndexFast,
     * because at 10M chars the per-character boxing in pass 1 becomes the bottleneck. The
     * design genuinely breaks when the input is a stream you cannot re-read AND cannot fully
     * buffer: then answer "first unique so far" online with a count map plus an ArrayDeque
     * of candidates, evicting from the head while the head's count > 1 - O(1) amortized per
     * character, queryable at any point.
     */
    char solution(String s) {
        int idx = firstUniqueIndex(s);
        return idx < 0 ? '_' : s.charAt(idx);
    }

    /**
     * Index of the first non-repeating character, or -1 if there is none.
     * Preferred over {@link #solution(String)} - -1 cannot collide with a valid answer.
     * <p>
     * Pass 2 iterates distinct characters (<= k), not the string, and does no map lookups.
     */
    int firstUniqueIndex(String s) {
        if (s == null || s.isEmpty()) return -1;

        // value = index of first sighting, or -1 once the character repeats.
        Map<Character, Integer> firstIndex = new LinkedHashMap<>();
        for (int i = 0; i < s.length(); i++) {
            firstIndex.merge(s.charAt(i), i, (seen, dup) -> -1);
        }
        // Insertion order == first-occurrence order, so the first survivor wins.
        for (Map.Entry<Character, Integer> e : firstIndex.entrySet()) {
            if (e.getValue() != -1) return e.getValue();
        }
        return -1;
    }

    /**
     * Same result with no hashing and no boxing: a flat frequency table indexed by the char
     * itself. Pass 1 is an array increment per character; pass 2 scans the 65536-entry table,
     * never the string, so its cost is fixed regardless of n.
     * <p>
     * Cost of the trade: a 256KB scratch array (65536 ints) allocated and zeroed per call.
     * That is noise against a 10M-char input but absurd for a 10-char one, so this is the
     * large-input specialisation, not the default. Narrow the table to {@code new int[128]}
     * (ASCII) or {@code c - 'a'} into {@code new int[26]} when the problem states the
     * alphabet - same idea, 512 bytes instead of 256KB.
     * <p>
     * Assumes BMP input: a supplementary character (emoji) is counted as its two surrogate
     * halves, same caveat as the map version.
     */
    int firstUniqueIndexFast(String s) {
        if (s == null || s.isEmpty()) return -1;

        int[] freq = new int[128];
        int[] firstIndex = new int[128];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (freq[c]++ == 0) firstIndex[c] = i;
        }

        int best = -1;
        for (char c = 'A'; c <= 'Z'; c++) {
            if (freq[c] == 1 && (best < 0 || firstIndex[c] < best)) best = firstIndex[c];
        }
        for (char c = 'a'; c <= 'z'; c++) {
            if (freq[c] == 1 && (best < 0 || firstIndex[c] < best)) best = firstIndex[c];
        }
        return best;
    }

    /*
     * Best time to buy & sell stock (single transaction).
     *
     * Invariant: after processing index i, minPrice is the cheapest price in [0..i]
     * and maxProfit is the best sell-at-j-buy-before-j profit over all j <= i.
     * Because a sale is optional, maxProfit is clamped at 0 and never negative.
     *
     * Complexity: O(n) time, O(1) space. Single pass, no allocation - already optimal;
     * you cannot beat O(n) since an unread element could be the global minimum.
     *
     * Concurrency: pure function over a caller-owned array. Thread-safe only if the
     * caller does not mutate `prices` during the call - there is no defensive copy,
     * and a concurrent write mid-scan yields a torn (non-serializable) result.
     *
     * Edge cases an interviewer probes:
     *   - null / length 0 / length 1        -> 0 (no transaction possible)
     *   - strictly descending [5,4,3,2,1]   -> 0, NOT a negative number
     *   - all equal [3,3,3]                 -> 0
     *   - buy and sell must be distinct days: profit is computed against the min
     *     seen BEFORE the current index, so same-day buy/sell can never win
     *     (price - price == 0, which never exceeds a maxProfit already >= 0).
     *   - overflow: `price - minPrice` can wrap only if prices span more than
     *     Integer.MAX_VALUE (i.e. negative prices). Real prices are >= 0, so it is
     *     safe; if negatives are allowed, widen the subtraction to long.
     *
     * What breaks at scale: nothing here - it is already streaming/online, O(1) state,
     * so it runs unchanged over a live tick feed without buffering history.
     *
     * Better alternative: none for this variant. Note the equivalence though - this is
     * Kadane's max-subarray over the delta array (prices[i] - prices[i-1]); that framing
     * costs an extra conceptual step but generalises cleanly to the k-transaction DP
     * (LC 123/188). For unlimited transactions (LC 122) the answer is instead the sum of
     * all positive deltas.
     *
     * Rewritten from the earlier version, which hoisted `profit` outside the loop and so
     * carried a stale value across down-ticks (correct only by accident, since that value
     * had already been folded into maxProfit), and seeded maxProfit with Integer.MIN_VALUE
     * despite the length<2 guard returning 0 - two different answers for "no profit".
     */
    int solutionPrices(int[] prices) {
        if (prices == null || prices.length < 2) return 0;

        int minPrice = prices[0];
        int maxProfit = 0;

        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            if (price < minPrice) {
                minPrice = price;              // new cheapest buy; nothing to sell yet
            } else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;  // better sell against the cheapest buy so far
            }
        }
        return maxProfit;
    }

    /**
     * Same scan, but also reports which days to trade. Ties resolve to the earliest
     * buy day / earliest sell day. Returns {-1, -1} when no profitable trade exists,
     * which is the follow-up an interviewer asks the moment the int version is done.
     */
    int[] solutionPricesWithDays(int[] prices) {
        if (prices == null || prices.length < 2) return new int[]{-1, -1};

        int minIdx = 0, buyDay = -1, sellDay = -1, maxProfit = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < prices[minIdx]) {
                minIdx = i;
            } else if (prices[i] - prices[minIdx] > maxProfit) {
                maxProfit = prices[i] - prices[minIdx];
                buyDay = minIdx;
                sellDay = i;
            }
        }
        return new int[]{buyDay, sellDay};
    }
}
