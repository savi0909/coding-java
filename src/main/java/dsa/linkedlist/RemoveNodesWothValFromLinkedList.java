package dsa.linkedlist;

public class RemoveNodesWothValFromLinkedList {

    LinkedListPalindrome<Integer> removeKFromList(LinkedListPalindrome<Integer> l, int k) {
        LinkedListPalindrome<Integer> head = l, current = l;
        LinkedListPalindrome<Integer> left = null;

        while (current != null) {
            if (current.value != k) {
                left = current;
            } else {
                LinkedListPalindrome<Integer> right = current.next;
                if (left != null) {
                    left.next = right;
                } else {
                    head = right;
                }
            }
            current = current.next;
        }
        return head;
    }

}
