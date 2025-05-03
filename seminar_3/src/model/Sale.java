package model;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sale transaction, holding information about items purchased and
 * totals.
 */
public class Sale {

    private final LocalDateTime saleDateTime;
    private final List<ItemDTO> boughtItems;
    private BigDecimal totalPrice;
    private BigDecimal totalVat;

    /**
     * Creates a new, empty Sale instance. Initializes totals to zero.
     */
    public Sale() {
        saleDateTime = LocalDateTime.now();
        boughtItems = new ArrayList<>();
        totalPrice = BigDecimal.valueOf(0);
        totalVat = BigDecimal.valueOf(0);
    }

    /**
     * Adds a specified item to the current sale and updates the running total
     * price and VAT.
     *
     * @param boughtItem the item to be added.
     * @return A details of the added item (with full price) and the updated
     * running total price and VAT for the sale.
     */
    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {

        boughtItems.add(boughtItem);
        BigDecimal[] prices = calculatePrices(boughtItem);
        BigDecimal itemFullPrice = prices[0];
        BigDecimal itemVatRate = prices[1];

        ItemDTO itemWithVat = new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, itemVatRate,
                boughtItem.description());

        return new SaleInfoDTO(itemWithVat, getTotalPrice(), this.totalVat);
    }

    private BigDecimal[] calculatePrices(ItemDTO item) {
        BigDecimal itemFullPrice = priceWithVat(item);
        includeItemInTotals(item, itemFullPrice);
        return new BigDecimal[]{itemFullPrice, item.vat()};
    }

    private BigDecimal priceWithVat(ItemDTO item) {
        BigDecimal basePrice = item.price();
        BigDecimal vat = item.vat();
        return basePrice.add(basePrice.multiply(vat));
    }

    private void includeItemInTotals(ItemDTO item, BigDecimal fullPrice) {
        BigDecimal vatAmount = fullPrice.subtract(item.price());
        this.totalVat = totalVat.add(vatAmount);
        this.totalPrice = totalPrice.add(fullPrice);
    }

    /**
     * Retrieves the current total price for the sale, including VAT.
     *
     * @return the total price
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retrieves all items that have been added to the sale.
     *
     * @return the bought items. Returns an empty array if no items have been
     * added.
     */
    public ItemDTO[] getBoughtItems() {
        return boughtItems.toArray(ItemDTO[]::new);
    }

    /**
     * Creates a payment with the specified amount.
     *
     * @param amount The paid amount
     * @return A new payment
     */
    public Payment setAmountPaid(BigDecimal amount) {
        return new Payment(amount);
    }

    /**
     * Sale information, including calculated change based on the amount paid.
     *
     * @param amount The amount paid by the customer
     * @return A saleDTO containing sale information.
     */
    public SaleDTO getSaleInfo(BigDecimal amount) {
        BigDecimal change = getChange(amount, this.totalPrice);
        return new SaleDTO(this.saleDateTime, getBoughtItems(), getTotalPrice(), this.totalVat, amount, change);
    }

    /**
     *  Calculates the change to return, or zero if underpaid.
     *
     * @param amount The amount paid by the customer.
     * @param totalPrice The total price of the sale.
     * @return The change.
     */
    private BigDecimal getChange(BigDecimal amount, BigDecimal totalPrice) {
        BigDecimal change = amount.subtract(totalPrice);
        if (change.compareTo(BigDecimal.ZERO) < 0) {
            change = BigDecimal.ZERO;
        }
        return change;
    }

    /**
     * Returns a receipt for a finalized sale.
     *
     * @param saleDTO The finalized sale details.
     * @return A new receipt.
     */
    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return new ReceiptDTO(saleDTO);
    }
}
