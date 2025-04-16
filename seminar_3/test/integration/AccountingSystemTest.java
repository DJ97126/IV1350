package integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountingSystemTest {
    private AccountingSystem accountingSystem;

    @BeforeEach
    public void setUp() {
        accountingSystem = new AccountingSystem();
    }

    @AfterEach
    public void tearDown() {
        accountingSystem = null;
    }

    @Test
    public void testSaleIsRecorded() {
        ItemDTO[] items = {
            new ItemDTO("abc123", "Test Item", new BigDecimal("29.90"), new BigDecimal("0.06"), "Sample desc")
        };
    }
}
