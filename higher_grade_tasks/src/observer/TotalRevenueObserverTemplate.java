package observer;

import model.Amount;

/**
 * Template class for total revenue observers using the Template Method pattern.
 */
public abstract class TotalRevenueObserverTemplate implements TotalRevenueObserver {
    protected Amount totalRevenue = new Amount();

    @Override
    public void updateTotalRevenue(Amount saleAmount) {
        calculateTotalIncome(saleAmount);
        showTotalIncome();
    }

    protected void calculateTotalIncome(Amount saleAmount) {
        totalRevenue = totalRevenue.add(saleAmount);
    }

    private void showTotalIncome() {
        try {
            doShowTotalIncome();
        } catch (Exception e) {
            handleErrors(e);
        }
    }

    /**
     * Implement this to show the total income (e.g., print or write to file).
     */
    protected abstract void doShowTotalIncome() throws Exception;

    /**
     * Implement this to handle errors that occur during showing total income.
     */
    protected abstract void handleErrors(Exception e);
}
