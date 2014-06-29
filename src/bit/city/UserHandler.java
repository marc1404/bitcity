package bit.city;

import java.io.BufferedOutputStream;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class UserHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange http) throws IOException {
		String html = UserHtml.generate();
		
		http.sendResponseHeaders(200, html.length());
		
		BufferedOutputStream out = new BufferedOutputStream(http.getResponseBody());
		
		out.write(html.getBytes());
		out.flush();
		out.close();
	}

}
