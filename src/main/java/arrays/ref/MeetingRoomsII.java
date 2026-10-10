package arrays.ref;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * D5. Minimum conference rooms required for a set of meetings (interval scheduling —
 * same shape as sizing a worker pool for overlapping jobs).
 * int minMeetingRooms(int[][] intervals)  where intervals[i] = [start, end)
 */
/**
 * Critique:
 * - Complexity: O(n log n) for the sort + O(n log r) for the heap ops (r = max
 *   concurrent rooms, r <= n) → O(n log n) overall, O(n) space.
 * - Correctness hinge: sorting by start time and always checking the *earliest-freeing*
 *   room (heap root) against the next meeting's start is what guarantees minimum room
 *   count — greedily reusing whichever room frees soonest can never do worse than any
 *   other room-assignment strategy.
 * - Concurrency: N/A, pure function — though the problem itself models a real
 *   concurrency concept (resource pool sizing for overlapping demands), which is why
 *   it's worth having in a backend-interview set even outside a literal calendar app.
 * - Edge cases covered: no meetings (0 rooms), touching intervals ([1,5],[5,10] don't
 *   overlap — a meeting ending at t and one starting at t can share a room), fully
 *   nested/overlapping intervals (rooms == max overlap depth).
 * - Alternative: sort starts[] and ends[] separately and two-pointer-sweep them
 *   (classic "line sweep") also gets O(n log n) without a heap at all — arguably
 *   cleaner; the heap version was chosen here since this file is specifically the
 *   heap-practice set, but naming the sweep alternative live shows you know it's not
 *   the only way.
 */
public class MeetingRoomsII {

    public static int minMeetingRooms(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, Comparator.comparingInt(iv -> iv[0]));

        PriorityQueue<Integer> roomEndTimes = new PriorityQueue<>(); // earliest-freeing room first
        for (int[] iv : intervals) {
            if (!roomEndTimes.isEmpty() && roomEndTimes.peek() <= iv[0]) {
                roomEndTimes.poll(); // reuse the room that's already free
            }
            roomEndTimes.offer(iv[1]);
        }
        return roomEndTimes.size();
    }

    public static void main(String[] args) {
        check(minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}), 2);
        check(minMeetingRooms(new int[][]{{7, 10}, {2, 4}}), 1);
        check(minMeetingRooms(new int[][]{}), 0); // no meetings
        check(minMeetingRooms(new int[][]{{1, 5}, {5, 10}}), 1); // touching, not overlapping
        check(minMeetingRooms(new int[][]{{1, 10}, {2, 9}, {3, 8}}), 3); // fully nested/overlapping
        System.out.println("arrays.ref.MeetingRoomsII: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
