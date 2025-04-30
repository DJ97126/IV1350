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
        // prices[0] will be itemFullPrice, prices[1] will be item's vatRate
        BigDecimal[] prices = calculatePrices(boughtItem);
        BigDecimal itemFullPrice = prices[0];
        BigDecimal itemVatRate = prices[1]; // Use the item's actual VAT rate

        // Since item from inventory is base price, but we need to show the full price to the view.
        // Use the correctly calculated itemFullPrice and the item's original vatRate
        ItemDTO itemWithVat = new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, itemVatRate,
                boughtItem.description());

        return new SaleInfoDTO(itemWithVat, getTotalPrice(), this.totalVat);
    }

    /**
     * Computes the full price of an item by applying VAT to its base price.
     *
     * @param item The bought item.
     * @return An array containing the item's full price at index 0. and the
     * item's VAT rate at index 1.
     */
    private BigDecimal[] calculatePrices(ItemDTO item) {
        BigDecimal itemFullPrice = priceWithVat(item);
        includeItemInTotals(item, itemFullPrice);
        return new BigDecimal[]{itemFullPrice, item.vat()};
    }

    /**
     * Calculates the full price (base price + VAT amount) for a single item.
     *
     * @param item The bought item.
     * @return The calculated full price as a {@link BigDecimal}.
     */
    private BigDecimal priceWithVat(ItemDTO item) {
        BigDecimal basePrice = item.price();
        BigDecimal vat = item.vat();
        return basePrice.add(basePrice.multiply(vat));
    }

    /**
     * Updates the running totals of the sale by adding the item's full price
     * and VAT amount.
     *
     * @param item The bought item.
     * @param fullPrice The calculated full price of the added item.
     */
    private void includeItemInTotals(ItemDTO item, BigDecimal fullPrice) {
        BigDecimal vatAmount = fullPrice.subtract(item.price());
        this.totalVat = totalVat.add(vatAmount);
        this.totalPrice = totalPrice.add(fullPrice);
    }

    /**
     * Retrieves the current total price for the sale, including VAT.
     *
     * @return The total price as a {@link BigDecimal}.
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retrieves all items that have been added to the sale so far.
     *
     * @return An array of {@link ItemDTO} objects representing the bought
     * items. Returns an empty array if no items have been added.
     */
    public ItemDTO[] getBoughtItems() {
        return boughtItems.toArray(ItemDTO[]::new);
    }

    /**
     * Processes the amount paid by the customer. Validates the amount and then
     * creates a new Payment object to hold this amount. Note: This
     * implementation does not store the amount directly within the Sale object
     * itself.
     *
     * @param amount The amount paid as a {@link BigDecimal}. Should not be
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
        // Create and return a new SaleDTO
        BigDecimal change = getChange(amount, this.totalPrice);
        return new SaleDTO(this.saleDateTime, getBoughtItems(), getTotalPrice(), this.totalVat, amount, change);
    }

    /**
     * Calculates the change to be returned to the customer based on the amount
     * paid and the total price.
     *
     * @param amount The amount paid by the customer.
     * @param totalPrice The total price of the sale.
     * @return The change to be returned to the customer, or zero if the amount
     * paid is less than the total price.
     */
    private BigDecimal getChange(BigDecimal amount, BigDecimal totalPrice) {
        // Calculate change
        BigDecimal change = amount.subtract(totalPrice);
        if (change.compareTo(BigDecimal.ZERO) < 0) {
            change = BigDecimal.ZERO; // Ensure change is not negative
        }
        return change;
    }

    /**
     * Creates a {@link ReceiptDTO} based on the current state of the sale.
     * Assumes the sale is finalized and amount paid has been set.
     *
     * @param saleDTO containing the finalized sale details
     * @return A new receipt.
     */
    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return new ReceiptDTO(saleDTO);
    }
}
