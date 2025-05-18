package controller;

import java.util.ArrayList;

import dto.DiscountDTO;
import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import integration.AccountingSystem;
import integration.DatabaseFailureException;
import integration.DiscountCatalog;
import integration.InventorySystem;
import integration.ItemNotFoundException;
import integration.Printer;
import model.Amount;
import model.discount.DiscountFactory;
import model.discount.DiscountStrategy;
import model.Sale;
import observer.TotalRevenueObserver;
import util.LogHandler;

/**
 * This serves as the main controller that the cashier interacts with the model and integration systems.
 */
public class Controller {
	private final AccountingSystem accountingSystem;
	private final InventorySystem inventorySystem;
	private final DiscountFactory discountFactory;
	private final Printer printer;

	private final LogHandler logger = LogHandler.getLogger();
	private final ArrayList<TotalRevenueObserver> observers = new ArrayList<>();

	private Sale sale;

	/**
	 * Constructor for the Controller class.
	 */
	public Controller() {
		accountingSystem = new AccountingSystem();
		inventorySystem = new InventorySystem();
		discountFactory = new DiscountFactory(new DiscountCatalog());
		printer = new Printer();
	}

	/**
	 * Registers an observer instance.
	 * 
	 * @param observer The observer instance to be added
	 */
	public void registerObserver(TotalRevenueObserver observer) {
		observers.add(observer);
	}

	/**
	 * Starts a new sale.
	 */
	public void startSale() {
		sale = new Sale();

		for (TotalRevenueObserver observer : observers) {
			sale.registerObserver(observer);
		}
	}

	/**
	 * Enter an item into the sale.
	 * 
	 * @param itemId The ID of the item to be entered into the sale.
	 * @return The current item information and running total.
	 * @throws ItemNotFoundException if the item is not found in the inventory.
	 * @throws RuntimeException      if the item cannot be retrieved due to inventory system failure.
	 */
	public SaleInfoDTO enterItem(String itemId) throws ItemNotFoundException {
		try {
			ItemDTO boughtItem = inventorySystem.retrieveItem(itemId);
			SaleInfoDTO saleInfo = sale.addBoughtItem(boughtItem);
			return saleInfo;
		} catch (DatabaseFailureException e) {
			logger.logException(e);
			throw new RuntimeException("An inventory database error occurred", e);
		}
	}

	/**
	 * Ends the current sale and returns the total price for this sale.
	 * 
	 * @return The total price of the current sale.
	 */
	public Amount endSale() {
		return sale.getTotalPrice().rounded();
	}

	/**
	 * Requests and applies all eligible discounts for the given customer.
	 *
	 * @param customerId The customer ID.
	 * @return The discounted total price.
	 */
	public Amount requestDiscount(int customerId) {
		ArrayList<ItemDTO> boughtItems = sale.getBoughtItems();
		Amount totalPrice = sale.getTotalPrice();

		DiscountDTO discountDTO = new DiscountDTO(boughtItems, totalPrice, customerId);
		ArrayList<DiscountStrategy> discounts = discountFactory.fetchEligibleDiscounts(discountDTO);

		Amount discountedPrice = sale.setDiscountedPrice(discounts);
		return discountedPrice;
	}

	/**
	 * Handles payment and returns the change, then prints the receipt.
	 * 
	 * @param amount The paid amount.
	 * @return The change to be returned to the customer.
	 */
	public Amount finalizeSaleWithPayment(Amount amount) {
		sale.setAmountPaid(amount);

		SaleDTO saleDTO = sale.getSaleInfo(amount);
		ReceiptDTO receiptDTO = sale.getReceiptInfo(saleDTO);

		accountingSystem.account(saleDTO);
		inventorySystem.updateInventory(saleDTO);
		printer.printReceipt(receiptDTO);

		return saleDTO.change().rounded();
	}
}
