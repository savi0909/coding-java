package dsa.hashing;

public class FirstDuplicateInArray {

    int firstDuplicate(int[] a) {
        int min = a.length;

        for(int i = 0; i < a.length; i++){
            for(int j=i+1; j < a.length; j++){
                if(a[i]==a[j]){
                    if(min > j) min = j;
                    break;
                }
            }
        }

        return min == a.length ? -1 : a[min];
    }
}
