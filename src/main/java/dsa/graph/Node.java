package dsa.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class Node {
  public int data;
  public List<Node> friends = new ArrayList<Node>();
  public Node(int d) {data = d;}
}

class Solution {
  private static Node clone_rec(Node root, HashMap<Node, Node> nodes_completed) {
    if (root == null) {
      return null;
    }

    Node newNode = new Node(root.data);
    nodes_completed.put(root, newNode);

    for (Node p : root.friends) {
      Node x = nodes_completed.get(p);
      if (x == null){
        newNode.friends.add(clone_rec(p, nodes_completed));
      } else {
        newNode.friends.add(x);
      }
    }
    return newNode;
  }

  public static Node clone(Node root) {
    HashMap<Node, Node> nodes_completed = new HashMap<Node, Node>();
      
    return clone_rec(root, nodes_completed);
  }

  public static void main(String[] args) {
    ArrayList<Node> vertices = CloneGraph.createTestGraphDirected(7, 18);

    CloneGraph.printGraph(vertices.get(0));

    Node cp = clone(vertices.get(0));
    System.out.println();
    System.out.println("After copy.");
    CloneGraph.printGraph(cp);

    HashSet<Node> set = new HashSet<Node>();
    System.out.println(CloneGraph.are_graphs_equal_rec(vertices.get(0), cp, set));
  }  
}