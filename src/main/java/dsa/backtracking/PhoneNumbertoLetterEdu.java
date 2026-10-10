package dsa.backtracking;

import java.util.*;
import java.util.Map;

class PhoneNumbertoLetterEdu {
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
    public static List<String> letterCombinationsOfPhoneNumber(String digits) {
        List<String> res = new ArrayList<>();
        dfs(new StringBuilder(), res, digits.toCharArray());
        return res;
    }
    private static void dfs(StringBuilder path, List<String> res, char[] digits) {
        if (path.length() == digits.length) {
            res.add(path.toString());
            return;
        }
        char next_digit = digits[path.length()];
        for (char letter : KEYBOARD.get(next_digit)) {
            path.append(letter);
            dfs(path, res, digits);
            path.deleteCharAt(path.length() - 1);
        }
    }
    // Driver code  
	public static void main(String[] args) {
        String[] inputs = {"56", "23", "235"};
        for(int i = 0; i < inputs.length; i++) {
            System.out.println("Letter combinations of phone number : " + PhoneNumbertoLetterEdu.letterCombinationsOfPhoneNumber(inputs[i]));
        }
    }
}