package controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.*;
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
		controller.startSale(); // Indirectly also tests the startSale method.
		BigDecimal totalPrice = controller.endSale();

		assertTrue(totalPrice.compareTo(BigDecimal.ZERO) == 0,
				String.format("Total price should be 0.00 SEK for an empty sale. Currently: %.2f%n", totalPrice));
	}
}
