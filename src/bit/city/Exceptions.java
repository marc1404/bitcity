package bit.city;

import javax.swing.JOptionPane;

public class Exceptions {

	public static void handleException(Exception ex){
		StringBuilder message = new StringBuilder();
		String ls = System.lineSeparator();
		
		message.append(ex.toString()).append(ls);
		appendStackTrace(message, ex.getStackTrace());
		
		Throwable cause = ex.getCause();
		
		if(cause != null){
			message.append("caused by " + cause.toString()).append(ls);
			appendStackTrace(message, cause.getStackTrace());
		}
		
		ex.printStackTrace();
		JOptionPane.showMessageDialog(null, message.toString(), ex.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	private static void appendStackTrace(StringBuilder message, StackTraceElement[] stackTrace){
		String ls = System.lineSeparator();
		
		for(StackTraceElement element:stackTrace){
			message.append("   at ").append(element.toString()).append(ls);
		}
	}
	
}
