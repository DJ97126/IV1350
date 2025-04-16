package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import integration.ItemDTO;

public class Sale {
    private final List<ItemDTO> boughtItems;
    private BigDecimal totalPrice;
    private BigDecimal totalVat;

    public Sale() {
        boughtItems = new ArrayList<>();
        totalPrice = BigDecimal.valueOf(0);
        totalVat = BigDecimal.valueOf(0);
    }

    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
        boughtItems.add(boughtItem);

        // Feel free to implement another helper method to shorten this method for the following 4 lines.
        BigDecimal itemBasePrice = boughtItem.price();
        BigDecimal vatRate = boughtItem.vat();
        BigDecimal vatPrice = itemBasePrice.multiply(vatRate);
        BigDecimal itemFullPrice = itemBasePrice.multiply(vatRate.add(BigDecimal.ONE));

        totalVat = totalVat.add(vatPrice);
        totalPrice = totalPrice.add(itemFullPrice);

        // Since item from inventory is base price, but we need to show the full price to the view.
        ItemDTO itemWithVat = new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, vatRate,
                boughtItem.description());

        return new SaleInfoDTO(itemWithVat, totalPrice, totalVat);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public ItemDTO[] getBoughtItems() {
        return null; // Placeholder
    }

    public void setAmountPaid(BigDecimal amount) {}

    public SaleDTO getSaleInfo(BigDecimal amount) {
        return null; // Placeholder
    }

    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return null; // Placeholder
    }
}
