package integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        ItemDTO[] boughtItems = new ItemDTO[12];
        for (int i = 0; i < 11; i++) {
            boughtItems[i] = new ItemDTO("test1", "test1",
                    new BigDecimal("12"), new BigDecimal("0.456"),
                    "testDesc");
        }
        boughtItems[11] = new ItemDTO("test2", "test kinda long name item",
                new BigDecimal("4567"), new BigDecimal("0.123"),
                "testDesc2");

        LocalDateTime saleDateTime = LocalDateTime.parse("2024-02-12T16:05");
        BigDecimal totalPrice = new BigDecimal("74.7");
        BigDecimal totalVat = new BigDecimal("4.23");
        BigDecimal amountPaid = new BigDecimal("100.0");
        BigDecimal change = new BigDecimal("25.3");
		SaleDTO saleInfo = new SaleDTO(saleDateTime, boughtItems, totalPrice, totalVat, amountPaid, change);
        ReceiptDTO receipt = new ReceiptDTO(saleInfo);

        printer.printReceipt(receipt);

        String output = printoutBuffer.toString();

        // --- Debugging Step: Print the actual output ---
        // Use originalSysOut because System.out is redirected to printoutBuffer
        originalSysOut.println("--- Actual Printer Output ---");
        originalSysOut.println(output);
        originalSysOut.println("-----------------------------");
        // --- End Debugging Step ---


        assertTrue(output.contains("Begin receipt"),
                "Faild to print the start of receipt.");
        assertTrue(output.contains("test kinda long nam...    1 x 4567:00    4567:00 SEK"), // This is line 63 (approx)
                "Failed to print bought items.");
        assertTrue(output.contains("Change:                                    25:30 SEK"),
                "Failed to print total price."); // Note: The message says "total price", but it checks "Change"
        assertTrue(output.contains("End receipt"),
                "Failed to print the end of receipt.");
    }
}
