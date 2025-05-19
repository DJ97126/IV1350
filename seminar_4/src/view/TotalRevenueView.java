package view;

import model.Amount;
import observer.TotalRevenueObserver;

/**
 * Observer that prints the total revenue to the console.
 */
public class TotalRevenueView implements TotalRevenueObserver {
	private Amount totalRevenue;

	/**
	 * Constructor for the class
	 */
	public TotalRevenueView(){
		totalRevenue = new Amount();
	}
	/**
	 * Print the observed total revenue in console
	 */
	@Override
	public void updateTotalRevenue(Amount saleAmount) {
		totalRevenue = totalRevenue.add(saleAmount);
		System.out.println("""
				Total Revenue: %s SEK
				""".formatted(totalRevenue.colonized()));
	}
}
