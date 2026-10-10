package dsa.backtracking.maze;

public class App {

	public static void main(String[] args) {
	/*	I  I  I  I  X  I  X  I  I  X
		I  X  I  I  I  X  I  I  I  I
		I  I  I  I  X  I  I  X  X  I
		I  I  I  I  X  I  I  I  I  X
		I  I  I  I  I  I  X  I  I  I

		I  I  I  I  I  I  I  I  I  I
		I  I  I  I  I  I  I  I  I  I
		I  I  I  I  I  I  I  I  X  X
		I  I  X  X  I  I  X  X  X  I
		I  X  I  I  I  I  I  I  I  I*/
		
		int mazeTable[][] = { { 1, 1, 1, 1 ,0,1,0,1,1,0},
						      { 1, 0, 1, 1, 1,0,1,1,1,1},
						      { 1, 1, 1, 1, 0,1,1,0,0,1},
						      { 1, 1, 1, 1, 0,1,1,1,1,0},
						      { 1, 1, 1, 1, 1,1,0,1,1,1},
								{1, 1, 1, 1, 1,1,1,1,1,1},
								{1, 1, 1, 1, 1,1,1,1,1,1},
								{1, 1, 1, 1, 1,1,1,1,0,0},
								{1, 1, 0, 0, 1,1,0,0,0,1},
								{1, 0, 1, 1, 1,1,1,1,1,1}

					        };

		int size = 7;

		int mazeTable10[][] = new int[size][size];
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				int random_int = (int)Math.floor(Math.random()*(100));

				mazeTable10[i][j] = random_int > 20 ? 1 : 0;

			}
		}
		mazeTable10[0][0] = 1; mazeTable10[size-1][size-1] = 1;

		Maze mazeProblem = new Maze(mazeTable10);
		mazeProblem.solveMaze();
		
	}


}
	/*I  I  I  I  I  I  I  I  I
		I  I  I  I  X  I  I  I  I
		X  X  I  I  I  I  I  I  I
		I  X  I  I  I  I  I  I  X
		I  I  I  I  I  X  I  I  X
		I  I  X  I  I  X  X  I  I
		I  I  X  I  I  I  I  I  I
		I  I  I  I  I  I  I  I  X
		I  I  X  X  I  I  I  I  I

		$  $  $  $  $  $  $  $  -
		$  $  $  $  -  $  $  $  $
		-  -  $  $  $  $  $  $  $
		-  -  $  $  $  $  $  $  -
		$  $  $  $  $  -  -  $  -
		$  $  -  $  $  -  -  $  $
		$  $  -  $  -  $  $  $  $
		$  $  $  $  $  $  $  $  -
		$  $  -  -  $  $  $  $  $
		Max Length Path=63
		Max Moves=56920736777*/