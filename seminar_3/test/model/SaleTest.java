package model;

import dto.ItemDTO;
import dto.SaleInfoDTO;
import java.math.BigDecimal;
import java.math.RoundingMode; // Import for comparison
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {

    private Sale saleInstance;

    @BeforeEach
    public void setUp() {
        // Create a new Sale instance before each test
        saleInstance = new Sale();
    }

    @AfterEach
    public void tearDown() {
        // Clean up the Sale instance after each test
        saleInstance = null;
    }

    @Test
    public void testSaleConstructor() {
        // Test initial state after construction
        assertNotNull(saleInstance.getBoughtItems(), "Bought items list should be initialized.");
        assertEquals(0, saleInstance.getBoughtItems().length, "Bought items list should be empty initially.");
        assertEquals(BigDecimal.ZERO.setScale(2), saleInstance.getTotalPrice().setScale(2), "Initial total price should be zero.");
        // Assuming totalVat is also accessible or testable indirectly
    }

    @Test
    public void testAddBoughtItem() {
        // Prepare a test item
        BigDecimal basePrice = new BigDecimal("10.00");
        BigDecimal vatRate = new BigDecimal("0.25"); // 25% VAT
        ItemDTO testItem = new ItemDTO("item1", "Test Item", basePrice, vatRate, "A test item.");

        // Expected values after adding the item
        BigDecimal expectedVat = basePrice.multiply(vatRate); // 10.00 * 0.25 = 2.50
        BigDecimal expectedFullPrice = basePrice.add(expectedVat); // 10.00 + 2.50 = 12.50

        // Add the item
        SaleInfoDTO resultInfo = saleInstance.addBoughtItem(testItem);

        // Verify the returned SaleInfoDTO
        assertNotNull(resultInfo, "Returned SaleInfoDTO should not be null.");
        // Use the correct accessor name 'currentItem' from the SaleInfoDTO record definition
        assertEquals(expectedFullPrice.setScale(2), resultInfo.currentItem().price().setScale(2), "Returned item DTO should have the full price.");
        assertEquals(expectedFullPrice.setScale(2), resultInfo.totalPrice().setScale(2), "Returned running total should match the item's full price.");
        // You might also want to check resultInfo.totalVat() if it's implemented and needed

        // Verify the Sale state
        assertEquals(1, saleInstance.getBoughtItems().length, "One item should be in the bought items list.");
        assertEquals(testItem.id(), saleInstance.getBoughtItems()[0].id(), "The correct item should be added.");
        assertEquals(expectedFullPrice.setScale(2), saleInstance.getTotalPrice().setScale(2), "Sale total price should be updated correctly.");
        // Add assertion for totalVat if accessible/needed
    }


    // Add more tests here for other methods like getSaleInfo, setDiscountedPrice etc.
    // Remember to test edge cases like adding zero quantity, null inputs where applicable.

}
