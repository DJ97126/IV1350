package startup;

import controller.Controller;
import view.TotalRevenueFileOutput;
import view.TotalRevenueView;
import view.View;

/**
 * This class serves as the entry point for the application.
 */
public class Main {
	/**
	 * The main method that starts the application.
	 *
	 * @param args Command-line arguments (not used).
	 */
	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.registerObserver(new TotalRevenueView());
		controller.registerObserver(new TotalRevenueFileOutput());

		View view = new View(controller);
		view.simulateExecution();
	}
}
