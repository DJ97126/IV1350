package view;

import java.math.BigDecimal;
import java.math.RoundingMode;

import controller.Controller;
import integration.ItemDTO;
import model.SaleInfoDTO;

/**
 * This class serves as the user interface for the system.
 */
public class View {
    private final Controller controller;

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
        controller.startSale();

        displayRunningInfo(controller.enterItem("abc123"));
        displayRunningInfo(controller.enterItem("abc123"));
        displayRunningInfo(controller.enterItem("def456"));

        BigDecimal totalPrice = controller.endSale();
        System.out.println("""
                End sale:
                Total cost (incl VAT): %s SEK
                """.formatted(formatNumber(totalPrice)));

        // controller.finalizeSaleWithPayment(new BigDecimal(100));
    }

    private void displayRunningInfo(SaleInfoDTO saleInfo) {
        ItemDTO currentItem = saleInfo.currentItem();

        System.out.println("""
                Add 1 item with item id %s:
                Item ID: %s
                Item name: %s
                Item cost: %s SEK
                VAT: %.0f%%
                Item description: %s

                Total cost (incl VAT): %s SEK
                Total VAT: %s SEK
                """.formatted(currentItem.id(), currentItem.id(), currentItem.name(), formatNumber(currentItem.price()),
                currentItem.vat().multiply(BigDecimal.valueOf(100)), currentItem.description(),
                formatNumber(saleInfo.totalPrice()), formatNumber(saleInfo.totalVat())));
    }

    private String formatNumber(BigDecimal number) {
        return String.format("%.2f", number.setScale(2, RoundingMode.HALF_UP)).replace('.', ':');
    }
}
