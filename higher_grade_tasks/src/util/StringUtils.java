package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * This class contains utility methods for string formatting.
 */
public class StringUtils {
	/**
	 * Formats a BigDecimal to a string with two decimal places, replacing the decimal point with a colon.
	 * 
	 * @param number The decimal number to format.
	 * @return The formatted string with a colon instead of a decimal point.
	 */
	public static String formatBigDecimalToColon(BigDecimal number) {
		DecimalFormat formatter = getStandardizedDecimalFormatter();
		return formatter.format(number.setScale(2, RoundingMode.HALF_UP));
	}

	private static DecimalFormat getStandardizedDecimalFormatter() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		symbols.setDecimalSeparator(':');
		DecimalFormat formatter = new DecimalFormat("0.00", symbols);
		return formatter;
	}
}
