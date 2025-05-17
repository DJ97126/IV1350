package model.discount;

import model.Amount;

/**
 * Discount strategy that applies a fixed discount based on bought items.
 */
public class ItemBasedDiscount implements DiscountStrategy {
	private final Amount discountAmount;
	private final String description;

	/**
	 * Creates a new item-based discount strategy.
	 *
	 * @param discountAmount The fixed discount amount to apply.
	 */
	public ItemBasedDiscount(Amount discountAmount, String description) {
		this.discountAmount = discountAmount;
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
		return discountAmount;
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