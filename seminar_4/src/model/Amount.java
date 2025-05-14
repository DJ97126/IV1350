package model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import util.StringUtils;

/**
 * Represents an amount based on BigDecimal.
 */
public class Amount {
	private final BigDecimal amount;

	/**
	 * Creates a new instance, representing the amount 0.
	 */
	public Amount() {
		this(BigDecimal.ZERO);
	}

	/**
	 * Creates a new instance, representing the specified amount.
	 *
	 * @param amount The amount represented by the newly created instance.
	 */
	public Amount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Creates a new instance, representing the specified amount as a string.
	 *
	 * @param amount The amount represented by the newly created instance, as a string.
	 */
	public Amount(String amount) {
		this(new BigDecimal(amount));
	}

	/**
	 * Adds the specified Amount to this object and returns an Amount instance with the result.
	 *
	 * @param other The Amount to add.
	 * @return The result of the addition.
	 */
	public Amount add(Amount other) {
		return new Amount(this.amount.add(other.amount));
	}

	/**
	 * Subtracts the specified Amount to this object and returns an Amount instance with the result.
	 *
	 * @param other The Amount to subtract.
	 * @return The result of the subtraction.
	 */
	public Amount subtract(Amount other) {
		return new Amount(this.amount.subtract(other.amount));
	}

	/**
	 * Multiplies this Amount by the specified Amount and returns a new Amount instance with the result.
	 *
	 * @param multiplier The Amount to multiply with.
	 * @return The result of the multiplication.
	 */
	public Amount multiply(Amount other) {
		return new Amount(this.amount.multiply(other.amount));
	}

	/**
	 * Divides this Amount by the specified Amount and returns a new Amount instance with the result.
	 *
	 * @param divisor The Amount to divide by.
	 * @return The result of the division.
	 */
	public Amount divide(Amount other) {
		return new Amount(this.amount.divide(other.amount, MathContext.DECIMAL128));
	}

	/**
	 * Returns a new Amount instance with the value rounded to two decimal places.
	 *
	 * @return A new Amount rounded to two decimal places.
	 */
	public Amount rounded() {
		return new Amount(this.amount.setScale(2, RoundingMode.HALF_UP));
	}

	/**
	 * Returns a string representation of this Amount with two decimal places, using a colon as the decimal separator.
	 *
	 * @return The formatted string with a colon as the decimal separator.
	 */
	public String colonized() {
		return StringUtils.formatBigDecimalToColon(this.amount);
	}

	/**
	 * Compares this Amount with the specified Amount.
	 *
	 * @param other The Amount to compare with.
	 * @return A negative integer, zero, or a positive integer as this Amount is less than, equal to, or greater than
	 *         the specified Amount.
	 */
	public int compareTo(Amount other) {
		return this.amount.compareTo(other.amount);
	}

	/**
	 * Checks if this Amount is zero.
	 *
	 * @return true if this Amount is zero, false otherwise.
	 */
	public boolean isZero() {
		return this.amount.compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * Checks if this Amount is negative.
	 *
	 * @return true if this Amount is less than zero, false otherwise.
	 */
	public boolean isNegative() {
		return this.amount.compareTo(BigDecimal.ZERO) < 0;
	}

	/**
	 * Checks if this Amount is positive.
	 *
	 * @return true if this Amount is greater than zero, false otherwise.
	 */
	public boolean isPositive() {
		return this.amount.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Two Amounts are equal if they represent the same amount.
	 *
	 * @param other The Amount to compare with this amount.
	 * @return true if the specified amount is equal to this amount, false if it is not.
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Amount)) {
			return false;
		}

		Amount otherAmount = (Amount) other;
		return amount.compareTo(otherAmount.amount) == 0;
	}

	@Override
	public int hashCode() {
		return amount.stripTrailingZeros().hashCode();
	}

	@Override
	public String toString() {
		return amount.toString();
	}
}
