package arrays.ref;

/**
 * Two pointers from the outside in, O(n) time / O(1) space.
 *
 * Critique:
 * - Correctness hinge: always move the pointer at the *shorter* line. The container
 *   width can only shrink from here on, so keeping a short line can never produce a
 *   better area than moving past it — the taller line remains available as a partner
 *   for every future width. This greedy step is the part worth stating out loud;
 *   without justifying it, it looks like an unmotivated heuristic.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: strictly increasing heights, all-equal heights, length exactly 2.
 * - Alternative: brute force O(n^2) checks every pair; the two-pointer version is only
 *   correct because of the shorter-line argument above, not because "less code = faster".
 */
public class ContainerWithMostWater {

    public static int maxArea(int[] height) {
        int left = 0, right = height.length - 1, best = 0;
        while (left < right) {
            int h = Math.min(height[left], height[right]);
            best = Math.max(best, h * (right - left));
            if (height[left] < height[right]) left++; else right--;
        }
        return best;
    }

    public static void main(String[] args) {
        check(maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}), 49);
        check(maxArea(new int[]{1, 1}), 1);
        check(maxArea(new int[]{1, 2, 3, 4, 5}), 6);       // strictly increasing
        check(maxArea(new int[]{4, 4, 4, 4}), 12);         // all equal heights
        System.out.println("arrays.ref.ContainerWithMostWater: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
