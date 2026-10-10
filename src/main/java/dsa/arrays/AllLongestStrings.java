package dsa.arrays;

import java.util.ArrayList;
import java.util.List;

public class AllLongestStrings {

    public static void main(String[] args) {
        new AllLongestStrings().longestWord("");
    }

    String longestWord(String text) {
        int maxLength = 0;
        int wordLength = 0;
        String current = "";
        StringBuffer word = new StringBuffer();
        for(int i=0;i<text.length();i++){

            Character c = text.charAt(i);
            if(Character.isLetter(c)){
                word.append(c);
                wordLength++;
                if(wordLength > maxLength){
                    maxLength = wordLength;
                    current = word.toString();
                }
                continue;
            }
            wordLength = 0;
            word.setLength(0);
        }
        System.out.println(current);
        return current;
    }


    String[] allLongestStrings(String[] inputArray) {
        int[] lengths = new int[inputArray.length];
        int max = -1;
        int maxCount = 0;
        int index = 0;
        for( String str : inputArray){
            int len = str.length();
            lengths[index] = len;
            if(max < len) {
                max = len;
                maxCount=0;
            }
            if(max == len ) maxCount++;
            index++;
        }

        List<String> longestStrings = new ArrayList<String>(inputArray.length);
        for(index=0; index < lengths.length;index++ ){
            if(max == lengths[index]){
                longestStrings.add(inputArray[index]);
            }
        }
        return longestStrings.toArray(new String[1]);

    }
}
