package dsa.tree;

import java.util.*;
import java.util.stream.Collectors;
class ZigZagLevelOrderTraversal {
    public static List<List<ZigZag>> zigZagLevelOrderTraversal(ZigZag root) {
        List<List<ZigZag>> res = new ArrayList<>();
        if (root == null) return res;

        Deque<ZigZag> queue = new ArrayDeque<>();
        queue.add(root); // at least one element in the queue to kick start bfs
        boolean leftToRight = true;
        while (queue.size() > 0) {  // as long as there is element in the queue        
            List<ZigZag> level = new ArrayList<>();
            int size = queue.size();            
            for (int i = 0; i < size; i++) {                
                //poll from q
                ZigZag zigZag = queue.pop();
                level.add(zigZag);
                //check if left not null add to queue
                if (zigZag.left != null) {
                    queue.add(zigZag.left);
                }                    
                if (zigZag.right != null) {
                    queue.add(zigZag.right);
                }                
            }
            if (!leftToRight) {
                Collections.reverse(level);
            }
            res.add(level);
            leftToRight = !leftToRight;
        }
        return res;
    }
    // Driver code  
	public static void main(String[] args) {

        String[] inputs = {"1 2 4 x 7 x x 5 x 8 x x 3 x 6 x x"};
		for (int i = 0; i<inputs.length; i++) {
			ZigZag root = ZigZag.buildTree(Arrays.stream(inputs[i].split(" ")).iterator());
            List<List<ZigZag>> actual_output = ZigZagLevelOrderTraversal.zigZagLevelOrderTraversal(root);
            String actualData = "";
    
            for (int j = 0; j< actual_output.size() ;j++) {   
                List<ZigZag> level = actual_output.get(j);
                actualData  += "["+level.stream().map(node -> Integer.toString(node.val)).collect(Collectors.joining(","))+"]";
            }
			System.out.println("Lowest common ancestor : [" + actualData+"]");
		}
	}
}
class ZigZag {
    int val;
    ZigZag left, right;

    public ZigZag(int val) {
        this.val = val;
    }

    public static ZigZag buildTree(Iterator<String> iter) {
        String nxt = iter.next();
        if (nxt.equals("x")) return null;
        ZigZag zigZag = new ZigZag(Integer.parseInt(nxt));
        zigZag.left = buildTree(iter);
        zigZag.right = buildTree(iter);
        return zigZag;
    }
}