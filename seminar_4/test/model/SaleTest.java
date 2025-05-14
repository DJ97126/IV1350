package model;

import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import dto.SaleInfoDTO;

public class SaleTest {

	private Sale saleInstance;

	@BeforeEach
	public void setUp() {
		saleInstance = new Sale();
	}

	@AfterEach
	public void tearDown() {
		saleInstance = null;
	}

	@Test
	public void testSaleConstructor() {
		assertNotNull(saleInstance.getBoughtItems(), "Bought items list should be initialized.");
		assertEquals(0, saleInstance.getBoughtItems().size(), "Bought items list should be empty initially.");
		assertEquals(new Amount("0"), saleInstance.getTotalPrice(),
				"Initial total price should be zero.");
	}

	@Test
	public void testAddBoughtItem() {
		Amount basePrice = new Amount("10.00");
		Amount vatRate = new Amount("0.25");
		ItemDTO testItem = new ItemDTO("item1", "Test Item", basePrice, vatRate, "A test item.");

		Amount expectedVat = basePrice.multiply(vatRate);
		Amount expectedFullPrice = basePrice.add(expectedVat);

		SaleInfoDTO resultInfo = saleInstance.addBoughtItem(testItem);

		assertNotNull(resultInfo,
				"Returned SaleInfoDTO should not be null.");
		assertEquals(expectedFullPrice, resultInfo.currentItem().price(),
				"Returned item DTO should have the full price.");
		assertEquals(expectedFullPrice, resultInfo.totalPrice(),
				"Returned running total should match the item's full price.");
		assertEquals(1, saleInstance.getBoughtItems().size(),
				"One item should be in the bought items list.");
		assertEquals(testItem.id(), saleInstance.getBoughtItems().get(0).id(),
				"The correct item should be added.");
		assertEquals(expectedFullPrice, saleInstance.getTotalPrice(),
				"Sale total price should be updated correctly.");
	}

	@Test
	public void testGetBoughtItems() {
		ItemDTO item1 = new ItemDTO("item1", "Test Item 1", new Amount("10.00"), new Amount("0.25"),
				"Test item number one");
		ItemDTO item2 = new ItemDTO("item2", "Test Item 2", new Amount("20.00"), new Amount("0.20"),
				"Test item number two");
		ItemDTO item3 = new ItemDTO("item3", "Test Item 3", new Amount("30.00"), new Amount("0.15"),
				"Test item number three");
		ItemDTO item4 = new ItemDTO("item4", "Test Item 4", new Amount("40.00"), new Amount("0.10"),
				"Test item number four");
		ItemDTO item5 = new ItemDTO("item5", "Test Item 5", new Amount("50.00"), new Amount("0.05"),
				"Test item number five");

		saleInstance.addBoughtItem(item1);
		saleInstance.addBoughtItem(item2);
		saleInstance.addBoughtItem(item3);
		saleInstance.addBoughtItem(item4);
		saleInstance.addBoughtItem(item5);

		List<ItemDTO> items = saleInstance.getBoughtItems();

		assertEquals(5, items.size(),
				"Failed to add Items");
		assertEquals("item1", items.get(0).id(),
				"Failed to read Item ID");
		assertEquals("Test Item 2", items.get(1).name(),
				"Failed to read Item name");
		assertEquals(new Amount("30.00"), items.get(2).price(),
				"Failed to read Item price");
		assertEquals(new Amount("0.10"), items.get(3).vat(),
				"Failed to read Item VAT");
		assertEquals("Test item number five", items.get(4).description(),
				"Failed to read Item description");
	}

	@Test
	public void testGetSaleInfo() {
		ItemDTO item = new ItemDTO("item1", "Test Item 1", new Amount("10.00"), new Amount("0.25"), "1L milk");
		saleInstance.addBoughtItem(item);

		Amount paid = new Amount("20.00");
		SaleDTO saleDTO = saleInstance.getSaleInfo(paid);

		assertEquals(paid, saleDTO.amountPaid(),
				"Fail to read amount paid");
		assertEquals(new Amount("12.50"), saleDTO.totalPrice(),
				"Failed to calculate total price");
		assertEquals(new Amount("7.50"), saleDTO.change(),
				"Failed to calculate change");
	}

	@Test
	public void testGetReceiptInfo() {
		ItemDTO item = new ItemDTO("item1", "Test Item 1", new Amount("10.00"), new Amount("0.25"), "1L milk");
		saleInstance.addBoughtItem(item);
		SaleDTO saleDTO = saleInstance.getSaleInfo(new Amount("20.00"));

		ReceiptDTO receipt = saleInstance.getReceiptInfo(saleDTO);

		assertNotNull(receipt,
				"Failed to generate receipt");
		assertEquals(saleDTO, receipt.sale(),
				"Failed to transfer saleDTO correctly to receipt");
	}
}
