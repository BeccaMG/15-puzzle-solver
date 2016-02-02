package Client;
import java.net.URI;
import Server.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class SkiersRestClient {
	static final String REST_URI = "http://localhost:9999/MyServer/";

	public static void main(String[] args) {
		Client client = Client.create(new DefaultClientConfig());
		URI uri=UriBuilder.fromUri(REST_URI).build();
		WebResource service = client.resource(uri);
		System.out.println("init skiers : \n"+
				service.path("skiers/init").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");
		
		System.out.println("all skiers in plain text : \n"+
				service.path("skiers/alltxt").type(MediaType.TEXT_PLAIN).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("all skiers in XML      : \n"+
				service.path("skiers/all").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("all skiers in json      : \n"+
				service.path("skiers/alljson").type(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("---------------------------------------------------");
		
		System.out.println("skiers of 41 years old in JSON       : \n"+
				service.path("skiers/agejson").queryParam("age","41").type(MediaType.APPLICATION_JSON).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("skiers of 41 years old in XML       : \n"+
				service.path("skiers").queryParam("age","41").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("skiers of name Killy       : \n"+
				service.path("skiers/search").queryParam("name","Killy").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("skiers of name Killo       : \n"+
				service.path("skiers/search").queryParam("name","Killo").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");
		
		System.out.println("skiers of 41 years old removed       : \n");
		service.path("skiers/delete/41").delete();
		System.out.println("all skiers in plain text : \n"+
				service.path("skiers/alltxt").type(MediaType.TEXT_PLAIN).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("add victory Chamonix to Killy       : \n");
		service.path("skiers/addvictory/Killy").put("Chamonix");
		System.out.println("all skiers in XML : \n"+
				service.path("skiers/all").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");

		System.out.println("add skier   Marion Rolland     : \n");
		service.path("skiers/addskier").type(MediaType.APPLICATION_XML).post(
				"<skier>"
				+     "<name>Marion Rolland</name>"
				+     "<age>31</age>"
				+     "<gender>Female</gender>"
				+     "<nationalTeam>France</nationalTeam>"
				+     " <achievements>"
				+     "   <achievement>1 World Championships</achievement>"
				+     "</achievements>"
				+ " </skier>");
		System.out.println("all skiers in XML : \n"+
				service.path("skiers/all").type(MediaType.APPLICATION_XML).get(String.class));
		System.out.println("---------------------------------------------------");

		
		System.out.println("---------------------------------------------------");

	}
} 