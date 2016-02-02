package Server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
 
@Path("/puzzle")
public class PuzzleWS {
 
    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_PLAIN)
    public String addPlainText(@QueryParam("a") double a, @QueryParam("b") double b) {
        return (a + b) + "";
    }
 
    @GET
    @Path("/sub")
    @Produces(MediaType.TEXT_PLAIN)
    public String subPlainText(@QueryParam("a") double a, @QueryParam("b") double b) {
        return (a - b) + "";
    }
 
    @GET
    @Path("/add")
    @Produces(MediaType.TEXT_XML)
    public String add(@QueryParam("a") double a, @QueryParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a + b) + "</result>";
    }
 
    @GET
    @Path("/sub")
    @Produces(MediaType.TEXT_XML)
    public String sub(@QueryParam("a") double a, @QueryParam("b") double b) {
        return "<?xml version=\"1.0\"?>" + "<result>" +  (a - b) + "</result>";
    }
}
