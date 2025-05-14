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
	void showErrorMsg(String message) {
		StringBuilder errorMsgBuilder = new StringBuilder();
		errorMsgBuilder.append(createTime());
		errorMsgBuilder.append(", ERROR: ");
		errorMsgBuilder.append(message);
		System.out.println(errorMsgBuilder);
	}

	private String createTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		return now.format(formatter);
	}
}