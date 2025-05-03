package integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.*;

import dto.ItemDTO;

import static org.junit.jupiter.api.Assertions.*;

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
	public void testInventorySystemRetrieveItem() {
		/* Same as from inventory */
		BigDecimal item1OriginalPrice = calculateOriginalPrice(new BigDecimal("29.9"), new BigDecimal("0.06"));
		BigDecimal item2OriginalPrice = calculateOriginalPrice(new BigDecimal("14.9"), new BigDecimal("0.06"));

		ItemDTO expectedItem1 = new ItemDTO("abc123", "BigWheel Oatmeal", item1OriginalPrice,
				BigDecimal.valueOf(0.06d),
				"BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free");
		ItemDTO expectedItem2 = new ItemDTO("def456", "YouGoGo Blueberry", item2OriginalPrice,
				BigDecimal.valueOf(0.06d),
				"YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour");

		/* The actual test part */
		String itemId1 = "abc123";
		String itemId2 = "def456";

		ItemDTO item1 = inventorySystem.retrieveItem(itemId1);
		ItemDTO item2 = inventorySystem.retrieveItem(itemId2);

		assertNotNull(item1, "Item 1 should not be null.");
		assertEquals(expectedItem1, item1, "Item 1 does not match the expected value.");

		assertNotNull(item2, "Item 2 should not be null.");
		assertEquals(expectedItem2, item2, "Item 2 does not match the expected value.");
	}

	private BigDecimal calculateOriginalPrice(BigDecimal fullPrice, BigDecimal vatRate) {
		return fullPrice.divide(vatRate.add(BigDecimal.ONE), MathContext.DECIMAL128);
	}
}
