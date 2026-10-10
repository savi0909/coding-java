package arrays.sliding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/*
 * CRITIQUE (maxInEveryWindow — deque O(n) vs brute force O(n*k))
 *
 * Problem: for every window of size k, return the window's maximum VALUE.
 * (Sibling class MaxIndexPerWindow returns the max's INDEX; same algorithm, different payload.)
 * Output length is n-k+1.
 *
 * ---- Complexity ----
 *   maxInEveryWindowBrute : O(n*k) time, O(1) extra space. Rescans each window from scratch.
 *   maxInEveryWindow      : O(n)   time, O(k) extra space (deque never exceeds k entries).
 * Both allocate the O(n-k+1) result. Brute force wins only when k is tiny (k <= ~3) where the
 * deque's bookkeeping isn't amortized away — worth saying in an interview, not worth coding.
 * Measured (see benchmark(), n=200k k=2000): deque ~3ms, brute ~76ms. The brute number is
 * flattered by JIT vectorization of a tight compare loop — it is still 4e8 comparisons and
 * grows linearly with k, while the deque does not move at all. Don't read constant factors
 * as a refutation of the asymptotics; scale k and the gap becomes unbounded.
 *
 * The brute force is not dead weight: it is the ORACLE. crossCheck() runs both on random input
 * so the deque version is differential-tested, which is how off-by-one expiry bugs
 * (`<= i-k` vs `< i-k`) actually get caught. Hand-written cases rarely hit them.
 *
 * ---- Why ArrayDeque and not an ArrayList-based deque ----
 * The algorithm needs O(1) at BOTH ends: pollFirst (expiry) and offerLast/pollLast (domination).
 *   ArrayList  : add/remove at the tail is amortized O(1), but remove(0) is O(size) — it
 *                System.arraycopy's every remaining element down one slot. The front is polled
 *                on every expiry, so worst case (a decreasing array, deque always full at k)
 *                degrades to O(n*k) — you'd have reimplemented the brute force with extra steps
 *                and extra memory. Strictly worse than the naive loop. Measured on a strictly
 *                decreasing array (n=100k, k=2000): ArrayDeque ~0ms, ArrayList ~30ms. The gap
 *                looks mild only because System.arraycopy is a memmove; the WORK is quadratic
 *                and scales with k, whereas ArrayDeque's does not.
 *   LinkedList : O(1) at both ends, so asymptotically fine, but allocates a ~40-byte Node per
 *                push and scatters the window across the heap. Pointer chasing + GC pressure
 *                makes it consistently slower in practice despite matching big-O.
 *   ArrayDeque : circular buffer — one backing array plus head/tail int cursors. Both ends are
 *                O(1) with no shifting, elements are contiguous (cache-friendly), zero per-node
 *                allocation, amortized growth by doubling. Correct default.
 *   int[] ring : the real optimization. The Deque<Integer> here boxes every index — an Integer
 *                allocation (or IntegerCache hit for values < 128) plus an unbox on every read.
 *                A raw `int[] dq = new int[k]` with head/tail ints removes boxing entirely and
 *                is what you'd ship in a hot path. Costs readability; mention it, code the
 *                ArrayDeque unless asked to squeeze.
 * Never `Deque<Integer> d = new LinkedList<>()` out of habit — ArrayDeque is the default.
 * Note ArrayDeque forbids nulls; irrelevant here (autoboxed ints), but it is why it can use a
 * sentinel-free circular buffer at all.
 *
 * ---- Invariant ----
 * The deque holds indices of the current window with STRICTLY DECREASING values front->back.
 * Any element smaller than a later arrival is dominated — smaller value AND expires sooner —
 * so it can never be a future max and is discarded. Hence the front is always the answer.
 * Each index is offered once and polled at most once => the inner while loop is amortized O(1).
 *
 * ---- Edge cases an interviewer probes ----
 *   - null / empty / k <= 0 / k > length -> int[0] in BOTH implementations (they must agree,
 *     or the cross-check is testing nothing). Empty result over a sentinel: "no windows exist".
 *   - k == 1 -> result is a copy of arr; deque holds at most one index.
 *   - k == arr.length -> one window, one output.
 *   - all negatives -> fine; no `int max = 0` seeding bug (a classic in the brute force —
 *     seed from the first element of the window, never from 0 or Integer.MIN_VALUE by habit).
 *   - ties / all equal -> strict `<` on the pop keeps the earlier equal index, so values are
 *     identical either way. Only matters if you return indices (see MaxIndexPerWindow).
 *   - overflow -> none; comparisons only, nothing accumulates.
 *
 * ---- Concurrency ----
 * Pure functions over a caller-owned array; the deque is a local, so thread-confined.
 * ArrayDeque is deliberately unsynchronized — correct here, and would be a data race if the
 * deque were ever hoisted to a field and shared. No defensive copy of arr is made, so callers
 * must not mutate it concurrently.
 *
 * ---- At scale ----
 * A true stream can't random-access arr[i-k], but this never needs to — only the values at
 * stored indices. Keep a k-element ring buffer alongside, or store (index,value) pairs, and
 * memory stays O(k) for an unbounded stream. Windows overlap by k-1, so sharding is easy:
 * split the array, recompute the k-1 seam per boundary.
 *
 * ---- Better alternative ----
 * Block prefix/suffix-max: partition into blocks of k, sweep prefix maxima forward and suffix
 * maxima backward, then each window's answer is one max of two lookups. Same O(n), two flat
 * array passes, zero allocation per element, better cache behaviour and branch prediction.
 * Costs two O(n) scratch arrays (vs O(k)) and is much less obvious to read. Deque is the
 * default answer; this is the "squeeze it" answer.
 */
public class MaxInEveryWindow {

    public static void main(String[] args) {

        // ---- hand-written cases: both implementations must agree with `expected` ----
        // windows: [2,1,5] [1,5,1] [5,1,3] [1,3,2]
        test(new int[]{2, 1, 5, 1, 3, 2}, 3, new int[]{5, 5, 5, 3});
        test(new int[]{5, 1, 1, 1}, 1, new int[]{5, 1, 1, 1});          // k = 1 -> copy of arr
        test(new int[]{1, 2, 3}, 3, new int[]{3});                      // k == length
        test(new int[]{-4, -1, -5, -2}, 2, new int[]{-1, -1, -2});      // all negative
        test(new int[]{4, 4, 4}, 2, new int[]{4, 4});                   // all equal
        test(new int[]{9, 1, 1, 1, 1}, 3, new int[]{9, 1, 1});          // max expires out of window
        test(new int[]{1, 2, 3, 4, 5}, 2, new int[]{2, 3, 4, 5});       // strictly increasing
        test(new int[]{5, 4, 3, 2, 1}, 2, new int[]{5, 4, 3, 2});       // strictly decreasing (deque stays full)
        test(new int[]{1, 3, 1, 3, 1}, 2, new int[]{3, 3, 3, 3});       // alternating, ties across windows
        test(new int[]{7}, 1, new int[]{7});                            // single element
        test(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, 2,
                new int[]{Integer.MAX_VALUE});                          // extremes, no overflow
        test(new int[]{}, 2, new int[]{});                              // empty
        test(null, 2, new int[]{});                                     // null
        test(new int[]{1, 2}, 0, new int[]{});                          // k <= 0
        test(new int[]{1, 2}, -3, new int[]{});                         // k negative
        test(new int[]{1, 2}, 5, new int[]{});                          // k > length

        // ---- differential test: deque vs brute force on random input ----
        crossCheck(2_000, 40, 20);   // small values -> many ties
        crossCheck(2_000, 5, 1_000_000);  // wide values -> few ties

        benchmark();
    }

    /* Runs BOTH implementations against the same expectation — they must never disagree. */
    static void test(int[] arr, int k, int[] expected) {

        int[] fast = maxInEveryWindow(arr, k);
        int[] brute = maxInEveryWindowBrute(arr, k);

        boolean fastOk = Arrays.equals(fast, expected);
        boolean bruteOk = Arrays.equals(brute, expected);

        if (fastOk && bruteOk) {
            System.out.println("PASS  " + Arrays.toString(arr) + " k=" + k
                    + " -> " + Arrays.toString(fast));
        } else {
            System.out.println("FAIL  " + Arrays.toString(arr) + " k=" + k);
            System.out.println("Expected : " + Arrays.toString(expected));
            System.out.println("Deque    : " + Arrays.toString(fast) + (fastOk ? "" : "   <-- wrong"));
            System.out.println("Brute    : " + Arrays.toString(brute) + (bruteOk ? "" : "   <-- wrong"));
        }
    }

    /* The brute force is the oracle: random arrays, every legal k, results must match exactly. */
    static void crossCheck(int trials, int maxLen, int valueBound) {

        Random rnd = new Random(42);   // fixed seed -> reproducible failures
        int checked = 0;

        for (int t = 0; t < trials; t++) {

            int n = rnd.nextInt(maxLen + 1);            // includes 0 -> empty array
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = rnd.nextInt(2 * valueBound + 1) - valueBound;   // negatives included
            }

            for (int k = 0; k <= n + 1; k++) {          // includes k=0 and k>n
                int[] fast = maxInEveryWindow(arr, k);
                int[] brute = maxInEveryWindowBrute(arr, k);
                checked++;

                if (!Arrays.equals(fast, brute)) {
                    System.out.println("FAIL  cross-check " + Arrays.toString(arr) + " k=" + k);
                    System.out.println("Deque : " + Arrays.toString(fast));
                    System.out.println("Brute : " + Arrays.toString(brute));
                    return;
                }
            }
        }
        System.out.println("PASS  cross-check: " + checked + " (array,k) pairs agree"
                + "  [len<=" + maxLen + ", |value|<=" + valueBound + "]");
    }

    /* Shows the O(n) vs O(n*k) gap concretely — the reason the deque exists. */
    static void benchmark() {

        int n = 200_000, k = 2_000;
        int[] arr = new int[n];
        Random rnd = new Random(7);
        for (int i = 0; i < n; i++) {
            arr[i] = rnd.nextInt();
        }

        // warm up the JIT so the numbers mean something
        for (int i = 0; i < 3; i++) {
            maxInEveryWindow(arr, k);
        }
        maxInEveryWindowBrute(arr, k);

        long t0 = System.nanoTime();
        maxInEveryWindow(arr, k);
        long dequeMs = (System.nanoTime() - t0) / 1_000_000;

        t0 = System.nanoTime();
        maxInEveryWindowBrute(arr, k);
        long bruteMs = (System.nanoTime() - t0) / 1_000_000;

        System.out.println("BENCH random  n=" + n + " k=" + k
                + "  deque O(n)=" + dequeMs + "ms"
                + "  brute O(n*k)=" + bruteMs + "ms"
                + "  (~" + (n * (long) k) + " comparisons for brute)");

        // worst case for the ArrayList variant: strictly decreasing keeps the deque full at k,
        // so every expiry shifts k elements. Random input hides this — the deque stays small.
        int m = 100_000;
        int[] desc = new int[m];
        for (int i = 0; i < m; i++) {
            desc[i] = m - i;
        }

        maxInEveryWindow(desc, k);
        maxInEveryWindowArrayListDeque(desc, k);

        t0 = System.nanoTime();
        maxInEveryWindow(desc, k);
        long adMs = (System.nanoTime() - t0) / 1_000_000;

        t0 = System.nanoTime();
        maxInEveryWindowArrayListDeque(desc, k);
        long alMs = (System.nanoTime() - t0) / 1_000_000;

        System.out.println("BENCH descending n=" + m + " k=" + k
                + "  ArrayDeque=" + adMs + "ms"
                + "  ArrayList(remove(0))=" + alMs + "ms"
                + "   <- same algorithm, O(1) vs O(k) front removal");
    }

    /*
        O(n) — monotonic deque of indices, values strictly decreasing front -> back.
        1. Drop the front once it slides out of the window.
        2. Drop tail entries dominated by arr[i] (smaller AND older).
        3. Front is the current window's max.
     */
    static int[] maxInEveryWindow(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return new int[0];
        }

        int[] result = new int[arr.length - k + 1];
        Deque<Integer> window = new ArrayDeque<>();   // indices, not values

        for (int i = 0; i < arr.length; i++) {

            // expire: front index is outside [i-k+1, i]
            if (!window.isEmpty() && window.peekFirst() <= i - k) {
                window.pollFirst();
            }

            // dominated: arr[last] can never beat arr[i] and dies first
            while (!window.isEmpty() && arr[window.peekLast()] < arr[i]) {
                window.pollLast();
            }

            window.offerLast(i);

            if (i >= k - 1) {                        // first full window ends at k-1
                result[i - k + 1] = arr[window.peekFirst()];
            }
        }

        return result;
    }

    /*
        O(n*k) — rescan every window from scratch. The oracle for crossCheck().
        Note max is seeded from arr[start], NOT 0 / Integer.MIN_VALUE — the all-negatives trap.
     */
    static int[] maxInEveryWindowBrute(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return new int[0];
        }

        int[] result = new int[arr.length - k + 1];

        for (int start = 0; start + k <= arr.length; start++) {
            int max = arr[start];
            for (int i = start + 1; i < start + k; i++) {
                if (arr[i] > max) {
                    max = arr[i];
                }
            }
            result[start] = max;
        }

        return result;
    }

    /*
        DELIBERATELY BAD — do not use. Kept to make the ArrayList-vs-ArrayDeque point concrete.
        Same algorithm, same O(n) comparison count, but remove(0) arraycopies the whole live
        window on every expiry. On a strictly decreasing array the deque stays at size k and
        every step pays O(k) => O(n*k) total, i.e. no better than the brute force while using
        O(k) extra memory. This is why "a list is basically a deque" is wrong.
     */
    static int[] maxInEveryWindowArrayListDeque(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return new int[0];
        }

        int[] result = new int[arr.length - k + 1];
        List<Integer> window = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {

            if (!window.isEmpty() && window.get(0) <= i - k) {
                window.remove(0);                    // <-- O(size) shift, the whole problem
            }

            while (!window.isEmpty() && arr[window.get(window.size() - 1)] < arr[i]) {
                window.remove(window.size() - 1);    // tail removal is fine, O(1)
            }

            window.add(i);

            if (i >= k - 1) {
                result[i - k + 1] = arr[window.get(0)];
            }
        }

        return result;
    }
}
