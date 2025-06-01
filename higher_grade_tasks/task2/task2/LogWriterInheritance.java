package task2;
import java.io.PrintWriter;

public class LogWriterInheritance extends PrintWriter {

    public LogWriterInheritance() {
        super(System.out, true);
    }

    @Override
    public void println(String x) {
        super.println("[LOG]: " + x);
    }
}