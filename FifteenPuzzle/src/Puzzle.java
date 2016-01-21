import java.awt.Point;
import java.util.Random;

public class Puzzle {
	Integer[][] puzzle = new Integer[4][4];
	
	/**
	 * 4x4 Matrix. Game elements [0,15], 0 is the blank space.
	 */
	Puzzle(){
		initGrid();
	}
	
	/**
	 * Set puzzle initial values (solution)
	 */
	void initGrid(){
		int val = 1;
		for(int r = 0; r < 4; r++){
			for(int c = 0; c < 4; c++){
				if(r==3 && c==3){
					puzzle[r][c] = 0;
				}else{
					puzzle[r][c] = val;
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
		
		if((nextRow >= 0 && nextRow <= 3) && (nextColumn >= 0 && nextColumn <= 3)){
			if(validMove(p,new Point(nextRow,nextColumn))){
				int temp = puzzle[nextRow][nextColumn];
				puzzle[nextRow][nextColumn] = id;
				puzzle[p.x][p.y] = temp;
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
	 * @param id
	 * @return Point(r,c) where r = row, c = column
	 */
	public Point searchIndex(int id){
		int val;
		for(int r = 0; r < 4; r++){
			for(int c = 0; c < 4; c++){
				val = puzzle[r][c];
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
		String str = "+-------+-------+-------+-------+\n";
		int val;
		for(int r = 0; r < 4; r++){
			for(int c = 0; c < 4; c++){
				val = puzzle[r][c];
				if(val == 0){
					str = str + "|  " + "  " + "\t";
				}else{
					str = str + "|  " + val + "\t";
				}
			}
			str = str + "|\n+-------+-------+-------+-------+\n";
		}
		return str;
	}
}