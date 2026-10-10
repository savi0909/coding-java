package dsa.tree;

import java.util.*;
import java.util.stream.Collectors;
class LevelOrderTraversal {
    public static List<List<NodeLevel>> levelOrderTraversal(NodeLevel root) {
        List<List<NodeLevel>> res = new ArrayList<>();
        if (root == null) return res;

        Deque<NodeLevel> queue = new ArrayDeque<>();
        queue.add(root);  // at least one element in the queue to kick start bfs
        while (queue.size() > 0) {  // as long as there is element in the queue
            int n = queue.size();
            List<NodeLevel> newLevel = new ArrayList<>();
            // dequeue each node in the current level
            for (int i = 0; i < n; i++) {
                NodeLevel node = queue.pop();
                newLevel.add(node);
                // enqueue non-null children
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            res.add(newLevel);
        }
        return res;
    }
    // Driver code  
	public static void main(String[] args) {

        String[] inputs = {"1 2 4 x 7 x x 5 x x 3 x 6 x x","0 x x"};
		for (int i = 0; i<inputs.length; i++) {
            NodeLevel root = NodeLevel.buildTree(Arrays.stream(inputs[i].split(" ")).iterator());
            List<List<NodeLevel>> actual_output = LevelOrderTraversal.levelOrderTraversal(root);
            String[] result = new String[inputs.length];
            String actualData = "";
            for (int j = 0; j< actual_output.size() ;j++) {
                List<NodeLevel> level = actual_output.get(j);
                actualData  += "["+level.stream().map(node -> Integer.toString(node.val)).collect(Collectors.joining(","))+"]";
            }
			System.out.println("Binary tree level order traversal : [" +actualData+"]");
		}
	}
}

class NodeLevel {
    int val;
    NodeLevel left, right;

    public NodeLevel(int val) {
        this.val = val;
    }

    public static NodeLevel buildTree(Iterator<String> iter) {
        String nxt = iter.next();
        if (nxt.equals("x")) return null;
        NodeLevel node = new NodeLevel(Integer.parseInt(nxt));
        node.left = buildTree(iter);
        node.right = buildTree(iter);
        return node;
    }
}