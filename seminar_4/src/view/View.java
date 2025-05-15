package view;

import controller.Controller;
import dto.ItemDTO;
import dto.SaleInfoDTO;
import integration.ItemNotFoundException;
import model.Amount;

/**
 * This class serves as the simulation of user interface for the system.
 */
public class View {
	private final Controller controller;

	private final ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();

	/**
	 * Sets up the view with the given controller.
	 *
	 * @param controller The controller to be used for this view.
	 */
	public View(Controller controller) {
		this.controller = controller;
		controller.registerObserver(new TotalRevenueView());
		controller.registerObserver(new TotalRevenueFileOutput());
	}

	/**
	 * Simulates the execution of the system. This method is a placeholder for actual user interaction.
	 */
	public void simulateExecution() {
		controller.startSale();

		tryEnterItem("abc123");
		tryEnterItem("abc123");
		tryEnterItem("def456");

		tryEnterItem("fail114514");

		tryEnterItem("nonExistentItem");

		Amount totalPrice = controller.endSale();
		displayEndSaleInfo(totalPrice);

		Amount change = controller.finalizeSaleWithPayment(new Amount("100"));
		displayChangeInfo(change);

		for (int i = 0; i < 3; i++){
			controller.startSale();

			tryEnterItem("abc123");
			tryEnterItem("def456");

			Amount total = controller.endSale();
			displayEndSaleInfo(total);

			Amount changeToCustomer = controller.finalizeSaleWithPayment(new Amount("100"));
			displayChangeInfo(changeToCustomer);
		}
	}

	private void tryEnterItem(String itemId) {
		try {
			displayRunningInfo(controller.enterItem(itemId));
		} catch (ItemNotFoundException e) {
			displayUiErrorMessage(e.getMessage());
		} catch (RuntimeException e) {
			displayUiErrorMessage(e.getMessage());
		}
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

	private void displayUiErrorMessage(String uiMessage) {
		errorMessageHandler.showErrorMessage(uiMessage);
	}
}
