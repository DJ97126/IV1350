package integration;

/**
 * Thrown when no item with the specified identifier is found in the inventory.
 */
public class ItemNotFoundException extends Exception {
	/**
	 * Creates a new instance with a message about the missing item.
	 * 
	 * @param itemId The identifier that was not found.
	 */
	ItemNotFoundException(String itemId) {
		super("No item with identifier '%s' was found in inventory".formatted(itemId));
	}
}