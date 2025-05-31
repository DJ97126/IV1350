package model;

/**
 * This serves as the register class that stores the amount of money in the register.
 */
public class Register {
	private Amount amount;

	/**
	 * Constructor for the Register class.
	 */
	public Register() {
		this.amount = new Amount();
	}

	/**
	 * Updates the amount in the register.
	 * 
	 * @param amount The amount in the register.
	 */
	public void updateAmount(Amount amount) {
		this.amount = this.amount.add(amount);
	}
}
