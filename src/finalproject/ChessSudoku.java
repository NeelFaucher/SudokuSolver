
package finalproject;

		import java.io.FileInputStream;
		import java.io.InputStream;
		import java.util.Arrays;
		import java.util.HashSet;


public class ChessSudoku
{
	/* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
	 * a standard Sudoku puzzle, SIZE is 3 and N is 9.
	 */
	public int SIZE, N;

	/* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
	 * not yet been revealed are stored as 0.
	 */
	public int grid[][];

	/* Booleans indicating whether of not one or more of the chess rules should be
	 * applied to this Sudoku.
	 */
	public boolean knightRule;
	public boolean kingRule;
	public boolean queenRule;

	// Indices of the rows and col of the 2D grid used for iteration through board
	// private int row = 0, col = 0;

	// Field that stores the same Sudoku puzzle solved in all possible ways
	public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();

	/* The solve() method should remove all the unknown characters ('x') in the grid
	 * and replace them with the numbers in the correct range that satisfy the constraints
	 * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL
	 * possible solutions and store them in the field named solutions. */
	public void solve(boolean allSolutions) {
		// replacing special/unknown characters with 0 (i = row, j = col)
		for (int i = 0 ; i < N ; i++) {
			for (int j = 0 ; j < N ; j++) {
				if (grid[i][j] < 1 || grid[i][j] > N) {
					grid[i][j] = 0;
				} else {
					break;
				}
			}
		}
		if (allSolutions == true) {
			chessSudokuSolver(true);

		} else if (allSolutions == false) {
			chessSudokuSolver(false);
		}
	}

	private boolean chessSudokuSolver(Boolean allSolutions) {
		int rowPos = (N + 1), colPos = (N + 1);// setting rowPos and colPos to a value not between 0 and N (inclusive) to check if there are 0s in the grid

		// storing the row and column index positions of cell with value '0' --> rowPos, colPos
		int row = 0, col = 0;
		while (row < N) {
			col = 0;
			while (col < N) {
				if (grid[row][col] == 0) {
					rowPos = row;
					colPos = col;
					break;
				}
				col ++;
			}
			if (rowPos != (N + 1) && colPos != (N + 1)) { // if value of rowPos / colPos is updated --> there is at least one 0, break out of for loop  && colPos != (N + 1)
				break;
			}
			row++;
		}

		if(row != (N) && col != (N)) { //NOT N - 1

			//setting values of the cells
			int cellNumber = 1;
			while (cellNumber <= N) {
				if (basicConstraintsCheck(rowPos, colPos, cellNumber) == true) { // checking sudoku constraints
					if (chessConstraintsCheck(rowPos, colPos, cellNumber) == true) { //checking chess constraints
						grid[rowPos][colPos] = cellNumber; // placing value in the position of cell with 0
						if (chessSudokuSolver(allSolutions) != true) { // if the solution is not found --> backtrack setting cellNumber to 0.
							grid[rowPos][colPos] = 0;
						} else {// algorithm found a solution
							return true;
						}
					}
				}
				cellNumber++;
			}
		}

		else { //the sudoku has been solved (base case)
			if (allSolutions == false) { // if allSolution is false, only one solution is required --> return;
				return true;
			} else { // if allSolutions is true, all possible solutions are required --> store in hashset Solutions
				int[][] gridCopy = new int[N][N]; //creating a 2D array of size N^2
				for (int i = 0; i < N; i++) {
					gridCopy[i] = Arrays.copyOf(grid[i], grid[i].length); // creating deep copy of the grid using iteration --> storing in new 2D array
				}
				ChessSudoku chessSudokuObject = new ChessSudoku(SIZE); //creating and storing the chess object
				chessSudokuObject.setGrid(gridCopy);
				solutions.add(chessSudokuObject);
			}
		}
		return false;
	}

	private void setGrid(int[][] grid) { // used for creating chessObject and adding sol to hash set
		this.grid = grid;
	}

	private boolean basicConstraintsCheck (int rowPos, int colPos, int cellNumber) { //checking the three sudoku constraints: row, column, smallBox
		if(checkRow(rowPos, cellNumber) && checkColumn(colPos, cellNumber) && checkBox(rowPos, colPos, cellNumber)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean chessConstraintsCheck (int rowPos, int colPos, int cellNumber) { //checking the three chess constraints: knight, king, queen
		if (checkQueenRule(rowPos, colPos, cellNumber) && checkKingRule(rowPos, colPos, cellNumber) && checkKnightRule(rowPos, colPos, cellNumber)) {
			return true;
		}
		else {
			return false;
		}
	}

	private boolean checkRow (int rowPosition, int cellNumber) {
		int i = 0;
		while (i < N) {
			if (grid[rowPosition][i] == cellNumber) { // value is found in another column in the same row
				return false;
			}
			i++;
		}
		return true;
	}

	private boolean checkColumn (int colPosition, int cellNumber) {
		int j = 0;
		while (j < N) {
			if (grid[j][colPosition] == cellNumber) { // value is found in another row in the same column
				return false;
			}
			j++;
		}
		return true;
	}

	private boolean checkBox (int rowPosition, int colPosition, int cellNumber) {
		int moduloRow = (rowPosition % SIZE);
		int moduloCol = (colPosition % SIZE);

		int rowSizeBox = rowPosition - moduloRow;
		int colSizeBox = colPosition - moduloCol;

		//checking that cellNumber is not repeated in the smaller SIZExSIZE boxes
		int k = rowSizeBox;
		int rowSizeBox2 = rowSizeBox + SIZE;
		int colSizeBox2 = colSizeBox + SIZE;
		while (k < rowSizeBox2) {
			int l = colSizeBox;
			while (l < colSizeBox2) {
				if (cellNumber == grid[k][l]) {
					return false;
				}
				l++;
			}
			k++;
		}
		return true;
	}

	private boolean checkKnightRule(int rowPosition, int colPosition, int cellNumber) {
		if(knightRule == true) {

			int temp1 = 0;
			int temp2 = 0;
			int temp3 = 0;
			int temp4 = 0;
			int temp5 = 0;
			int temp6 = 0;
			int temp7 = 0;
			int temp8 = 0;

			// left horizontal (positive) knight move
			if((rowPosition - 1) > 0 && (colPosition - 2) > 0) {
				temp1 = grid[rowPosition - 1][colPosition - 2];
			}

			// left vertical (positive) knight move
			if((rowPosition - 2) > 0 && (colPosition - 1) > 0) {
				temp2 = grid[rowPosition - 2][colPosition - 1];
			}

			// right horizontal (positive) knight move
			if((rowPosition - 1) > 0 && (colPosition + 2) < N) {
				temp3 = grid[rowPosition - 1][colPosition + 2];
			}

			// right vertical (positive) knight move
			if((rowPosition - 2) > 0 && (colPosition + 1) < N) {
				temp4 = grid[rowPosition - 2][colPosition + 1];
			}

			// left horizontal (negative) knight move
			if((rowPosition + 1) < N && (colPosition - 2) > 0) {
				temp5 = grid[rowPosition + 1][colPosition - 2];
			}

			// left vertical (negative) knight move
			if((rowPosition + 2) < N && (colPosition - 1) > 0) {
				temp6 = grid[rowPosition + 2][colPosition - 1];
			}

			// right horizontal (negative) knight move
			if((rowPosition + 1) < N && (colPosition + 2) < N) {
				temp7 = grid[rowPosition + 1][colPosition + 2];
			}

			// right vertical (negative) knight move
			if((rowPosition + 2) < N && (colPosition + 1) < N) {
				temp8 = grid[rowPosition + 2][colPosition + 1];
			}

			if(temp1 == cellNumber || temp2 == cellNumber || temp3 == cellNumber || temp4 == cellNumber || temp5 == cellNumber || temp6 == cellNumber || temp7 == cellNumber || temp8 == cellNumber) {
				return false;
			}
		}
		return true;
	}

	private boolean checkQueenRule(int rowPosition, int colPosition, int cellNumber) {
		// checking queenRule is true and the value is equal to the largest value in the sudoku
		if(queenRule == true) {
			if (cellNumber == N) {

				// diagonal row increasing, col increasing
				for (int i = 1; i < N; i++) {
					if (rowPosition + i >= N || colPosition + i >= N) {
						break;
					} else {
						if (grid[rowPosition + i][colPosition + i] == cellNumber) {
							return false;
						}
					}
				}

				// diagonal row decreasing, col increasing
				for (int i = 1; i < N; i++) {
					if (rowPosition - i < 0 || colPosition + i >= N) {
						break;
					} else {
						if (grid[rowPosition - i][colPosition + i] == cellNumber) {
							return false;
						}
					}
				}

				// diagonal row decreasing, col decreasing
				for (int i = 1; i < N; i++) {
					if (rowPosition - i < 0 || colPosition - i < 0) {
						break;
					} else {
						if (grid[rowPosition - i][colPosition - i] == cellNumber) {
							return false;
						}
					}
				}

				// diagonal row increasing, col decreasing
				for (int i = 1; i < N; i++) {
					if (rowPosition + i >= N || colPosition - i < 0) {
						break;
					} else {
						if (grid[rowPosition + 1][colPosition - i] == cellNumber) {
							return false;
						}
					}
				}
				return true;
			}
		}
		return true;
	}

	private boolean checkKingRule(int rowPosition, int colPosition, int cellNumber) {
		if (kingRule == true) {
			if(colPosition != 0 && colPosition != N-1) {
				if (rowPosition == 0) { // first row
					if ((grid[rowPosition + 1][colPosition + 1] == cellNumber)) {
						return false;
					}
					else if ((grid[rowPosition + 1][colPosition - 1] == cellNumber)) {
						return false;
					}
				}
				else if (rowPosition == (N - 1)) { // last row
					if (grid[rowPosition - 1][colPosition + 1] == cellNumber) {
						return false;
					}
					else if (grid[rowPosition - 1][colPosition - 1] == cellNumber) {
						return false;
					}
				}
				// anywhere else on the board
				else{
					if (grid[rowPosition + 1][colPosition + 1] == cellNumber || (grid[rowPosition - 1][colPosition + 1] == cellNumber)) {
						return false;
					}
					else if (grid[rowPosition - 1][colPosition - 1] == cellNumber || grid[rowPosition + 1][colPosition - 1] == cellNumber) {
						return false;
					}
				}
			}
		}
		return true;
	}


	/*****************************************************************************/
	/* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE METHODS BELOW THIS LINE. */
	/*****************************************************************************/

	/* Default constructor.  This will initialize all positions to the default 0
	 * value.  Use the read() function to load the Sudoku puzzle from a file or
	 * the standard input. */
	public ChessSudoku( int size ) {
		SIZE = size;
		N = size*size;

		grid = new int[N][N];
		for( int i = 0; i < N; i++ )
			for( int j = 0; j < N; j++ )
				grid[i][j] = 0;
	}


	/* readInteger is a helper function for the reading of the input file.  It reads
	 * words until it finds one that represents an integer. For convenience, it will also
	 * recognize the string "x" as equivalent to "0". */
	static int readInteger( InputStream in ) throws Exception {
		int result = 0;
		boolean success = false;

		while( !success ) {
			String word = readWord( in );

			try {
				result = Integer.parseInt( word );
				success = true;
			} catch( Exception e ) {
				// Convert 'x' words into 0's
				if( word.compareTo("x") == 0 ) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}

		return result;
	}


	/* readWord is a helper function that reads a word separated by white space. */
	static String readWord( InputStream in ) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while( whiteSpace.indexOf(currentChar) > -1 ) {
			currentChar = in.read();
		}

		// Read all characters until you reach white space
		while( whiteSpace.indexOf(currentChar) == -1 ) {
			result.append( (char) currentChar );
			currentChar = in.read();
		}
		return result.toString();
	}


	/* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
	 * grid is filled in one row at at time, from left to right.  All non-valid
	 * characters are ignored by this function and may be used in the Sudoku file
	 * to increase its legibility. */
	public void read( InputStream in ) throws Exception {
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				grid[i][j] = readInteger( in );
			}
		}
	}


	/* Helper function for the printing of Sudoku puzzle.  This function will print
	 * out text, preceded by enough ' ' characters to make sure that the printint out
	 * takes at least width characters.  */
	void printFixedWidth( String text, int width ) {
		for( int i = 0; i < width - text.length(); i++ )
			System.out.print( " " );
		System.out.print( text );
	}


	/* The print() function outputs the Sudoku grid to the standard output, using
	 * a bit of extra formatting to make the result clearly readable. */
	public void print() {
		// Compute the number of digits necessary to print out each number in the Sudoku puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

		// Create a dashed line to separate the boxes
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for( int lineInit = 0; lineInit < lineLength; lineInit++ )
			line.append('-');

		// Go through the grid, printing out its values separated by spaces
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				printFixedWidth( String.valueOf( grid[i][j] ), digits );
				// Print the vertical lines between boxes
				if( (j < N-1) && ((j+1) % SIZE == 0) )
					System.out.print( " |" );
				System.out.print( " " );
			}
			System.out.println();

			// Print the horizontal line between boxes
			if( (i < N-1) && ((i+1) % SIZE == 0) )
				System.out.println( line.toString() );
		}
	}


	/* The main function reads in a Sudoku puzzle from the standard input,
	 * unless a file name is provided as a run-time argument, in which case the
	 * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
	 * outputs the completed puzzle to the standard output. */
	public static void main( String args[] ) throws Exception {
		InputStream in = new FileInputStream("veryEasy3x3.txt");

		// The first number in all Sudoku files must represent the size of the puzzle.  See
		// the example files for the file format.
		int puzzleSize = readInteger( in );
		if( puzzleSize > 100 || puzzleSize < 1 ) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}

		ChessSudoku s = new ChessSudoku( puzzleSize );

		// You can modify these to add rules to your sudoku
		s.knightRule = false;
		s.kingRule = false;
		s.queenRule = false;

		// read the rest of the Sudoku puzzle
		s.read( in );

		System.out.println("Before the solve:");
		s.print();
		System.out.println();

		// Solve the puzzle by finding one solution.
		s.solve(false);

		// Print out the (hopefully completed!) puzzle
		System.out.println("After the solve:");
		s.print();
	}
}