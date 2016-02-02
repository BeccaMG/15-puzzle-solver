import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {
	static Puzzle p = new Puzzle(6);

	public static void main(String[] args){
		//1 = up, 2 = down, 3 = right, 4 left
		System.out.println(p.toString());
		System.out.println("Displayed puzzle solved? (should be true)... "+p.isSolved());
		p.shuffle(10000);
		System.out.println("Shuffled  puzzle solved? (should be false)... "+p.isSolved()+"\n");
		System.out.println(Arrays.toString(p.puzzle_array));
		System.out.println(p.toString());
        System.out.print(p.isSolvable());
		Solver s = new Solver(p);
		// MOSTAFA YOU BROKE IT @MFateen
// 		System.out.println(s.oop_fitness()); 
		
		
		//Test if clone is OK
		System.out.println("\n\n************ START  Test clone() ************");
		Puzzle puzzleTest = p.clone();
		System.out.print("P1:\n"+p.toString()+"P2:\n"+puzzleTest.toString());
		System.out.print("P1 EQUALS? P2: "+puzzleTest.equals(p));
		System.out.println(", pZero in P2 --> ROW: "+puzzleTest.pZero.x+" COLUMN: "+puzzleTest.pZero.y+", n: "+puzzleTest.n);
		System.out.println("\n************ FINISH Test clone() ************");
		
		//Test neighbors
		System.out.println("\n\n************ START  Test neighbors() ************");
		System.out.print("Puzzle\n"+p.toString());
		List<Integer> l = p.validMoves();
		System.out.print("Blank space neighbors: ");
		Iterator<Integer> iterator = l.iterator();
		while (iterator.hasNext()) {
		    System.out.print(iterator.next()+" ");
		}
		System.out.println("\n************ FINISH Test neighbors() ************");
		
		//Test movePiece(id)
		System.out.println("\n\n************ START  Test movePiece(int id) ************");
		System.out.print("Parent Puzzle\n"+p.toString());
		List<Integer> moves = p.validMoves();
		System.out.println("Move piece: "+moves.get(0));
		Puzzle pChild = p.movePiece(moves.get(0));
		System.out.print("Parent\n"+p.toString());
		System.out.print("Child\n"+pChild.toString());
		System.out.println("\n************ FINISH Test movePiece(int id) ************");
		
		//Test movePiece(id)
		System.out.println("\n\n************ START  Test Puzzle(Integer[] array) ************");
		Puzzle pFromArray = new Puzzle(new Integer[]{1,2,3,4,5,0,7,8,6});
		System.out.println(pFromArray.toString());
		System.out.println("************ FINISH Test Puzzle(Integer[] array) ************");
	}
}
