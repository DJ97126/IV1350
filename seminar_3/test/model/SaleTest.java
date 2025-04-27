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

    @Test
    public void testAddMultipleBoughtItems() {
        // Prepare a test item
        BigDecimal basePrice = new BigDecimal("5.00");
        BigDecimal vatRate = new BigDecimal("0.10"); // 10% VAT
        ItemDTO testItem = new ItemDTO("item2", "Another Item", basePrice, vatRate, "Another test item.");
        int quantity = 3;

        // Expected values after adding 3 items
        BigDecimal singleItemVat = basePrice.multiply(vatRate); // 5.00 * 0.10 = 0.50
        BigDecimal singleItemFullPrice = basePrice.add(singleItemVat); // 5.00 + 0.50 = 5.50
        BigDecimal expectedTotalVat = singleItemVat.multiply(new BigDecimal(quantity)); // 0.50 * 3 = 1.50
        BigDecimal expectedTotalPrice = singleItemFullPrice.multiply(new BigDecimal(quantity)); // 5.50 * 3 = 16.50

        // Add multiple items
        SaleInfoDTO resultInfo = saleInstance.addMultipleBoughtItem(testItem, quantity);

        // --- Print values for debugging ---
        System.out.println("--- testAddMultipleBoughtItems Values ---");
        System.out.println("Expected Single Item Full Price: " + singleItemFullPrice.setScale(2));
        System.out.println("Actual Returned Item Price:      " + resultInfo.currentItem().price().setScale(2));
        System.out.println("Expected Total Price:            " + expectedTotalPrice.setScale(2));
        System.out.println("Actual Returned Total Price:     " + resultInfo.totalPrice().setScale(2));
        System.out.println("Expected Total VAT:              " + expectedTotalVat.setScale(2));
        System.out.println("Actual Returned Total VAT:       " + resultInfo.totalVat().setScale(2));
        System.out.println("Expected Item Count:             " + quantity);
        System.out.println("Actual Item Count in Sale:       " + saleInstance.getBoughtItems().length);
        System.out.println("Actual Total Price in Sale:      " + saleInstance.getTotalPrice().setScale(2));
        // If Sale has getTotalVat():
        // System.out.println("Actual Total VAT in Sale:        " + saleInstance.getTotalVat().setScale(2));
        System.out.println("---------------------------------------");
        // --- End Print values ---


         // Verify the returned SaleInfoDTO
        assertNotNull(resultInfo, "Returned SaleInfoDTO should not be null.");
        // Note: The returned item DTO price is the *full* price of ONE item
        // Use the correct accessor name 'currentItem' from the SaleInfoDTO record definition
        assertEquals(singleItemFullPrice.setScale(2), resultInfo.currentItem().price().setScale(2), "Returned item DTO should have the full price of one item.");
        assertEquals(expectedTotalPrice.setScale(2), resultInfo.totalPrice().setScale(2), "Returned running total should be the total for all items.");
        // Added assertion for totalVat in the returned DTO
        assertEquals(expectedTotalVat.setScale(2), resultInfo.totalVat().setScale(2), "Returned running total VAT should be the total VAT for all items.");

        // Verify the Sale state
        assertEquals(quantity, saleInstance.getBoughtItems().length, "Correct number of items should be in the list.");
        assertEquals(expectedTotalPrice.setScale(2), saleInstance.getTotalPrice().setScale(2), "Sale total price should be updated correctly for multiple items.");
         // Add assertion for totalVat if accessible/needed in Sale object itself
         // If Sale class has a getTotalVat() method:
         // assertEquals(expectedTotalVat.setScale(2), saleInstance.getTotalVat().setScale(2), "Sale total VAT should be updated correctly.");
    }

    // Add more tests here for other methods like getSaleInfo, setDiscountedPrice etc.
    // Remember to test edge cases like adding zero quantity, null inputs where applicable.

}
