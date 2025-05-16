package model.discount;

import java.util.ArrayList;

import dto.DiscountDTO;
import integration.DiscountCatalog;

/**
 * Factory for creating eligible discount strategies based on sale and customer data.
 */
public class DiscountFactory {
	private static final DiscountCatalog discountCatalog = new DiscountCatalog();

	/**
	 * Returns a list of eligible discount strategies for the given sale and customer.
	 *
	 * @param discountDTO The object containing sale and customer information.
	 * @return A list of applicable discount strategies.
	 */
	public static ArrayList<DiscountStrategy> getEligibleDiscounts(DiscountDTO discountDTO) {
		return discountCatalog.fetchEligibleDiscounts(discountDTO);
	}
}