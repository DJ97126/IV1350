package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter);
	}
}