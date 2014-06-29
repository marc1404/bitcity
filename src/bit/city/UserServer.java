package bit.city;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class UserServer {

	public UserServer(){
		try{
			HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
			
			server.createContext("/", new UserHandler());
			server.start();
		}catch(Exception ex){
			Exceptions.handleException(ex);
		}
	}
	
}
