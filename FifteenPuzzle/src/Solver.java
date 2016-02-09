import java.util.*;
import java.lang.reflect.Method;

public class Solver {
    
    private final double normalizedStepCost = (1.0/80.0);
//     private final double normalizedStepCost = (1.0);

    /**
     * Represents a node in the A* algorithm (solver) - more details to come...
     *
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
     */
    class PuzzleNode implements Comparable<PuzzleNode> {
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
/*
    Solver(Puzzle puzzle) {
        this.normalizedStepCost = (1.0/80.0);
    }*/

    //TODO check whether it complies with hamming's the real algo

    /**
     * Hamming Distance checks the number of tiles out of place
     * for each piece in place the degree adds 1/size
     *
     * @param puzzle the puzzle of which the degree will be computed
     * @return The degree of the passed puzzle
     */
    public double hammingDistance(Puzzle puzzle) {
        float degree = 1;
        int size = puzzle.getSize();
        float weight = 1 / (float) size;
        for (int i = 0; i < size; i++)
            if (puzzle.toArray()[i] != (i + 1) % size)
                degree -= weight;

        //Use this as a return to return a rounded float to the nearest hundredth
        //return (float) Math.round(degree*100)/100;
        return degree;
    }


    //TODO get the value of the most mixed up puzzle to return the 1- distance/value

    /**
     * Manhattan Distance
     * compute the degree by calculating the distance between each tile and it's place
     *
     * @return The degree of the current puzzle
     */
    public double manhattanDistance(Puzzle puzzle) {
        int size = puzzle.getSize();
        int n = (int) Math.sqrt(size);
        int distance = 0;
        int rows,columns;
//        int [] test_array = {0,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};
//        int [] test_array = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,0};
        for (int i = 0; i < size; i++) {
            if (puzzle.toArray()[i] != 0) {
                //difference in rows between current place and the correct one
                rows = Math.abs(i/n - (puzzle.toArray()[i] - 1)/n);
                //difference in columns between current place and the correct one
                columns = Math.abs(i%n - (puzzle.toArray()[i] - 1)%n);
                distance += rows + columns;
            }
        }
        return (distance / Math.pow(n, 3));
//         return (distance / 58.0);
//         return distance;
    }


    /**
     * aStar
     *
     * A* Algorithm
     *
     * @param puzzle          The puzzle to solve
     * @param fitnessFunction Fitness function used to compute the priority of
     *                        nodes (states of the puzzle).
     * @return The ordered list of steps to follow for solving the puzzle, in
     *         the form of Integer meaning which tile to move (swap with the 
     *         blank space).
     * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* Search Algorithm</a>
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
     */
    public List<Integer> aStar(Puzzle puzzle, Method fitnessFunction) {

        // If the puzzle is not solvable, return null.
        if (!puzzle.isSolvable())
            return null;

        PriorityQueue<PuzzleNode> pqueue = new PriorityQueue();        
        List<Integer> maxs;
        int maxn = 0;
        long start;
        long end;

                    
        // Execute the fitnessFunction
        try {
            
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
            System.out.println("START Puzzle in aStar with " + 
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
//                     System.out.println("neighbor\n" + neighbor.toString());

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
                        
                        if (newPuzzleNode.getListOfSteps().size() > maxn) {
                            maxs = newPuzzleNode.getListOfSteps();
                            maxn = maxs.size();
                        }
                    }
                }

                currentPuzzleNode = pqueue.poll();
                currentPuzzle = currentPuzzleNode.getCurrentState();

            }
                        
/* --------------------------- STOP COUNTING TIME --------------------------- */            
                        
            end = System.currentTimeMillis();
            
            System.out.println("SOLVED Puzzle in aStar\n" + currentPuzzle.toString());
            System.out.println("Solution in " + currentPuzzleNode.getListOfSteps().size() + " steps:\n" + currentPuzzleNode.getListOfSteps());
            System.out.println("Time: " + (end-start)/1000 + " seconds");
            System.out.println("Maxn: " + maxn);
            System.out.println("Pqueue: " + pqueue.size());
        
            return currentPuzzleNode.getListOfSteps();
        
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
        
    public double search(Puzzle puzzle, double cost, double bound, Method fitnessFunction) {
    
        try {
            
            double f = cost + (double) fitnessFunction.invoke(this, puzzle);
            if (f > bound) {
                return f;
            }
            if (puzzle.isSolved()) {
                return -1.0;
            }
            
            double min = Double.POSITIVE_INFINITY;
            
            List<Integer> validMoves = new ArrayList<Integer>();
            Puzzle neighbor;
            validMoves = puzzle.validMoves();
                
            for (Integer tile : validMoves) {
                neighbor = puzzle.movePiece(tile);
                double t = search(neighbor, cost + normalizedStepCost, bound, fitnessFunction);
                if (t == -1.0) {
                    return -1.0;
                }
                if (t < min) {
                    min = t;
                }
            }
            
            return min;
            
        } catch (Exception e) {
            e.printStackTrace();
            return -3.0;
        }
    
    }
        
    /**
     * idaStar
     * 
     * IDA* Algorithm
     *
     * @param puzzle          The puzzle to solve
     * @param fitnessFunction Fitness function used to compute the priority of
     *                        nodes (states of the puzzle).
     * @return The ordered list of steps to follow for solving the puzzle, in
     * the form of Integer meaning which tile to move (swap with the
     * blank space).
     * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">A* Search Algorithm</a>
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
     */
    public double idaStar(Puzzle puzzle, Method fitnessFunction) {

        // If the puzzle is not solvable, return null. (Or might throw an exception depending of our implementation)
        if (!puzzle.isSolvable())
            return -2.0;
    
        long start;
        long end;
        
//         long start = System.currentTimeMillis();
        double returnValue;
    
        try { // Execute the fitnessFunction

            double bound = (double) fitnessFunction.invoke(this, puzzle);  // REFLECTION NOT COMPLETELY WELL USED
            
            System.out.println("START Puzzle in idaStar with " + bound + "\n" + puzzle.toString());

            while (true) {
                
                double t = search(puzzle, 0.0, bound, fitnessFunction);
                if (t == -1.0) {
//                     end = System.currentTimeMillis();
//                     System.out.println("Time: " + (end-start)/1000 + " seconds");
                    return -1.0; //FOUND
                }
                if (t == Double.POSITIVE_INFINITY) {
//                     end = System.currentTimeMillis();
//                     System.out.println("Time: " + (end-start)/1000 + " seconds");
                    System.out.println("Does this ever happen?");
                    return-2.0; //NOT_FOUND
                }
                if (t == -3.0) {
//                     end = System.currentTimeMillis();
//                     System.out.println("Time: " + (end-start)/1000 + " seconds");
                    return -3.0; //EXCEPTION
                }
                bound = t;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return -3.0;
        }

    }

}