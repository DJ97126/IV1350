package controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dto.SaleInfoDTO;
import integration.ItemNotFoundException;
import model.Amount;

public class ControllerTest {
	private Controller controller;

	private ByteArrayOutputStream printoutBuffer;
	private PrintStream originalSysOut;

	@BeforeEach
	public void setUp() {
		controller = new Controller();

		printoutBuffer = new ByteArrayOutputStream();
		PrintStream inMemSysOut = new PrintStream(printoutBuffer);
		originalSysOut = System.out;
		System.setOut(inMemSysOut);
	}

	@AfterEach
	public void tearDown() {
		controller = null;

		printoutBuffer = null;
		System.setOut(originalSysOut);
	}

	@Test
	public void testEmptySale() {
		controller.startSale();
		Amount totalPrice = controller.endSale();

		assertTrue(totalPrice.isZero(), "Total price should be 0.00 SEK for an empty sale.");
	}

	@Test
	public void testEnterValidItem() throws ItemNotFoundException {
		controller.startSale();
		String itemId = "abc123";

		SaleInfoDTO saleInfo = controller.enterItem(itemId);

		assertNotNull(saleInfo, "SaleInfoDTO should not be null after entering a valid item.");
		assertNotNull(saleInfo.currentItem(), "Current item in SaleInfoDTO should not be null.");
	}

	@Test
	public void testEnterInvalidItem() {
		controller.startSale();
		String invalidItemId = "nonExistentItem";

		Exception exception = assertThrows(ItemNotFoundException.class, () -> {
			controller.enterItem(invalidItemId);
		});

		assertTrue(exception.getMessage().contains(invalidItemId),
				"Exception message should mention the invalid item ID.");
	}

	@Test
	public void testDatabaseFailureException() {
		controller.startSale();
		String failItemId = "fail114514";

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			controller.enterItem(failItemId);
		});

		assertTrue(exception.getMessage().contains("inventory database error"),
				"Exception message should mention inventory database error.");
		assertNotNull(exception.getCause(), "The cause should not be null.");
		assertEquals("Database server is not running", exception.getCause().getMessage(),
				"The cause message should match the DatabaseFailureException message.");
	}

	@Test
	public void testFinalizeSaleWithPayment() throws ItemNotFoundException {
		controller.startSale();

		controller.enterItem("abc123");
		controller.enterItem("abc123");
		controller.enterItem("def456");

		Amount totalPrice = controller.endSale();
		Amount expectedTotalPrice = new Amount("74.70");

		Amount change = controller.finalizeSaleWithPayment(new Amount("100"));
		Amount expectedChange = new Amount("25.30");

		assertEquals(0, totalPrice.compareTo(expectedTotalPrice), "Total Price should be 74.70 SEK.");
		assertEquals(0, change.compareTo(expectedChange), "Change should be 25.30 SEK.");
	}
}
