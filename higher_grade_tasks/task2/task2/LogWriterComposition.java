package task2;
import java.io.PrintWriter;

public class LogWriterComposition {
    private PrintWriter writer;

    public LogWriterComposition() {
        this.writer = new PrintWriter(System.out, true);
    }

    public void log(String message) {
        writer.println("[LOG]: " + message);
    }
}