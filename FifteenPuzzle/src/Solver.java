import java.util.*;
import java.lang.reflect.Method;
import java.lang.Math.*;

public class Solver {

    /**
     * Represents a node in the A* algorithm (solver) - more details to come...
     *
     * @see <a href="http://www.cs.princeton.edu/courses/archive/spr10/cos226/assignments/8puzzle.html">The 8-Puzzle</a>
     */
    class PuzzleNode implements Comparable<PuzzleNode> {
        private Puzzle currentState;
        private Puzzle previousState; // To don't enqueue the previous state
        public double priority; // Priority = number of moves + fitness function
        private int movesDoneSoFar;
        private List<Integer> listOfSteps; // To keep a track of movements

        public PuzzleNode(Puzzle current, Puzzle previous, double priority, int moves) {
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

        public int getMovesDoneSoFar() {
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

    Solver(Puzzle init) {
        //call ff , solve the whole puzzle, gets next move or whatever
        // this.puzzle = init;
    }

    //TODO check whether it complies with hamming's real algo
    /**
     * Hamming Distance checks the number of tiles out of place
     * for each piece in place the degree adds 1/size
     *
     * @param puzzle the puzzle of which the degree will be computed
     * @return The degree of the passed puzzle
     */
    public double hammingDistance(Puzzle puzzle) {
        float degree = 0;
        int size = puzzle.getSize();
        float weight = 1 / (float) size;
        for (int i = 0; i < size; i++)
            if (puzzle.puzzle_array[i] != (i + 1) % size)
                degree += weight;

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
            if (puzzle.puzzle_array[i] != 0) {
                //difference in rows between current place and the correct one
                rows = Math.abs(i/n - (puzzle.puzzle_array[i] - 1)/n);
                //difference in columns between current place and the correct one
                columns = Math.abs(i%n - (puzzle.puzzle_array[i] - 1)%n);
                distance += rows + columns;
            }
        }
//         return (distance / Math.pow(n, 3));
//         return 1 - distance / Math.pow(n, 3);
        return distance;
    }


    /**
     * aStar
     * <p>
     * A* Algorithm
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
    public List<Integer> aStar(Puzzle puzzle, Method fitnessFunction) {

        long start = System.currentTimeMillis();
        // If the puzzle is not solvable, return null. (Or might throw an exception depending of our implementation)
        if (!puzzle.isSolvable())
            return null;

        List<Integer> maxs;
        int maxn = 0;

        PriorityQueue<PuzzleNode> pqueue = new PriorityQueue<PuzzleNode>();

        try { // Execute the fitnessFunction

            double result = (double) fitnessFunction.invoke(this, puzzle);  // REFLECTION NOT COMPLETELY WELL USED

//             System.out.println(result); // First result

            // Enqueue the initial state of the board (passed as parameter)
            // with its priority computed and 0 moves done so far.
            pqueue.add(
                new PuzzleNode(puzzle, null, result, 0)
            );

            PuzzleNode currentPuzzleNode = pqueue.poll();

            List<Integer> validMoves = new ArrayList<Integer>();
            Puzzle currentPuzzle = currentPuzzleNode.getCurrentState();
            Puzzle neighbor;
            PuzzleNode newPuzzleNode;
            double newPriority = 0;
//             int newMoves;
            int newMoves;

            System.out.println("START Puzzle in aStar with " + result + "\n" + currentPuzzle.toString());
//             int i = 0;
            while (!currentPuzzle.isSolved()) {
//                     !pqueue.isEmpty()) {
//             for (int i = 0; i < 100; i++) {
//                 System.out.println(i+"-Puzzle in aStar with " +
//                 currentPuzzleNode.priority + "\n" + currentPuzzle.toString());

                validMoves = currentPuzzle.validMoves();

                for (Integer tile : validMoves) {
//                     System.out.println("Valid move: " + tile);
                    // This will give me a board with the moved piece
                    neighbor = currentPuzzle.movePiece(tile);
//                     System.out.println("neighbor\n" + neighbor.toString());

                    // Optimization to not enqueue when I already used that board
                    if (!neighbor.equals(currentPuzzleNode.getPreviousState())) {
                        result = (double) fitnessFunction.invoke(this, neighbor);
                        // TEST
                        newMoves = currentPuzzleNode.getMovesDoneSoFar() + 1;
//                         newPriority = result + ((newMoves * 1.0) / 80.0);
                        newPriority = result + newMoves;
                        newPuzzleNode = new PuzzleNode(
                                            neighbor,
                                            currentPuzzle,
                                            newPriority, // Think about adding the moves done...
                                            newMoves
                                        );
                        newPuzzleNode.addStepToList(currentPuzzleNode.getListOfSteps(), tile);

                        pqueue.add(newPuzzleNode);
                    }

//                     i++;
                }

                if (currentPuzzleNode.getListOfSteps().size() > maxn) {
                    maxs = currentPuzzleNode.getListOfSteps();
                    maxn = maxs.size();
                }
                currentPuzzleNode = pqueue.poll();
                currentPuzzle = currentPuzzleNode.getCurrentState();

            }
//
            // Supposed to find the solution
            System.out.println("SOLVED Puzzle in aStar\n" + currentPuzzle.toString());
            System.out.println("Solution in " + currentPuzzleNode.getListOfSteps().size() + " steps:\n" + currentPuzzleNode.getListOfSteps());
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end-start)/1000 + " seconds");
//             System.out.println("Maxn: " + maxn);
             return currentPuzzleNode.getListOfSteps();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
        
        
    public double search(Puzzle puzzle, double cost, double bound, Method fitnessFunction) {
    
        try {
            
            double f = cost + (double) fitnessFunction.invoke(this, puzzle);
            if (f > bound)
                return f;
            if (puzzle.isSolved())
                return -1.0;
            
            double min = Double.POSITIVE_INFINITY;
            
            List<Integer> validMoves = new ArrayList<Integer>();
            Puzzle neighbor;
            validMoves = puzzle.validMoves();
                
            for (Integer tile : validMoves) {
                neighbor = puzzle.movePiece(tile);
                double t = search(neighbor, cost + 1.0, bound, fitnessFunction);
                if (t == -1.0)
                    return -1.0;
                if (t < min)
                    min = t;
            }
            
            return min;
                
            
        } catch (Exception e) {
            e.printStackTrace();
            return -3.0;
        }
    
    }
        
    /**
     * aStar
     * <p>
     * A* Algorithm
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

        long start = System.currentTimeMillis();
        // If the puzzle is not solvable, return null. (Or might throw an exception depending of our implementation)
        if (!puzzle.isSolvable())
            return -2.0;
    
        double returnValue;
    
        try { // Execute the fitnessFunction

            double bound = (double) fitnessFunction.invoke(this, puzzle);  // REFLECTION NOT COMPLETELY WELL USED
            long end;
            System.out.println("START Puzzle in idaStar with " + bound + "\n" + puzzle.toString());

            while (true) {
                
                double t = search(puzzle, 0, bound, fitnessFunction);
                if (t == -1.0) {
                    end = System.currentTimeMillis();
                    System.out.println("Time: " + (end-start)/1000 + " seconds");
                    return -1.0; //FOUND
                }
                if (t == Double.POSITIVE_INFINITY) {
                    end = System.currentTimeMillis();
                    System.out.println("Time: " + (end-start)/1000 + " seconds");
                    return-2.0; //NOT_FOUND
                }
                if (t == -3.0) {
                    end = System.currentTimeMillis();
                    System.out.println("Time: " + (end-start)/1000 + " seconds");
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
