package view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import observer.TotalRevenueObserverTemplate;

/**
 * Observer that prints the total revenue to a file.
 */
public class TotalRevenueFileOutput extends TotalRevenueObserverTemplate {
    private static final String REVENUE_FILE_NAME = "total_revenue.log";
    private PrintWriter revenueFile;

    /**
     * Constructor of the class, creates a log file, which name is recorded in REVENUE_FILE_NAME
     */
    public TotalRevenueFileOutput() {
        try {
            revenueFile = new PrintWriter(new FileWriter(REVENUE_FILE_NAME, true), true);
        } catch (IOException e) {
            System.out.println("Could not create revenue log file.");
            e.printStackTrace();
        }
    }

    @Override
    protected void doShowTotalIncome() throws IOException {
        if (revenueFile == null) {
            throw new IOException("Revenue file is not initialized.");
        }
        String logMessage = "%s, Total Revenue: %s SEK".formatted(createTime(), totalRevenue.colonized());
        revenueFile.println(logMessage);
    }

    @Override
    protected void handleErrors(Exception e) {
        System.out.println("Error writing total revenue to file: " + e.getMessage());
        e.printStackTrace();
    }

    private String createTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
