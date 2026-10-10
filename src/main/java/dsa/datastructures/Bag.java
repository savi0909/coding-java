package dsa.datastructures;

import java.util.Arrays;
import java.util.Iterator;

public class Bag<T> implements Iterable<T>{
    private T[] items;
    private int count;

    public Bag(int initialCapacity){
        items = (T[]) new Object[initialCapacity];
        count = 0;
    }

   /* public T take(){
        if(isEmpty()) throw new IllegalArgumentException("empty");
        T item = items[count-1];
        items[count-1] = null; //
        return item;
    }*/

    private boolean isEmpty() {
        return count == 0 ;
    }

    public void put(T item){
        if(count == items.length) resize(2* items.length);
        items[count++] = item; // 0 to n-1, n
    }

    private void resize(int n) {
        assert n > count;
        items = Arrays.copyOf(items,n);
        T[] copy = (T[]) new Object[n];

        for(int k =0 ; k < items.length ; k++){
              copy[k] = items[k];
        }
        items = copy;

    }

    public static void main(String[] args) {
        Bag<Integer> bag = new Bag<>(10);
        bag.put(5);
        bag.put(6);bag.put(7);

        bag.put(5);
        bag.put(6);bag.put(7);
        bag.put(5);
        bag.put(6);bag.put(7);
        bag.put(5);
        bag.put(6);bag.put(7);

        Iterator<Integer> iterator = bag.iterator();

        while(iterator.hasNext()){
            Integer item = iterator.next();
            System.out.println(item);
        }

       /* Bag<String> bag2 = new Bag<>(10);
        bag2.put("hi");*/
    }

    @Override
    public Iterator<T> iterator() {
        return new BagIterator<>();
    }


    private class BagIterator<T> implements Iterator<T> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < count;
        }

        @Override
        public T next() {
            if(hasNext()){
                T item = (T) items[i];
                i++;
                return item;
            }
            return null;
        }
    }
}
