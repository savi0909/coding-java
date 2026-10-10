package dsa.twopointers;

import java.util.Arrays;

public class MoveZerostoLeft {

    public static void main(String[] args){

        int[] v =  new int[] {1, 10, 20, 0, 59, 63, 0, 88, 0};
        Arrays.stream(v).forEach(item  ->System.out.print(item + " ,"));
        System.out.println();
        new MoveZerostoLeft().move_zeros_to_left(v);

        Arrays.stream(v).forEach(item  ->System.out.print(item + " ,"));


    }

    public void move_zeros_to_left(int[] list){

        int len = list.length;
        int  write = len - 1;
        int read = len -1;

        while(read >= 0){
            if(list[read] != 0){
                list[write] = list[read];
                write--;
            }
            read--;
        }

        while( write>= 0){
            list[write] = 0;
            write--;

        }

    }

}
