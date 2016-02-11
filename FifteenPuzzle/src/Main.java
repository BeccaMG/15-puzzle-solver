import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.management.*;

public class Main {
    
//     static int[] a = {15,4,9,2,0,11,14,1,5,13,10,7,8,12,3,6};
    static int[] a = {10,11,9,14,3,5,8,4,7,0,13,15,2,12,6,1};
//     static int[] a = {0,12,9,13,15,11,10,14,3,7,2,5,4,8,6,1};
//     static int[] a = {0,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
//     static int[] a = {11,12,14,13,15,0,10,9,8,7,6,5,4,3,2,1};
	static Puzzle p = new Puzzle(4);

	public static void main(String[] args){
		//1 = up, 2 = down, 3 = right, 4 left
		p.shuffle();
		int vertical;
        int horizontal;
		int horizontal1;
        Puzzle np = p.transpose();
		Puzzle np1 = p.rotateRight();
        System.out.println(p.toString());
        System.out.println(np.toString());
        System.out.println(np1.toString());
        
        vertical = p.inversions();
        horizontal = np.inversions();
//         horizontal = 0;
//         horizontal1 = np1.inversions();
        horizontal1 = 0;ls
        
        System.out.println(vertical);
        System.out.println(horizontal);
		System.out.println(horizontal1);
		System.out.println((vertical + horizontal +horizontal1)/3 + (vertical + horizontal+horizontal1)%3);
		
// 		System.out.println("Displayed puzzle solved? (should be true)... "+p.isSolved());

// 		System.out.println("Shuffled  puzzle solved? (should be false)... "+p.isSolved()+"\n");
// 		System.out.println(Arrays.toString(p.puzzle_array));
// 		System.out.println(p.toString());
//         System.out.println(p.isSolvable());
		Solver s = new Solver();
//  		System.out.println("Hamming distance = "+s.hammingDistance(p));
        System.out.println("Manhattan Distance = "+s.manhattanDistance(p));
        System.out.println("Walking Distance = "+s.walkingDistancePrime(p));



//         //Test if clone is OK
// 		System.out.println("\n\n************ START  Test clone() ************");
// 		Puzzle puzzleTest = p.clone();
// 		System.out.print("P1:\n"+p.toString()+"P2:\n"+puzzleTest.toString());
// 		System.out.print("P1 EQUALS? P2: "+puzzleTest.equals(p));
// 		System.out.println(", pZero in P2 --> ROW: "+puzzleTest.pZero.x+" COLUMN: "+puzzleTest.pZero.y+", n: "+puzzleTest.n);
// 		System.out.println("\n************ FINISH Test clone() ************");
// 
//         //Test neighbors
//         System.out.println("\n\n************ START  Test neighbors() ************");
//         System.out.print("Puzzle\n"+p.toString());
//         List<Integer> l = p.validMoves();
//         System.out.print("Blank space neighbors: ");
//         Iterator<Integer> iterator = l.iterator();
//         while (iterator.hasNext()) {
//             System.out.print(iterator.next()+" ");
//         }
//         System.out.println("\n************ FINISH Test neighbors() ************");
// 
// 		//Test movePiece(id)
// 		System.out.println("\n\n************ START  Test movePiece(int id) ************");
// 		System.out.print("Parent Puzzle\n"+p.toString());
// 		List<Integer> moves = p.validMoves();
// 		System.out.println("Move piece: "+moves.get(0));
// 		Puzzle pChild = p.movePiece(moves.get(0));
// 		System.out.print("Parent\n"+p.toString());
// 		System.out.print("Child\n"+pChild.toString());
// 		System.out.println("\n************ FINISH Test movePiece(int id) ************");
// 		
// 		
// 		//Test neighbors
// 		System.out.println("\n\n************ START  Test neighbors() ************");
// 		System.out.print("Puzzle\n"+p.toString());
// 		l = p.validMoves();
// 		System.out.print("Blank space neighbors: ");
// 		iterator = l.iterator();
// 		while (iterator.hasNext()) {
// 		    System.out.print(iterator.next()+" ");
// 		}
// 		System.out.println("\n************ FINISH Test neighbors() ************");
// 		
// 		
// 		//Test movePiece(id)
//         System.out.println("\n\n************ START  Test Puzzle(int [] array) ************");
//         Puzzle pFromArray = new Puzzle(new int []{1,2,3,4,5,0,7,8,6});
//         System.out.println(pFromArray.toString());
//         Puzzle pFromArray2 = new Puzzle(new int []{1,2,3,4,5,6,7,8,0});
//         System.out.println(pFromArray2.toString());
//         Puzzle newPuzzle = pFromArray2.movePiece(6);
//         System.out.println(newPuzzle.toString());
//         System.out.println("Are they equal? " + pFromArray.equals(pFromArray2));
//         System.out.println("************ FINISH Test Puzzle(int [] array) ************");
//         


        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        int BYTE = (1024);
        
        Class[] argTypes = new Class[] { Puzzle.class };
        List<Integer> list;
        

        System.out.println("\n\n************ START  Test aStar() ************");
        try {
//             Method m = (Solver.class).getDeclaredMethod("hammingDistance", argTypes);
//             Method m = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
//             Method m = (Solver.class).getDeclaredMethod("geometricSeries", argTypes);
            Method m = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
            list = s.aStar(p, m);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long maxMemory = heapUsage.getMax() / BYTE;
            long usedMemory = heapUsage.getUsed() / BYTE;
            System.out.println("\n>>>> OUT OF MEMORY\n\nMemory Use: " + usedMemory + "B/" + maxMemory + "B");
        }
        System.out.println("************ FINISH Test aStar() ************");
        

        
		System.out.println("\n\n************ START  Test idaStar() ************");
		try {
//             Method m = (Solver.class).getDeclaredMethod("hammingDistance", argTypes);
//             Method m = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
//             Method m = (Solver.class).getDeclaredMethod("geometricSeries", argTypes);
            Method m = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
            list = s.idaStar(p, m);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long maxMemory = heapUsage.getMax() / BYTE;
            long usedMemory = heapUsage.getUsed() / BYTE;
            System.out.println("\n>>>> OUT OF MEMORY\n\nMemory Use: " + usedMemory + "B/" + maxMemory + "B");            
        }
		System.out.println("************ FINISH Test idaStar() ************");
		
// 		System.out.println("\n\n************ START   Test followMovements() ************");
// 		Puzzle pshuffled = new Puzzle(new int []{1,2,4,8,0,5,3,13,10,6,14,7,9,11,15,12});
// 		System.out.println("Shuffled:\n"+pshuffled.toString());
// 		List<Integer> mList = new ArrayList<Integer>();
// 		Collections.addAll(mList,5,6,14,7,13,8,4,3,7,13,12,15,11,14,13,11,14,13,10,9,13,14,15);
// 		pshuffled.followMovements(mList);
// 		System.out.println("Solved:\n"+pshuffled.toString());
// 		System.out.println("\n\n************ FINISH  Test followMovements() ************");
	}
}