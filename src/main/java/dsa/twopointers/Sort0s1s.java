package dsa.twopointers;

class Sort0s1s {

  public static void sort(int[] arr) {
    // all elements < low are 0 and all elements > high are 2
    // all elements from >= low < i are 1
    int low = 0, high = arr.length - 1;
    /* Method 1, little tricky
    for (int i = 0; i <= high;i++) {

      if (arr[i] == 0) {
        swap(arr, i, low);
        low++;
      }
    }
    */

    // Method 2
    while(low < high){
      if(arr[low] == 0) low++;
      else if(arr[high] == 1) high--;
      else {
        swap(arr, low, high);
      }
    }
  }

  private static void swap(int[] arr, int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

  public static void main(String[] args) {
    int[] arr = new int[] { 1, 1, 1, 1, 0 };
    Sort0s1s.sort(arr);
    for (int num : arr)
      System.out.print(num + " ");
    System.out.println();

    arr = new int[] { 1, 1, 0, 0, 0, 0 };
    Sort0s1s.sort(arr);
    for (int num : arr)
      System.out.print(num + " ");
  }
}