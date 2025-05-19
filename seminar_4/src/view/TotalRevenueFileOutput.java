package view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Amount;
import observer.TotalRevenueObserver;

/**
 * Observer that prints the total revenue to a file.
 */
public class TotalRevenueFileOutput implements TotalRevenueObserver {
	private static final String REVENUE_FILE_NAME = "total_revenue.log";
	private PrintWriter revenueFile;
    private Amount totalRevenue;

	/**
	 * Constructor of the class, creates a log file, which name is recording to REVENUE_FILE_NAME
	 */
	public TotalRevenueFileOutput() {
		totalRevenue = new Amount();
		try {
			revenueFile = new PrintWriter(new FileWriter(REVENUE_FILE_NAME, true), true);
		} catch (IOException e) {
			System.out.println("Could not create revenue log file.");
			e.printStackTrace();
		}
	}

	/**
	 * Updates the log with the sale price
	 * 
	 * @param saleAmount the total price of the sale that is finalized
	 */
	@Override
	public void updateTotalRevenue(Amount saleAmount) {
		totalRevenue = totalRevenue.add(saleAmount);
		String logMessage = "%s, Total Revenue: %s SEK".formatted(createTime(), totalRevenue.colonized());
		revenueFile.println(logMessage);
	}

	private String createTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter);
	}
}
