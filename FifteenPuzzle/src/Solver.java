import java.util.*;
import java.lang.reflect.Method;
import java.lang.Math.*;

public class Solver {
    

    
    /**
     * Represents a node in the A* algorithm (solver) - more details to come...
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
     */
    class PuzzleNode implements Comparable<PuzzleNode> {
        Puzzle currentState;
        Puzzle previousState; // To don't enqueue the previous state
        float priority; // Priority = number of moves + fitness function
        int movesDoneSoFar;
        List<Integer> listOfSteps; // To keep a track of movements
        
        public PuzzleNode(Puzzle current, Puzzle previous, float priority, int moves) {
            this.currentState = current;
            this.previousState = previous;
            this.priority = priority;
            this.movesDoneSoFar = moves;
            this.listOfSteps = new ArrayList<Integer>();
        }
        
        public PuzzleNode(Puzzle current, Puzzle previous, float priority, int moves, Integer newStep) {
            this.currentState = current;
            this.previousState = previous;
            this.priority = priority;
            this.movesDoneSoFar = moves;
            this.listOfSteps.add(newStep);
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
        
        public List<Integer> getListOfSteps() {
            return this.listOfSteps;
        }
        
        @Override
        public int compareTo(PuzzleNode pn) {
            return (int) Math.signum(this.priority - pn.priority);
        }
    }
    
    Solver(Puzzle init){
        //call ff , solve the whole puzzle, gets next move or whatever
       // this.puzzle = init;
    }
    
// @TODO check whether it complies with hamming's the real algo
    /**
     * Out Of Place fitness function
     * for each piece in place the degree adds 0.0625
     * @return The degree of the current puzzle as calculated by the oop
     */
    public double hammingDistance(Puzzle puzzle){ //not object oriented programming :D - nor general at all xD
        float degree = 1;
        int size = puzzle.getSize();
        size *= size;
        float weight = 1/(float)size;
        for(int i=0;i<size;i++){
            if(puzzle.puzzle_array[i]!=(i+1)%size)
                degree-= weight;
        }
        //Use this as a return to return a rounded float to the nearest hundredth
        //return (float) Math.round(degree*100)/100;
        return degree;
    }
    
    /**
     * aStar
     *
     * A* Algorithm
     * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* Search Algorithm</a>
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
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
        
        // If the puzzle is not solvable, return null. (Or might throw an exception depending of our implementation)
        if (!puzzle.isSolvable())
            return null;
        
        PriorityQueue<PuzzleNode> pqueue = new PriorityQueue<PuzzleNode>();
        
        try { // Execute the fitnessFunction
            Object[] parameters = new Object[1];
            Object result = new Object(); // Float with the fitness value
            parameters[0] = puzzle; // The fitness function should receive a puzzle        
            fitnessFunction.invoke(result, parameters);
            
            // Enqueue the initial state of the board (passed as parameter)
            // with its priority computed and 0 moves done so far.
            pqueue.add(new PuzzleNode(puzzle, null, (float) result, 0));
            PuzzleNode currentPuzzleNode = pqueue.poll();
            
            List<Integer> validMoves = new ArrayList<Integer>();
            Puzzle currentPuzzle = currentPuzzleNode.getCurrentState();
            Puzzle neighbor = new Puzzle(4); // Obviously it's not like that
            
//             while (!currentPuzzle.isSolved() 
//                    && !pqueue.isEmpty()) {
            while (!pqueue.isEmpty()) {
//                 validMoves = currentPuzzle.validMoves();

                for (Integer tile : validMoves) {
                    // This will give me a board with the moved piece
//                     neighbor = currentPuzzle.movePiece((int) tile);

                    // Optimization to not enqueue when I already used that board
                    if (!neighbor.equals(currentPuzzleNode.getPreviousState())) {
                        parameters[0] = neighbor;
                        fitnessFunction.invoke(result, parameters);
                        
                        pqueue.add(
                            new PuzzleNode(
                                    neighbor, 
                                    currentPuzzle, 
                                    (float) result, // Think about adding the moves done...
                                    currentPuzzleNode.getMovesDoneSoFar() + 1,
                                    tile
                            ));

                        currentPuzzleNode = pqueue.poll();
                        currentPuzzle = currentPuzzleNode.getCurrentState();
                    }
                }
            }
                            
            // Supposed to find the solution
            return currentPuzzleNode.getListOfSteps();
            
        } catch (Exception e) {
            return null;
        }

    }
}