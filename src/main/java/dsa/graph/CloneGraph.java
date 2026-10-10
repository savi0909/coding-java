package dsa.graph;

import java.util.*;
import java.lang.*;

class Pair<X, Y> {
  public X first;
  public Y second;
  public Pair(X first, Y second) {
    this.first = first;
    this.second = second;
  }
}

class CloneGraph {
// if there is an edge from x to y
// that means there must be an edge from y to x
// and there is no edge from a node to itself
// hence there can maximim of (nodes * nodes - nodes) / 2 edgesin this graph
  static ArrayList<Node> createTestGraphDirected(int nodes_count, int edges_count) {
    ArrayList<Node> vertices = new ArrayList<Node>();
    for (int i = 0; i < nodes_count; ++i) {
      vertices.add(new Node(i));
    }

    List<Pair<Integer, Integer>> all_edges = new ArrayList<Pair<Integer, Integer>>();
    for (int i = 0; i < vertices.size(); ++i) {
      for (int j = i + 1; j < vertices.size(); ++j) {
        all_edges.add(new Pair<Integer, Integer>(i, j));
      }
    }

    Collections.shuffle(all_edges);

    for (int i = 0; i < edges_count && i < all_edges.size(); ++i) {
      Pair<Integer, Integer> edge = all_edges.get(i);
      vertices.get(edge.first).friends.add(vertices.get(edge.second));
      vertices.get(edge.second).friends.add(vertices.get(edge.first));
    }

    return vertices;
  }

  static void printGraph(List<Node> vertices) {
    for (Node n : vertices) {
      System.out.print(n.data + ": {");
      for (Node t : n.friends) {
        System.out.print(t.data + " ");
      }
      System.out.println();
    }
  }

  static void printGraph(Node root, HashSet<Node> visited_nodes) {
    if (root == null || visited_nodes.contains(root)) {
      return;
    }

    visited_nodes.add(root);

    System.out.print(root.data + ": {");
    for (Node n : root.friends) {
      System.out.print(n.data + " ");
    }
    System.out.println("}");

    for (Node n : root.friends) {
      printGraph(n, visited_nodes);
    }
  }

  static void printGraph(Node root) {
    HashSet<Node> visited_nodes = new HashSet<Node>();
    printGraph(root, visited_nodes);
  }

  static boolean are_graphs_equal_rec(Node root1, Node root2, HashSet<Node> visited) {
    if (root1 == null && root2 == null) {
      return true;
    }

    if (root1 == null || root2 == null) {
      return false;
    }

    if (root1.data != root2.data) {
      return false;
    }

    if (root1.friends.size() != root2.friends.size()) {
      return false;
    }

    for (Node nbr1 : root1.friends) {
      boolean found = false;
      for (Node nbr2 : root2.friends) {
        if (nbr1.data == nbr2.data) {
          if (!visited.contains(nbr1)) {
            visited.add(nbr1);
            are_graphs_equal_rec(nbr1, nbr2, visited);
          }
          found = true;
          break;
        }
      }
      if (!found) {
        return false;
      }
    }

    return true;
  }
}

