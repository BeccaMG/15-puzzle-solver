import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Puzzle {

    private int[][] puzzle_grid;
    private int[] puzzle_array;
    private int n;
    private Point pZero = null;

    /**
     * A (n*n)-1 Puzzle constructor.
     *
     * It creates a N-Puzzle with game elements in [0, (n*n)-1], 0 being the 
     * blank space. If the user wants to generate a 15-Puzzle, the parameter
     * passed to the constructor should be 4. This will guaranty that the puzzle
     * created is always an (n*n-1)-Puzzle so it matches the general algorithms
     * 	
     * @param n The dimension of the puzzle grid. Should be > 1.
     */
    public Puzzle(int n) {
        if (n < 1)
            return;
        
        this.n = n;
        puzzle_grid = new int[n][n];
        puzzle_array = new int[n*n];
        initGrid();
        
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                puzzle_array[r * n + c] = puzzle_grid[r][c];

        pZero = new Point(n - 1, n - 1);
    }


    /**
     * Constructs a (n*n)-1 Puzzle from an array.
     *
     * Initialize a puzzle from an array of 16 elements in [0,(n*n)-1], 0 being
     * the blank space. The dimension of the grid: n = sqrt(array.length).
     *
     * @param array A N-Puzzle in array representation.
     */
    public Puzzle(int[] array) {
        this.n = (int) Math.sqrt(array.length);
        puzzle_grid = new int[n][n];
        puzzle_array = new int[n * n];
        System.arraycopy(array, 0, puzzle_array, 0, array.length);
        
        for (int i = 0; i < (n * n); i++)
        	puzzle_grid[i / n][i % n] = puzzle_array[i];
        
        int val;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                val = puzzle_grid[r][c];
                if (val == 0) {
                    pZero = new Point(r, c);
                    break;
                }
            }
        }
    }


    /**
     * Size of the puzzle.
     *
     * @return The size of the puzzle. Total tiles, including the blank space.
     */
    public int getSize() {
        return (n*n);
    }
    
    
    /**
     * Dimension of the puzzle grid.
     *
     * @return The number of rows/columns in the puzzle equal to the square root
     * of the size of the puzzle minus 1. A 15-Puzzle has dimension 4.
     */
    public int getDimension() {
        return n;
    }
    
    
    /**
     * Array representation of the puzzle
     *
     * @return The array representation of the puzzle
     */
    public int [] toArray(){
    	return this.puzzle_array;
    }


    /**
     * Shuffles the puzzle. 
     *
     * It shuffles the puzzle making random movements of the blank space. The 
     * number of random movements is in the range of [10^(n-2), 10^(n-1)).
     */
    public void shuffle() {
        Random r = new Random();
        int upperLimit = (int) Math.pow(10, n-1);
        
        int lowerLimit = (int) Math.pow(10, n-2);
        
        int times = r.nextInt(upperLimit)+lowerLimit;
        int direction;
        for (int i = 0; i <= times; i++) {
            direction = r.nextInt(4) + 1;
            while (!moveBlankSpace(direction)) {
                direction = r.nextInt(4) + 1;
            }
        }
    }


    /**
     * Swap a piece with the blank space.
     *
     * A piece can only be moved if it is adjacent to the blank space. This
     * method clones the puzzle instance and move the piece in the new puzzle.
     *
     * @param id Piece id in [1,size]
     * @return A new puzzle with the piece specified in the parameter swapped 
     * with the blank space. NULL if the piece can't be moved.
     */
    public Puzzle movePiece(int id) {
        Puzzle child = this.clone();
        List<Integer> moves = child.validMoves();
        if (moves.contains(id)) {
            Point index = child.searchIndex(id);
            child.puzzle_grid[child.pZero.x][child.pZero.y] = id;
            child.puzzle_grid[index.x][index.y] = 0;
            
            child.puzzle_array[child.pZero.x * n + child.pZero.y] = id;
            child.puzzle_array[index.x * n + index.y] = 0;
            
            child.pZero = new Point(index.x, index.y);
            return child;
        }
        return null;
    }


    /**
     * Computes the possible moves in the board.
     *
     * @return A list of integers representing the blank space neighbors. 
     * This list is never empty.
     */
    public List<Integer> validMoves() {
        List<Integer> neighbors = new LinkedList<Integer>();
        Point p = new Point(this.pZero.x, this.pZero.y);
        int row = p.x;
        int column = p.y;

        //search UP
        if (row != 0) {
            neighbors.add(this.puzzle_grid[row - 1][column]);
        }
        //search RIGHT
        if (column != (n - 1)) {
            neighbors.add(this.puzzle_grid[row][column + 1]);
        }
        //search DOWN
        if (row != (n - 1)) {
            neighbors.add(this.puzzle_grid[row + 1][column]);
        }
        //search LEFT
        if (column != 0) {
            neighbors.add(this.puzzle_grid[row][column - 1]);
        }

        return neighbors;
    }


    /**
     * Prints the puzzle board.
     *
     * Before printing, it verifies the invariant of the puzzle.
     */
    public String toString() {
        //Before printing check invariant
        if (!invariant()) {
            System.out.println("WARNING: Something may be wrong in the puzzle");
        }
        String line = "+";
        for (int i = 0; i < n; i++) {
            line = line + "-------+";
        }
        String str = line + "\n";
        int val;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                val = puzzle_grid[r][c];
                if (val == 0) {
                    str = str + "|  " + "  " + "\t";
                } else {
                    str = str + "|  " + val + "\t";
                }
            }
            str = str + "|\n" + line + "\n";
        }
        return str;
    }


    /**
     * Checks whether the puzzle is solvable or not.
     *
     * It calculates the number of inversions and verifies if the puzzle is
     * solvable depending on the board dimension.
     *
     * @return Boolean indicating whether the puzzle solvable or not.
     *
     * @see <a href="https://goo.gl/AO9Fyx">The 8 puzzle problem</a>
     */
    public boolean isSolvable() {
        int sum = 0;

        if (n % 2 == 0)  // If the board is even, we add the row of the blank space
            sum += pZero.x;
            
        for (int i = 0; i < (n*n); i++) {
            for (int j = i + 1; j < (n*n); j++)
                if (puzzle_array[i] > puzzle_array[j] && puzzle_array[j] != 0)
                    sum++;
        }

        return sum % 2 != n % 2;
    }


    /**
     * Checks whether a passed puzzle is equal to this puzzle
     *
     * @param obj An instance of an object.
     * @return True if the puzzles are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        Puzzle py = (Puzzle) obj;

        for (int i = 0; i < (n*n); i++)
            if (py.puzzle_array[i] != this.puzzle_array[i])
                return false;
            
        return true;
    }


    /**
     * Clone the puzzle
     *
     * @return New instance of a puzzle equal to this.
     */
    public Puzzle clone() {
        Puzzle newPuzzle = new Puzzle(this.n);
        for (int i = 0; i < (n * n); i++) {
            newPuzzle.puzzle_array[i] = this.puzzle_array[i];
        }

        for (int i = 0; i < (n * n); i++)
        	newPuzzle.puzzle_grid[i / n][i % n] = newPuzzle.puzzle_array[i];
        
        newPuzzle.pZero = this.searchIndex(0);
        return newPuzzle;
    }


    /**
     * Verifies the invariant of the puzzle.
     * 
     * Checks on:
     * <ul>
     * <li>The grid and the array are equal</li>
     * <li>The values of tiles are from 0 to size-1</li>
     * <li>The array's length is equal to the grid's length equal to the size of puzzle</li>
     * <li>The zero coordinate is the current position of the blank space</li>
     * </ul>
     *
     * @return True if the state of the puzzle is correct.
     */
    public boolean invariant() {
        //To make sure the array's length is equal to the grid's length equal to the size of puzzle
        if (puzzle_grid.length * puzzle_grid[0].length != puzzle_array.length)
            return false;

        if (puzzle_array.length != n * n)
            return false;

        //To make sure the grid and the array are equal
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (puzzle_array[i * n + j] != puzzle_grid[i][j])
                    return false;

        //To make sure there are tiles with values from 0 to n*n-1
        boolean[] state = new boolean[n * n];
        for (int i = 0; i < n * n; i++)
            state[i] = false;
        for (int i = 0; i < n * n; i++)
            state[puzzle_array[i]] = true;
        for (int i = 0; i < n * n; i++)
            if (!state[i])
                return false;

        //To make sure the coordinate of the blank space is correct
        Point zeroCoordinate = null;
        int val;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                val = puzzle_grid[r][c];
                if (val == 0) {
                    zeroCoordinate = new Point(r, c);
                }
            }
        }
        if (!zeroCoordinate.equals(this.searchIndex(0)))
            return false;

        return true;
    }


    /**
     * Verifies if the puzzle is solved.
     *
     * @return True if the puzzle is already solved.
     */
    public boolean isSolved() {
        return this.equals(new Puzzle(this.n));
    }


    /**
     * Follow the list of movements specified in the parameter.
     *
     * @param l Integer list with the sequence of pieces to be moved.
     */
    public void followMovements(List<Integer> l) {
        Iterator<Integer> iterator = l.iterator();
        while (iterator.hasNext()) {
            Integer piece = iterator.next();
            Point index = searchIndex(piece);
            puzzle_grid[pZero.x][pZero.y] = piece;
            puzzle_grid[index.x][index.y] = 0;
            puzzle_array[pZero.x * n + pZero.y] = piece;
            puzzle_array[index.x * n + index.y] = 0;
            pZero = new Point(index.x, index.y);
        }
    }
    
    
    /**
     * Set puzzle initial values (in solved position).
     */
    private void initGrid() {
        int val = 1;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (r == (n - 1) && c == (n - 1)) {
                    puzzle_grid[r][c] = 0;
                } else {
                    puzzle_grid[r][c] = val;
                    val++;
                }
            }
        }
    }
                
    
    /**
     * Move the blank space if possible.
     *
     * @param direction 1 = up, 2 = down, 3 = right, 4 left
     * @return True = piece moved, False = invalid move
     */
    private boolean moveBlankSpace(int direction) {
        int nextRow = pZero.x;
        int nextColumn = pZero.y;

        switch (direction) {
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

        if ((nextRow >= 0 && nextRow <= (n - 1)) && (nextColumn >= 0 && nextColumn <= (n - 1))) {
        	int temp = puzzle_grid[nextRow][nextColumn];
            puzzle_grid[nextRow][nextColumn] = 0;
            puzzle_grid[pZero.x][pZero.y] = temp;
                
            puzzle_array[nextRow * n + nextColumn] = 0;
            puzzle_array[pZero.x * n + pZero.y] = temp;
               
            pZero = new Point(nextRow,nextColumn);
               
            return true;
        }
        return false;
    }
    
    
    /**
     * Search piece index in the grid.
     *
     * @param id Piece id
     * @return Point(r, c) where r = row, c = column
     */
    private Point searchIndex(int id) {
        if (id == 0) {
            return pZero;
        }

        int val;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                val = puzzle_grid[r][c];
                if (val == id) {
                    return new Point(r, c);
                }
            }
        }
        return null;
    }

}
