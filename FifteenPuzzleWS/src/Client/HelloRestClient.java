package Client;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class HelloRestClient {
    static final String REST_URI = "http://localhost:9999/MyServer/";
    
  public static void main(String[] args) {
    Client client = Client.create(new DefaultClientConfig());
    URI uri=UriBuilder.fromUri(REST_URI).build();
    WebResource service = client.resource(uri);
    // Get plain text
    System.out.println(uri+"hello GET TEXT_PLAIN");
    System.out.println("hello output as plain text : "+service.path("hello").accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("---------------------------------------------------");
    // Get HTML
    System.out.println(uri+"hello GET TEXT_HTML");
    System.out.println("hello output as html       : "+service.path("hello").accept(MediaType.TEXT_HTML).get(String.class));
    System.out.println("---------------------------------------------------");
    
    System.out.println(uri+"hello/USERNAME GET TEXT_PLAIN");
    System.out.println("hello/chantal output       : "+service.path("hello/"+System.getenv("USERNAME")).accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("---------------------------------------------------");
   
    System.out.println(uri+"hello/replace?newmsg=bonjour GET TEXT_PLAIN");
    System.out.println("hello/replace output       : "+service.path("hello").path("replace").queryParam("newmsg", "bonjour").accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("---------------------------------------------------");

    System.out.println(uri+"hello GET TEXT_PLAIN");
    System.out.println("hello output as plain text : "+service.path("hello").accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("---------------------------------------------------");

    System.out.println(uri+"hello/replace PUT coucou TEXT_PLAIN");
    service.path("hello").path("replace").put("coucou");
    System.out.println("---------------------------------------------------");
    
    System.out.println(uri+"hello GET TEXT_PLAIN");
    System.out.println("hello output as plain text : "+service.path("hello").accept(MediaType.TEXT_PLAIN).get(String.class));
    System.out.println("---------------------------------------------------");
   
    System.out.println(uri+"hello/delete DELETE TEXT_PLAIN");
    service.path("hello").path("delete").delete();
    System.out.println("---------------------------------------------------");
   
    System.out.println(uri+"hello GET TEXT_PLAIN");
    System.out.println("hello output as plain text : "+service.path("hello").accept(MediaType.TEXT_PLAIN).get(String.class));
  }
} 
