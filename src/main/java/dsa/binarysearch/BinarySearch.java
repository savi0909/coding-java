package dsa.binarysearch;

import java.util.Arrays;
class BinarySearch {
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) { // <= here because left and right could point to the same element, < would miss it
            int mid = left + (right - left) / 2; // use `(right - left) /2` to prevent `left + right` potential overflow
            // found target, return its index
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) {
                // middle less than target, discard left half by making left search boundary `mid + 1`
                left = mid + 1;
            } else {
                // middle greater than target, discard right half by making right search boundary `mid - 1`
                right = mid - 1;
            }
        }
        return -1; // if we get here we didn't hit above return so we didn't find target
    }

    public static int first_not_smaller(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // use `(right - left) /2` to prevent `left + right` potential overflow
            // found target, return its index
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) {
                // middle less than target, discard left half by making left search boundary `mid + 1`
                left = mid + 1;
            } else {
                // middle greater than target, discard right half by making right search boundary `mid - 1`
                right = mid - 1;
            }
        }

        return right != arr.length -1 ? right + 1 : -1;

        /* Cleaner BinarySearch
        int left = 0;
        int right = arr.length - 1;
        int boundaryIndex = -1;
        while (left <= right) {
            int mid = left  + (right - left) / 2;
            if (arr[mid] >= target) {
                boundaryIndex = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return boundaryIndex;
         */
    }


    //[]



    public static int find_first_occurrence(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        int boundaryIndex = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                boundaryIndex = mid;
                right = mid - 1;
            }
            else if(arr[mid] > target )  {

                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
        }
        return boundaryIndex;

    }

    public static int find_rotation(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        int boundaryIndex = -1;
        int target = arr[right];

        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] <= target) {
                boundaryIndex = mid;
                right = mid - 1;
            }
            else  {
                left = mid + 1;
            }

        }
        return boundaryIndex;

    }

    // Driver code
    public static void main(String[] args) {


        System.out.println("Binary search : " + BinarySearch.find_rotation(new int[] {30, 40, 50, 10, 20}));


        System.out.println("Binary search : " + BinarySearch.find_first_occurrence(
                new int[] {1, 3, 3, 3, 3, 6, 10, 10, 10, 100}, 3));


        System.out.println("Binary search : " + BinarySearch.binarySearch(new int[] {1, 3, 5, 7, 8}, 5));
        System.out.println("Binary search : " + BinarySearch.binarySearch(new int[] {1, 2, 3, 4, 5, 6, 7}, 0));
        System.out.println("Binary search : " + BinarySearch.binarySearch(new int[] {2, 8, 89, 120, 1000}, 120));

        System.out.println("Find boundry :" + BinarySearch.findBoundary(new boolean[]{false, false, true, true, true}));
        System.out.println("Find boundry :" + BinarySearch.findBoundary(new boolean[]{true}));
        System.out.println("Find boundry :" + BinarySearch.findBoundary(new boolean[]{false, false, false}));
        System.out.println("Find boundry :" + BinarySearch.findBoundary(new boolean[]{true, true, true, true, true}));
        System.out.println("Find boundry :" + BinarySearch.findBoundary(new boolean[]{false, true}));

        System.out.println("Find first element :" + BinarySearch.first_not_smaller(new int[]{1, 3, 3, 5, 8, 8, 10}, 2));
        System.out.println("Find first element :" + BinarySearch.first_not_smaller(new int[]{0}, 0));
        System.out.println("Find first element :" + BinarySearch.first_not_smaller(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 10));
        System.out.println("Find first element :" + BinarySearch.first_not_smaller(new int[]{1, 1, 1, 1, 4, 5}, 3));

    }

        public static int findBoundary(boolean[] arr) {
            // WRITE YOUR BRILLIANT CODE HERE
            int left = 0;
            int right = arr.length - 1;
            int boundaryIndex = -1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (arr[mid]) {
                    boundaryIndex = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            return boundaryIndex;
        }

}