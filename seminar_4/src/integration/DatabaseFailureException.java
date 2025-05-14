package integration;

/**
 * Thrown when the inventory database cannot be reached or is unavailable.
 */
public class DatabaseFailureException extends RuntimeException {
	/**
	 * Creates a new instance representing the condition described in the specified message.
	 * 
	 * @param message A message that describes what went wrong.
	 */
	DatabaseFailureException(String message) {
		super(message);
	}
}