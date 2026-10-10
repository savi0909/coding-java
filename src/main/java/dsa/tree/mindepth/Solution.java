package dsa.tree.mindepth;

import java.io.IOException;
import java.util.*;
class Solution {
    public static int getBinaryTreeMinDepth(Node root) {
        Deque<Node> queue = new ArrayDeque<>();  // at least one element in the queue to kick start bfs
        int depth = -1;  // we start from -1 because popping root will add 1 depth
        queue.add(root);

        while (queue.size() > 0) {  // as long as there is element in the queue
            depth++;
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                Node node = queue.pop();
                if (node.left == null && node.right == null) {
                    // found leaf node, early return
                    return depth;
                }
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);                
            }
        }

        return depth;
    }
      // Driver code  
	public static void main(String[] args) {

        String[] inputs ={"1 2 4 x 7 x x 5 x x 3 x 6 x x","0 x x"};
		for (int i = 0; i<inputs.length; i++) {
			Node root = Node.buildTree(Arrays.stream(inputs[i].split(" ")).iterator());
			System.out.println("Binary tree min depth : " + Solution.getBinaryTreeMinDepth(root));
		}
	}
}
// Driver code
class Node {
    int val;
    Node left, right;

    public Node(int val) {
        this.val = val;
    }

    public static Node buildTree(Iterator<String> iter) {
        String nxt = iter.next();
        if (nxt.equals("x")) return null;
        Node node = new Node(Integer.parseInt(nxt));
        node.left = buildTree(iter);
        node.right = buildTree(iter);
        return node;
    }
}