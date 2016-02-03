import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
 
@Path("/puzzle")
public class PuzzleWS {
 
    @GET
    @Path("/newShuffled")
    @Produces(MediaType.TEXT_PLAIN)
    public String newShuffled(@QueryParam("n") Integer n) {
        Puzzle puzzle = new Puzzle(n);
        puzzle.shuffle(200*n);
        return Arrays.toString(puzzle.puzzle_array);
    }
 
    @GET
    @Path("/isSolvable")
    @Produces(MediaType.TEXT_PLAIN)
    public String isSolvable(@QueryParam("puzzle") String puzzle) {
        String puzzleString[]  = puzzle.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "") .split(","); 
        int puzzleSeed[] = new int[puzzleString.length];
        for (int i = 0; i < puzzleString.length; i++){
            try {
            puzzleSeed[i] = Integer.parseInt(puzzleString[i]);
            } catch (NumberFormatException nfe) {
                return "ERROR -- Puzzle is not parsable";
            }
        }
        Puzzle npuzzle;
        try {
        npuzzle = new Puzzle(puzzleSeed);
        } catch (ArrayIndexOutOfBoundsException nfe) {
            return "ERROR - Puzzle is not valid";        
            }
        return String.valueOf(npuzzle.isSolvable());
    }
    
    @GET
    @Path("/solve")
    @Produces(MediaType.TEXT_XML)
    public String add(@QueryParam("a") double a, @QueryParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a + b) + "</result>";
    }
 
    @GET
    @Path("/nextStep")
    @Produces(MediaType.TEXT_XML)
    public String sub(@QueryParam("a") double a, @QueryParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a - b) + "</result>";
    }
}
