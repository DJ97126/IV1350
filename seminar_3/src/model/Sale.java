package model;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Represents a sale transaction, holding information about items purchased and totals.
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
     * Adds a specified item to the current sale and updates the running total price and VAT.
     * This method delegates to addMultipleBoughtItem with a quantity of 1.
     *
     * @param boughtItem The {@link ItemDTO} representing the item to be added.
     * @return A {@link SaleInfoDTO} containing details of the added item (with full price)
     *         and the updated running total price and VAT for the sale.
     */
    public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
        // Delegate to the method that handles multiple items with quantity 1
        return addMultipleBoughtItem(boughtItem, 1);
    }

    // --- Helper method to create ItemDTO with full price ---
    /**
     * Creates a new ItemDTO instance with the full price (base price + VAT) calculated.
     * Useful for displaying item price including VAT.
     *
     * @param baseItem The original ItemDTO containing the base price.
     * @return A new ItemDTO instance with the calculated full price.
     */
    private ItemDTO createItemWithFullPrice(ItemDTO baseItem) {
        // Inlined itemBasePrice and vatRate into the calculation and constructor
        BigDecimal itemFullPrice = baseItem.price().multiply(BigDecimal.ONE.add(baseItem.vat()));

        return new ItemDTO(baseItem.id(), baseItem.name(), itemFullPrice, baseItem.vat(),
                baseItem.description());
    }

    // --- Helper method for efficient bulk update ---
    /**
     * Calculates the total price and VAT for a given item and quantity,
     * and updates the sale's running totals efficiently.
     *
     * @param item The item being added.
     * @param quantity The number of units of the item.
     */
    private void updateTotalsForQuantity(ItemDTO item, int quantity) {
        if (quantity <= 0) {
            return; // No update needed if quantity is not positive
        }

        // Inlined itemBasePrice and vatRate
        BigDecimal singleItemVatAmount = item.price().multiply(item.vat());
        BigDecimal singleItemFullPrice = item.price().add(singleItemVatAmount); // Or item.price().multiply(BigDecimal.ONE.add(item.vat()));

        // Inlined quantityBD
        BigDecimal totalVatForQuantity = singleItemVatAmount.multiply(BigDecimal.valueOf(quantity));
        BigDecimal totalPriceForQuantity = singleItemFullPrice.multiply(BigDecimal.valueOf(quantity));

        // Update the sale totals
        totalVat = totalVat.add(totalVatForQuantity);
        totalPrice = totalPrice.add(totalPriceForQuantity);
    }


     /**
     * Adds multiple units of the same item to the current sale efficiently.
     * Calculates the total price/VAT impact once for the entire quantity.
     *
     * @param boughtItem The item to add.
     * @param quantity   The number of units to add. Must be positive.
     * @return Information about the added item (with its full price) and the current running total.
     *         Returns the current state without adding if quantity is not positive.
     */
    public SaleInfoDTO addMultipleBoughtItem(ItemDTO boughtItem, int quantity) {
        // Create the DTO for the return value *before* potentially modifying totals
        ItemDTO itemWithFullPrice = createItemWithFullPrice(boughtItem);

        if (quantity <= 0) {
            // Return current state using the pre-calculated item DTO
            return new SaleInfoDTO(itemWithFullPrice, totalPrice, totalVat);
        }

        // Add the item reference 'quantity' times to the list
        boughtItems.addAll(Collections.nCopies(quantity, boughtItem));
        /* // Alternatively, a simple loop:
        for (int i = 0; i < quantity; i++) {
            boughtItems.add(boughtItem);
        }
        */

        // Update totals efficiently using the helper
        updateTotalsForQuantity(boughtItem, quantity);

        // Return SaleInfoDTO with the item's full price and updated totals
        return new SaleInfoDTO(itemWithFullPrice, totalPrice, totalVat);
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
     * @return An array of {@link ItemDTO} objects representing the bought items.
     *         Returns an empty array if no items have been added.
     */
    public ItemDTO[] getBoughtItems() {
        return boughtItems.toArray(ItemDTO[]::new);
    }


    /**
     * Processes the amount paid by the customer.
     * Validates the amount and then creates a new Payment object
     * to hold this amount. Note: This implementation does not store
     * the amount directly within the Sale object itself.
     *
     * @param amount The amount paid as a {@link BigDecimal}. Should not be null.
     * @throws IllegalArgumentException if amount is null.
     */
    public void setAmountPaid(BigDecimal amount) {
            if(amount == null) {
                throw new IllegalArgumentException("Amount paid cannot be null");
            }
        
            Payment payment = new Payment();
            payment.setAmount(amount);
    }

    /**
     * Generates the final sale information, including calculated change based on the amount paid.
     * Assumes {@link #setAmountPaid(BigDecimal)} has been called previously.
     *
     * @param amountPaid The amount paid by the customer (Note: This parameter might be redundant
     *                   if setAmountPaid is always called first, consider removing it).
     * @return A {@link SaleDTO} containing the list of bought items and the calculated change.
     *         Change will be zero if amount paid is less than the total price.
     */
    public SaleDTO getSaleInfo(BigDecimal amount) {
        return null; // Placeholder
    }

    /**
     * Sets the total price of the sale to a specific discounted value.
     * Warning: This directly overwrites the calculated total price. Consider a more robust
     * discount mechanism (e.g., storing discount amount separately) for complex scenarios.
     * It also does not adjust the total VAT based on the discount.
     *
     * @param discountedPrice The final price after discount.
     * @return The new total price (the discounted price). Returns current price if discountedPrice is null.
     */
    public BigDecimal setDiscountedPrice(BigDecimal discountedPrice) {
        return null; // Placeholder
    }

    /**
     * Creates a {@link ReceiptDTO} based on the current state of the sale.
     * Assumes the sale is finalized and amount paid has been set.
     *
     * @param saleDTO The {@link SaleDTO} containing the finalized sale details (items and change).
     *                (Note: change is taken from saleDTO, other info from this Sale object).
     * @return A {@link ReceiptDTO} suitable for printing a receipt.
     */
    public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
        return null; // Placeholder
    }
}
