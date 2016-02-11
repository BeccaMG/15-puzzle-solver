import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PuzzleTest extends Puzzle {
	public PuzzleTest(){
		super();
	}
	
	Puzzle puzzle = new Puzzle(4);
	Puzzle puzzleFromArray = new Puzzle(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0});
	protected Puzzle puzzleSolvable = new Puzzle(new int []{1,2,4,8,0,5,3,13,10,6,14,7,9,11,15,12});
	Puzzle puzzleNotSolvable = new Puzzle(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,0,15,14});
	
	/**
	 * Test int getSize() method.
	 * puzzleFromArray is a 15-puzzle, size = 15 tiles + blank space = 16
	 */
	@Test
	public void test_getSize() {
		assertEquals(16,puzzleFromArray.getSize());
	}
	
	/**
	 * Test int getDimension() method.
	 * puzzleFromArray is a 15-puzzle since it is a [(n*n)-1]-puzzle so n should be 4 (n is the grid dimension)
	 */
	@Test
	public void test_getDimension() {
		assertEquals(4,puzzleFromArray.getDimension());
	}
	
	/**
	 * Test int [] toArray() method.
	 * puzzle is initially in a solved position so the array should be ordered from 1 to 15 and finally the 0
	 */
	@Test
	public void test_toArray() {
		assertArrayEquals(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0},puzzle.toArray());
	}
	
	/**
	 * Test Puzzle movePiece(int id)
	 * 1st - try to move a correct piece and verify the movement
	 * 2nd - try to move a piece that cant be moved
	 * 3rd - try to move a piece that isnt in the board
	 * Finally check with invariant that the puzzle is in a safe state.
	 */
	@Test
	public void test_movePiece() {
		Puzzle pMove15 = puzzle.movePiece(15);
		assertArrayEquals(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,15},pMove15.toArray());
		assertFalse(pMove15.toArray().equals(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0}));
		Puzzle pMove1 = puzzle.movePiece(1);
		assertNull(pMove1);
		Puzzle pMove2 = puzzle.movePiece(30);
		assertNull(pMove2);
		assertTrue(puzzle.invariant());
	}
	/**
	 * Test List<Integer> validMoves()
	 * 1st - this list should never be empty
	 * 2nd - valid moves of puzzleFromArray should be 15 AND 12 NOT 1
	 */
	@Test
	public void test_validMoves() {
		List<Integer> vm = puzzleFromArray.validMoves();
		assertNotNull(vm);
		assertTrue(vm.contains(15) && vm.contains(12) && !vm.contains(1));
	}
	
	/**
	 * Test String toString()
	 * The correct string should contain at least two sequences of +-------+-------+ AND the number 1 NEVER number 0
	 */
	@Test
	public void test_toString(){
		String puzzleString = puzzleFromArray.toString();
		assertTrue(puzzleString.contains("+-------+-------+") && puzzleString.contains(" 1") && !puzzleString.contains(" 0"));
	}
	
	/**
	 * Test boolean isSolvable()
	 * puzzleSolvable is a solvable puzzle --> TRUE
	 * puzzleNotSolvable is a puzzle in a no solvable position --> FALSE
	 */
	@Test
	public void test_isSolvable(){
		assertTrue(puzzleSolvable.isSolvable());
		assertFalse(puzzleNotSolvable.isSolvable());
	}
	
	/**
	 * Test boolean equals(Object obj)
	 * puzzle and puzzleFromArray are equal --> TRUE
	 * puzzle is in solved position, puzzleSolvable is shuffled --> FALSE
	 * 
	 */
	@Test
	public void test_equals(){
		assertTrue(puzzle.equals(puzzleFromArray));
		assertFalse(puzzle.equals(puzzleSolvable));
	}
	
	/**
	 * Test Puzzle clone()
	 * puzzle is in a solvable position and a cloned puzzle of it should be equal --> TRUE
	 * puzzleSolvable is shuffled so it is not equal to the cloned one --> FALSE
	 */
	@Test
	public void test_clone(){
		Puzzle pc = puzzle.clone();
		assertTrue(pc.equals(puzzle));
		assertFalse(pc.equals(puzzleSolvable));
	}
	
	/**
	 * Test boolean isSolved()
	 * puzzle is in a solved position --> TRUE
	 * puzzleSolvable is shuffled --> FALSE
	 */
	@Test
	public void test_isSolved(){
		assertTrue(puzzle.isSolved());
		assertFalse(puzzleSolvable.isSolved());
	}
	
	/**
	 * Test void followMovements(List<Integer> l)
	 * l is a list with the movements that reach puzzleSolvable to a solved position
	 * try followMovements and check that the puzzle is solved
	 * Finally check with invariant that the puzzle is in a safe state.
	 */
	@Test
	public void test_followMovements(){
		List<Integer> l = new ArrayList<Integer>();
 		Collections.addAll(l,5,6,14,7,13,8,4,3,7,13,12,15,11,14,13,11,14,13,10,9,13,14,15);
		puzzleSolvable.followMovements(l);
		assertTrue(puzzleSolvable.isSolved());
		assertTrue(puzzleSolvable.invariant());
	}
	
	/**
	 * Test void shuffle()
	 * 1st puzzle is in a solved position
	 * 2nd shuffle it
	 * 3rd verify it is not solved
	 * Finally check with invariant that the puzzle is in a safe state.
	 */
	@Test
	public void test_shuffle(){
		assertTrue(puzzle.isSolved());
		puzzle.shuffle();
		assertTrue(puzzle.isSolvable());
		assertFalse(puzzle.isSolved());
		assertFalse(puzzle.equals(puzzleFromArray));
		assertTrue(puzzle.invariant());
	}
	
	/**
	 * Test int inversions() 
	 * puzzleSolvable has 20 inversions
	 */
	@Test
	public void test_inversions(){
		assertEquals(20,puzzleSolvable.inversions());
	}
	
	/**
	 * Test Puzzle transpose()
	 * Check that the transpose is equal to a manually computed transpose
	 */
	@Test
	public void test_transpose(){
		assertArrayEquals(new int []{1,0,10,9,2,5,6,11,4,3,14,15,8,13,7,12},puzzleSolvable.transpose().toArray());
	}
	
	/**
	 * Test Puzzle rotateRight()
	 * Check that the rotation is equal to a manually computed rotation
	 */
	@Test
	public void test_rotateRight(){
		assertArrayEquals(new int []{9,10,0,1,11,6,5,2,15,14,3,4,12,7,13,8},puzzleSolvable.rotateRight().toArray());
	}
	
	/**
	 * Test boolean moveBlankSpace(int direction)
	 * From a puzzle in a solved position it should be able to move the blank space up but no right.
	 * Finally check with invariant that the puzzle is in a safe state.
	 */
	@Test
	public void test_moveBlankSpace(){
		//direction 1 = up, 2 = down, 3 = right, 4 left
		assertEquals(true,puzzle.moveBlankSpace(1));
		assertEquals(false,puzzle.moveBlankSpace(3));
		assertTrue(puzzle.invariant());
	}
	
	/**
	 * Test Point searchIndex(int id)
	 * puzzle is in a solved position so:
	 * - Blank space position row = 3 column = 3 --> (3,3)
	 * - Piece #1 position row = 0 column = 0 --> (0,0)
	 * - There is no Piece #30 so the index should be null
	 */
	@Test
	public void test_searchIndex(){
		assertEquals(new Point(3,3),puzzle.searchIndex(0));
		assertEquals(new Point(0,0),puzzle.searchIndex(1));
		assertEquals(null,puzzle.searchIndex(30));
	}
	
	/**
	 * Test boolean invariant()
	 * WE DONT CHECK IN THE PUZZLE CONSTRUCTOR FROM ARRAY THAT THE USER
	 * ENTERS A VALID ARRAY BECAUSE WE WANT TO USE IT TO GENERATE AN INVARIANT
	 * twoBlankSpacePuzzle has two blank spaces and is missing the piece #15 so it shouldnt be in a safe state.
	 */
	@Test
	public void test_invariant(){
		Puzzle twoBlankSpacePuzzle = new Puzzle(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,0});
		assertFalse(twoBlankSpacePuzzle.invariant());
	}
}
