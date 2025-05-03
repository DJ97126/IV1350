package integration;

import java.util.ArrayList;
import java.util.List;

import dto.SaleDTO;

/**
 * The AccountingSystem class is responsible for recording sales information.
 * It stores the sales data in a list of SaleDTO objects.
 * Instructed by Leif, this class can omit testing.
 */
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
}
