package lld.cache.listbased;// Linked list operations
// LinkedListNode(int data) 
// LinkedListNode(int key, int data)
// LinkedListNode(int data, LinkedListNode next)
// LinkedListNode(int data, LinkedListNode next, LinkedListNode arbitrary_pointer)

import dsa.linkedlist.LinkedListNode;
import java.util.HashMap;
import  java.util.LinkedList;
import java.util.ListIterator;

class LRUCache {
  int capacity;
  HashMap<Integer, LinkedListNode> cache;
  java.util.LinkedList<LinkedListNode> cacheVals;

  public LRUCache(int capacity) {
    this.capacity = capacity;
    cache = new HashMap<Integer, LinkedListNode>(capacity);
    cacheVals = new LinkedList<LinkedListNode>();
  }

  LinkedListNode Get(int key) {
    if(!cache.containsKey(key)){
      return null;
    }
    else {
      int value = cache.get(key).data;
      cacheVals.remove(cache.get(key));
      LinkedListNode node = new LinkedListNode(key, value);
      cacheVals.addLast(node);
      return node;
    }
  }

  void Set(int key, int value) {
    if (!cache.containsKey(key)){
      evictIfNeeded();
      LinkedListNode newNode = new LinkedListNode(key, value);
      cacheVals.addLast(newNode);
      cache.put(key, newNode);
    }
    else {
      cacheVals.remove(cache.get(key));
      LinkedListNode newNode = new LinkedListNode(key, value);
      cacheVals.addLast(newNode);
      cache.put(key, newNode);
    }
  }

  void evictIfNeeded(){
    if(cacheVals.size() >= capacity) {
      LinkedListNode node = cacheVals.remove();
      cache.remove(node.key);
    }
  }

  void print() {
    ListIterator<LinkedListNode> iterator = cacheVals.listIterator(0);
    while(iterator.hasNext()){
      LinkedListNode node = iterator.next();
      System.out.print("(" + node.key + "," + node.data + ")");  
    }
    System.out.println("");
  }
  public static void main(String[] args){
    LRUCache cache = new LRUCache(3);
    System.out.print("The most recently watched titles are: (key, value)");
    cache.Set(10,20);
    cache.print();

    cache.Set(15,25);
    cache.print();

    cache.Set(20,30);
    cache.print();

    cache.Set(25,35);
    cache.print();

    cache.Set(5,40);
    cache.print();

    cache.Get(25);
    cache.print();
  }
}