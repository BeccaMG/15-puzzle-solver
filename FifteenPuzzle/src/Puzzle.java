import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Puzzle {

	Integer[][] puzzle_grid;
    Integer[] puzzle_array;
    int n; // nxn-1 puzzle (ie. a 15puzzle will have an n = 4).
    Point pZero = null;

    /**
     * nxn Matrix. Game elements [0,(n*n)-1], 0 is the blank space.
     * @param n - the dimension of the matrix
     */
    Puzzle(int n){
		this.n = n;
		puzzle_grid = new Integer[n][n];
		puzzle_array = new Integer[n*n];
		initGrid();
        toArray();
        pZero = new Point(n-1,n-1);
	}
    
    /**
     * Game elements [0,(n*n)-1], 0 is the blank space. n = sqrt(array.length)
     * @param array - a puzzle in an Array representation
     */
    Puzzle(Integer[] array){
		this.n = (int) Math.sqrt(array.length);
		puzzle_grid = new Integer[n][n];
		puzzle_array = new Integer[n*n];
		System.arraycopy(array, 0, puzzle_array, 0, array.length);
        toGrid();
        int val;
		for(int r = 0; r < n; r++){
			for(int c = 0; c < n; c++){
				val = puzzle_grid[r][c];
				if(val == 0){
					pZero = new Point(r,c);
					break;
				}
			}
		}
	}

	/**
	 *
	 * @return the size of the puzzle
     */
	int getSize(){
		return n*n;
	}
	
    /**
	 * Set puzzle initial values (in solved position)
	 */
    void initGrid(){
		int val = 1;
		for(int r = 0; r < n; r++){
			for(int c = 0; c < n; c++){
				if(r==(n-1) && c==(n-1)){
					puzzle_grid[r][c] = 0;
				}else{
					puzzle_grid[r][c] = val;
					val++;
				}
			}
		}
	}
	
	/**
	 * Shuffles the puzzle
	 * @param times - number of random movements to shuffle the puzzle
	 */
	public void shuffle(int times){
		Random r = new Random();
		int direction;
		for(int i = 0; i <= times; i++){
			direction = r.nextInt(4)+1;
			movePiece(0,direction);
		}
	}

	/**
	 * Move a piece if possible, it can only be moved if it is adjacent to the blank space
	 * @param id - Piece id
	 * @param direction - 1 = up, 2 = down, 3 = right, 4 left
	 * @return true = piece moved, false = invalid move
	 */
	public boolean movePiece(int id, int direction){
		Point p = searchIndex(id);
		int nextRow = p.x;
		int nextColumn = p.y;
		
		switch(direction){
		case 1: //up
			nextRow--;
			break;
		case 2: //down
			nextRow++;
			break;
		case 3: //right
			nextColumn++;
			break;
		case 4: //left
			nextColumn--;
			break;
		}
		
		
		if((nextRow >= 0 && nextRow <= (n-1)) && (nextColumn >= 0 && nextColumn <= (n-1))){
			if(validMove(p,new Point(nextRow,nextColumn))){
				int temp = puzzle_grid[nextRow][nextColumn];
				puzzle_grid[nextRow][nextColumn] = id;
				puzzle_grid[p.x][p.y] = temp;
                toArray();
                
                if(id == 0){
        			pZero.x = nextRow;
        			pZero.y = nextColumn;
        		}
                
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	//Solo deberia de ejecutarse cuando se haya verificado que el movimiento es v[alido
		//Null si no es valido
		//New Puzzle no the actual puzzle
	/**
	 * Move a piece if possible, it can only be moved if it is adjacent to the blank space
	 * @param id - Piece id
	 * @return a new Puzzle with the piece specified in the parameter swapped with the blank space
	 *		   If the piece can't be moved, return NULL
	 */
	public Puzzle movePiece(int id){
		Puzzle child = this.clone();
		List<Integer> moves = child.validMoves();
		if(moves.contains(id)){
			Point index = child.searchIndex(id);
			child.puzzle_grid[index.x][index.y] = 0;
			child.puzzle_grid[child.pZero.x][child.pZero.y] = id;
			child.pZero.x = index.x;
			child.pZero.y = index.y;
			toArray();
			return child;
		}
		return null;
	}
	
	/**
	 * Possible moves
	 * @return a List of integers, these integers are the blank space neighbors
	 */
	public List<Integer> validMoves(){
		List<Integer> neighbors = new LinkedList<Integer>();
		Point p = new Point(this.pZero.x,this.pZero.y);
		int row = p.x;
		int column = p.y;
		
		//search UP
		if(row != 0){
			neighbors.add(this.puzzle_grid[row-1][column]);
		}
		//search RIGHT
		if(column != (n-1)){
			neighbors.add(this.puzzle_grid[row][column+1]);
		}
		//search DOWN
		if(row != (n-1)){
			neighbors.add(this.puzzle_grid[row+1][column]);
		}
		//search LEFT
		if(column != 0){
			neighbors.add(this.puzzle_grid[row][column-1]);
		}
		
		return neighbors;
	}

	/**
	 * Verifies if a desired move is possible
	 * @param p1 initial position
	 * @param p2 final position
	 * @return true = possible, false = not possible
	 */
	public boolean validMove(Point p1, Point p2){
		return pZero.equals(p1) || pZero.equals(p2);
	}
	
	/**
	 * Search piece index in the grid.
	 * @param id Piece id
	 * @return Point(r,c) where r = row, c = column
	 */
	public Point searchIndex(int id){
		if(id == 0){
			return pZero;
		}
		
		int val;
		for(int r = 0; r < n; r++){
			for(int c = 0; c < n; c++){
				val = puzzle_grid[r][c];
				if(val == id){
					return new Point(r,c);
				}
			}
		}
		return null;
	}
	
	/**
	 * Prints the current puzzle.
	 */
	public String toString(){
		String line = "+";
		for(int i = 0;i < n;i++){
			line = line+"-------+";
		}
		String str = line+"\n";
		int val;
		for(int r = 0; r < n; r++){
			for(int c = 0; c < n; c++){
				val = puzzle_grid[r][c];
				if(val == 0){
					str = str + "|  " + "  " + "\t";
				}else{
					str = str + "|  " + val + "\t";
				}
			}
			str = str + "|\n"+line+"\n";
		}
		return str;
	}

    /**
     * Transforms the grid representation into the array
     * and writes it in @puzzle_array
     */
	public void toArray(){
		for (int i=0; i<n; i++)
			for(int j=0;j<n; j++)
				puzzle_array[i*n+j]=puzzle_grid[i][j];
	}

    /**
     * Transforms the array representation into the grid
     * and writes it in @puzzle_grid
     */
    public void toGrid(){
        for(int i=0; i<(n*n);i++)
            puzzle_grid[i/n][i%n]=puzzle_array[i];
    }

    /**
     * Checks whether the puzzle is solvable or no
     * That is done for even-nd-puzzles by calculating number of inversions + the row of the blank tile
     * if odd then it is solvable and vice-versa
	 * but for odd-nd-puzzles the row of the blank tile is not added and the puzzle is solvable if the sum is even
     * @see <a href="https://goo.gl/AO9Fyx">The 8 puzzle problem</a>
     * @return boolean indicating whether puzzle solvable or not
     */
	public boolean isSolvable(){
		int sum = 0;
		for(int i=0; i<n*n; i++) {
            for (int j = i + 1; j < n*n; j++)
                if (puzzle_array[i] > puzzle_array[j] && puzzle_array[j] != 0)
                    sum++;

			sum = (puzzle_array[i]==0 && n%2 == 0) ? sum+i/n : sum;
        }

        return sum%2 != n%2;
	}

    /**
     * Checks whether a passed Puzzle is equal to this puzzle
     * @param obj an instance of Puzzle
     * @return true if this puzzle is equal to y
     */
	public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Puzzle py = (Puzzle) obj;
        for(int i = 0; i<(n*n);i++)
            if(py.puzzle_array[i]!=this.puzzle_array[i])
                return false;
        return true;
	}
	
	/**
	 * Clone the puzzle
	 * @return new instance of puzzle
	 */
	public Puzzle clone(){
		Puzzle newPuzzle = new Puzzle(this.n);
		for(int i = 0; i < (n*n); i++){
			newPuzzle.puzzle_array[i] = this.puzzle_array[i];
		}
		newPuzzle.pZero = this.searchIndex(0);
		newPuzzle.toGrid();
		return newPuzzle;
	}


	//TODO write the javadoc in a better way with list and everything
	//TODO think about other invariant conditions
	/**
	 * Checks on:
	 * 1- the array's length is equal to the grid's length equal to the size of puzzle
	 * 2- the grid and the array are equal
	 * 3- the values of tiles are from 0 to size-1
	 *
	 * @return the state of the puzzle
     */
	public boolean invariant(){

		//To make sure the array's length is equal to the grid's length equal to the size of puzzle
		if(puzzle_grid.length*puzzle_grid[0].length != puzzle_array.length)
			return false;
		if(puzzle_array.length!=n*n)
			return false;

		//To make sure the grid and the array are equal
		for (int i=0; i<n; i++)
			for(int j=0;j<n; j++)
				if(puzzle_array[i*n+j]!=puzzle_grid[i][j])
					return false;


		//To make sure there are tiles with values from 0 to n*n-1
		boolean[] state = new boolean[n*n];
		for (int i=0; i<n*n; i++)
			state[i]=false;
		for (int i=0; i<n*n; i++) {
			state[puzzle_array[i]] = true;
		}
		for (int i=0; i<n*n; i++)
			if(!state[i])
				return false;


		return true;
	}
	
	/**
	 * 
	 * @return true if the puzzle is already solved
	 */
	public boolean isSolved(){
		return this.equals(new Puzzle(this.n));
	}
}
