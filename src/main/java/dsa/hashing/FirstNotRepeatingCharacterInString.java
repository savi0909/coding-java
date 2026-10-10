package dsa.hashing;

public class FirstNotRepeatingCharacterInString {

    char firstNotRepeatingCharacter(String s) {

        char[] pattern = s.toCharArray();
        char current = '_';

        for(int i = 0 ; i < pattern.length; i++){
            if(pattern[i] != '_') {
                current = pattern[i];
                for(int j = i+1; j<pattern.length; j++){
                    if(current == pattern[j]){
                        pattern[i] = pattern[j] = '_';
                    }
                }
            }
        }

        for(int i=0 ; i < pattern.length; i++){
            if(pattern[i] != '_') return pattern[i];
        }

        return '_';
    }


}
