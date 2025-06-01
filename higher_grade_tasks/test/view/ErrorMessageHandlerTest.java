package view;

import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorMessageHandlerTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;
    private ErrorMessageHandler handler;

    @BeforeEach
    public void setUp() {
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
        handler = new ErrorMessageHandler();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalSysOut);
    }

    @Test
    public void testShowErrorMessagePrintsToSystemOut() {
        handler.showErrorMessage("Test error message");

        String output = printoutBuffer.toString().trim();
        // Check the output contains the error message text
        assertTrue(output.contains("ERROR: Test error message"));

        // Optional: you could add a simple check to ensure the output starts with a timestamp (yyyy-MM-dd)
        assertTrue(output.matches("\\d{4}-\\d{2}-\\d{2}.*ERROR: Test error message"));
    }
}
