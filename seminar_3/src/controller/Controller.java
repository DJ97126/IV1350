package controller;

import integration.AccountingSystem;
import integration.DiscountCatalog;
import integration.DiscountDTO;
import integration.InventorySystem;
import integration.ItemDTO;
import integration.Printer;
import model.ReceiptDTO;
import model.Sale;
import model.SaleDTO;
import model.SaleInfoDTO;

/**
 * This serves as the main controller that the user interacts with the model and
 * integration systems.
 */
public class Controller {
    private AccountingSystem accountingSystem = new AccountingSystem();
    private DiscountCatalog discountCatalog = new DiscountCatalog();
    private InventorySystem inventorySystem = new InventorySystem();
    private Printer printer = new Printer();
    private Sale sale;

    /**
     * Constructor for the Controller class.
     */
    public Controller() {
    }

    /**
     * Starts a new sale.
     */
    public void startSale() {
        sale = new Sale();
    }

    /**
     * Enter a single item into the sale.
     * 
     * @param itemID The ID of the item to be entered into the sale.
     * @return The current item information and running total.
     */
    public SaleInfoDTO enterItem(int itemID) {
        ItemDTO boughtItem = inventorySystem.retrieveItem(itemID);
        if (boughtItem != null) {
            SaleInfoDTO saleInfo = sale.addBoughtItem(boughtItem);
            return saleInfo;
        }
        return new SaleInfoDTO(); // No error handling yet.
    }

    /**
     * Enter a single item into the sale with a specified quantity.
     * 
     * @param itemID   The ID of the item.
     * @param quantity The quantity of the item.
     * @return The current item information and running total.
     */
    public SaleInfoDTO enterItem(int itemID, int quantity) {
        ItemDTO boughtItem = inventorySystem.retrieveItem(itemID);
        if (boughtItem != null) {
            SaleInfoDTO saleInfo = sale.addBoughtItem(boughtItem, quantity);
            return saleInfo;
        }
        return new SaleInfoDTO(); // No error handling yet.
    }

    /**
     * Ends the current Sale instance.
     * 
     * @return The total price of the sale.
     */
    public float endSale() {
        float totalPrice = sale.getTotalPrice();
        return totalPrice;
    }

    /**
     * Requests the eligible discounts of this sale for the customer from the
     * discount catalog.
     * 
     * @param customerID The ID of the customer.
     * @return The total price after applying the discount.
     */
    public float requestDiscount(int customerID) {
        ItemDTO[] boughtItems = sale.getBoughtItems();
        float totalPrice = sale.getTotalPrice();
        DiscountDTO[] discountInfo = discountCatalog.fetchEligibleDiscounts(boughtItems, totalPrice, customerID);
        float discountedPrice = sale.setDiscountedPrice(discountInfo);
        return discountedPrice;
    }

    /**
     * Handles payment and returns the change, and prints the receipt.
     * 
     * @param amount The paid amount.
     * @return The change to be returned to the customer.
     */
    public float enterAmountPaid(float amount) {
        sale.setAmountPaid(amount);
        SaleDTO saleDTO = sale.getSaleInfo(amount);
        ReceiptDTO receiptDTO = sale.getReceiptInfo(saleDTO);
        accountingSystem.account(saleDTO);
        inventorySystem.updateInventory(saleDTO);
        printer.printReceipt(receiptDTO);
        float change = saleDTO.getChange();
        return change;
    }
}
