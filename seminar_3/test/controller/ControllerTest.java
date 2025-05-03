package controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.*;

import dto.SaleInfoDTO;

import static org.junit.jupiter.api.Assertions.*;

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
		BigDecimal totalPrice = controller.endSale();

		assertTrue(totalPrice.compareTo(BigDecimal.ZERO) == 0, "Total price should be 0.00 SEK for an empty sale.");
	}

	@Test
	public void testEnteringValidItem() {
		controller.startSale();
		String itemId = "abc123";
		SaleInfoDTO saleInfo = controller.enterItem(itemId);

		assertNotNull(saleInfo, "SaleInfoDTO should not be null after entering a valid item.");
		assertNotNull(saleInfo.currentItem(), "Current item in SaleInfoDTO should not be null.");
	}

	@Test
	public void testFinalizeSaleWithPayment() {
		controller.startSale();

		controller.enterItem("abc123");
		controller.enterItem("abc123");
		controller.enterItem("def456");

		BigDecimal totalPrice = controller.endSale();
		BigDecimal expectedTotalPrice = new BigDecimal("74.70").setScale(2);

		BigDecimal change = controller.finalizeSaleWithPayment(new BigDecimal(100));
		BigDecimal expectedChange = new BigDecimal("25.30").setScale(2);

		assertEquals(0, totalPrice.compareTo(expectedTotalPrice), "Total Price should be 74.70 SEK.");
		assertEquals(0, change.compareTo(expectedChange), "Change should be 25.30 SEK.");
	}
}
