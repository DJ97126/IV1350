package model;

import java.util.ArrayList;
import java.util.List;

import integration.ItemDTO;

public class Sale {
    private List<ItemDTO> boughtItems;
    private double totalPrice;
    private double totalVat;

    public Sale() {
        boughtItems = new ArrayList<>();
        totalPrice = 0.0;
        totalVat = 0.0;
    }

    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
        boughtItems.add(boughtItem);

        // Feel free to implement another helper method to shorten this method for the following 4 lines.
        double itemBasePrice = boughtItem.price();
        double vatRate = boughtItem.vat();
        double vatPrice = Math.round((itemBasePrice * vatRate) * 100.0) / 100.0;
        double itemFullPrice = Math.round((itemBasePrice * (1 + vatRate)) * 100.0) / 100.0;

        totalVat += vatPrice;
        totalPrice += itemFullPrice;

        // Since item from inventory is base price, but we need to show the full price to the view.
        ItemDTO itemWithVat = new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, vatRate,
                boughtItem.description());

        return new SaleInfoDTO(itemWithVat, totalPrice, totalVat);
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public ItemDTO[] getBoughtItems() {
        return null; // Placeholder
    }

    public void setAmountPaid(double amount) {}

    public SaleDTO getSaleInfo(double amount) {
        return null; // Placeholder
    }

    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return null; // Placeholder
    }
}
