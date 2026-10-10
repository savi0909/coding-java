package dsa.hashing;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PossiblesumsGoogle {

    public static int possibleSumsIterative(int[] coins, int[] quantity) {
        HashSet<Integer> sums = new HashSet<Integer>(100);

        int coin = coins[0];
        int coinQuantity = quantity[0];

        for(int i = 0 ; i <= coinQuantity; i++){
            sums.add(i*coin);
        }
        for(int j=1; j<coins.length;j++){
            coin = coins[j];
            coinQuantity = quantity[j];
            List<Integer> collect = sums.stream().collect(Collectors.toList());

            for(int i = 0; i <= coinQuantity; i++) {
                for (int val : collect) {
                    int newVal = val + coin * i;
                    sums.add(newVal);
                }
            }
        }
        return sums.size()-1;
    }

    public static int possibleSums(int[] coins, int[] quantity) {
        HashSet<Integer> sums = new HashSet<Integer>(100);
        possibleSums(coins, quantity,coins.length,sums);
        return sums.size()-1;
    }

    public static void possibleSums(int[] coins, int[] quantity, int length, HashSet<Integer> sums) {

        if(length > 1) {
            possibleSums( coins, quantity, length-1,  sums);
            int coin = coins[length-1];
            int coinQuantity = quantity[length-1];
            int[] newsums = new int[sums.size()*(coinQuantity+1)];
            int j=0;
            for(int i = 0; i <= coinQuantity; i++){
                for(int val : sums){
                    int newVal = val + coin*i;
                    newsums[j] = newVal;
                    j++;
                }
            }
            for( j = 0; j<newsums.length; j++ ){
                sums.add(newsums[j]);
            }
        }
        else {
            possibleSumsForCoin(coins[0], quantity[0], sums);
        }

    }


    public static void  possibleSumsForCoin(int coin, int quantity, HashSet<Integer> sums) {
        if(quantity > 0){
            possibleSumsForCoin(coin, quantity-1,sums);
        }
        sums.add(coin*quantity);
    }
}
