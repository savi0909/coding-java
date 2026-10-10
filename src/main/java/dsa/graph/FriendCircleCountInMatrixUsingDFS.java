package dsa.graph;

class FriendCircleCountInMatrixUsingDFS {

  public static void DFS (boolean[][] friends, int n, boolean[] visited, int v) {
    for (int i = 0; i < n; ++i) {

        // A user is in the friend circle if he/she is friends with the user represented by
        // user index and if he/she is not already in a friend circle
        if (friends[v][i] == true && !visited[i] && i != v) {
            visited[i] = true;
            DFS(friends, n, visited, i);
        }
    }
  }

  public static int friendCircles(boolean[][] friends, int n) {
    if (n == 0) {
        return 0;
    }
 
    int numCircles = 0;     //Number of friend circles
    
    //Keep track of whether a user is already in a friend circle
    boolean visited[] = new boolean[n];

    for (int i=0;i < n; i++){
      visited[i] = false;
    }
    
    //Start with the first user and recursively find all other users in his/her
    //friend circle. Then, do the same thing for the next user that is not already
    //in a friend circle. Repeat until all users are in a friend circle. 
    for (int i = 0; i < n; ++i) {
        if (!visited[i]) {
            visited[i] = true;
            DFS(friends, n, visited, i); //Recursive step to find all friends
            numCircles = numCircles + 1;
        }
    }
    
    return numCircles;
  }

  public static void main(String args[]) 
  { 
      int n = 4;
      boolean[][] friends = {
        {true,true,false,false},
        {true,true,true,false},
        {false,true,true,false},
        {false,false,false,true}
      };
     System.out.println("Number of friends circles: " + friendCircles(friends, n)); 
  } 

}