package dsa.backtracking;

import java.util.*;

class PermutationsOfLetters {
    public static List<List<Character>> permute(char[] letters) {
        List<List<Character>> res = new ArrayList<>();
        dfs(new ArrayList<>(), new boolean[letters.length], res, letters);
        return res;
    }

    private static void dfs(List<Character> path, boolean[] used, List<List<Character>> res, char[] letters) {
        if (path.size() == used.length) {
            // make a deep copy since otherwise we'd be append the same list over and over
            res.add(new ArrayList<Character>(path));
            return;
        }

        for (int i = 0; i < used.length; i++) {
            // skip used letters
            if (used[i]) continue;
            // add letter to permutation, mark letter as used
            path.add(letters[i]);
            used[i] = true;
            dfs(path, used, res, letters);
            // remove letter from permutation, mark letter as unused
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
         // Driver code  
	public static void main(String[] args) {

        String[] inputs = {"ab","abc"};
		for (int i = 0; i<inputs.length; i++) {
			System.out.println("Permutations : " + PermutationsOfLetters.permute(inputs[i].toCharArray()));
		}
	}
}