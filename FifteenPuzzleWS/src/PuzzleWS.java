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
 
    @GET
    @Path("/newShuffled")
    @Produces("application/json")
    public int[] newShuffled(@QueryParam("n") Integer n) {
        Puzzle puzzle = new Puzzle(n);
        puzzle.shuffle();
        System.out.println(puzzle.toArray());
        return puzzle.toArray();
    }
 
    @GET
    @Path("/isSolvable")
    @Produces("application/json")
    public boolean isSolvable(@QueryParam("puzzle") String puzzle) {
        return stringToPuzzle(puzzle).isSolvable();
    }
    
    @GET
    @Path("/solve")
    @Produces("application/json")
    public String add(@QueryParam("puzzle") String puzzle) {
    	List<Integer> list = null;    	
        Puzzle npuzzle = stringToPuzzle(puzzle);
        Solver s;
        try {
        	s = new Solver();
        } catch (ArrayIndexOutOfBoundsException nfe) {
            return "Error - not parsable";        
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
		return strbul.toString();
    }
 
    @GET
    @Path("/interface")
    @Produces("text/html")
    public File sub(@QueryParam("a") double a, @QueryParam("b") double b) {
      
     File file = new File("resources/interface.html");

     return file;
    }
    
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
