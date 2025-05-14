package view;

import model.Amount;
import util.TotalRevenueObserver;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Observer that prints the total revenue to a file.
 */
public class TotalRevenueFileOutput implements TotalRevenueObserver {
    private static final String REVENUE_FILE_NAME = "total_revenue.log";
    private PrintWriter revenueFile;

    public TotalRevenueFileOutput() {
        try {
            revenueFile = new PrintWriter(new FileWriter(REVENUE_FILE_NAME, true), true);
        } catch (IOException e) {
            System.out.println("Could not create revenue log file.");
            e.printStackTrace();
        }
    }

    @Override
    public void updateTotalRevenue(Amount totalRevenue) {
        String logMessage = "%s, Total Revenue: %s SEK".formatted(createTime(), totalRevenue.colonized());
        revenueFile.println(logMessage);
    }

    private String createTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        return now.format(formatter);
    }
}
