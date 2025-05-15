package integration;

import java.util.ArrayList;

import dto.SaleDTO;

/**
 * The AccountingSystem class is responsible for recording sales information.
 */
public class AccountingSystem {
	private final ArrayList<SaleDTO> recordedSales;

	/**
	 * Constructor for the AccountingSystem class.
	 */
	public AccountingSystem() {
		recordedSales = new ArrayList<>();
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
