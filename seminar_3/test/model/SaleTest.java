package model;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;
import java.math.BigDecimal;
import java.math.RoundingMode; // Import for comparison
import java.util.List;

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
        assertEquals(0, saleInstance.getBoughtItems().size(), "Bought items list should be empty initially.");
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
        assertEquals(1, saleInstance.getBoughtItems().size(), "One item should be in the bought items list.");
        assertEquals(testItem.id(), saleInstance.getBoughtItems().get(0).id(), "The correct item should be added.");
        assertEquals(expectedFullPrice.setScale(2), saleInstance.getTotalPrice().setScale(2), "Sale total price should be updated correctly.");
        // Add assertion for totalVat if accessible/needed
    }

    @Test
    public void testGetBoughtItems() {
        ItemDTO item1 = new ItemDTO("item1", "Test Item 1", new BigDecimal("10.00"), new BigDecimal("0.25"), "Test item number one");
        ItemDTO item2 = new ItemDTO("item2", "Test Item 2", new BigDecimal("20.00"), new BigDecimal("0.20"), "Test item number two");
        ItemDTO item3 = new ItemDTO("item3", "Test Item 3", new BigDecimal("30.00"), new BigDecimal("0.15"), "Test item number three");
        ItemDTO item4 = new ItemDTO("item4", "Test Item 4", new BigDecimal("40.00"), new BigDecimal("0.10"), "Test item number four");
        ItemDTO item5 = new ItemDTO("item5", "Test Item 5", new BigDecimal("50.00"), new BigDecimal("0.05"), "Test item number five");

        saleInstance.addBoughtItem(item1);
        saleInstance.addBoughtItem(item2);
        saleInstance.addBoughtItem(item3);
        saleInstance.addBoughtItem(item4);
        saleInstance.addBoughtItem(item5);

        List<ItemDTO> items = saleInstance.getBoughtItems();

        assertEquals(5, items.size());
        assertEquals("item1", items.get(0).id());
        assertEquals("Test Item 2", items.get(1).name());
        assertEquals(new BigDecimal("30.00"), items.get(2).price().setScale(2));
        assertEquals(new BigDecimal("0.10"), items.get(3).vat().setScale(2));
        assertEquals("Test item number five", items.get(4).description());
    }

    @Test
    public void testGetSaleInfo() {
        ItemDTO item = new ItemDTO("item1", "Test Item 1", new BigDecimal("10.00"), new BigDecimal("0.25"), "1L milk");
        saleInstance.addBoughtItem(item);

        BigDecimal paid = new BigDecimal("20.00");
        SaleDTO saleDTO = saleInstance.getSaleInfo(paid);

        assertEquals(paid, saleDTO.amountPaid());
        assertEquals(new BigDecimal("12.50"), saleDTO.totalPrice().setScale(2));
        assertEquals(new BigDecimal("7.50"), saleDTO.change().setScale(2));
    }

    @Test
    public void testGetReceiptInfo() {
        ItemDTO item = new ItemDTO("item1", "Test Item 1", new BigDecimal("10.00"), new BigDecimal("0.25"), "1L milk");
        saleInstance.addBoughtItem(item);
        SaleDTO saleDTO = saleInstance.getSaleInfo(new BigDecimal("20.00"));

        ReceiptDTO receipt = saleInstance.getReceiptInfo(saleDTO);

        assertNotNull(receipt);
        assertEquals(saleDTO, receipt.sale());
    }
    // Add more tests here for other methods like getSaleInfo, setDiscountedPrice etc.
    // Remember to test edge cases like adding zero quantity, null inputs where applicable.

}
