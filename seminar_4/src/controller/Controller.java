package controller;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import integration.AccountingSystem;
import integration.InventorySystem;
import integration.InventorySystemException;
import integration.Printer;
import model.Amount;
import model.Sale;

/**
 * This serves as the main controller that the cashier interacts with the model and integration systems.
 */
public class Controller {
	private final AccountingSystem accountingSystem;
	private final InventorySystem inventorySystem;
	private final Printer printer;

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
	 * Starts a new sale.
	 */
	public void startSale() {
		sale = new Sale();
	}

	/**
	 * Enter an item into the sale.
	 * 
	 * @param itemId The ID of the item to be entered into the sale.
	 * @return The current item information and running total.
	 * @throws RuntimeException if the item cannot be retrieved due to inventory system failure.
	 */
	public SaleInfoDTO enterItem(String itemId) {
		try {
			ItemDTO boughtItem = inventorySystem.retrieveItem(itemId);
			SaleInfoDTO saleInfo = sale.addBoughtItem(boughtItem);
			return saleInfo;
		} catch (InventorySystemException e) {
			System.err.println(e.getMessage());
			throw new RuntimeException("Could not retrieve item information. Please try again later.");
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
		sale.setAmountPaid(amount);

		SaleDTO saleDTO = sale.getSaleInfo(amount);
		ReceiptDTO receiptDTO = sale.getReceiptInfo(saleDTO);

		accountingSystem.account(saleDTO);
		inventorySystem.updateInventory(saleDTO);
		printer.printReceipt(receiptDTO);

		return saleDTO.change().rounded();
	}
}
