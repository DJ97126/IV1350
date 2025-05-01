package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtils {
	public StringUtils() {}

	/**
	 * Formats a BigDecimal to a string with two decimal places, replacing the decimal point with a colon.
	 * 
	 * @param number
	 * @return The formatted string with a colon instead of a decimal point.
	 */
	public static String formatBigDecimalToColon(BigDecimal number) {
		return String.format("%.2f", number.setScale(2, RoundingMode.HALF_UP)).replace('.', ':');
	}
}
