package integration;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import dto.ItemDTO;
import dto.SaleDTO;

public class InventorySystem {
    private final Map<String, InventoryItem> inventory;

    /**
     * Constructor for the InventorySystem class.
     */
    public InventorySystem() {
        inventory = new HashMap<String, InventoryItem>();
        simulateInventory();
    }

    /**
     * Retrieves the item from the inventory based on the item ID.
     * 
     * @param itemId The ID of the item to be retrieved.
     * @return The object containing information about this item.
     */
    public ItemDTO retrieveItem(String itemId) {
        return inventory.get(itemId).item;
    }

    /**
     * Updates the inventory based on the sale information. Reduces the quantity of each item sold.
     * 
     * @param saleDTO The sale information.
     */
    public void updateInventory(SaleDTO saleDTO) {
        for (ItemDTO soldItem : saleDTO.boughtItems()) {
            InventoryItem item = inventory.get(soldItem.id());
            item.reduceQuantity(1);
        }
    }

    /* Below are only simulation code, which will not be actually documented. */
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
        // Reason to calculate original price is because the sample in the task seem not to have given this.
        BigDecimal item1OriginalPrice = calculateOriginalPrice(new BigDecimal("29.9"), new BigDecimal("0.06"));
        BigDecimal item2OriginalPrice = calculateOriginalPrice(new BigDecimal("14.9"), new BigDecimal("0.06"));

        ItemDTO item1 = new ItemDTO("abc123", "BigWheel Oatmeal", item1OriginalPrice, BigDecimal.valueOf(0.06d),
                "BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free");
        ItemDTO item2 = new ItemDTO("def456", "YouGoGo Blueberry", item2OriginalPrice, BigDecimal.valueOf(0.06d),
                "YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour");

        inventory.put(item1.id(), new InventoryItem(item1, 2));
        inventory.put(item2.id(), new InventoryItem(item2, 2));
    }

    private BigDecimal calculateOriginalPrice(BigDecimal fullPrice, BigDecimal vatRate) {
        return fullPrice.divide(vatRate.add(BigDecimal.ONE), MathContext.DECIMAL128);
    }
}
