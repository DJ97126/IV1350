package integration;

/**
 * Thrown when the inventory database cannot be reached or is unavailable.
 * This simulates a database failure, such as the server not running.
 */
public class DatabaseFailureException extends RuntimeException {
	
    /**
     * Creates a new instance with a detailed message.
     * @param message Information about the error condition.
     */
    public DatabaseFailureException(String message) {
        super(message);
    }
}