package task2;
public class Main {
    public static void main(String[] args) {
        System.out.println("Using inheritance:");
        LogWriterInheritance w1 = new LogWriterInheritance();
        w1.println("System starting...");
        w1.println("User logged in.");

        System.out.println("\nUsing composition:");
        LogWriterComposition w2 = new LogWriterComposition();
        w2.log("System starting...");
        w2.log("User logged in.");
    }
}
