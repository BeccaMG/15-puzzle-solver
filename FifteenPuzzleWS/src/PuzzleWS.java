import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;

import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Method;
import java.io.File;

@Path("/puzzle")
public class PuzzleWS {
	
	/**
     * Generates a new shuffled puzzle.
     *
     * This function of the WS creates a new puzzle shuffle with the standard  
     * shuffle function of the Puzzle class and respond to the remote client
     * with the array representation of the puzzle JSONized
     * e.g. "http://ip:port/puzzle/newShuffled?n=4" Returns a puzzle with dimension
     * 4.
     * 	
     * @param n The dimension of the puzzle grid. Should be > 1. (Fetched from url)
     */
    @GET
    @Path("/newShuffled")
    @Produces("application/json")
    public int[] newShuffled(@QueryParam("n") Integer n) {
        Puzzle puzzle = new Puzzle(n);
        puzzle.shuffle();
        System.out.println(puzzle.toArray());
        return puzzle.toArray();
    }
    /**
     * Verifies if a puzzle is solvable.
     *
     * This function receives a puzzle in an array representation verifies if its solvable using
     * the isSolvable function from puzzle class.
     * e.g. "http://ip:port/puzzle/isSolvable?puzzle=1,7,10,8,0,3,11,2,5,13,12,15,6,9,14,4" Returns true
     * @param puzzle The puzzle in array representation (Fetched from url)
     */
    @GET
    @Path("/isSolvable")
    @Produces("application/json")
    public boolean isSolvable(@QueryParam("puzzle") String puzzle) {
        return stringToPuzzle(puzzle).isSolvable();
    }
    
    /**
     * Solve the puzzle received
     * 
     * This function returns a array with the list of pieces to move to reach the goal state.
     * Uses the aStar function from solver with fitness function manhattanDistance, the faster
     * fitness in our tests.
     * @param puzzle The puzzle in array representation (Fetched from url)
     */
    @GET
    @Path("/solve")
    @Produces("application/json")
    public int[] add(@QueryParam("puzzle") String puzzle) {
    	List<Integer> list = null;    	
        Puzzle npuzzle = stringToPuzzle(puzzle);
        Solver s;
        try {
        	s = new Solver();
        } catch (ArrayIndexOutOfBoundsException nfe) {
            return null;        
            }
    	Class[] argTypes = new Class[] { Puzzle.class };		
		try {
            Method m = (Solver.class).getDeclaredMethod("manhattanDistance", argTypes);
            list = s.aStar(npuzzle, m);
        } catch (Exception e) {
            e.printStackTrace();
        }
		StringBuilder strbul  = new StringBuilder();
	     Iterator<Integer> iter = list.iterator();
	     while(iter.hasNext())
	     {
	         strbul.append(iter.next());
	        if(iter.hasNext()){
	         strbul.append(",");
	        }
	     }
	     
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
	    	  
		return ret;
    }
    
    /**
     * Shows a graphic WebGUI (Beta this was not finished and it only displays a shuffle puzzle)
     * 
     * Function to serve the html resource
     * @return the html file
     */
    @GET
    @Path("/interface")
    @Produces("text/html")
    public File serveInterface() {
      
     File file = new File("resources/interface.html");

     return file;
    }
    
    /**
     * Function that parse a puzzle from a string
     * 
     * This functions receives the string fetched from the URL removes the unwanted characters
     * and returns a clean puzzle.
     * 
     * @param puzzleStr Puzzle in a string representation with an array like structure
     * @return puzzle created from the string
     */
    public Puzzle stringToPuzzle(String puzzleStr) {
    	Puzzle npuzzle = null;
    	String puzzleString[]  = puzzleStr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "") .split(",");
    	int puzzleSeed[] = new int[puzzleString.length];
    	for (int i = 0; i < puzzleString.length; i++){
            try {
            puzzleSeed[i] = Integer.parseInt(puzzleString[i]);
            } catch (NumberFormatException nfe) {
                return npuzzle;
            }
        }
    	try {
    	npuzzle = new Puzzle(puzzleSeed);
    	} catch (ArrayIndexOutOfBoundsException nfe) {
            return npuzzle;        
            }
    	return npuzzle;
    }



}
