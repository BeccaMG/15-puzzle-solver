import java.util.*;
import java.lang.reflect.Method;

public class Solver {
    
//     private final double normalizedStepCost = (1.0/80.0);
    private final double normalizedStepCost = (1.0);

    /**
     * Represents a node in the A* algorithm (solver) - more details to come...
     *
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
     */
    private class PuzzleNode implements Comparable<PuzzleNode> {
        private Puzzle currentState;
        private Puzzle previousState; // To don't enqueue the previous state
        private double priority; // Priority = number of moves + fitness function
        private double movesDoneSoFar;
        private List<Integer> listOfSteps; // To keep a track of movements

        public PuzzleNode(Puzzle current, Puzzle previous, double priority, double moves) {
            this.currentState = current;
            this.previousState = previous;
            this.priority = priority;
            this.movesDoneSoFar = moves;
            this.listOfSteps = new ArrayList<Integer>();
        }

        public Puzzle getCurrentState() {
            return this.currentState;
        }

        public Puzzle getPreviousState() {
            return this.previousState;
        }

        public double getMovesDoneSoFar() {
            return this.movesDoneSoFar;
        }
        
        public double getPriority() {
            return this.priority;
        }

        public List<Integer> getListOfSteps() {
            return this.listOfSteps;
        }

        public void addStepToList(List<Integer> list, Integer step) {
            this.listOfSteps.addAll(list);
            this.listOfSteps.add(step);
        }

        @Override
        public int compareTo(PuzzleNode pn) {
            return (int) Math.signum(this.priority - pn.priority);
        }
    }


    /**
     * The Hamming distance computes the number of tiles out of place.
     *
     * The Zero tile is not taken into consideration so that the fitness function
     * becomes admissible.
     *
     * @see <a href="https://goo.gl/uEbt77">Hamming Distance</a>
     *
     * @param puzzle The puzzle of which the fitness value will be computed
     * @return A number between 0 and 1, representing the estimated cost to 
     *         reach the solved puzzle (normalized). 
     */
    public double hammingDistance(Puzzle puzzle) {
        double misplacedTiles = 0;
        int size = puzzle.getSize();
        int[] array = puzzle.toArray();
        
        for (int i = 0; i <= size; i++)
            if ((array[i] != (i + 1)) && (array[i] != 0))
                misplacedTiles++;

//         return misplacedTiles / (size);
        return misplacedTiles;
    }


    // TODO get the value of the most mixed up puzzle to return the distance/value
    /**
     * The Manhattan distance computes the total <i>"square distance"</i> between 
     * each tile and its correct place.
     *
     * The Zero tile is not taken into consideration so that the fitness function
     * becomes admissible.
     *
     * @see <a href="https://goo.gl/9GgtHd">Manhattan Distance</a>
     *
     * @param puzzle The puzzle of which the fitness value will be computed
     * @return A number between 0 and 1, representing the estimated cost to 
     *         reach the solved puzzle (normalized). 
     */
    public double manhattanDistance(Puzzle puzzle) {
        int size = puzzle.getSize();
        int n = puzzle.getDimension(); // Get the dimension of the matrix
        int distance = 0;
        int rows, columns;
        int[] array = puzzle.toArray();

        for (int i = 0; i <= size; i++) {
            if (array[i] != 0) {
                //difference in rows between current place and the correct one
                rows = Math.abs(i/n - (array[i] - 1)/n);
                //difference in columns between current place and the correct one
                columns = Math.abs(i%n - (array[i] - 1)%n);
                distance += rows + columns;
            }
        }
//         return (distance / Math.pow(n, 3));
//         return (distance / 58.0);
//         return (distance / 57.0);
        return distance;
    }


    /**
     * A* Search Algorithm.
     *
     * As a search algorithm, it starts from the initial board computing the
     * estimated cost to reach the goal board (solved) and, using a priority
     * queue, creates a tree and explores the more promising branches. Known
     * for being extremely memory-overhead.
     *
     * It outputs the time and sequence of moves found.
     *
     * @param puzzle The puzzle to solve
     * @param fitnessFunction Fitness function used to compute the priority of
     *                        nodes (states of the puzzle).
     * @return The ordered list of steps to follow for solving the puzzle, in
     *         the form of Integer meaning which tile to move (swap with the 
     *         blank space).
     *
     * @see <a href="https://goo.gl/LyNvTh">A* Search Algorithm</a>
     * @see <a href="http://goo.gl/jGxpLQ">The 8-Puzzle</a>
     */
    public List<Integer> aStar(Puzzle puzzle, Method fitnessFunction) {

        // If the puzzle is not solvable, return null.
        if (!puzzle.isSolvable())
            return null;

        PriorityQueue<PuzzleNode> pqueue = new PriorityQueue<PuzzleNode>();        
//         List<Integer> maxs;
//         int maxn = 0;
        long start;
        long end;
        
        try {  // Execute the fitnessFunction
            
            start = System.currentTimeMillis();
            
/* -------------------------- START COUNTING TIME --------------------------- */            

            double result = (double) fitnessFunction.invoke(this, puzzle);

            // Enqueue the initial state of the board (passed as parameter)
            // with its priority computed and 0 moves done so far.
            pqueue.add(
                new PuzzleNode(puzzle, null, result, 0.0)
            );

            PuzzleNode currentPuzzleNode = pqueue.poll();
            Puzzle currentPuzzle = currentPuzzleNode.getCurrentState();
            System.out.println("Initial puzzle in aStar with fitness of " + 
                               result + "\n" + currentPuzzle.toString());

            List<Integer> validMoves;
            Puzzle neighbor;
            PuzzleNode newPuzzleNode;
            double newPriority;
            double newMoves;
            

            while (!currentPuzzle.isSolved()) {

                validMoves = currentPuzzle.validMoves();

                for (Integer tile : validMoves) {
                    // This will give me a board with the moved piece
                    neighbor = currentPuzzle.movePiece(tile);

                    // Optimization to not enqueue when I already had that board
                    if (!neighbor.equals(currentPuzzleNode.getPreviousState())){

                        result = (double) fitnessFunction.invoke(this, neighbor);
                        
                        newMoves = currentPuzzleNode.getMovesDoneSoFar() + normalizedStepCost;
                        newPriority = result + newMoves;
                        
                        newPuzzleNode = new PuzzleNode(
                            neighbor, currentPuzzle, newPriority, newMoves
                        );
                        newPuzzleNode.addStepToList(
                            currentPuzzleNode.getListOfSteps(), tile);

                        pqueue.add(newPuzzleNode);
                        
//                         if (newPuzzleNode.getListOfSteps().size() > maxn) {
//                             maxs = newPuzzleNode.getListOfSteps();
//                             maxn = maxs.size();
//                         }
                    }
                }

                currentPuzzleNode = pqueue.poll();
                currentPuzzle = currentPuzzleNode.getCurrentState();

            }
                        
/* --------------------------- STOP COUNTING TIME --------------------------- */            
                        
            end = System.currentTimeMillis();
            
            System.out.println("Solution in " + currentPuzzleNode.getListOfSteps().size() 
                               + " steps:\n" + currentPuzzleNode.getListOfSteps());
            System.out.println("Time: " + (end-start)/1000 + " seconds");
//             System.out.println("Maxn: " + maxn);
//             System.out.println("Pqueue: " + pqueue.size());
        
            return currentPuzzleNode.getListOfSteps();
        
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
        

        
    /**
     * IDA* Algorithm.
     *
     * Iterative deepening A* is a memory optimization of the A* algorithm,
     * sacrificing performance. It explores every branch of the tree and cuts it
     * when reaching a threshold.
     *
     * It outputs the time and sequence of moves found.
     *
     * @param puzzle The puzzle to solve
     * @param fitnessFunction Fitness function used to compute the priority of
     *                        nodes (states of the puzzle).
     * @return The ordered list of steps to follow for solving the puzzle, in
     *         the form of Integer meaning which tile to move (swap with the
     *         blank space).
     *
     * @see <a href="https://goo.gl/Vt0ixg">IDA* Algorithm</a>
     * @see <a href="https://goo.gl/3jMx4i">IDA*</a>
     */
    public List<Integer> idaStar(Puzzle puzzle, Method fitnessFunction) {

        // If the puzzle is not solvable, return null.
        if (!puzzle.isSolvable())
            return null;
    
        long start;
        long end;
        double deepSearch = 0.0;
        
        try { // Execute the fitnessFunction

            start = System.currentTimeMillis();
            
/* -------------------------- START COUNTING TIME --------------------------- */

            double threshold = (double) fitnessFunction.invoke(this, puzzle);
            PuzzleNode root = new PuzzleNode(puzzle, null, threshold, 0.0);
            
            System.out.println("Initial puzzle in idaStar with fitness of " 
                            + threshold + "\n" + puzzle.toString());

            while (deepSearch != -1.0) { // While not found in the children
                deepSearch = dfs(root, threshold, fitnessFunction);            
                threshold = deepSearch;
            }

/* --------------------------- STOP COUNTING TIME --------------------------- */            

            end = System.currentTimeMillis();
            
            Collections.reverse(root.getListOfSteps());
            System.out.println("Solution in " + root.getListOfSteps().size() + 
                               " steps:\n" + root.getListOfSteps());
            System.out.println("Time: " + (end-start)/1000 + " seconds");

            return root.getListOfSteps();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    
    private double dfs(PuzzleNode pn, double threshold, Method fitnessFunction) throws Exception {
    
        Puzzle currentPuzzle = pn.getCurrentState();
        
        if (pn.getPriority() > threshold) {
            return pn.getPriority();
        }
            
        if (currentPuzzle.isSolved()) {
            return -1.0;
        }
            
        double min = Double.POSITIVE_INFINITY;
            
        List<Integer> validMoves = new ArrayList<Integer>();
        validMoves = currentPuzzle.validMoves();
            
        Puzzle neighbor;
        PuzzleNode newPuzzleNode;
        double newMoves;
        double newPriority;
        double deepSearch;
                
        for (Integer tile : validMoves) {

            neighbor = currentPuzzle.movePiece(tile);
                
            if (!neighbor.equals(pn.getPreviousState())) {
                    
                newMoves = pn.getMovesDoneSoFar() + normalizedStepCost;
                newPriority = newMoves + (double) fitnessFunction.invoke(this, neighbor);
                
                newPuzzleNode = 
                    new PuzzleNode(neighbor, currentPuzzle, newPriority, newMoves);
                
                deepSearch = dfs(newPuzzleNode, threshold, fitnessFunction);
                
                if (deepSearch == -1.0) {
                    pn.addStepToList(newPuzzleNode.getListOfSteps(), tile);
                    return -1.0;
                }
                
                if (deepSearch < min) {
                    min = deepSearch;
                }
            }
        }
            
        return min;
    }
                
}
