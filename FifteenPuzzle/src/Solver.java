import java.util.*;
import java.lang.reflect.Method;
import java.lang.Math.*;

public class Solver {
    
    Puzzle puzzle;
    
    /**
     * Represents a node in the A* algorithm (solver) - more details to come...
     * @see http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html
     */
    class PuzzleNode implements Comparable<PuzzleNode> {
        Puzzle currentState;
        Puzzle previousState;
        float priority;
        int movesDoneSoFar;
        
        public PuzzleNode(Puzzle current, Puzzle previous, float priority, int moves){
            this.currentState = current;
            this.previousState = previous;
            this.priority = priority;
            this.movesDoneSoFar = moves;
        }
        
        public Puzzle getCurrentState() {
            return this.currentState;
        }
        
        public Puzzle getPreviousState() {
            return this.previousState;
        }
        
        public int getMovesDoneSoFar() {
            return this.movesDoneSoFar;
        }
        
        @Override
        public int compareTo(PuzzleNode pn) {
            return (int) Math.signum(this.priority - pn.priority);
        }
    }
    
    Solver(Puzzle init){
        //call ff , solve the whole puzzle, gets next move or whatever
        this.puzzle = init;
    }
    
// @TODO think about changing to Hamming distance
    /**
     * Out Of Place fitness function
     * for each piece in place the degree adds 0.0625
     * @return The degree of the current puzzle as calculated by the oop
     */
    public double oop_fitness(){ //not object oriented programming :D - nor general at all xD
        double degree = 1.0;
        for(int i=0;i<16;i++){
            if(puzzle.puzzle_array[i]!=i+1 && i!=15)
                degree-=0.0625;
            else if(puzzle.puzzle_array[i]!=0 && i==15)
                degree-=0.0625;
        }
        return degree;
    }
    
    /**
     * aStar
     *
     * A* algorithm
     * @see https://en.wikipedia.org/wiki/A*_search_algorithm
     *
     * @param puzzle The puzzle to solve
     * @param fitnessFunction Fitness function used to compute the priority of
     *                        nodes (states of the puzzle).
     *
     * @return The ordered list of steps to follow for solving the puzzle, in 
     *         the form of Integer meaning which tile to move (swap with the 
     *         blank space).
     */
    public List<Integer> aStar(Puzzle puzzle, Method fitnessFunction) {
        PriorityQueue<PuzzleNode> pqueue = new PriorityQueue<PuzzleNode>();
        List<Integer> listOfSteps = new ArrayList<Integer>();
        
        Object[] parameters = new Object[1];
        Object result = new Object();
        parameters[0] = puzzle;
        
        try {
            fitnessFunction.invoke(result, parameters);
        } catch (Exception e) {
            
        }
        
        return null;
    }
}
