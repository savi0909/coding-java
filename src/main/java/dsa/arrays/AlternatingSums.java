package dsa.arrays;

public class AlternatingSums {

    int[] alternatingSums(int[] a) {
        int[] sums = new int[2];
        boolean flip = false;

        for(int i=0; i < a.length; i++){
            int index = flip ? 1 : 0;
            flip = !flip;
            sums[index]  = sums[index] + a[i];
        }
        return sums;
    }
}
