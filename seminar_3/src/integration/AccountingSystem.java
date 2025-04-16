package integration;

import java.util.ArrayList;
import java.util.List;

import model.SaleDTO;

public class AccountingSystem {
    private final List<SaleDTO> recordedSales;

    /**
     * Constructor for the AccountingSystem class.
     */
    public AccountingSystem() {
        recordedSales = new ArrayList<SaleDTO>();
    }

    /**
     * Records the sale information in the accounting system.
     * 
     * @param saleDTO The sale information.
     */
    public void account(SaleDTO saleDTO) {
        recordedSales.add(saleDTO);
    }

    // Package-private method for testing purposes.
    List<SaleDTO> recordedSales() {
        return recordedSales;
    }
}
