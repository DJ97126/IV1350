package integration;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import model.Amount;

/**
 * This class simulates a printer that prints the receipt to the console. 
 * It formats the receipt with the sale information and item details.
 */
public class Printer {
	/**
	 * Prints the receipt to the console. Simulates a printer.
	 * 
	 * @param receiptDTO The receipt information to be printed.
	 */
	public void printReceipt(ReceiptDTO receiptDTO) {
		SaleDTO saleInformation = receiptDTO.sale();

		HashMap<ItemDTO, Integer> quantizedItems = getQuantizedItems(saleInformation);
		String itemsString = getItemsString(quantizedItems);

		String formattedReceipt = getFormattedReceipt(saleInformation, itemsString);
		System.out.println(formattedReceipt);
	}

	private HashMap<ItemDTO, Integer> getQuantizedItems(SaleDTO saleInfo) {
		HashMap<ItemDTO, Integer> itemQuantities = new HashMap<>();

		for (ItemDTO item : saleInfo.boughtItems()) {
			itemQuantities.merge(item, 1, Integer::sum);
		}

		return itemQuantities;
	}

	private String getItemsString(HashMap<ItemDTO, Integer> itemQuantities) {
		StringBuilder itemsStringBuilder = new StringBuilder();

		for (HashMap.Entry<ItemDTO, Integer> entry : itemQuantities.entrySet()) {
			ItemDTO item = entry.getKey();
			int quantity = entry.getValue();

			Amount priceWithVat = item.price().multiply(item.vat().add(new Amount("1")));
			Amount totalItemPrice = priceWithVat.multiply(new Amount(Integer.toString(quantity)));

			String itemName = (item.name().length() > 21)
					? item.name().substring(0, 19) + "..."
					: item.name();
			String itemQuantity = String.valueOf(quantity);
			String itemPrice = priceWithVat.colonized();
			String itemTotal = totalItemPrice.colonized();

			String formattedString = """
					%-24s %2s x %7s %10s SEK
					""".formatted(itemName, itemQuantity, itemPrice, itemTotal);

			itemsStringBuilder.append(formattedString);
		}

		return itemsStringBuilder.toString();
	}

	private String getFormattedReceipt(SaleDTO saleInfo, String itemsString) {
		String time = saleInfo.saleDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		String total = saleInfo.totalPrice().colonized();
		String vat = saleInfo.totalVat().colonized();
		String paid = saleInfo.amountPaid().colonized();
		String change = saleInfo.change().colonized();
		String discountTotal = saleInfo.discountedPrice().colonized();

		return """
				------------------ Begin receipt -------------------
				Time of Sale: %38s

				%s
				Discount Applied: %30s SEK
				Total: %41s SEK
				VAT: %43s SEK

				Cash: %42s SEK
				Change: %40s SEK
				------------------ End receipt ---------------------
				""".formatted(time, itemsString, discountTotal, total, vat, paid, change);
	}
}
