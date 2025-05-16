package integration;

import java.util.HashMap;

import dto.ItemDTO;
import dto.SaleDTO;
import model.Amount;

/**
 * This class simulates an inventory system that stores the items available for sale. It provides methods to retrieve
 * item information and update the inventory based on sales.
 */
public class InventorySystem {
	private final HashMap<String, InventoryItem> inventory;

	/**
	 * Constructor for the InventorySystem class.
	 */
	public InventorySystem() {
		inventory = new HashMap<>();
		simulateInventory();
	}

	/**
	 * Retrieves the item from the inventory based on the item ID.
	 * 
	 * @param itemId The ID of the item to be retrieved.
	 * @return The object containing information about this item. Null if not found.
	 * @throws DatabaseFailureException if the database server is not running.
	 * @throws ItemNotFoundException    if the item is not found in the inventory.
	 */
	public ItemDTO retrieveItem(String itemId) throws DatabaseFailureException, ItemNotFoundException {
		if ("fail114514".equals(itemId)) {
			throw new DatabaseFailureException("Database server is not running");
		}

		InventoryItem item = inventory.get(itemId);

		if (item == null) {
			throw new ItemNotFoundException(itemId);
		}

		return item.item;
	}

	/**
	 * Updates the inventory based on the sale information. Reduces the quantity of each item sold. This class is only
	 * for simulation purposes and does not represent the actual external inventory.
	 * 
	 * @param saleDTO The sale information.
	 */
	public void updateInventory(SaleDTO saleDTO) {
		for (ItemDTO soldItem : saleDTO.boughtItems()) {
			InventoryItem item = inventory.get(soldItem.id());
			item.reduceQuantity(1);
		}
	}

	/* Below are only simulation code. */
	private static class InventoryItem {
		private final ItemDTO item;
		private int quantity;

		public InventoryItem(ItemDTO item, int quantity) {
			this.item = item;
			this.quantity = quantity;
		}

		public void reduceQuantity(int amount) {
			quantity = Math.max(0, quantity - amount);
		}
	}

	private void simulateInventory() {
		Amount vatAmount = new Amount("0.06");
		Amount item1OriginalPrice = calculateOriginalPrice(new Amount("29.9"), vatAmount);
		Amount item2OriginalPrice = calculateOriginalPrice(new Amount("14.9"), vatAmount);

		ItemDTO item1 = new ItemDTO(
				"abc123",
				"BigWheel Oatmeal",
				item1OriginalPrice,
				vatAmount,
				"BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free");
		ItemDTO item2 = new ItemDTO(
				"def456",
				"YouGoGo Blueberry",
				item2OriginalPrice,
				vatAmount,
				"YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour");

		inventory.put(item1.id(), new InventoryItem(item1, 2));
		inventory.put(item2.id(), new InventoryItem(item2, 2));
	}

	private Amount calculateOriginalPrice(Amount fullPrice, Amount vatRate) {
		Amount divisor = vatRate.add(new Amount("1"));
		return fullPrice.divide(divisor);
	}
}
