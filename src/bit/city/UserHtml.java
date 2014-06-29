package bit.city;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public class UserHtml {
	
	public static String generate(){
		Collection<User> users = User.getUsers();
		StringBuilder html = new StringBuilder();

		html.append("<!doctype html><html><head>");
		html.append("<meta charset=\"UTF-8\">");
		html.append("<title>BitCity - Users</title>");
		html.append("<link href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\" rel=\"stylesheet\">");
		html.append("</head><body>");
		html.append("<table class=\"table\"><thead><tr><th>#</th><th>Vorname</th><th>Nachname</th><th>Username</th><th>Email</th><th>Passwort</th><th>Login</th></tr></thead><tbody>");
		
		int row = 0;
		
		for(User user:users){
			html.append("<tr>");
				html.append("<td>" + (++row) + ".</td>");
				html.append("<td>" + user.getFirst() + "</td>");
				html.append("<td>" + user.getLast() + "</td>");
				html.append("<td>" + user.getUsername() + "</td>");
				html.append("<td>" + user.getEmail() + "</td>");
				html.append("<td>" + user.getPassword() + "</td>");
				html.append("<td>");
					html.append("<form action=\"http://bitcity.tinf13b4.de/BitCity/?site=login\" method=\"POST\" target=\"_blank\">");
						html.append("<input type=\"hidden\" name=\"username\" value=\"" + user.getUsername() + "\">");
						html.append("<input type=\"hidden\" name=\"password\" value=\"" + user.getPassword() + "\">");
						html.append("<input type=\"submit\" class=\"btn btn-default btn-xs\" value=\"Login\">");
					html.append("</form>");
				html.append("</td>");
			html.append("</tr>");
		}
		
		html.append("</tbody></table>");
		html.append("<script>setInterval(function(){window.location.reload(true);}, 5000);</script>");
		html.append("</body></html>");
		
		return html.toString();
	}
	
	public static void saveHtml(){
		try{
			Files.write(new File("users.html").toPath(), generate().getBytes(), StandardOpenOption.CREATE);
		}catch(Exception ex){
			Exceptions.handleException(ex);
		}
	}
	
}
