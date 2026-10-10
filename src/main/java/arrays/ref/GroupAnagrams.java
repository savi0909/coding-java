package arrays.ref;

import java.util.*;

/**
 * Two keying strategies, same O(n·k) grouping shape, different constant/assumption tradeoff.
 *
 * Critique:
 * - Complexity: sortedKey is O(n·k log k) (sort per word); countSignature is O(n·k)
 *   (linear scan + fixed 26-slot key). countSignature wins asymptotically for long
 *   words; sortedKey often wins in practice for short words (better constants, no
 *   26-length StringBuilder per call).
 * - The real tradeoff is alphabet assumption, not speed: countSignature hardcodes
 *   {@code charAt - 'a'} into a 26-slot array — any non-lowercase-ASCII input (e.g.
 *   "Eat") throws ArrayIndexOutOfBoundsException. sortedKey has no such ceiling; it
 *   works for any Comparable char range. Default to sortedKey unless the input
 *   alphabet is known and small.
 * - Edge cases covered (see main): empty array -> empty list, single empty string ->
 *   its own singleton group (both keys degenerate cleanly to "all zero"/"empty"),
 *   single word -> singleton group.
 * - Concurrency: N/A, pure function.
 * - Space: O(n·k) for the map either way.
 * - Not implemented: packing the 26 counts into a single long (5 bits/letter) avoids
 *   the String/StringBuilder allocation for the key entirely — standard "make it
 *   faster" follow-up, skipped here for correctness-first clarity.
 */
public class GroupAnagrams {

    /** Key = sorted chars. O(k log k) per word, any alphabet. */
    public static List<List<String>> groupBySortedKey(String[] words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : words) {
            char[] chars = w.toCharArray();
            Arrays.sort(chars);
            groups.computeIfAbsent(new String(chars), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(groups.values());
    }

    /** Key = 26-slot char-count signature. O(k) per word, lowercase a-z only. */
    public static List<List<String>> groupByCountSignature(String[] words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : words) {
            groups.computeIfAbsent(countSignatureTreMap(w), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(groups.values());
    }

    private static String countSignature(String w) {
        int[] counts = new int[26];
        for (int i = 0; i < w.length(); i++) {
            counts[w.charAt(i) - 'a']++;
        }
        StringBuilder key = new StringBuilder();
        for (int c : counts) {
            key.append(c).append(',');
        }
        return key.toString();
    }

    // write concise functional equivalent of countSignatureTreMap


    private static String countSignatureTreMap(String w) {

        Map<Character, Integer> countMap = new TreeMap<>();

        for (int i = 0; i < w.length(); i++) {
            countMap.put(w.charAt(i), countMap.getOrDefault(w.charAt(i), 0) + 1);

        }
        StringBuilder key = new StringBuilder();
        for (Character c : countMap.keySet()) {
            // add comma to separate each key, plus add value of count

            key.append(c).append(':').append(countMap.get(c)).append(',');
        }
        return key.toString();
    }

    public static void main(String[] args) {
        String[][] tests = {
                {"eater", "teare", "tan", "ate", "nat", "bat"},
                {},
                {""},
                {"a"}
        };
        for (String[] t : tests) {
            System.out.println(Arrays.toString(t)
                    + " -> sorted=" + groupBySortedKey(t)
                    + " | count=" + groupByCountSignature(t));
        }
    }
}
