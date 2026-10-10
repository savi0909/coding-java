package dsa.stack;

public class MinStack {
    int maxSize;
    Stack<Integer> mainStack;
    Stack<Integer> minStack;
    //constructor

    public MinStack(int maxSize) {
        this.maxSize = maxSize;
        mainStack = new Stack<>(maxSize);
        minStack = new Stack<>(maxSize);
    }
    //removes and returns value from stack
    public int pop(){
        minStack.pop();
        return mainStack.pop();
    }
    //pushes value into the stack
    public void push(Integer value){
        //1. Push value in mainStack and check value with the top value of maxStack
        //2. If value is greater than top, then push top in maxStack
        //else push value in maxStack
        mainStack.push(value);
        if(!minStack.isEmpty() && minStack.top() < value)
            minStack.push(minStack.top());
        else
            minStack.push(value);
    }



    //returns minimum value in O(1)
    public int min(){
        if(!minStack.isEmpty()){
            return minStack.top();
        }
        return Integer.MIN_VALUE;
    }
}