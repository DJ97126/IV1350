package model.discount;

import model.Amount;

/**
 * Discount strategy that applies a percentage discount based on the total price.
 */
public class TotalPricePercentageDiscount implements DiscountStrategy {
	private final Amount discountPercentage;
	private final String description;

	/**
	 * Creates a new total price percentage discount strategy.
	 *
	 * @param discountPercentage The percentage in decimal form to discount.
	 */
	public TotalPricePercentageDiscount(Amount discountPercentage, String description) {
		this.discountPercentage = discountPercentage;
		this.description = description;
	}

	/**
	 * Calculates the discount to be applied based on the provided total price.
	 *
	 * @param totalPrice The total price before discount.
	 * @return The calculated discount amount to be applied on totalPrice.
	 */
	@Override
	public Amount calculateDiscount(Amount totalPrice) {
		return totalPrice.multiply(discountPercentage);
	}

	/**
	 * Retrieves the description of the discount strategy.
	 *
	 * @return The description of the discount strategy.
	 */
	@Override
	public String getDescription() {
		return description;
	}
}