package view;

import observer.TotalRevenueObserverTemplate;

/**
 * Observer that prints the total revenue to the console.
 */
public class TotalRevenueView extends TotalRevenueObserverTemplate {
    /**
     * Print the observed total revenue in console
     */
    @Override
    protected void doShowTotalIncome() {
        System.out.println("""
                Total Revenue: %s SEK
                """.formatted(totalRevenue.colonized()));
    }

    @Override
    protected void handleErrors(Exception e) {
        System.out.println("Error displaying total revenue: " + e.getMessage());
        e.printStackTrace();
    }
}
