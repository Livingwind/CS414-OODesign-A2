package a2;

/**
 * Exception class to encapsulate exceptions that come from the a2.Chatbot itself.<br />
 * This is a checked exception.  It will be thrown when the a2.Chatbot AI goes into a bad state and must be restarted, or when the input is malformed.<br />
 * This exception requires a message.
 * 
 * @author Dave Besen
 */
public class AIException extends Exception {
	public AIException(String message) {
		super(message);
	}
}