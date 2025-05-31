package integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import model.Amount;

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
		ArrayList<ItemDTO> boughtItems = new ArrayList<>();
		for (int i = 0; i < 11; i++) {
			boughtItems.add(new ItemDTO("test1", "test1",
					new Amount("12"), new Amount("0.456"),
					"testDesc"));
		}
		boughtItems.add(new ItemDTO("test2", "test kinda long name item",
				new Amount("4567"), new Amount("0.123"),
				"testDesc2"));

		LocalDateTime saleDateTime = LocalDateTime.parse("2024-02-12T16:05");
		Amount totalPrice = new Amount("74.7");
		Amount totalVat = new Amount("4.23");
		Amount amountPaid = new Amount("100.0");
		Amount change = new Amount("25.3");
		Amount discountedPrice = new Amount("70.97");
		SaleDTO saleInfo = new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amountPaid, change, discountedPrice);
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
