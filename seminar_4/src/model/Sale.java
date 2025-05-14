package model;

import dto.DiscountDTO;
import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import util.TotalRevenueObserver;

/**
 * Represents a sale transaction, holding information about items purchased and totals.
 */
public class Sale {
	private final LocalDateTime saleDateTime;
	private final List<ItemDTO> boughtItems;
	private Amount totalPrice;
	private Amount totalVat;
	private Payment payment;
	private List<TotalRevenueObserver> observers = new ArrayList<>();

	/**
	 * Creates a new, empty Sale instance. Initializes totals to zero.
	 */
	public Sale() {
		saleDateTime = LocalDateTime.now();
		boughtItems = new ArrayList<>();
		totalPrice = new Amount();
		totalVat = new Amount();
		observers = new ArrayList<>();
	}

	/**
	 * Registers an observer for total revenue updates.
	 *
	 * @param observer The observer to register.
	 */
	public void registerObserver(TotalRevenueObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers() {
		for (TotalRevenueObserver observer : observers) {
			observer.updateTotalRevenue(totalPrice);
		}
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
		Amount itemBasePrice = boughtItem.price();
		Amount vatRate = boughtItem.vat();
		Amount vatPrice = itemBasePrice.multiply(vatRate);
		Amount itemFullPrice = itemBasePrice.multiply(vatRate.add(new Amount("1")));

		totalVat = totalVat.add(vatPrice);
		totalPrice = totalPrice.add(itemFullPrice);

		return new ItemDTO(boughtItem.id(), boughtItem.name(), itemFullPrice, vatRate, boughtItem.description());
	}

	/**
	 * Retrieves the current total price for the sale, including VAT.
	 *
	 * @return The total price.
	 */
	public Amount getTotalPrice() {
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
	public void setAmountPaid(Amount amount) {
		payment.setAmount(amount);
	}

	/**
	 * Sale information, including calculated change based on the amount paid.
	 *
	 * @param amount The amount paid by the customer.
	 * @return Sale information.
	 * @throws IllegalArgumentException if the amount paid is less than the total price, or is null.
	 */
	public SaleDTO getSaleInfo(Amount amount) {
		if (amount == null) {
			throw new IllegalArgumentException("Payment amount must be non-null");
		}
		if (amount.compareTo(totalPrice) < 0) {
			throw new IllegalArgumentException("Paid amount is less than total price");
		}
		Amount change = getChange(amount);
		notifyObservers();
		return new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amount, change);
	}

	private Amount getChange(Amount amount) {
		Amount change = amount.subtract(totalPrice);
		return change;
	}

	/**
	 * Returns a receipt for a finalized sale.
	 *
	 * @param saleDTO The finalized sale details.
	 * @return A new receipt.
	 * @throws IllegalArgumentException if the saleDTO is null.
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
	 * @throws IllegalArgumentException if discountInfo is null or if discount amount is negative or if discount amount
	 *                                  is greater then total price.
	 */
	public Amount setDiscountedPrice(DiscountDTO discountInfo) {
		if (discountInfo == null) {
			throw new IllegalArgumentException("Discount information cannot be null");
		}

		Amount discountAmount = discountInfo.amount();

		if (discountAmount == null || discountAmount.isNegative()) {
			throw new IllegalArgumentException("Discount amount must be non-null and non-negative");
		}
		if (discountAmount.compareTo(totalPrice) > 0) {
			throw new IllegalArgumentException("Discount amount cannot be greater than total price");
		}

		totalPrice = totalPrice.subtract(discountAmount);
		return discountAmount;
	}
}
