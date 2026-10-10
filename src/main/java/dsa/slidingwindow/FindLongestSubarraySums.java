package dsa.slidingwindow;

import java.util.HashMap;

public class FindLongestSubarraySums {

    public static void main(String[] args) {
        int[] arr =  {1, 2, 3, 4, 5, 0, 0, 0, 6, 7, 8, 9, 10};
        int sum = 26;
        FindLongestSubarraySums sums = new FindLongestSubarraySums();
        sums.findLongestSubarrayBySum(sum,arr);

    }

    public int findMaxUsingDivideAndConquer(int[] arr, int beg, int end){
        if(beg > end) return -1;
        else if(beg == end) return  arr[beg];
        else if(beg+1 == end ) return Math.max(arr[beg], arr[end]);

        int mid = (beg+end)/2;
        int first = findMaxUsingDivideAndConquer(arr,beg,mid);
        int second = findMaxUsingDivideAndConquer(arr,mid+1,end);

        return Math.max(first, second);
    }



    int[] findShortestSubarrayBySum(int s, int[] arr) {
        int maxLength = Integer.MAX_VALUE;
        int[] result = new int[2];

        HashMap<Integer, Integer> history = new HashMap<>();
        history.put(0,-1);

        int sum = 0;
        for(int i =0; i < arr.length;i++){
            sum = sum + arr[i];

            if(history.containsKey(sum-s)){
                int newLength = i - history.get(sum - s);
                if(newLength < maxLength){
                    maxLength = newLength;
                    result[0] = history.get(sum - s)+1;
                    result[1] = i;
                }
            }
            history.put(sum,i);
        }

        return result;

    }

    int[] findLongestSubarrayBySum(int s, int[] arr) {
        int maxLength = 0;
        int[] result = new int[2];

        HashMap<Integer, Integer> history = new HashMap<>();
        history.put(0,-1);

        int sum = 0;
        for(int i =0; i < arr.length;i++){
            sum = sum + arr[i];

            if(history.containsKey(sum-s)){
                int newLength = i - history.get(sum - s);
                if(newLength > maxLength){
                    maxLength = newLength;
                    result[0] = history.get(sum - s)+1;
                    result[1] = i;
                }
            }
            history.putIfAbsent(sum,i);
        }

        return result;

    }

    int[] findLongestSubarrayBySumBest(int s, int[] arr) {
        int resultLength = 0;
        int[] result = new int[2];
        int i = 0;
        int sum = 0;
        for(int j=0; j < arr.length;j++) {
            sum += arr[j];
            while (sum > s) {
                sum = sum - arr[i];
                i++;
            }
            if (sum == s){
                if(j-i+1 > resultLength) {
                    result[0] = i+1;
                    result[1] = j+1;
                    resultLength = j-i+1;
                }
            }
        }
        return resultLength > 0 ? result : new int[]{-1};
    }

    //first working--duplicate loops
    int[] findLongestSubarrayBySum2(int s, int[] arr) {
        int resultLength = 0;
        int[] result = new int[2];
        for(int i =0; i < arr.length;i++){
            int sum = 0;
            for(int j=i; j < arr.length;j++) {
                sum += arr[j];
                while (sum > s) {
                    sum = sum - arr[i];
                    i++;
                }
                if (sum == s){
                    while ( j+1 < arr.length && arr[j+1]==0) {
                        j++;
                    }
                    if(j-i+1 > resultLength) {
                        result[0] = i+1;
                        result[1] = j+1;
                        resultLength = j-i+1;
                    }
                }
            }
        }

        return resultLength > 0 ? result : new int[]{-1};
    }
}
