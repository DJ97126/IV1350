package view;

import controller.Controller;
import dto.ItemDTO;
import dto.SaleInfoDTO;
import java.math.BigDecimal;
import static utils.StringUtils.formatBigDecimalToColon;

/**
 * This class serves as the simulation of user interface for the system.
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
		controller.startSale();

		displayRunningInfo(controller.enterItem("abc123"));
		displayRunningInfo(controller.enterItem("abc123"));
		displayRunningInfo(controller.enterItem("def456"));

		BigDecimal totalPrice = controller.endSale();
		displayEndSaleInfo(totalPrice);

		BigDecimal change = controller.finalizeSaleWithPayment(new BigDecimal(100));
		displayChangeInfo(change);
	}

	private void displayRunningInfo(SaleInfoDTO saleInfo) {
		ItemDTO currentItem = saleInfo.currentItem();

		String id = currentItem.id();
		String name = currentItem.name();
		String price = formatBigDecimalToColon(currentItem.price());
		String vat = formatBigDecimalToColon(currentItem.vat().multiply(BigDecimal.valueOf(100)));
		String description = currentItem.description();
		String totalPrice = formatBigDecimalToColon(saleInfo.totalPrice());
		String totalVat = formatBigDecimalToColon(saleInfo.totalVat());

		System.out.println("""
				Add 1 item with item id %s:
				Item ID: %s
				Item name: %s
				Item cost: %s SEK
				VAT: %s%%
				Item description: %s

				Total cost (incl VAT): %s SEK
				Total VAT: %s SEK
				""".formatted(id, id, name, price, vat, description, totalPrice, totalVat));
	}

	private void displayEndSaleInfo(BigDecimal totalPrice) {
		System.out.println("""
				End sale:
				Total cost (incl VAT): %s SEK
				""".formatted(formatBigDecimalToColon(totalPrice)));
	}

	private void displayChangeInfo(BigDecimal change) {
		System.out.println("""
				Change to give the customer : %s SEK
								""".formatted(formatBigDecimalToColon(change)));
	}
}
