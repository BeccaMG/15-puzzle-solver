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
        double priority; // Priority = number of moves + fitness function
        int movesDoneSoFar;
        List<Integer> listOfSteps; // To keep a track of movements
        
        public PuzzleNode(Puzzle current, Puzzle previous, double priority, int moves) {
            this.currentState = current;
            this.previousState = previous;
            this.priority = priority;
            this.movesDoneSoFar = moves;
            this.listOfSteps = new ArrayList<Integer>();
        }
        
        public PuzzleNode(Puzzle current, Puzzle previous, double priority, int moves, Integer newStep) {
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
    
    //TODO check whether it complies with hamming's the real algo
    /**
     * Hamming Distance checks the number of tiles out of place
     * for each piece in place the degree adds 1/size
     * @param puzzle the puzzle of which the degree will be computed
     * @return The degree of the passed puzzle
     */
    public double hammingDistance(Puzzle puzzle){
        float degree = 1;
        int size = puzzle.getSize();
        float weight = 1/(float)size;
        for(int i=0;i<size;i++)
            if(puzzle.puzzle_array[i]!=(i+1)%size)
                degree-= weight;

        //Use this as a return to return a rounded float to the nearest hundredth
        //return (float) Math.round(degree*100)/100;
        return degree;
    }


    //TODO get the value of the most mixed up puzzle to return the 1- distance/value
    /**
     * Manhattan Distance
     * compute the degree by calculating the distance between each tile and it's place
     * @return The degree of the current puzzle
     */
    public double manhattanDistance(Puzzle puzzle){        
        int size = puzzle.getSize();
        int n = (int) Math.sqrt(size);
        int distance = 0;
        int temp;
        //int [] test_array = {0,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};

        for(int i=0;i<size;i++){
            if(puzzle.puzzle_array[i]!=0) {
                temp = Math.abs(i - puzzle.puzzle_array[i] + 1);
                distance += (temp / n + temp % n);
            }
        }
        return 1- distance/Math.pow(n,3);
        //return distance;
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
            Object result = fitnessFunction.invoke(this, (Object) puzzle);
            
            System.out.println(result); // First result

            // Enqueue the initial state of the board (passed as parameter)
            // with its priority computed and 0 moves done so far.
            pqueue.add(new PuzzleNode(puzzle, null, (double) result, 0));
            PuzzleNode currentPuzzleNode = pqueue.poll();
            
            List<Integer> validMoves = new ArrayList<Integer>();
            Puzzle currentPuzzle = currentPuzzleNode.getCurrentState();
            Puzzle neighbor = new Puzzle(4); // Obviously it's not like that
            
//             while (!currentPuzzle.isSolved()) {
//                     !pqueue.isEmpty()) {
//             while (!pqueue.isEmpty()) {
                System.out.println("Puzzle in aStar\n" + currentPuzzle.toString());

                validMoves = currentPuzzle.validMoves();

                for (Integer tile : validMoves) {
                    System.out.println("Valid move: " + tile);
//                     // This will give me a board with the moved piece
//                     neighbor = currentPuzzle.movePiece((int) tile);
// 
//                     // Optimization to not enqueue when I already used that board
//                     if (!neighbor.equals(currentPuzzleNode.getPreviousState())) {
//                         result = fitnessFunction.invoke(this, (Object) neighbor);
//                         
//                         pqueue.add(
//                             new PuzzleNode(
//                                     neighbor, 
//                                     currentPuzzle, 
//                                     (double) result, // Think about adding the moves done...
//                                     currentPuzzleNode.getMovesDoneSoFar() + 1,
//                                     tile
//                             ));
// 
//                         currentPuzzleNode = pqueue.poll();
//                         currentPuzzle = currentPuzzleNode.getCurrentState();
//                     }
                }
//             }
//                             
//             // Supposed to find the solution
//             return currentPuzzleNode.getListOfSteps();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
