package controller;

import integration.AccountingSystem;
import integration.InventorySystem;
import integration.ItemDTO;
import integration.Printer;
import model.ReceiptDTO;
import model.Sale;
import model.SaleDTO;
import model.SaleInfoDTO;

/**
 * This serves as the main controller that the user interacts with the model and integration systems.
 */
public class Controller {
    private AccountingSystem accountingSystem;
    private InventorySystem inventorySystem;
    private Printer printer;

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
     * @param itemID The ID of the item to be entered into the sale.
     * @return The current item information and running total.
     */
    public SaleInfoDTO enterItem(String itemID) {
        ItemDTO boughtItem = inventorySystem.retrieveItem(itemID);
        SaleInfoDTO saleInfo = sale.addBoughtItem(boughtItem);
        return saleInfo;
    }

    /**
     * Ends the current sale and returns the total price for this sale.
     * 
     * @return The total price of the current sale.
     */
    public float endSale() {
        return sale.getTotalPrice();
    }

    /**
     * Handles payment and returns the change, then prints the receipt.
     * 
     * @param amount The paid amount.
     * @return The change to be returned to the customer.
     */
    public float finalizeSaleWithPayment(float amount) {
        sale.setAmountPaid(amount);

        SaleDTO saleDTO = sale.getSaleInfo(amount);
        ReceiptDTO receiptDTO = sale.getReceiptInfo(saleDTO);

        accountingSystem.account(saleDTO);
        inventorySystem.updateInventory(saleDTO);
        printer.printReceipt(receiptDTO);

        return saleDTO.change();
    }
}
