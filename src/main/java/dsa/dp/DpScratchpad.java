package dsa.dp;

import java.util.*;
import java.util.stream.Collectors;

public class DpScratchpad {



    public static void main(String[] args){

        System.out.println("Max Value for Rod \n");
        int s = 15;
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] vals = {1,5,8,9,10,17,17,20};
        DpScratchpad main = new DpScratchpad();
        int maxVal = main.maxVal(vals, new int[9], 8, -1);
        System.out.println("Max Value for Rod " + maxVal);
        main.f(20,4);
    }

    int f(int n, int k) {

        if(n<2) return 1;
        if(n==2) return 2;

        // dp is a 2D array of n+1 rows and k+1 cols
        // dp[i][j] stores the result f(i,j)

        int[][]  dp = new int[n+1][k+1];

        for(int j=0; j<=k; j++){
            dp[0][j] = 0;
            dp[1][j] = 1;
            dp[2][j] = 2;
        }

        for(int i=3; i<=n; i++){
            for(int j=0; j<=k; j++){

                dp[i][j] = dp[i-1][j] + dp[i-2][j];
                if(j>0) {
                    dp[i][j] += dp[i-3][j-1];
                }
            }
        }

        for( int i = 0 ;i <= n; i++){
            for(int j =0; j <= k; j++){
                System.out.print(" "+ dp[i][j]);
            }
            System.out.println("");
        }

        return dp[n][k];
    }

    public int maxVal(int[] prices ,int[] best, int length, int max_val){
        if(length == 0){
            return 0;
        }
        if(best[length] > 0) return best[length];

        for(int i = 1; i<=length; i++){
            int part = maxVal(prices, best, length-i,max_val);
            int temp = prices[i-1] + part;
            if(temp > max_val){
                max_val = temp;
            }
        }
        best[length] = max_val;
        return max_val;
    }
        public int productExceptSelf(int[] nums, int m) {
            int s =0, p=1;

            for(int num : nums){
                s = (p + s*num) % m;
                p =  (p*num) %m;
            }

            return s;
        }

    private boolean primeFactor(int m, int num) {

        int number = m>=num ? m : num;

        for(int i = 2; i <= number/2;i++){
            if(m%i==0 && num%i==0) return true;
        }

        return false;

    }


    String swapLexOrder(String str, int[][] pairs) {
        char[] chars = str.toCharArray();
        String biggest = str;
        boolean swap = true;

        while(swap){
            boolean change = false;
            for(int i=0; i < pairs.length;i++){
                swap(pairs[i], chars);
            }
            String newString = new String(chars);
            if(biggest.compareTo(newString) < 0){
                biggest = newString;
                chars = biggest.toCharArray();
                change = true;
            }
           swap = change ;
        }
        return biggest;
    }

    private void swap(int[] pair, char[] chars) {
        int a = pair[0]-1;
        int b = pair[1]-1;
        char tmp = chars[a];
        chars[a] = chars[b];
        chars[b] = tmp;
    }




}
