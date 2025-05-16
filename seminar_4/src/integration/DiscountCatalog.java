package integration;

import java.util.ArrayList;

import dto.DiscountDTO;
import model.Amount;
import model.discount.DiscountStrategy;
import model.discount.ItemBasedDiscount;
import model.discount.TotalPricePercentageDiscount;
import model.discount.CustomerBasedDiscount;

/**
 * Simulates a discount catalog.
 */
public class DiscountCatalog {
	/**
	 * Fetches all eligible discounts for the given sale and customer.
	 *
	 * @param discountDTO The DTO containing bought items, total price, and customer ID.
	 * @return A list of applicable discount strategies.
	 */
	public ArrayList<DiscountStrategy> fetchEligibleDiscounts(DiscountDTO discountDTO) {
		ArrayList<DiscountStrategy> strategies = new ArrayList<>();

		if (discountDTO.boughtIitems().size() > 2) {
			strategies.add(new ItemBasedDiscount(new Amount("5.00"), "5 SEK off for buying more than 2 items"));
		}
		if (discountDTO.totalPrice().compareTo(new Amount("100")) > 0) {
			strategies.add(new TotalPricePercentageDiscount(new Amount("0.10"), "10% off for total price > 100"));
		}
		if (discountDTO.customerId() == 114514) {
			strategies.add(new CustomerBasedDiscount(new Amount("0.05"), "5% off for customer 114514"));
		}

		return strategies;
	}
}