package integration;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;

import static utils.StringUtils.formatBigDecimalToColon;

/**
 * This class simulates a printer that prints the receipt to the console.
 * It formats the receipt with the sale information and item details.
 */
public class Printer {
	public Printer() {}

	/**
	 * Prints the receipt to the console. Simulates a printer.
	 * 
	 * @param receiptDTO
	 */
	public void printReceipt(ReceiptDTO receiptDTO) {
		SaleDTO saleInformation = receiptDTO.sale();
		Map<ItemDTO, Integer> quantizedItems = getQuantizedItems(saleInformation);
		String itemsString = getItemsString(quantizedItems);
		String formattedReceipt = getFormattedReceipt(saleInformation, itemsString);
		System.out.println(formattedReceipt);
	}

	private Map<ItemDTO, Integer> getQuantizedItems(SaleDTO saleInfo) {
		Map<ItemDTO, Integer> itemQuantities = new HashMap<>();

		for (ItemDTO item : saleInfo.boughtItems()) {
			itemQuantities.merge(item, 1, Integer::sum);
		}

		return itemQuantities;
	}

	private String getItemsString(Map<ItemDTO, Integer> itemQuantities) {
		StringBuilder itemsStringBuilder = new StringBuilder();

		for (Map.Entry<ItemDTO, Integer> entry : itemQuantities.entrySet()) {
			ItemDTO item = entry.getKey();
			int quantity = entry.getValue();

			BigDecimal priceWithVat = item.price().multiply(item.vat().add(BigDecimal.ONE));
			BigDecimal totalItemPrice = priceWithVat.multiply(new BigDecimal(quantity));

			String itemName = (item.name().length() > 21)
					? item.name().substring(0, 19) + "..."
					: item.name();
			String itemQuantity = String.valueOf(quantity);
			String itemPrice = formatBigDecimalToColon(priceWithVat);
			String itemTotal = formatBigDecimalToColon(totalItemPrice);

			String formattedString = """
					%-24s %2s x %7s %10s SEK
					""".formatted(itemName, itemQuantity, itemPrice, itemTotal);

			itemsStringBuilder.append(formattedString);
		}

		return itemsStringBuilder.toString();
	}

	private String getFormattedReceipt(SaleDTO saleInfo, String itemsString) {
		String time = saleInfo.saleDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		String total = formatBigDecimalToColon(saleInfo.totalPrice());
		String vat = formatBigDecimalToColon(saleInfo.totalVat());
		String paid = formatBigDecimalToColon(saleInfo.amountPaid());
		String change = formatBigDecimalToColon(saleInfo.change());

		return """
				------------------ Begin receipt -------------------
				Time of Sale: %38s

				%s

				Total: %41s SEK
				VAT: %43s SEK

				Cash: %42s SEK
				Change: %40s SEK
				------------------ End receipt ---------------------
				""".formatted(time, itemsString, total, vat, paid, change);
	}
}
