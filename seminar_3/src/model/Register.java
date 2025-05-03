package model;

import java.math.BigDecimal;

/**
 * This serves as the register class that stores the amount of money in the register.
 */
public class Register {
	private BigDecimal amount;

	/**
	 * Constructor for the Register class.
	 */
	public Register() {
		// Pretend we have a register with 0 kr in it.
		this.amount = BigDecimal.valueOf(0);
	}

	/**
	 * Updates the amount in the register.
	 * 
	 * @param amount The amount in the register.
	 */
	public void updateAmount(BigDecimal amount) {
		this.amount = this.amount.add(amount);
	}
}
