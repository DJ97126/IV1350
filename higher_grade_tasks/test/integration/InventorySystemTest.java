package integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dto.ItemDTO;
import model.Amount;

public class InventorySystemTest {
	private InventorySystem inventorySystem;

	private ByteArrayOutputStream printoutBuffer;
	private PrintStream originalSysOut;

	@BeforeEach
	public void setUp() {
		inventorySystem = new InventorySystem();

		printoutBuffer = new ByteArrayOutputStream();
		PrintStream inMemSysOut = new PrintStream(printoutBuffer);
		originalSysOut = System.out;
		System.setOut(inMemSysOut);
	}

	@AfterEach
	public void tearDown() {
		inventorySystem = null;

		printoutBuffer = null;
		System.setOut(originalSysOut);
	}

	@Test
	public void testInventorySystemRetrieveItem() throws DatabaseFailureException, ItemNotFoundException {
		Amount vatAmount = new Amount("0.06");
		Amount item1OriginalPrice = calculateOriginalPrice(new Amount("29.9"), vatAmount);
		Amount item2OriginalPrice = calculateOriginalPrice(new Amount("14.9"), vatAmount);

		ItemDTO expectedItem1 = new ItemDTO(
				"abc123",
				"BigWheel Oatmeal",
				item1OriginalPrice,
				vatAmount,
				"BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free");
		ItemDTO expectedItem2 = new ItemDTO(
				"def456",
				"YouGoGo Blueberry",
				item2OriginalPrice,
				vatAmount,
				"YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour");

		String itemId1 = "abc123";
		String itemId2 = "def456";

		ItemDTO item1 = inventorySystem.retrieveItem(itemId1);
		ItemDTO item2 = inventorySystem.retrieveItem(itemId2);

		assertNotNull(item1, "Item 1 should not be null.");
		assertEquals(expectedItem1, item1, "Item 1 does not match the expected value.");

		assertNotNull(item2, "Item 2 should not be null.");
		assertEquals(expectedItem2, item2, "Item 2 does not match the expected value.");
	}

	private Amount calculateOriginalPrice(Amount fullPrice, Amount vatRate) {
		Amount divisor = vatRate.add(new Amount("1"));
		return fullPrice.divide(divisor);
	}

	@Test
	void testInventorySystemExceptionDatabaseFailure() {
		Exception exception = assertThrows(DatabaseFailureException.class, () -> {
			inventorySystem.retrieveItem("fail114514");
		});
		assertTrue(exception.getMessage().contains("Database server is not running"));
	}

	@Test
	void testInventorySystemThrowsItemNotFoundException() {
		String invalidItemId = "nonexistent123";
		Exception exception = assertThrows(ItemNotFoundException.class, () -> {
			inventorySystem.retrieveItem(invalidItemId);
		});
		assertTrue(exception.getMessage().contains(invalidItemId));
	}
}
