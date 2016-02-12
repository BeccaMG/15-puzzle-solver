import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.management.*;

public class Main {
    
	public static void main(String[] args){

//         int[] a = {15,4,9,2,0,11,14,1,5,13,10,7,8,12,3,6};
//         int[] a = {10,11,9,14,3,5,8,4,7,0,13,15,2,12,6,1};
//         int[] a = {0,12,9,13,15,11,10,14,3,7,2,5,4,8,6,1};
//         int[] a = {0,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
//         int[] a = {11,12,14,13,15,0,10,9,8,7,6,5,4,3,2,1};
        Puzzle p = new Puzzle(4);
        p.shuffle();
		Solver s = new Solver();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        int BYTE = (1024);
        
        Class[] argTypes = new Class[] { Puzzle.class };
        List<Integer> list;
        
        System.out.println("\n\n************ START  Test aStar() ************");
        try {
//             Method m = (Solver.class).getDeclaredMethod("hammingDistance", argTypes);
//             Method m = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
//             Method m = (Solver.class).getDeclaredMethod("geometricSeries", argTypes);
//             list = s.aStar(p, m);
//             m = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
//             m = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
//             list = s.aStar(p, m);
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
//             m = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
//             list = s.idaStar(p, m);
//             m = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
//             list = s.idaStar(p, m);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
            long maxMemory = heapUsage.getMax() / BYTE;
            long usedMemory = heapUsage.getUsed() / BYTE;
            System.out.println("\n>>>> OUT OF MEMORY\n\nMemory Use: " + usedMemory + "B/" + maxMemory + "B");            
        }
		System.out.println("************ FINISH Test idaStar() ************");
	}
}