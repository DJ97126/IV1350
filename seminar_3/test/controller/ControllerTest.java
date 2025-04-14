package controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private ByteArrayOutputStream printoutBuffer;
    private PrintStream originalSysOut;

    @BeforeEach
    public void setUp() {
        printoutBuffer = new ByteArrayOutputStream();
        PrintStream inMemSysOut = new PrintStream(printoutBuffer);
        originalSysOut = System.out;
        System.setOut(inMemSysOut);
    }

    @AfterEach
    public void tearDown() {
        printoutBuffer = null;
        System.setOut(originalSysOut);
    }

    @Test
    public void toBeImplemented() {
        assertTrue(true, "This test is not implemented yet.");
    }
}
