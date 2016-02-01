import java.awt.Point;
import java.util.Random;

public class Puzzle {

	Integer[][] puzzle_grid;
    int [] puzzle_array;
    int n; // nxn Matrix.
    
    Point pZero = null;

    /**
     * nxn Matrix. Game elements [0,n-1], 0 is the blank space.
     * @param n - the dimension of the matrix
     */
    Puzzle(int n){
		this.n = n;
		puzzle_grid = new Integer[n][n];
		puzzle_array = new int [n*n];
		initGrid();
        toArray();
        pZero = new Point(n-1,n-1);
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

	/**
	 * Verifies if a desired move is possible
	 * @param p1 initial position
	 * @param p2 final position
	 * @return true = possible, false = not possible
	 */
	public boolean validMove(Point p1, Point p2){
		Point space = searchIndex(0);
		return space.equals(p1) || space.equals(p2);
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

}