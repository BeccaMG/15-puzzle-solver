import java.util.*;
import java.lang.reflect.Method;

public class Solver {
    
    /**
    * Maximum possible Manhattan Distance in a 15-Puzzle.
    *
    * @see <a href="http://goo.gl/PY47HB">Walking Distance</a>
    */
    static final double MAX_MD_15PUZZLE = 60.0;
//     static final double MAX_MD_15PUZZLE = 1.0;
    
    /**
     * Maximum number of steps to solve a 15-Puzzle.
     *
     * Used to normalize the cost of moving a tile in the board.
     *
     * @see <a href="http://goo.gl/6n2R6x">The parallel search bench ZRAM and its applications</a>
     */
    static final double MAX_STEPS_15PUZZLE = 80.0;
    static final double normalizedStepCost = (1.0/MAX_STEPS_15PUZZLE);
    
    private int depthLevel = 0;
//     private final double normalizedStepCost = (1.0);


    /**
     * Represents a node in the solver algorithm.
     *
     * @see aStar(Puzzle puzzle, Method fitnessFunction)
     * @see idaStar(Puzzle puzzle, Method fitnessFunction)
     */
    private class PuzzleNode implements Comparable<PuzzleNode> {
        private Puzzle currentState;
        private Puzzle previousState; 
        private double priority;
        private double movesDoneSoFar;
        private List<Integer> listOfSteps;

        /**
         * PuzzleNode constructor.
         *
         * It creates a node in the tree of the solver algorithm.
         *  
         * @param current Puzzle with the current board.
         * @param previous Puzzle with the previous board or parent.
         * @param priority priority = fitnessFunction(node) + moves(node).
         * @param moves Cost from the root to the current node.
         */
        public PuzzleNode(Puzzle current, Puzzle previous, double priority, double moves) {
            this.currentState = current;
            this.previousState = previous;
            this.priority = priority;
            this.movesDoneSoFar = moves;
            this.listOfSteps = new ArrayList<Integer>();
        }

        /**
         * Returns the puzzle board of this node.
         *
         * @return The puzzle board corresponding to the node.
         */
        public Puzzle getCurrentState() {
            return this.currentState;
        }
        
        /**
         * Returns the puzzle board of the parent node.
         *
         * @return The puzzle board corresponding to the parent node in the tree.
         */
        public Puzzle getPreviousState() {
            return this.previousState;
        }

        /**
         * Returns the moves done to get to this node.
         *
         * @return The cost of moving from the root to the current node.
         */
        public double getMovesDoneSoFar() {
            return this.movesDoneSoFar;
        }
        
        /**
         * Returns the priority of this node.
         *
         * @return The priority of this node given by 
         *         number of moves + fitness function
         */        
        public double getPriority() {
            return this.priority;
        }

        /**
         * Returns the list of movements done from the root to this node.
         *
         * @return The list of tiles moved in the root board to get to this board.
         */  
        public List<Integer> getListOfSteps() {
            return this.listOfSteps;
        }

        /**
         * Adds a step on the list of movements.
         *
         * @return Adds a movement of a tile to this list of steps.
         */  
        public void addStepToList(List<Integer> list, Integer step) {
            this.listOfSteps.addAll(list);
            this.listOfSteps.add(step);
        }

        /**
         * Compares two PuzzleNodes.
         *
         * @return -1 is this node has a lower priority than the node passed as
         *         a parameter. 0 is equals, 1 if greater.
         */  
        @Override
        public int compareTo(PuzzleNode pn) {
            return (int) Math.signum(this.priority - pn.priority);
        }
    }


/* --------------------------- FITNESS FUNCTIONS -----------------------------*/

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
        
        for (int i = 0; i < size; i++)
            if ((array[i] != (i + 1)) && (array[i] != 0))
                misplacedTiles++;

        return misplacedTiles / (size-1);
    }


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

        for (int i = 0; i < size; i++) {
            if (array[i] != 0) {
                //difference in rows between current place and the correct one
                rows = Math.abs(i/n - (array[i] - 1)/n);
                //difference in columns between current place and the correct one
                columns = Math.abs(i%n - (array[i] - 1)%n);
                distance += rows + columns;
            }
        }
        return (distance / MAX_MD_15PUZZLE);
    }


    /**
     * The Geometric Series of sum(2^-n).
     *
     * This fitness function adds the term 2^-<i>i</i> if the tile <i>i</i> is in its
     * place on the current board. Then returns the 1 minus the sum of all terms
     * of tiles in the right place. Proposed by J. Paul Gibson.
     *
     * @see <a href="https://goo.gl/3P0uPB">Wolfram-Alpha geometric series</a>
     *
     * @param puzzle The puzzle of which the fitness value will be computed
     * @return A number between 0 and 1, representing the estimated cost to 
     *         reach the solved puzzle (normalized).
     */     
    public double geometricSeries(Puzzle puzzle) {   
        // Given the fact the sum is not exactly 1
        if (puzzle.isSolved())
            return 0;
        
        double sum = 1;
        int size = puzzle.getSize();
        int[] array = puzzle.toArray();
        
        for (int i = 0; i < size; i++)
            if ((array[i] == (i + 1)) && (array[i] != 0)) {
                sum -= Math.pow(2, -(i+1));
            }
        
        return sum;
    }
            
            
    /**
     * The Geometric Series of sum(2^-n).
     *
     * This fitness function adds the term 2^-<i>i</i> if the tile <i>i</i> is in its
     * place on the current board. Then returns the 1 minus the sum of all terms
     * of tiles in the right place. Proposed by J. Paul Gibson.
     *
     * @see <a href="https://goo.gl/3P0uPB">Wolfram-Alpha geometric series</a>
     *
     * @param puzzle The puzzle of which the fitness value will be computed
     * @return A number between 0 and 1, representing the estimated cost to 
     *         reach the solved puzzle (normalized).
     */     
    public double walkingDistancePrime(Puzzle puzzle) {
        
        double md = manhattanDistance(puzzle);
        Puzzle np = puzzle.transpose();
        int vertical = puzzle.inversions();
        int horizontal = np.inversions();
        double id = (vertical + horizontal) / 3 + (vertical + horizontal) % 3;
        
        return (md * MAX_MD_15PUZZLE > id ? md : id/58.0);
    }
            
    
/* ---------------------------- SOLVING ALGORITHMS ---------------------------*/

    /**
     * A* Search Algorithm.
     *
     * As a search algorithm, it starts from the initial board computing the
     * estimated cost to reach the goal board (solved) and, using a priority
     * queue, creates a tree and explores the more promising branches. Known
     * for being extremely memory-overhead.
     *
     * It outputs the elapsed time and sequence of moves found to solve the puzzle.
     * It resets the depth level of the solver.
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
            System.out.println("Initial puzzle in aStar with " + fitnessFunction.getName() + " of " + 
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
                        
                    }
                }

                currentPuzzleNode = pqueue.poll();
                currentPuzzle = currentPuzzleNode.getCurrentState();
                
                if (currentPuzzleNode.getListOfSteps().size() > this.depthLevel)
                    depthLevel = currentPuzzleNode.getListOfSteps().size();

            }
                        
/* --------------------------- STOP COUNTING TIME --------------------------- */            
                        
            end = System.currentTimeMillis();
            
            System.out.println("Solution in " + currentPuzzleNode.getListOfSteps().size() 
                               + " steps:\n" + currentPuzzleNode.getListOfSteps());
            System.out.println("Time: " + (end-start)/1000 + " seconds");
            System.out.println("Max depth level reached: " + depthLevel);
            depthLevel = 0;
        
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
     * It outputs the elapsed time and sequence of moves found to solve the puzzle.
     * It resets the depth level of the solver.
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
            
            System.out.println("Initial puzzle in idaStar with " + fitnessFunction.getName() + " of " 
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
            System.out.println("Max depth level reached: " + depthLevel);
            depthLevel = 0;

            return root.getListOfSteps();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    /**
     * Deep First Search.
     *
     * It explores recursively the branch of the PuzzleNode in parameter as the
     * root. It cuts off the recursion when the priority of a node is higher
     * than a threshold. Throws an exception if the fitnessFunction can't be
     * invoked.
     *
     * @param pn The root node of the branch.
     * @param threshold The given bound.
     * @param fitnessFunction Fitness function used to compute the priority of
     *                        nodes (states of the puzzle).
     * @return The priority higher than a threshold if found or -1 if the solved
     *         puzzle is reached.
     */    
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
                    if (pn.getListOfSteps().size() > this.depthLevel)
                        depthLevel = pn.getListOfSteps().size();
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