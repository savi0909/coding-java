package interviews;

class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solution("dfgfhdgfmh"));
    }

    public int solution(String s) {
        char[] seq = s.toCharArray();
        int[] teams = new int[26];
        int[] loc = new int[26];

        // Count occurrences and record first position
        for (int i = 0; i < seq.length; i++) {
            int indexVal = seq[i] - 'a';
            teams[indexVal]++;
            if (teams[indexVal] == 1) {
                loc[indexVal] = i;
            }
        }
        int min = Integer.MAX_VALUE;
        // Find the leftmost character that occurs exactly once
        for (int i = 0; i < 26; i++) {
            if (teams[i] == 1 && loc[i] < min) {
                min = loc[i];
            }
        }

        return min == Integer.MAX_VALUE ? -1 : min;
    }
}