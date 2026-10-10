package dsa.tree.rightview;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class Solution {

    public static List<Node> getRightSideView(Node root) {
        List<Node> res = new ArrayList<>();
        if (root == null) return res;

        Deque<Node> queue = new ArrayDeque<>();
        queue.add(root);  // at least one element in the queue to kick start bfs

        while (queue.size() > 0) {  // as long as there is element in the queue
            int n = queue.size();  // number of nodes in current level
            res.add(queue.peek());  // only append the first node we encounter since it's the rightmost
            // dequeue each node in the current level
            for (int i = 0; i < n; i++) {
                Node node = queue.pop();
                // we add right children first so it'll pop out of the queue first
                if (node.right != null) queue.add(node.right);
                if (node.left != null) queue.add(node.left);
            }
        }

        return res;
    }


    public static List<Node> getRightSideViewMy(Node root) {
        List<Node> res = new ArrayList<>();
        if (root == null) return res;

        Deque<Node> queue = new ArrayDeque<>();
        queue.add(root);  // at least one element in the queue to kick start bfs

        while (queue.size() > 0) {  // as long as there is element in the queue
            int n = queue.size();  // number of nodes in current level
          //  res.add(queue.peek());  // only append the first node we encounter since it's the rightmost
            // dequeue each node in the current level
            Node last = null;
            for (int i = 0; i < n; i++) {
                Node node = queue.pop();
                last = node;
                // we add right children first so it'll pop out of the queue first
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);

            }
            res.add(last);
        }

        return res;
    }
      // Driver code  
	public static void main(String[] args) {

        String[] inputs ={"1 2 4 x 7 x x 5 x x 3 x 6 x x","0 x x"};
		for (int i = 0; i<inputs.length; i++) {
			Node root = Node.buildTree(Arrays.stream(inputs[i].split(" ")).iterator());
			System.out.println("Binary tree right side : " + Solution.getRightSideView(root)
            .stream().map(node -> Integer.toString(node.val))
            .collect(Collectors.joining(" ")));
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