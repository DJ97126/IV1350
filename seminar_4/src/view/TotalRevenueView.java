package view;

import model.Amount;
import observer.TotalRevenueObserver;

/**
 * Observer that prints the total revenue to the console.
 */
public class TotalRevenueView implements TotalRevenueObserver {
	@Override
	public void updateTotalRevenue(Amount totalRevenue) {
		System.out.println("Total Revenue: %s SEK".formatted(totalRevenue.colonized()));
	}
}
