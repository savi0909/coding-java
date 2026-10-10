package arrays.ref;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * D6. Running median over a stream (two heaps — e.g. live p50 of request latencies).
 * lowerHalf (max-heap) holds the smaller half, upperHalf (min-heap) the larger half;
 * lowerHalf is allowed exactly one extra element when the stream size is odd.
 */
/**
 * Critique:
 * - Complexity: O(log n) per addNum() (heap insert/rebalance), O(1) per findMedian(),
 *   O(n) total space.
 * - Correctness hinge: the size invariant (|lowerHalf| - |upperHalf| in {0, 1}) is what
 *   makes findMedian() O(1) — it never has to know *which* values are in which heap,
 *   only their counts and their two peeks. Insert-then-rebalance (always add to
 *   lowerHalf first, then shuffle across if needed) is simpler and just as correct as
 *   trying to pick the "right" heap directly on the first attempt.
 * - Concurrency: this instance is stateful and mutated by addNum() — not thread-safe
 *   for concurrent writers (e.g. a live metrics service ingesting from multiple
 *   threads) without external synchronization; each arrays.ref.MedianFinder here is used from a
 *   single thread in the test harness.
 * - Edge cases covered: single element, duplicates, values arriving in descending
 *   order (rebalancing logic doesn't assume any arrival order).
 * - Alternative: a single sorted structure (e.g. a balanced BST or skip list) also
 *   gives O(log n) insert with O(1) median, but two heaps avoid needing an
 *   order-statistics-augmented tree — simpler to implement correctly under pressure.
 */
public class MedianFinder {

    private final PriorityQueue<Integer> lowerHalf = new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> upperHalf = new PriorityQueue<>();

    public void addNum(int num) {
        if (lowerHalf.isEmpty() || num <= lowerHalf.peek()) {
            lowerHalf.offer(num);
        } else {
            upperHalf.offer(num);
        }
        if (lowerHalf.size() > upperHalf.size() + 1) {
            upperHalf.offer(lowerHalf.poll());
        } else if (upperHalf.size() > lowerHalf.size()) {
            lowerHalf.offer(upperHalf.poll());
        }
    }

    public double findMedian() {
        if (lowerHalf.size() > upperHalf.size()) return lowerHalf.peek();
        return (lowerHalf.peek() + upperHalf.peek()) / 2.0;
    }

    public static void main(String[] args) {
        MedianFinder mf = new MedianFinder();
        mf.addNum(1);
        mf.addNum(2);
        check(mf.findMedian(), 1.5);
        mf.addNum(3);
        check(mf.findMedian(), 2.0);

        MedianFinder single = new MedianFinder();
        single.addNum(5);
        check(single.findMedian(), 5.0); // single element

        MedianFinder dup = new MedianFinder();
        dup.addNum(2);
        dup.addNum(2);
        dup.addNum(2);
        check(dup.findMedian(), 2.0); // duplicates

        MedianFinder descending = new MedianFinder();
        for (int n : new int[]{5, 4, 3, 2, 1}) descending.addNum(n);
        check(descending.findMedian(), 3.0); // arrives in descending order

        System.out.println("arrays.ref.MedianFinder: all checks passed");
    }

    private static void check(double actual, double expected) {
        if (Math.abs(actual - expected) > 1e-9) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
