package integration;

import java.math.BigDecimal;

import org.junit.jupiter.api.*;

import model.SaleDTO;

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
    public void testSaleAccounting() {
        ItemDTO[] items = { new ItemDTO("test1", "test", new BigDecimal("123"), new BigDecimal("0.456"), "testDesc") };
        SaleDTO sale = new SaleDTO(items, new BigDecimal("10"));

        accountingSystem.account(sale);
        assertEquals(1, accountingSystem.recordedSales().size(), "Should contain 1 recorded sale.\n");
        assertEquals(sale, accountingSystem.recordedSales().get(0), "Stored sale should match input.");
    }
}
