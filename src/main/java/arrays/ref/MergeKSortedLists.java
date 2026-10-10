package arrays.ref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * D4. Merge k sorted linked lists (e.g. merging k sorted log shards/query results).
 * ListNode mergeKLists(ListNode[] lists)
 */
/**
 * Critique:
 * - Complexity: O(N log k) time where N = total nodes across all lists, k = number of
 *   lists — each node is offered/polled from a heap bounded to size k exactly once.
 *   O(k) heap space (output list reuses existing nodes, no new node allocation).
 * - Concurrency: N/A, pure function; the ListNode graph itself isn't thread-safe to
 *   mutate concurrently, but that's out of scope for a single-threaded merge.
 * - Edge cases covered: empty lists array, some lists null/empty (never offered to the
 *   heap in the first place), a single-element list.
 * - Alternative: pairwise merge (merge lists 2 at a time, repeat) is also O(N log k)
 *   but with a friendlier constant and no extra data structure — the heap version is
 *   more natural once k is large and lists arrive incrementally (e.g. merging k live
 *   sorted streams/shards), which is the realistic backend framing for this problem.
 */
public class MergeKSortedLists {

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode head : lists) {
            if (head != null) minHeap.offer(head);
        }
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        while (!minHeap.isEmpty()) {
            ListNode smallest = minHeap.poll();
            tail.next = smallest;
            tail = smallest;
            if (smallest.next != null) minHeap.offer(smallest.next);
        }
        return dummy.next;
    }

    private static ListNode build(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int v : vals) {
            tail.next = new ListNode(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    private static int[] toArray(ListNode head) {
        List<Integer> out = new ArrayList<>();
        while (head != null) {
            out.add(head.val);
            head = head.next;
        }
        return out.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        ListNode[] lists = {build(1, 4, 5), build(1, 3, 4), build(2, 6)};
        check(toArray(mergeKLists(lists)), new int[]{1, 1, 2, 3, 4, 4, 5, 6});

        check(toArray(mergeKLists(new ListNode[]{})), new int[]{}); // no lists

        ListNode[] withNulls = {null, build(1), null};
        check(toArray(mergeKLists(withNulls)), new int[]{1}); // some lists empty/null

        ListNode[] singleEmpty = {null};
        check(toArray(mergeKLists(singleEmpty)), new int[]{});

        System.out.println("arrays.ref.MergeKSortedLists: all checks passed");
    }

    private static void check(int[] actual, int[] expected) {
        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("expected " + Arrays.toString(expected)
                    + " but got " + Arrays.toString(actual));
        }
    }
}
