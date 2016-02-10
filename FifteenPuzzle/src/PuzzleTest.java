import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PuzzleTest {
	Puzzle puzzle = new Puzzle(4);
	Puzzle puzzleFromArray = new Puzzle(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0});
	Puzzle puzzleSolvable = new Puzzle(new int []{1,2,4,8,0,5,3,13,10,6,14,7,9,11,15,12});
	Puzzle puzzleNotSolvable = new Puzzle(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,0,15,14});
	
	@Test
	public void test_getSize() {
		assertEquals(16,puzzleFromArray.getSize());
	}
	
	@Test
	public void test_toArray() {
		assertArrayEquals(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0},puzzle.toArray());
	}
	
	@Test
	public void test_movePiece() {
		Puzzle pMove15 = puzzle.movePiece(15);
		assertArrayEquals(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,15},pMove15.toArray());
		assertFalse(pMove15.toArray().equals(new int []{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0}));
		Puzzle pMove1 = puzzle.movePiece(1);
		assertNull(pMove1);
	}
	
	@Test
	public void test_validMoves() {
		List<Integer> vm = puzzleFromArray.validMoves();
		assertNotNull(vm);
		assertTrue(vm.contains(15) && vm.contains(12) && !vm.contains(1));
	}
	
	@Test
	public void test_toString(){
		String puzzleString = puzzleFromArray.toString();
		assertTrue(puzzleString.contains("+-------+-------+") && puzzleString.contains(" 1") && !puzzleString.contains(" 0"));
	}
	
	@Test
	public void test_isSolvable(){
		assertTrue(puzzleSolvable.isSolvable());
		assertFalse(puzzleNotSolvable.isSolvable());
	}
	
	@Test
	public void test_equals(){
		assertTrue(puzzle.equals(puzzleFromArray));
		assertFalse(puzzle.equals(puzzleSolvable));
	}
	
	@Test
	public void test_clone(){
		Puzzle pc = puzzle.clone();
		assertTrue(pc.equals(puzzle));
		assertFalse(pc.equals(puzzleSolvable));
	}
	
	@Test
	public void test_isSolved(){
		assertTrue(puzzle.isSolved());
		assertFalse(puzzleSolvable.isSolved());
	}
	
	@Test
	public void test_followMovements(){
		List<Integer> l = new ArrayList<Integer>();
 		Collections.addAll(l,5,6,14,7,13,8,4,3,7,13,12,15,11,14,13,11,14,13,10,9,13,14,15);
		puzzleSolvable.followMovements(l);
		assertTrue(puzzleSolvable.isSolved());
	}
	
	@Test
	public void test_shuffle(){
		assertTrue(puzzle.isSolved());
		puzzle.shuffle();
		assertTrue(puzzle.isSolvable());
		assertFalse(puzzle.isSolved());
		assertFalse(puzzle.equals(puzzleFromArray));
	}
}
