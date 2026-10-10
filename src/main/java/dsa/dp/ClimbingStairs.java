package dsa.dp;

public class ClimbingStairs {

    int climbingStairs(int n) {
        int[] number = new int[n];
        number[0] =  1;
        number[1] = 2;

        for ( int i = 2; i < n;i++){

            number[i] = number[i-1]+number[i-2];
        }
        return number[n-1];
    }
}
