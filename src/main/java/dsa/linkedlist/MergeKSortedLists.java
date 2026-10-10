package dsa.linkedlist;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MergeKSortedLists {

    public LinkedListNode mergeKLists(LinkedListNode[] lists) {
        if(lists==null||lists.length==0)
            return null;

        PriorityQueue<LinkedListNode> queue = new PriorityQueue<LinkedListNode>(new Comparator<LinkedListNode>(){
            public int compare(LinkedListNode l1, LinkedListNode l2){
                return l1.data - l2.data;
            }
        });

        LinkedListNode head = new LinkedListNode(0);
        LinkedListNode p = head;

        for(LinkedListNode list: lists){
            if(list!=null)
                queue.offer(list);
        }

        while(!queue.isEmpty()){
            LinkedListNode n = queue.poll();
            p.next = n;
            p=p.next;

            if(n.next!=null)
                queue.offer(n.next);
        }

        return head.next;

    }
}
