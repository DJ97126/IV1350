package observer;

import model.Amount;

/**
 * Observer interface for total revenue updates.
 */
public interface TotalRevenueObserver {
    void updateTotalRevenue(Amount totalRevenue);
}