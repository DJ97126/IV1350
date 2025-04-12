package view;

import controller.Controller;

/**
 * This class serves as the user interface for the system.
 */
public class View {
    private Controller controller;

    /**
     * Sets up the view with the given controller.
     *
     * @param controller The controller to be used for this view.
     */
    public View(Controller controller) {
        this.controller = controller;
    }

    /**
     * Simulates the execution of the system.
     * This method is a placeholder for actual user interaction.
     */
    public void simulateExecution() {
        controller.startSale();
        System.out.println("A new sale as been started.");
    }
}
