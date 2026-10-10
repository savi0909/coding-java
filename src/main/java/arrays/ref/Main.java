/*
 * Fixed-size sliding window — max sum of k consecutive elements
 *
 * Time: O(n) — single pass, deque ops are O(1) amortized
 * Space: O(k) — deque stores at most k elements
 *
 * Edge cases:
 *  - k > arr.length → invalid, undefined (add guard if needed)
 *  - k = 1 → max of array
 *  - empty array → undefined
 *  - negative numbers → works fine (sum can be negative)
 *  - overflow → int sum can overflow; use long if array values large
 *
 * Interviewer probes:
 *  - "Why ArrayDeque?" → O(1) add/remove at both ends, better than LinkedList overhead
 *  - "Alternative?" → Two-pointer sum (no deque) is simpler & faster for fixed window:
 *      sum = sum(arr[0..k-1]), then slide: sum += arr[i] - arr[i-k]
 *    Deque wins if you need to track window *elements* (e.g., max in window with monotonic deque).
 *
 * At scale: This is single-machine. For distributed sliding windows (streaming), use
 * Flink/Kafka Streams with time-based or count-based tumbling/sliding windows.
 */
package arrays.ref;

import java.util.ArrayDeque;
import java.util.Deque;

public class Main {

    public static void main(String[] args) {

        // Basic cases
        test(new int[]{2,1,5,1,3,2}, 3, 9);
        test(new int[]{1,2,3,4,5}, 2, 9);
        test(new int[]{5}, 1, 5);
        test(new int[]{-1,-2,-3,-4}, 2, -3);

        // Edge cases
        test(new int[]{1,2,3,4}, 4, 10);                    // k = arr.length
        test(new int[]{-5,-2,-8,-1}, 3, -11);              // all negative (windows: -15, -11)
        test(new int[]{10,-5,2,-3,8}, 3, 7);               // mixed, max in middle
        test(new int[]{0,0,0,0}, 2, 0);                     // all zeros
        test(new int[]{5,5,5,5}, 2, 10);                    // duplicates
        test(new int[]{-10}, 1, -10);                       // single negative
        test(new int[]{100,-50,200,-100,50}, 1, 200);      // k=1 → max element
    }

    static void test(int[] arr, int k, int expected) {

        System.out.printf("Test: arr=%s, k=%d → expected=%d%n",
                          java.util.Arrays.toString(arr), k, expected);
        int actual = solve(arr, k);

        if (actual == expected) {
            System.out.println("  ✓ PASS\n");
        } else {
            System.out.println("  ✗ FAIL");
            System.out.println("    Expected: " + expected);
            System.out.println("    Actual  : " + actual + "\n");
        }
    }

    static int solve(int[] arr, int k) {

        if (arr == null || arr.length == 0 || k <= 0 || k > arr.length) {
            return 0; // or throw
        }

        Deque<Integer> window = new ArrayDeque<>(k);
        int currentSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for (int i = 0; i < arr.length; i++) {
            window.addLast(arr[i]);
            currentSum += arr[i];

            if (window.size() == k) {
                maxSum = Math.max(maxSum, currentSum);
                int removed = window.removeFirst();
                currentSum -= removed;
            }
        }

        return maxSum;
    }
}