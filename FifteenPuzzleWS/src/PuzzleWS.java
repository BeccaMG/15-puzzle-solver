import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Arrays;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

@Path("/puzzle")
public class PuzzleWS {
 
    @GET
    @Path("/newShuffled")
    @Produces("application/json")
    public int[] newShuffled(@QueryParam("n") Integer n) {
        Puzzle puzzle = new Puzzle(n);
        puzzle.shuffle(200*n);
        System.out.println(puzzle.toString());
        return puzzle.puzzle_array;
    }
 
    @GET
    @Path("/isSolvable")
    @Produces("application/json")
    public boolean isSolvable(@QueryParam("puzzle") String puzzle) {
        String puzzleString[]  = puzzle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "") .split(","); 
        int puzzleSeed[] = new int[puzzleString.length];
        for (int i = 0; i < puzzleString.length; i++){
            try {
            puzzleSeed[i] = Integer.parseInt(puzzleString[i]);
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        Puzzle npuzzle;
        try {
        npuzzle = new Puzzle(puzzleSeed);
        } catch (ArrayIndexOutOfBoundsException nfe) {
            return false;        
            }
        return npuzzle.isSolvable();
    }
    
    @GET
    @Path("/solve")
    @Produces(MediaType.TEXT_XML)
    public String add(@QueryParam("a") double a, @QueryParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a + b) + "</result>";
    }
 
    @GET
    @Path("/interface")
    @Produces("text/html")
    public File sub(@QueryParam("a") double a, @QueryParam("b") double b) {
      
     File file = new File("resources/interface.html");

     return file;
    }



}
