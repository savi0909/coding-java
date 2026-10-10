package dsa.backtracking;

import java.util.*;
import java.util.Map;

class PhoneNumbertoLetter {
    private static final Map<Character, char[]> KEYBOARD = new HashMap<>();
    static {
        KEYBOARD.put('2', "abc".toCharArray());
        KEYBOARD.put('3', "def".toCharArray());
        KEYBOARD.put('4', "ghi".toCharArray());
        KEYBOARD.put('5', "jkl".toCharArray());
        KEYBOARD.put('6', "mno".toCharArray());
        KEYBOARD.put('7', "pqrs".toCharArray());
        KEYBOARD.put('8', "tuv".toCharArray());
        KEYBOARD.put('9', "wxyz".toCharArray());
    }

    public static void main(String[] args) {
        letterCombinationsOfPhoneNumber("516");

    }

    public static List<String> letterCombinationsOfPhoneNumber(String digits) {

        ArrayList<String> res = new ArrayList<>();
        String path = "";
        char[] digitsChar = digits.toCharArray();
        letterCombinationsOfPhoneNumber(path, digitsChar,0, res );

        res.stream().forEach( item -> System.out.println(item));

        return  res;
    }

    public static void letterCombinationsOfPhoneNumber(String path , char[] digits, int pos, ArrayList<String> res) {

        if (path.length() == digits.length) {
            res.add(path);
            return;
        }
        char[] chars = KEYBOARD.get(digits[pos]);

        // To handle 1 which has no characters
        if (chars == null) {
            path = path + '$';
            letterCombinationsOfPhoneNumber(path, digits, pos + 1, res);
        }
        else {
            for (int j = 0; j < chars.length; j++) {
                path = path + chars[j];
                letterCombinationsOfPhoneNumber(path, digits, pos + 1, res);
                path = path.substring(0, path.length() - 1);
            }
        }
    }
}