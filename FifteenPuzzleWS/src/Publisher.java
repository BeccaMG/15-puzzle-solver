import java.io.IOException;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
 
public class Publisher {
	/**
	 * String for the base URI of webservices, change with appropiate hostname and port if neccesary. (Currently uses port 80 and requires sudo permission)
	 * 
	 */
    static final String BASE_URI = "http://localhost/";
    
    /**
     * Main function that runs the server
     * 
     * Handle IllegalArgumentException and IOException prints them and continue operating.
     * @param args
     */
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServerFactory.create(BASE_URI);
            // The root resources in the java path will be automatically detected
            server.start();
            System.out.println("Browse the available operations with this URL : "+BASE_URI+"application.wadl");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
