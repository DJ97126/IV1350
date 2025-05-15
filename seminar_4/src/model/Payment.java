package model;

/**
 * This serves as the payment class that stores the amount of money paid by the customer.
 */
public class Payment {
	private final Register register;
	private Amount amount;

	/**
	 * Constructor for the Payment class. Initializes the amount to 0.
	 */
	public Payment() {
		this.register = new Register();
		this.amount = new Amount();
	}

	/**
	 * Sets the amount paid by the customer and updates the register accordingly.
	 * 
	 * @param amount The amount paid by the customer.
	 */
	public void setAmount(Amount amount) {
		this.amount = amount;
		register.updateAmount(this.amount);
	}
}
