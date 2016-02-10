import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Puzzle {

    private int[][] puzzle_grid;
    private int[] puzzle_array;
    private int n; // (nxn-1)-puzzle (ie. a 15-puzzle will have an n = 4)
    private Point pZero = null;

    /**
     * nxn Matrix. Game elements [0,(n*n)-1], 0 is the blank space. The puzzle generated would be solved.
     * 	
     * @param n - the dimension of the matrix. It doesn't make sense to enter n = 1.
     */
    public Puzzle(int n) {
        this.n = n;
        puzzle_grid = new int[n][n];
        puzzle_array = new int[n * n];
        initGrid();
        
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                puzzle_array[r * n + c] = puzzle_grid[r][c];

        pZero = new Point(n - 1, n - 1);
    }

    /**
     * Initialize a puzzle from an array.
     * Game elements [0,(n*n)-1], 0 is the blank space. n = sqrt(array.length).
     *
     * @param array - a puzzle in an Array representation
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
     * @return the size of the puzzle (total tiles, including the blank space)
     */
    public int getSize() {
        return (n*n)-1;
    }
    
    public int getDimension() {
        return n;
    }
    
    /**
     * 
     * @return the Array representation of the puzzle
     */
    public int [] toArray(){
    	return this.puzzle_array;
    }

    /**
     * Shuffles the puzzle making random movements. The number of random movements are in the range of [10^(n-2),10^(n-1)].
     *
     * @param times - number of random movements to shuffle the puzzle
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
     * Move a piece if possible, it can only be moved if it is adjacent to the blank space
     *
     * @param id - Piece id
     * @return a new Puzzle with the piece specified in the parameter swapped with the blank space
     * If the piece can't be moved, return NULL
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
     * Possible moves
     *
     * @return a List of integers, these integers are the blank space neighbors. This list is never empty.
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
     * Prints the current puzzle.
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
     * Checks whether the puzzle is solvable or no
     * That is done for even-nd-puzzles by calculating number of inversions + the row of the blank tile
     * if odd then it is solvable and vice-versa
     * but for odd-nd-puzzles the row of the blank tile is not added and the puzzle is solvable if the sum is even
     *
     * @return boolean indicating whether puzzle solvable or not
     * @see <a href="https://goo.gl/AO9Fyx">The 8 puzzle problem</a>
     */
    public boolean isSolvable() {
        int sum = 0;
        for (int i = 0; i < n * n; i++) {
            for (int j = i + 1; j < n * n; j++)
                if (puzzle_array[i] > puzzle_array[j] && puzzle_array[j] != 0)
                    sum++;

            sum = (puzzle_array[i] == 0 && n % 2 == 0) ? sum + i / n : sum;
        }

        return sum % 2 != n % 2;
    }

    /**
     * Checks whether a passed Puzzle is equal to this puzzle
     *
     * @param obj an instance of Puzzle
     * @return true if the puzzles are equal
     */
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        Puzzle py = (Puzzle) obj;

        for (int i = 0; i < (n * n); i++)
            if (py.puzzle_array[i] != this.puzzle_array[i])
                return false;
        return true;
    }

    /**
     * Clone the puzzle
     *
     * @return new instance of puzzle
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
     * Checks on:
     * <ul>
     * <li>The grid and the array are equal</li>
     * <li>The values of tiles are from 0 to size-1</li>
     * <li>The array's length is equal to the grid's length equal to the size of puzzle</li>
     * <li>The zero coordinate is the current position of the blank space</li>
     * </ul>
     *
     * @return the state of the puzzle
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
     * @return true if the puzzle is already solved
     */
    public boolean isSolved() {
        return this.equals(new Puzzle(this.n));
    }

    /**
     * Follow the list of movements specified in the parameter
     *
     * @param l - Integer list with the sequence of pieces to be moved
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
     * Set puzzle initial values (in solved position)
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
     * @param direction - 1 = up, 2 = down, 3 = right, 4 left
     * @return true = piece moved, false = invalid move
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
