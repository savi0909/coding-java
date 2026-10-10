package arrays.ref;

import java.util.PriorityQueue;

/**
 * D1. Kth largest element in an array (one-shot, not a stream).
 * int findKthLargest(int[] nums, int k) -> the kth largest value (1-indexed, so k=1 is the max).
 * [3,2,1,5,6,4], k=2 -> 5    [3,2,3,1,2,4,5,5,6], k=4 -> 4
 * Constraints: 1 <= k <= nums.length <= 1e5. Edge: k==1 (max); k==nums.length (min); duplicates.
 */
/**
 * Critique:
 * - Complexity: O(n log k) time (n offers/polls on a heap bounded to size k), O(k) space.
 * - Concurrency: N/A, PriorityQueue here is not thread-safe (see PriorityBlockingQueue
 *   if this needed to be shared across threads) — irrelevant for this single-threaded use.
 * - Edge cases covered: k == 1 (heap degenerates to tracking just the max), k == length
 *   (heap ends up holding everything, root is the min), duplicates (heap doesn't
 *   dedupe, so counts naturally fall out right).
 * - Alternative: Quickselect gets average O(n) time / O(1) extra space (in-place
 *   partitioning) but worst-case O(n^2) and destroys the input order; the heap version
 *   trades a log factor for worst-case guarantees and leaves nums untouched. For a
 *   one-shot query, quickselect is the "I know a better way" answer if asked; for a
 *   stream of queries, min-heap-of-size-k (this) or arrays.ref.KthLargestInStream (D2) is right.
 */
public class KthLargestElement {

    public static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // keeps the k largest seen so far
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }

    public static void main(String[] args) {
        check(findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2), 5);
        check(findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4), 4);
        check(findKthLargest(new int[]{7, 2, 9, 1}, 1), 9);   // k == 1 -> max
        check(findKthLargest(new int[]{7, 2, 9, 1}, 4), 1);   // k == length -> min
        check(findKthLargest(new int[]{4, 4, 4, 4}, 2), 4);   // duplicates
        System.out.println("arrays.ref.KthLargestElement: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
