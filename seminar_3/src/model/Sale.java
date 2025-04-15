package model;

import integration.ItemDTO;

public class Sale {
    public Sale() {}

    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
        return null; // Placeholder
    }

    public float getTotalPrice() {
        return 0.0f; // Placeholder
    }

    public ItemDTO[] getBoughtItems() {
        return null; // Placeholder
    }

    public void setAmountPaid(float amount) {}

    public SaleDTO getSaleInfo(float amount) {
        return null; // Placeholder
    }

    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return null; // Placeholder
    }
}
