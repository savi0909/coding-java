package queue;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class QueueDemo {

    public static void main(String[] args) {
        // Create a queue and add items
        Queue<String> q = new LinkedList<>();
        q.add("Apple");
        q.add("Banana");
        q.add("Cherry");

        // Remove an item
        while (!q.isEmpty()){
            System.out.println(q.peek());
            q.remove();
        } // Expects "Apple"


        // Create a deque
        Deque<String> d = new ArrayDeque<>();
        d.addLast("Apple");
        d.addLast("Banana");
        d.addLast("Cherry");

        // Rotate the deque to the right by 1 place

            d.addFirst(d.removeLast());


        System.out.println(d);

    }


}
