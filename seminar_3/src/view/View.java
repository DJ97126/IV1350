package view;

import controller.Controller;
import integration.ItemDTO;
import model.SaleInfoDTO;

/**
 * This class serves as the user interface for the system.
 */
public class View {
    private Controller controller;

    /**
     * Sets up the view with the given controller.
     *
     * @param controller The controller to be used for this view.
     */
    public View(Controller controller) {
        this.controller = controller;
    }

    /**
     * Simulates the execution of the system. This method is a placeholder for actual user interaction.
     */
    public void simulateExecution() {
        // Mock Items
        ItemDTO itemABC123 = new ItemDTO("abc123", "BigWheel Oatmeal", 29.9f, 0.06f,
                "BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free");
        ItemDTO itemDEF456 = new ItemDTO("def456", "YouGoGo Blueberry", 14.9f, 0.06f,
                "YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour");

        controller.startSale();
        System.out.println("A new sale as been started.");

        // Doesn't work yet because other classes are not implemented
        SaleInfoDTO firstEnteredItem = controller.enterItem("abc123");
        displayItemInfo(firstEnteredItem);
        SaleInfoDTO secondEnteredItem = controller.enterItem("invalidID");
        displayItemInfo(secondEnteredItem);
        SaleInfoDTO thirdEnteredItem = controller.enterItem("def456");
        displayItemInfo(thirdEnteredItem);

        float total = controller.endSale();
        System.out.println("""
                End sale:
                Total cost (incl VAT): %.2f
                """.formatted(total));

        controller.finalizeSaleWithPayment(100);
    }

    private void displayItemInfo(SaleInfoDTO saleInfo) {
        System.out.println("""
                Add 1 item with item id %s:
                Item ID: %s
                Item name: %s
                Item cost: %.2f
                VAT: %.0f%%
                Item description: %s

                Total cost (incl VAT): %.2f SEK
                Total VAT: %.2f SEK
                """.formatted(/* to be implemented */));
    }
}
