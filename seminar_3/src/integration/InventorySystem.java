package integration;

import java.util.HashMap;
import java.util.Map;

import model.SaleDTO;

public class InventorySystem {
    private Map<String, ItemDTO> inventory;

    public InventorySystem() {
        /*
         * Mocked inventory.
         */
        inventory = new HashMap<>();
        inventory.put("abc123", new ItemDTO("abc123", "BigWheel Oatmeal", 29.9 / 1.06, 0.06,
                "BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free"));
        inventory.put("def456", new ItemDTO("def456", "YouGoGo Blueberry", 14.9 / 1.06, 0.06,
                "YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour"));
    }

    public ItemDTO retrieveItem(String itemId) {
        return inventory.get(itemId);
    }

    public void updateInventory(SaleDTO saleDTO) {}
}
