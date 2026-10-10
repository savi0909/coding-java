package dsa.arrays;

public class AlmostIncreasingSequence {
    boolean almostIncreasingSequence4(int[] sequence) {
        int flag = 0;
        int i = 0;
        while(i<sequence.length-1){
            if(sequence[i] < sequence[i+1]){
                i = i+1;
                continue;
            } else {
                flag = flag + 1;
                if(i>0 && i+2 < sequence.length && sequence[i+1] <= sequence[i-1] && sequence[i+2] <= sequence[i]){
                    flag = flag + 1;
                } else {
                    i = i+1;
                }
                if(flag > 1){
                    return false;
                }
            }
        }
        return true;
    }
    boolean almostIncreasingSequence2(int[] sequence) {
        int failed = 0;
        boolean one_chance;
        for(int i = 0; i < sequence.length - 1; i++){
            int curr=i,next=i+1;
            if(sequence[curr] >= sequence[next]) {
                failed++;
                if( curr > 0 && next < sequence.length - 1 ){
                    // Problem is not on head neither tail
                    // So check if removing one of 2 problematic numbers solves the issue
                    one_chance = false;
                    if( sequence[curr - 1] < sequence[next] )
                        one_chance = true ;
                    if ( sequence[curr] < sequence[next+1] )
                        one_chance = true ;
                    if( one_chance == false ) return false;
                }

            }
            if( failed > 1 ) return false;
        }
        return true;
    }

    boolean almostIncreasingSequence(int[] sequence) {
        int totalFailCount = 0;
        int lastIndex = sequence.length-1;

        for(int i = 0; i < lastIndex; i++){
            if(sequence[i] >= sequence[i+1]) {
                totalFailCount++;
                if( i > 0 && (i+1 < lastIndex) ){
                    boolean canFix = false;
                    if( sequence[i-1] < sequence[i+1] )
                        canFix = true ; // i is the problem
                    if ( sequence[i] < sequence[i+1+1] )
                        canFix = true ; // i+1 is the problem
                    if( canFix == false ) return false; //both i and i+1 are problem
                }
            }
            if( totalFailCount > 1 ) return false;
        }
        return true;
    }

}
