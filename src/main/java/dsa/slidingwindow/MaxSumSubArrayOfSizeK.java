package dsa.slidingwindow;

class MaxSumSubArrayOfSizeK {

  public static int findMaxSumSubArray2(int k, int[] arr) {
    int maxSum = 0;
    int windowSum = 0;
    int windowStart = 0;

    for (int windowEnd = 0; windowEnd < arr.length; windowEnd++) {
      windowSum += arr[windowEnd];

      if (windowEnd+1 - windowStart == k) {
        maxSum = Math.max(maxSum, windowSum);
        windowSum -= arr[windowStart];
        windowStart++;
      }
    }
    return maxSum;
  }

  public static int findMaxSumSubArray(int k, int[] arr) {
    assert  arr.length >= k;
    int sum = 0 ;

    for(int i =0 ; i< k; i++){
      sum  = sum + arr[i];
    }
    int maxSum = sum;
    for(int i =k ; i< arr.length; i++){
      sum=  sum + arr[i] - arr[i-k];
      if(sum > maxSum) maxSum = sum;
    }
    return maxSum;
  }


}