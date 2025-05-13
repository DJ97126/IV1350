package integration;

/**
 * Thrown when the inventory database cannot be reached or is unavailable.
 */
public class InventorySystemException extends RuntimeException {

	/**
	 * Creates a new instance representing the condition described in the specified message.
	 * 
	 * @param message A message that describes what went wrong.
	 */
	InventorySystemException(String message) {
		super(message);
	}
}