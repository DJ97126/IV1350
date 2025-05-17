package model.discount;

import java.util.ArrayList;

import dto.DiscountDTO;
import dto.DiscountInfoDTO;
import integration.DiscountCatalog;

/**
 * Factory for creating eligible discount strategies based on sale and customer data.
 */
public class DiscountFactory {
	private final DiscountCatalog discountCatalog;

	/**
	 * Constructor for the DiscountFactory class.
	 *
	 * @param discountCatalog The discount catalog to fetch eligible discounts from.
	 */
	public DiscountFactory(DiscountCatalog discountCatalog) {
		this.discountCatalog = discountCatalog;
	}

	/**
	 * Fetches eligible discount strategies based on the provided discount information.
	 *
	 * @param discountDTO The object containing sale and customer information.
	 * @return A list of applicable discount strategies.
	 */
	public ArrayList<DiscountStrategy> fetchEligibleDiscounts(DiscountDTO discountDTO) {
		ArrayList<DiscountInfoDTO> eligibleDiscounts = discountCatalog.fetchEligibleDiscounts(discountDTO);
		ArrayList<DiscountStrategy> discountStrategies = new ArrayList<>();

		for (DiscountInfoDTO discount : eligibleDiscounts) {
			switch (discount.type()) {
			case "ITEM_BASED" -> discountStrategies
					.add(new ItemBasedDiscount(discount.value(), discount.description()));
			case "TOTAL_PERCENT" -> discountStrategies
					.add(new TotalPricePercentageDiscount(discount.value(), discount.description()));
			case "CUSTOMER_PERCENT" -> discountStrategies
					.add(new CustomerBasedDiscount(discount.value(), discount.description()));
			}
		}

		return discountStrategies;
	}
}