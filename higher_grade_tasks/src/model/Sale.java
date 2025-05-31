package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import model.discount.DiscountStrategy;
import observer.TotalRevenueObserver;

/**
 * Represents a sale transaction, holding information about items purchased and totals.
 */
public class Sale {
	private final LocalDateTime saleDateTime;
	private final ArrayList<ItemDTO> boughtItems;
	private Amount totalPrice;
	private Amount totalVat;
	private Amount totalDiscounted;
	private Payment payment;

	private ArrayList<TotalRevenueObserver> observers = new ArrayList<>();

	/**
	 * Creates a new, empty Sale instance. Initializes totals to zero.
	 */
	public Sale() {
		saleDateTime = LocalDateTime.now();
		boughtItems = new ArrayList<>();
		totalPrice = new Amount();
		totalVat = new Amount();
		totalDiscounted = new Amount();
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
	 * Applies the given discounts to the sale and returns the discounted price.
	 *
	 * @param discounts The list of discount strategies to apply.
	 * @return The discounted total price.
	 */
	public Amount setDiscountedPrice(ArrayList<DiscountStrategy> discounts) {
		Amount discountTotal = new Amount();

		for (DiscountStrategy discount : discounts) {
			discountTotal = discountTotal.add(discount.calculateDiscount(totalPrice));
		}

		this.totalDiscounted = discountTotal;
		totalPrice = totalPrice.subtract(discountTotal);
		return totalPrice;
	}

	/**
	 * Retrieves all items that have been added to the sale.
	 *
	 * @return The bought items. Returns an empty list if no items have been added.
	 */
	public ArrayList<ItemDTO> getBoughtItems() {
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
	 */
	public SaleDTO getSaleInfo(Amount amount) {
		Amount change = getChange(amount);
		notifyObservers();

		return new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amount, change, totalDiscounted);
	}

	private Amount getChange(Amount amount) {
		return amount.subtract(totalPrice);
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
