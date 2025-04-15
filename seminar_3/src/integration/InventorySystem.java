package integration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import model.SaleDTO;

public class InventorySystem {
    private Map<String, ItemDTO> inventory;

    public InventorySystem() {
        inventory = new HashMap<>();
        simulateInventory();
    }

    private void simulateInventory() {
        // Reason to calculate original price is because the sample in the task seem not to have given this.
        BigDecimal item1OriginalPrice = calculateOriginalPrice(29.9, 0.06);
        BigDecimal item2OriginalPrice = calculateOriginalPrice(14.9, 0.06);

        ItemDTO item1 = new ItemDTO("abc123", "BigWheel Oatmeal", item1OriginalPrice, BigDecimal.valueOf(0.06d),
                "BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free");
        ItemDTO item2 = new ItemDTO("def456", "YouGoGo Blueberry", item2OriginalPrice, BigDecimal.valueOf(0.06d),
                "YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour");

        inventory.put(item1.id(), item1);
        inventory.put(item2.id(), item2);
    }

    private BigDecimal calculateOriginalPrice(double fullPrice, double vatRate) {
        return BigDecimal.valueOf(fullPrice / (1 + vatRate));
    }

    public ItemDTO retrieveItem(String itemId) {
        return inventory.get(itemId);
    }

    public void updateInventory(SaleDTO saleDTO) {}
}
