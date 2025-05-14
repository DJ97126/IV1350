package controller;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import integration.AccountingSystem;
import integration.DatabaseFailureException;
import integration.InventorySystem;
import integration.ItemNotFoundException;
import integration.Printer;
import java.util.ArrayList;
import java.util.List;
import model.Amount;
import model.Sale;
import util.LogHandler;
import util.TotalRevenueObserver;

/**
 * This serves as the main controller that the cashier interacts with the model and integration systems.
 */
public class Controller {
	private final AccountingSystem accountingSystem;
	private final InventorySystem inventorySystem;
	private final Printer printer;

	private LogHandler logger = LogHandler.getLogger();
	private List<TotalRevenueObserver> observers = new ArrayList<>();

	private Sale sale;

	/**
	 * Constructor for the Controller class.
	 */
	public Controller() {
		accountingSystem = new AccountingSystem();
		inventorySystem = new InventorySystem();
		printer = new Printer();
	}

	/**
	 * Constructor for the add an observer instance.
	 * 
	 * @param observer the observer instance to be added
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
	 * @throws ItemNotFoundException    if the item is not found in the inventory.
	 * @throws DatabaseFailureException if the item cannot be retrieved due to inventory system failure.
	 */
	public SaleInfoDTO enterItem(String itemId) {
		try {
			ItemDTO boughtItem = inventorySystem.retrieveItem(itemId);
			SaleInfoDTO saleInfo = sale.addBoughtItem(boughtItem);
			return saleInfo;
		} catch (ItemNotFoundException e) {
			logger.logException(e);
			throw new RuntimeException("Item not found in inventory");
		} catch (DatabaseFailureException e) {
			logger.logException(e);
			throw new RuntimeException("Could not retrieve item information");
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
	 * Handles payment and returns the change, then prints the receipt.
	 * 
	 * @param amount The paid amount.
	 * @return The change to be returned to the customer.
	 */
	public Amount finalizeSaleWithPayment(Amount amount) {
		try {
			if (amount == null || amount.isNegative()) {
				throw new IllegalArgumentException("Paid amount must be non-null and non-negative");
			}

			sale.setAmountPaid(amount);

			SaleDTO saleDTO = sale.getSaleInfo(amount);
			ReceiptDTO receiptDTO = sale.getReceiptInfo(saleDTO);

			accountingSystem.account(saleDTO);
			inventorySystem.updateInventory(saleDTO);
			printer.printReceipt(receiptDTO);

			return saleDTO.change().rounded();
		} catch (IllegalArgumentException e) {
			logger.logException(e);
			throw new RuntimeException("Invalid payment: %s".formatted(e.getMessage()));
		} catch (Exception e) {
			logger.logException(e);
			throw new RuntimeException("An error occurred while finalizing the sale");
		}
	}
}
