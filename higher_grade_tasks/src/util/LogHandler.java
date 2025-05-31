package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is responsible for the log.
 */
public class LogHandler {
	private static final String LOG_FILE_NAME = "exceptions.log";
	private static final LogHandler INSTANCE = new LogHandler();
	private PrintWriter logFile;

	/**
	 * Returns the singleton instance of LogHandler.
	 * 
	 * @return The singleton instance of LogHandler.
	 */
	public static LogHandler getLogger() {
		return INSTANCE;
	}

	private LogHandler() {
		try {
			logFile = new PrintWriter(new FileWriter(LOG_FILE_NAME, true), true);
		} catch (IOException e) {
			System.out.println("Could not create exception logger or file.");
			e.printStackTrace();
		}
	}

	/**
	 * Writes a log entry describing a thrown exception.
	 * 
	 * @param exception The exception that shall be logged.
	 */
	public void logException(Exception exception) {
		String logMessage = "%s, Exception was thrown: %s".formatted(createTime(), exception.getMessage());
		logFile.println(logMessage);
		exception.printStackTrace(logFile);
		logFile.println("\n");
	}

	private String createTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter);
	}
}
