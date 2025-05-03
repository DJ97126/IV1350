package integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;

import static org.junit.jupiter.api.Assertions.*;

public class PrinterTest {
	private Printer printer;

	private ByteArrayOutputStream printoutBuffer;
	private PrintStream originalSysOut;

	@BeforeEach
	public void setUp() {
		printer = new Printer();

		printoutBuffer = new ByteArrayOutputStream();
		PrintStream inMemSysOut = new PrintStream(printoutBuffer);
		originalSysOut = System.out;
		System.setOut(inMemSysOut);
	}

	@AfterEach
	public void tearDown() {
		printer = null;

		printoutBuffer = null;
		System.setOut(originalSysOut);
	}

	@Test
	public void testPrinterPrint() {
		List<ItemDTO> boughtItems = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			boughtItems.add(new ItemDTO("test1", "test1",
					new BigDecimal("12"), new BigDecimal("0.456"),
					"testDesc"));
		}
		boughtItems.add(new ItemDTO("test2", "test kinda long name item",
				new BigDecimal("4567"), new BigDecimal("0.123"),
				"testDesc2"));

		LocalDateTime saleDateTime = LocalDateTime.parse("2024-02-12T16:05");
		BigDecimal totalPrice = new BigDecimal("74.7");
		BigDecimal totalVat = new BigDecimal("4.23");
		BigDecimal amountPaid = new BigDecimal("100.0");
		BigDecimal change = new BigDecimal("25.3");
		SaleDTO saleInfo = new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amountPaid, change);
		ReceiptDTO receipt = new ReceiptDTO(saleInfo);

		printer.printReceipt(receipt);
		
		String output = printoutBuffer.toString();
		assertTrue(output.contains("Begin receipt"),
				"Faild to print the start of receipt.");
		assertTrue(output.contains("test kinda long nam...    1 x 5128:74    5128:74 SEK"),
				"Failed to print bought items.");
		assertTrue(output.contains("Change:                                    25:30 SEK"),
				"Failed to print total price.");
		assertTrue(output.contains("End receipt"),
				"Failed to print the end of receipt.");
	}
}