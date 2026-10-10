package arrays.ref;

/**
 * Two pointers with running leftMax/rightMax, O(n) time / O(1) space.
 *
 * Critique:
 * - Correctness hinge: water trapped at any index is
 *   min(maxToLeft, maxToRight) - height[index]. Whichever side currently has the
 *   smaller running max is the side whose water level is already fully determined —
 *   its trapped water can't be affected by anything further away on the other side,
 *   so that pointer is safe to resolve and advance.
 * - Concurrency: N/A, pure function.
 * - Edge cases covered: monotonic array (traps 0, since one side's max is always the
 *   current height), single valley, flat array (traps 0).
 * - Alternative: precomputed leftMax[]/rightMax[] arrays solve it in O(n) time but
 *   O(n) space; the two-pointer version gets the same time bound in O(1) space by
 *   computing the running maxes on the fly instead of storing them.
 */
public class TrapRainWater {

    public static int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            if (height[left] <= height[right]) {
                leftMax = Math.max(leftMax, height[left]);
                water += leftMax - height[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, height[right]);
                water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }

    public static void main(String[] args) {
        check(trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}), 6);
        check(trap(new int[]{4, 2, 0, 3, 2, 5}), 9);
        check(trap(new int[]{1, 2, 3, 4, 5}), 0);   // monotonic -> traps 0
        check(trap(new int[]{3, 0, 3}), 3);         // single valley
        check(trap(new int[]{2, 2, 2, 2}), 0);      // flat array
        System.out.println("arrays.ref.TrapRainWater: all checks passed");
    }

    private static void check(int actual, int expected) {
        if (actual != expected) {
            throw new AssertionError("expected " + expected + " but got " + actual);
        }
    }
}
