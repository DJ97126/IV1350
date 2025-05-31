package view;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import controller.Controller;

public class ViewTest {
    private View view;
    private Controller controller;
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;

    @BeforeEach
    public void setUp() {
        controller = new Controller();
        view = new View(controller);

        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        view = null;
        controller = null;
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void testNonExistentItem() {
        view.simulateEnterInvalidItem(); // This will call tryEnterItem with "nonExistentItem"
        String output = printoutBuffer.toString();

        // Verify error message for non-existent item
        assertTrue(output.contains("Item with ID nonExistentItem not found"), 
            "Output should contain error message for non-existent item");
    }

    @Test
    public void testDatabaseFailure() {
        view.simulateDatabaseFailure(); // This will call tryEnterItem with "fail114514"
        String output = printoutBuffer.toString();

        // Verify error message for database failure
        assertTrue(output.contains("A system error occurred. Please try again or contact support."), 
            "Output should contain error message for database failure");
    }
}