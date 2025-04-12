package model;

import integration.DiscountDTO;
import integration.ItemDTO;

public class Sale {
    public Sale() {}

    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
        return new SaleInfoDTO(); // Placeholder
    }

    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem, int quantity) {
        return new SaleInfoDTO(); // Placeholder
    }

    public float getTotalPrice() {
        return 0.0f; // Placeholder
    }

    public ItemDTO[] getBoughtItems() {
        return new ItemDTO[0]; // Placeholder
    }

    public float setDiscountedPrice(DiscountDTO[] discountInfo) {
        return 0.0f; // Placeholder
    }

    public void setAmountPaid(float amount) {}

    public SaleDTO getSaleInfo(float amount) {
        return new SaleDTO(); // Placeholder
    }

    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return new ReceiptDTO(); // Placeholder
    }
}
