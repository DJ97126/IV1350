package integration;

import java.util.ArrayList;
import java.util.HashMap;

import dto.DiscountDTO;
import model.Amount;

/**
 * Simulates a discount catalog.
 */
public class DiscountCatalog {
	private final HashMap<DiscountType, Discount> catalog;

	/**
	 * Constructor for the DiscountCatalog class.
	 */
	public DiscountCatalog() {
		catalog = new HashMap<>();
		simulateCatalog();
	}

	/**
	 * Fetches eligible discounts based on the provided discount information.
	 *
	 * @param discountDTO The object containing sale and customer information.
	 * @return A list of applicable discounts.
	 */
	public ArrayList<Discount> fetchEligibleDiscounts(DiscountDTO discountDTO) {
		ArrayList<Discount> eligibleDiscounts = new ArrayList<>();

		if (discountDTO.boughtIitems().size() > 2) {
			Discount discount = catalog.get(DiscountType.ITEM_BASED);
			if (discount != null)
				eligibleDiscounts.add(discount);
		}
		if (discountDTO.totalPrice().compareTo(new Amount("100")) > 0) {
			Discount discount = catalog.get(DiscountType.TOTAL_PERCENT);
			if (discount != null)
				eligibleDiscounts.add(discount);
		}
		if (discountDTO.customerId() == 114514) {
			Discount discount = catalog.get(DiscountType.CUSTOMER_PERCENT);
			if (discount != null)
				eligibleDiscounts.add(discount);
		}

		return eligibleDiscounts;
	}

	private void simulateCatalog() {
		catalog.put(DiscountType.ITEM_BASED,
				new Discount(DiscountType.ITEM_BASED, new Amount("5.00"), "5 SEK off for buying more than 2 items"));
		catalog.put(DiscountType.TOTAL_PERCENT,
				new Discount(DiscountType.TOTAL_PERCENT, new Amount("0.10"), "10% off for total price > 100"));
		catalog.put(DiscountType.CUSTOMER_PERCENT,
				new Discount(DiscountType.CUSTOMER_PERCENT, new Amount("0.05"), "5% off for customer 114514"));
	}

	public enum DiscountType {
		ITEM_BASED,
		TOTAL_PERCENT,
		CUSTOMER_PERCENT
	}

	public static record Discount(DiscountType type, Amount value, String description) {}
}