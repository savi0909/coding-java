package dsa.tree;

import java.util.Stack; 

class Node 
{ 
    int data; 
    NodeLevel left, right;
  
    public Node(int item) 
    { 
        data = item; 
        left = right = null; 
    } 
} 

@SuppressWarnings("unchecked")
class InOrderTraversal
{
    NodeLevel root;
    public static void inOrderTraversal(NodeLevel root)
    {
      Stack<NodeLevel> stack = new Stack();
      NodeLevel curr = root;
      while (!stack.empty() || curr != null)
      {
        if (curr != null)
        {
          stack.push(curr);
          curr = curr.left;
        }
        else
        {
          curr = stack.pop();
          System.out.print(curr.val + " ");
          curr = curr.right;
        }
      }
    }

    public static void preOrderTraversal(NodeLevel root) {
        Stack<NodeLevel> stack = new Stack();
        NodeLevel curr = root;
        while (!stack.empty() || curr != null) {
            if (curr != null) {
                System.out.print(curr.val + " ");
                stack.push(curr.right);
                stack.push(curr.left);
                curr = null;
            } else {
                curr = stack.pop();
            }
        }
    }

    public static void postOrderTraversal(NodeLevel root) {
        Stack<NodeLevel> stack = new Stack();
        NodeLevel curr = root;
        while (!stack.empty() || curr != null) {
            if (curr != null) {

                stack.push(curr);
                stack.push(curr.left);
                curr = curr.right;
            } else {
                curr = stack.pop();
;
            }
        }
    }

    public static void main( String args[] )
    {
        InOrderTraversal tree = new InOrderTraversal();
        tree.root = new NodeLevel(1);
        tree.root.left = new NodeLevel(2);
        tree.root.right = new NodeLevel(3);
        tree.root.left.left = new NodeLevel(4);
        tree.root.left.right = new NodeLevel(5);
        tree.preOrderTraversal(tree.root);
        System.out.println("");
        tree.inOrderTraversal(tree.root);
        System.out.println("");
        tree.postOrderTraversal(tree.root);
    }
}