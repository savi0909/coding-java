package dsa.linkedlist;

class LinkedListPalindrome<T> {
    LinkedListPalindrome(T x) {
        value = x;
    }

    T value;
    LinkedListPalindrome<T> next;

    boolean isListPalindrome(LinkedListPalindrome<Integer> l) {

        LinkedListPalindrome<Integer> head = l;
        if(head == null || head.next==null)
            return true;
        LinkedListPalindrome fast = head;
        LinkedListPalindrome slow = head;

        while(fast.next!=null && fast.next.next!=null){
            fast = fast.next.next;
            slow = slow.next;
        }

        LinkedListPalindrome secondHead = slow.next;
        slow.next = null;

        //reverse second part of the list
        LinkedListPalindrome p1 = secondHead;
        LinkedListPalindrome p2 = p1.next;

        while(p1!=null && p2!=null){
            LinkedListPalindrome temp = p2.next;
            p2.next = p1;
            p1 = p2;
            p2 = temp;
        }
        secondHead.next = null;

        LinkedListPalindrome p = (p2==null?p1:p2);
        LinkedListPalindrome q = head;
        while(p!=null){
            if(!p.value.equals(q.value))
                return false;

            p = p.next;
            q = q.next;

        }

        return true;
    }

}

