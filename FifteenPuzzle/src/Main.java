import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.management.*;
import java.util.Scanner;

public class Main {
    
	public static void main(String[] args){
        int[] a = {1,7,10,8,0,3,11,2,5,13,12,15,6,9,14,4};
        Puzzle p = new Puzzle(a);
        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.println("\n\nWelcome to the Awesome 2.0 Analyser");
            System.out.println("1. Solve an easy puzzle like:");
            System.out.println(p.toString());            
            System.out.println("2. Solve a shuffled puzzle");
            System.out.println("Exit with Ctrl+C");
            System.out.print("\nEnter option: ");
            try {
                int opt = in.nextInt();
                if (opt == 1) {
                    break;
                } else if (opt == 2) {
                    System.out.println("\n\nSince a randomly shuffled puzzle" + 
                    " will be generated, we can't guaranty that it won't run " +
                    "out of memory or how long it will take.\nYou can pause " +
                    "the execution at any time by pressing Ctrl+C.\n\nWe " +
                    "strongly recommend you take a cup of coffee, sit down " +
                    "and relax... Press enter to begin solving.");
                    in = new Scanner(System.in);
                    String line = in.nextLine();
                    p = new Puzzle(4);
                    p.shuffle();
                    break;
                }
                System.out.println("\n\nInvalid option.\n");
            } catch (Exception e) {
                System.out.println("\n\nInvalid option.\n");
            }
            
        } 
//         int[] a = {10,11,9,14,3,5,8,4,7,0,13,15,2,12,6,1};
//         int[] a = {0,12,9,13,15,11,10,14,3,7,2,5,4,8,6,1};
//         int[] a = {0,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
//         int[] a = {11,12,14,13,15,0,10,9,8,7,6,5,4,3,2,1};
		Solver s = new Solver();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        int BYTE = (1024);
        
        Class[] argTypes = new Class[] { Puzzle.class };
        List<Integer> list;
        
        System.out.println("\n\n************ START  Test aStar() ************");
        try {
            Method m1 = (Solver.class).getDeclaredMethod("xDistance", argTypes);
            Method m2 = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
            Method m3 = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
            list = s.aStar(p, m1);
            list = s.aStar(p, m2);
            list = s.aStar(p, m3);
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
            Method m1 = (Solver.class).getDeclaredMethod("xDistance", argTypes);
            Method m2 = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
            Method m3 = (Solver.class).getDeclaredMethod("walkingDistancePrime", argTypes);
            list = s.aStar(p, m1);
            list = s.aStar(p, m2);
            list = s.aStar(p, m3);
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