import java.io.IOException;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
 
public class Publisher {
 
    static final String BASE_URI = "http://localhost/";
 
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
