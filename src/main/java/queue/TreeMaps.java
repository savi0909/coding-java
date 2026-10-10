package queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class TreeMaps {
    public static void main(char[] args) {

    }
    public static void main(String[] args) {

                TreeMap<String, Integer> treeMap = new TreeMap<>();
                treeMap.put("banana", 3);
                treeMap.put("apple", 4);
                treeMap.put("pear", 1);
                treeMap.put("orange", 2);

                int x = 9;
                int y = 10;
        System.out.println(++x+y++);

                // treemap with value as a list of integer
                TreeMap<String, List<Integer>> treeMap2 = new TreeMap<>();

                treeMap2.put("banana", new ArrayList<>(Arrays.asList(1,2,3)));
                // print banana list
        List<Integer> banana = treeMap2.get("banana");
        banana.add(4);
        System.out.println(treeMap ); // Output


                treeMap2.put("apple", List.of(4,5,6));

                 // if orange is present, add 7 to the list or create a new list with 7
        List<Integer> orange = treeMap2.computeIfAbsent("orange", nextInt -> new ArrayList<>());
            // add 5,6,7 to the list
                orange.add(5);
                orange.add(6);
                orange.add(7);
                System.out.println("orange list: " + orange); // Output: orange list: [5, 6, 7]

        // Get the list for orange or create a new list if it doesn't exist'
                  List<Integer> orangeList = treeMap2.computeIfAbsent("orange", nextInt -> new ArrayList<>());
                orangeList.add(8);
                System.out.println("orange list: " + orangeList); // Output: orange list:



                // Attempt to fetch a nonexisting key
                Integer value = treeMap.get("apple");
                if (value == null) {
                    System.out.println("Value: Not found"); // Output: Value: Not found
                }

                treeMap.computeIfPresent("apple", (k, v) -> v + 1);
                 treeMap.firstKey();
                // Get the last key-value pair
                System.out.println("first entry: " + treeMap.firstEntry()); // Output: pear=1
              //  print last entry
              System.out.println("last entry: " + treeMap.lastEntry());

              //ceiling of orange
              System.out.println("ceiling of mango: " + treeMap.ceilingKey("mango"));
              //floor of mango
              System.out.println("floor of mango: " + treeMap.floorKey("mango"));
            }

}
