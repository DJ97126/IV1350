package view;

import controller.Controller;
import dto.ItemDTO;
import dto.SaleInfoDTO;
import model.Amount;

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

		Amount totalPrice = controller.endSale();
		displayEndSaleInfo(totalPrice);

		Amount change = controller.finalizeSaleWithPayment(new Amount("100"));
		displayChangeInfo(change);
	}

	private void displayRunningInfo(SaleInfoDTO saleInfo) {
		ItemDTO currentItem = saleInfo.currentItem();

		String id = currentItem.id();
		String name = currentItem.name();
		String price = currentItem.price().colonized();
		String vat = currentItem.vat().multiply(new Amount("100")).colonized();
		String description = currentItem.description();
		String totalPrice = saleInfo.totalPrice().colonized();
		String totalVat = saleInfo.totalVat().colonized();

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

	private void displayEndSaleInfo(Amount totalPrice) {
		System.out.println("""
				End sale:
				Total cost (incl VAT): %s SEK
				""".formatted(totalPrice.colonized()));
	}

	private void displayChangeInfo(Amount change) {
		System.out.println("""
				Change to give the customer : %s SEK
								""".formatted(change.colonized()));
	}
}
