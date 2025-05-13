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

	private Payment payment;

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
	 * @throws IllegalArgumentException If the provided item is null.
	 */
	public SaleInfoDTO addBoughtItem(ItemDTO boughtItem) {
		if (boughtItem == null) {
			throw new IllegalArgumentException("Item cannot be null");
		}
		boughtItems.add(boughtItem);
		ItemDTO itemWithVat = calculateRunningTotal(boughtItem);
		return new SaleInfoDTO(itemWithVat, totalPrice, totalVat);
	}

	private ItemDTO calculateRunningTotal(ItemDTO boughtItem) {
		BigDecimal itemBasePrice = boughtItem.price();
		BigDecimal vatRate = boughtItem.vat();
		BigDecimal vatPrice = itemBasePrice.multiply(vatRate);
		BigDecimal itemFullPrice = itemBasePrice.multiply(vatRate.add(BigDecimal.ONE));

		totalVat = totalVat.add(vatPrice);
		totalPrice = totalPrice.add(itemFullPrice);

		return new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, vatRate, boughtItem.description());
	}

	/**
	 * Retrieves the current total price for the sale, including VAT.
	 *
	 * @return The total price.
	 */
	public BigDecimal getTotalPrice() {
		this.payment = new Payment();
		return this.totalPrice;
	}

	/**
	 * Retrieves all items that have been added to the sale.
	 *
	 * @return The bought items. Returns an empty list if no items have been added.
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
		payment.setAmount(amount);
	}

	/**
	 * Sale information, including calculated change based on the amount paid.
	 *
	 * @param amount The amount paid by the customer.
	 * @return Sale information.
	 * @throws IllegalArgumentException If the amount paid is less than the total price.
	 */
	public SaleDTO getSaleInfo(BigDecimal amount) {
		if (amount.compareTo(totalPrice) < 0) {
			throw new IllegalArgumentException("Paid amount is less than total price");
		}
		BigDecimal change = getChange(amount);
		return new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amount, change);
	}

	private BigDecimal getChange(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Payment amount must be non-null and non-negative");
		}
		BigDecimal change = amount.subtract(totalPrice);
		if (change.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Change amount cannot be negative");
		}
		return change;
	}

	/**
	 * Returns a receipt for a finalized sale.
	 *
	 * @param saleDTO The finalized sale details.
	 * @return A new receipt.
	 * @throws IllegalArgumentException If the saleDTO is null.
	 */
	public ReceiptDTO getReceiptInfo(SaleDTO saleDTO) {
		if (saleDTO == null) {
			throw new IllegalArgumentException("Sale information cannot be null");
		}
		return new ReceiptDTO(saleDTO);
	}

	/**
	 * Applies a discount to the sale's total price.
	 *
	 * @param discountInfo The discount information to be applied.
	 * @return The discounted price
	 * @throws IllegalArgumentException If discountInfo is null or if discount amount is negative or if discount amount
	 *                                  is greater then total price.
	 */
	public BigDecimal setDiscountedPrice(DiscountDTO discountInfo) {
		if (discountInfo == null) {
			throw new IllegalArgumentException("Discount information cannot be null");
		}
		BigDecimal discountAmount = discountInfo.amount();
		if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Discount amount must be non-null and non-negative");
		}
		if (discountAmount.compareTo(totalPrice) > 0) {
			throw new IllegalArgumentException("Discount amount cannot be greater than total price");
		}

		totalPrice = totalPrice.subtract(discountAmount);
		return discountAmount;
	}
}
