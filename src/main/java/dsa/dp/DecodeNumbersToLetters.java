package dsa.dp;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class DecodeNumbersToLetters {

    private static final List<String> LETTERS = IntStream.range(1, 27).mapToObj(Integer::toString).collect(Collectors.toList());

    public static int countDecodeWays(String digits) {
        // use numbers 1 to 26 to represent all alphabet letters
        int[] memo = new int[digits.length()];
        Arrays.fill(memo, -1);
        return dfs(0, memo, digits);
    }

    private static int dfs(int i, int[] memo, String digits) {
        if (i == digits.length()) return 1;

        if (memo[i] == -1) {
            int ways = 0;
            String remaining = digits.substring(i);
            for (String prefix : LETTERS) {
                if (remaining.startsWith(prefix)) {
                    ways += dfs(i + prefix.length(), memo, digits);
                }
            }
            memo[i] = ways;
        }

        return memo[i];
    }
    // Driver code  
	public static void main(String[] args) {

        String[] inputs = {"12", "123", "11223"};
		for (int i = 0; i<inputs.length; i++) {
			System.out.println("Decode ways : " + DecodeNumbersToLetters.countDecodeWays(inputs[i]));
		}
	}
}