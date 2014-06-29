package bit.city;

import java.util.Collection;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {

	private static final Random RND = new Random();
	private static final String[] NAMES = new String[]{"Alpha","Bravo","Charlie","Delta","Echo","Foxtrot","Golf","Hotel","India","Julia","Kilo","Lima","Mike","November","Oscar","Papa","Quebec","Romeo","Sierra","Tango","Uniform","Victor","Whiskey","X-Ray","Yankee","Zulu"};
	private static final String[] TLD = new String[]{"com","de","net","uk","org","info","nl","eu","cn","biz","ch","at","li"};
	private static Collection<User> users = new ConcurrentLinkedQueue<User>();
	private static int userCount = 0;
	
	public static Collection<User> getUsers(){
		return users;
	}
	
	private String first;
	private String last;
	private String username;
	private String email;
	private String password;
	
	public User(){
		this.first = "";
		this.last = "";
		
		while(first.equals(last)){
			this.first = generateRandomName();
			this.last = generateRandomName();
		}
		
		int number = RND.nextInt(1000000000);
		this.username = first + " " + last + " " + number;
		this.email = first + "." + last + "@" + number + "." + TLD[RND.nextInt(TLD.length)];
		this.password = first.toLowerCase() + last.toLowerCase() + number;
		
		users.add(this);
	}
	
	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
	
	public Vector<String> toVector(){
		Vector<String> vector = new Vector<String>();
		
		vector.add(++userCount + ".");
		vector.add(first);
		vector.add(last);
		vector.add(username);
		vector.add(email);
		vector.add(password);
		
		return vector;
	}
	
	private String generateRandomName(){
		return NAMES[RND.nextInt(NAMES.length)];
	}
	
}
