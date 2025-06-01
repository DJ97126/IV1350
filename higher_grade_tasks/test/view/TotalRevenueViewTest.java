package view;

import model.Amount;
import org.junit.jupiter.api.*;
import java.io.*;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TotalRevenueViewTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testDoShowTotalIncomePrintsCorrectRevenue() {
        // Arrange
        TotalRevenueView view = new TotalRevenueView();
        view.updateTotalRevenue(new Amount("500"));

        // Act
        String output = outContent.toString().trim();
        System.out.println("Captured Output:\n" + output); // Debug print

        // Assert
        assertTrue(output.contains("Total Revenue: 500:00 SEK"));
    }

    @Test
    void testHandleErrorsPrintsErrorMessage() {
        TotalRevenueView view = new TotalRevenueView();

        // Act
        view.handleErrors(new RuntimeException("Simulated error"));

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("Error displaying total revenue: Simulated error"));
    }
}
