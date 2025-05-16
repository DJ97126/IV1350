package model.discount;

import model.Amount;

/**
 * Strategy interface for calculating discounts.
 */
public interface DiscountStrategy {
	/**
	 * Calculates the discount to be applied based on the provided total price.
	 *
	 * @param totalPrice The total price before discount.
	 * @return The calculated discount amount to be applied on totalPrice.
	 */
	Amount calculateDiscount(Amount totalPrice);

	/**
	 * Retrieves the description of the discount strategy.
	 *
	 * @return The description of the discount strategy.
	 */
	String getDescription();
}