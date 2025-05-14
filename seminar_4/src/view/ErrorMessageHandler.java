package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * This class is responsible for showing error messages to the user.
 */
class ErrorMessageHandler {
	/**
	 * Displays the specified error message.
	 * 
	 * @param message The error message.
	 */
	void showErrorMessage(String message) {
		String errorMessage = "%s, ERROR: %s".formatted(createTime(), message);
		System.out.println(errorMessage);
	}

	private String createTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		return now.format(formatter);
	}
}