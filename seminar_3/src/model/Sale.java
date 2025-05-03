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
	 * Adds a bought item to the sale and calculates the running total.
	 *
	 * @param boughtItem The item to be added to the sale.
	 * @return The current item information and running total.
	 */
	public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
		boughtItems.add(boughtItem);
		ItemDTO itemWithVat = calculateRunningTotal(boughtItem);

		return new SaleInfoDTO(itemWithVat, totalPrice, totalVat);
	}

	private ItemDTO calculateRunningTotal(ItemDTO boughtItem) {
		BigDecimal itemBasePrice = boughtItem.price();
		BigDecimal vatRate = boughtItem.vat();
		BigDecimal vatPrice = itemBasePrice.multiply(vatRate);
		// Since item from inventory is base price, but we need to show the full price to the view.
		BigDecimal itemFullPrice = itemBasePrice.multiply(vatRate.add(BigDecimal.ONE));

		totalVat = totalVat.add(vatPrice);
		totalPrice = totalPrice.add(itemFullPrice);

		return new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, vatRate, boughtItem.description());
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
	 * @return The bought items. Returns an empty array if no items have been added.
	 */
	public List<ItemDTO> getBoughtItems() {
		return this.boughtItems;
	}

	/**
	 * Creates a payment with the specified amount.
	 *
	 * @param amount The paid amount.
	 */
	public void setAmountPaid(BigDecimal amount) {
		Payment payment = new Payment();
		payment.setAmount(amount);
	}

	/**
	 * Sale information, including calculated change based on the amount paid.
	 *
	 * @param amount The amount paid by the customer
	 * @return Aale information.
	 */
	public SaleDTO getSaleInfo(BigDecimal amount) {
		BigDecimal change = getChange(amount);
		return new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amount, change);
	}

	/**
	 * Calculates the change.
	 *
	 * @param amount The amount paid by the customer.
	 * @return The change.
	 */
	private BigDecimal getChange(BigDecimal amount) {
		BigDecimal change = amount.subtract(totalPrice);
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
