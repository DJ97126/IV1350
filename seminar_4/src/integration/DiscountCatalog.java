package integration;

import java.util.ArrayList;
import java.util.HashMap;

import dto.DiscountDTO;
import dto.DiscountInfoDTO;
import model.Amount;

/**
 * Simulates a discount catalog.
 */
public class DiscountCatalog {
	private final HashMap<String, DiscountInfoDTO> catalog;

	/**
	 * Constructor for the DiscountCatalog class.
	 */
	public DiscountCatalog() {
		catalog = new HashMap<>();
		simulateCatalog();
	}

	/**
	 * Fetch discount based on the type of the discount.
	 * 
	 * @param discountDTO The discount information.
	 * @return A list of eligible discounts.
	 */
	public ArrayList<DiscountInfoDTO> fetchEligibleDiscounts(DiscountDTO discountDTO) {
		ArrayList<DiscountInfoDTO> eligibleDiscounts = new ArrayList<>();

		if (discountDTO.boughtIitems().size() > 2) {
			DiscountInfoDTO discount = catalog.get("ITEM_BASED");
			if (discount != null) {
				eligibleDiscounts.add(discount);
			}
		}
		if (discountDTO.totalPrice().compareTo(new Amount("100")) > 0) {
			DiscountInfoDTO discount = catalog.get("TOTAL_PERCENT");
			if (discount != null) {
				eligibleDiscounts.add(discount);
			}
		}
		if (discountDTO.customerId() == 114514) {
			DiscountInfoDTO discount = catalog.get("CUSTOMER_PERCENT");
			if (discount != null) {
				eligibleDiscounts.add(discount);
			}
		}

		return eligibleDiscounts;
	}

	private void simulateCatalog() {
		catalog.put("ITEM_BASED",
				new DiscountInfoDTO("ITEM_BASED", new Amount("5.00"), "5 SEK off for buying more than 2 items"));
		catalog.put("TOTAL_PERCENT",
				new DiscountInfoDTO("TOTAL_PERCENT", new Amount("0.10"), "10% off for total price > 100"));
		catalog.put("CUSTOMER_PERCENT",
				new DiscountInfoDTO("CUSTOMER_PERCENT", new Amount("0.05"), "5% off for customer 114514"));
	}
}