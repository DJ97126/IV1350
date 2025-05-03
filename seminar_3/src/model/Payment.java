package model;

import java.math.BigDecimal;

/**
 * This serves as the payment class that stores the amount of money paid by the customer.
 */
public class Payment {
	private Register register;
	private BigDecimal amount;

	/**
	 * Constructor for the Payment class. Initializes the amount to 0.
	 */
	public Payment() {
		this.amount = BigDecimal.valueOf(0);
	}

	/**
	 * Sets the amount paid by the customer and updates the register accordingly.
	 * 
	 * @param amount The amount paid by the customer.
	 */
	public void setAmount(BigDecimal amount) {
        this.register = new Register();
		this.amount = amount;
		register.updateAmount(this.amount);
	}
}