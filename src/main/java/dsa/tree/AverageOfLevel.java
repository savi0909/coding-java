package dsa.tree;

import java.util.LinkedList;
import java.util.Queue;

public class AverageOfLevel {

     private void averageAtLevel(NodeLevel root) {
         int level = 0;
         Queue<NodeLevel> queue = new LinkedList<>();
         queue.add(root);
         while (!queue.isEmpty()) {
             int level_size = queue.size(); // keeps track of nodes in a level. for root node level size is 1

             // inner loop to traverse the nodes on the queue on a level. queue size will be greater then level size while nodes from
             // parent level are processed and children added to the queue
             // once all parent nodes are processed and removed, all the child nodes are remaining on the queue.
             while (level_size > 0) {
                 NodeLevel temp = queue.poll();
                 if (temp.right != null) queue.add(temp.right);
                 if (temp.left != null) queue.add(temp.left);
                 level_size--;
             }
             level++;
         }
     }


}
