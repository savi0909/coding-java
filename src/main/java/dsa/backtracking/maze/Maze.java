package dsa.backtracking.maze;

public class Maze {

	private int[][] mazeTable;
	private int[][] solutionTable;
	private int[][] maxSolutionTable;

	private boolean[][] deadEnd;
	private boolean[][] isVisited;
	private int mazeSize;

	private int[][][] allSolutionTable;

	private int count = 0;
	int maxLength = 0;
	private long maxTries = 0;

	public Maze(int[][] mazeTable) {
		this.mazeTable = mazeTable;
		this.mazeSize = mazeTable.length;
		this.solutionTable = new int[this.mazeSize][this.mazeSize];
		this.maxSolutionTable = new int[this.mazeSize][this.mazeSize];

		this.deadEnd = new boolean[this.mazeSize][this.mazeSize];
	//	this.allSolutionTable = new int[10000][this.mazeSize][this.mazeSize];
		this.isVisited = new boolean[this.mazeSize][this.mazeSize];
	}

	public void solveMaze() {

		showMaze();
		System.out.println("");
		solve(0, 0);

		showResult();

	}

	public boolean solve(int x, int y) {



		if ( isValid(x, y) && !isVisited[x][y]) {
			if ( isFinished(x, y)) {
				return true;
			}

			boolean solutionViaNode = false;
			isVisited[x][y] = true;
			solutionTable[x][y] = 1; // it is valid so it is part of the solution

			
			if (solve(x + 1, y)){
				copySolution();
			}

			if (solve(x - 1, y)){
				copySolution();
			}
			
			if (solve(x, y + 1)) {
				copySolution();
			}

			if (solve(x, y - 1)) { // If moving in x direction is not feasible: go up in y direction
				copySolution();
			}

			solutionTable[x][y] = 0; // All paths expose through this node, backtrack
			isVisited[x][y] = false; // this node is done to allow for alternate solutions
		}

		return false;
	}
	
	public boolean isFinished(int x, int y){
		
		if (x == this.mazeSize - 1 && y == this.mazeSize - 1) {
			solutionTable[x][y] = 1;
			return true;
		}
		
		return false;
	}
	
	public boolean isValid(int x, int y) {
		maxTries++;
		if( x<0 || x >= this.mazeSize ) return false;
		if( y<0 || y >= this.mazeSize ) return false;
		if( this.mazeTable[x][y] != 1 ) return false;
		
		return true;
	}

	public void copySolution() {
		int pathLength = 0;
		for (int i = 0; i < this.mazeSize; ++i) {
			for (int j = 0; j < this.mazeSize; ++j){
				// allSolutionTable[count][i][j] = solutionTable[i][j] ;
				if(solutionTable[i][j] == 1) pathLength++;
			}
		}

		if(pathLength > maxLength){
			maxLength = pathLength;
			for (int i = 0; i < this.mazeSize; ++i) {
				for (int j = 0; j < this.mazeSize; ++j){
						maxSolutionTable[i][j] = solutionTable[i][j];
				}
			}

		}
		// count++;
	}

	public void showAllResult() {

        int maxIndex = -1;

		for(int k =0; k < count; k ++){
			int pathLength = 0;
			System.out.println("Path = " + k);
			for (int i = 0; i < this.mazeSize; ++i) {
				for (int j = 0; j < this.mazeSize; ++j) {
					if (this.allSolutionTable[k][i][j] == 1) {
						System.out.print(" $ ");
						pathLength++;
					} else {
						System.out.print(" - ");
					}
				}
				System.out.println();
			}
		    System.out.println("***********************************");

			if(maxLength < pathLength) {
			   maxLength = pathLength;
			   maxIndex = k;
			}
		}

		System.out.println("Max Length Path=" + maxIndex + " length = " + maxLength);
		System.out.println("Max Moves=" + maxTries);
	}

	public void showMaze() {

		for (int i = 0; i < this.mazeSize; ++i) {
			for (int j = 0; j < this.mazeSize; ++j){
				if( this.mazeTable[i][j] == 1 ){
					System.out.print(" I ");
				}else{
					System.out.print(" X ");
				}
			}

			System.out.println();
		}
	}

	public void showResult() {
		
		for (int i = 0; i < this.mazeSize; ++i) {
			for (int j = 0; j < this.mazeSize; ++j){
				if( this.maxSolutionTable[i][j] == 1 ){
					System.out.print(" $ ");
				}else{
					System.out.print(" - ");
				}
			}
				
			System.out.println();
		}

		System.out.println("Max Length Path="+ maxLength);
		System.out.println("Max Moves=" + maxTries);
	}


}
