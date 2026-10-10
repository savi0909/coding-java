package dsa.dp;

public class RodCutter {



    private static int counter = 0;
    public static void main(String[] args){
        int s = 15;
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] vals = {1,5,8,9,10,17,17,29};
        Node node = new Node("a","B",6);
        Node node1 = null;

        node1.left = node;

        System.out.println(new RodCutter().maxVal(vals,new int[9],8));
    }

    public int maxVal(int[] prices, int[] best, int length){
        if(length == 0){
            return 0;
        }
        if(best[length] > 0) {
            System.out.println("Length " + length + " --" + ++counter);
            return best[length];
        }

        int max_val=-1;
        for(int i = 1; i<=length; i++){
            int part = maxVal(prices, best, length-i);
            int temp = prices[i-1] + part;
            if(temp > max_val){
                max_val = temp;
            }
        }
        best[length] = max_val;
        return max_val;
    }


    private static class Node {
        private String key;           // sorted by key
        private String val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(String key, String val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }
}
