package arrays.ref;

import java.util.PriorityQueue;

/**
 * D2. Kth largest element in a stream — a running leaderboard/top-k service.
 * Constructor seeds with initial nums; add(val) inserts one value and returns the
 * current kth largest. Classic size-k min-heap: root is always the kth largest so far.
 */
/**
 * Critique:
 * - Complexity: O(log k) per add() (amortized, since heap size is capped at k), O(k)
 *   space total — this is the actual reason a heap beats re-sorting on every insert
 *   (which would be O(n log n) per add for a stream of size n).
 * - Concurrency: single PriorityQueue instance mutated per call — not thread-safe if
 *   add() can be invoked concurrently (e.g. this backing a shared leaderboard service);
 *   would need external synchronization or a concurrent structure for that case.
 * - Edge cases covered: k == 1 with an empty seed array (heap starts empty, first
 *   add() both fills and answers it).
 * - This is the online/streaming counterpart to arrays.ref.KthLargestElement (D1) — same core
 *   idea (bounded min-heap), but state persists across calls instead of being
 *   recomputed from scratch each time, which is the right shape for a live leaderboard
 *   or top-k monitoring metric.
 */
public class KthLargestInStream {

    private final int k;
    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    public KthLargestInStream(int k, int[] nums) {
        this.k = k;
        for (int n : nums) add(n);
    }

    public int add(int val) {
        minHeap.offer(val);
        if (minHeap.size() > k) minHeap.poll();
        return minHeap.peek();
    }

    public static void main(String[] args) {
        KthLargestInStream stream = new KthLargestInStream(3, new int[]{4, 5, 8, 2});
        check(stream.add(3), 4);
        check(stream.add(5), 5);
        check(stream.add(10), 5);
        check(stream.add(9), 8);
        check(stream.add(4), 8);

        KthLargestInStream single = new KthLargestInStream(1, new int[]{});
        check(single.add(-1), -1); // k == 1, empty seed

        System.out.println("arrays.ref.KthLargestInStream: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
