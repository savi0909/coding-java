package dsa.arrays;

public class FindMaxUsingDivideAndConquer {

    public static void main(String[] args) {
        int[] arr =  {1, 2, 3, 4, 5, 0, 19, 0, 6, 7, 8, 9, 10};
        FindMaxUsingDivideAndConquer max = new FindMaxUsingDivideAndConquer();
        int maxUsingDivideAndConquer = max.findMaxUsingDivideAndConquer(arr, 0, arr.length - 1);

        System.out.println("Max::" + maxUsingDivideAndConquer);

        int maxUsingRecursion = max.findMaxUsingRecursion(arr, arr.length);
        System.out.println("Max::" + maxUsingRecursion);

    }

    public int findMaxUsingRecursion(int[] arr, int length){
        if(length ==1) return arr[0];

        int currentMax = arr[length-1]; // let max be last element of array
        length--; // reduce length by 1
        int arrayMax = findMaxUsingRecursion(arr, length);// recurse remaining part

        return Math.max(currentMax,arrayMax);
    }



    public int findMaxUsingDivideAndConquer(int[] arr, int beg, int end){
        if(beg > end) return -1; // invalid input return -1
        else if(beg == end) return  arr[beg]; // single element array
        // handle two element array,
        // more of an optimization to avoid single element recursive call
        else if(beg+1 == end ) return Math.max(arr[beg], arr[end]);

        int mid = (beg+end)/2;
        int first = findMaxUsingDivideAndConquer(arr,beg,mid);
        int second = findMaxUsingDivideAndConquer(arr,mid+1,end);

        return Math.max(first, second);
    }
}
