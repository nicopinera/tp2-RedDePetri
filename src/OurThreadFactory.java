import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadFactory;

public class OurThreadFactory implements ThreadFactory {
    private static int counterThreads;
    private ArrayList<String> stats;

    public OurThreadFactory() {
        counterThreads = 0;
        stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(Runnable r) {
        counterThreads++;
        Thread t = new Thread(r, "Thread-" + counterThreads + " para " + r.toString());
        stats.add("hilo " + t.getName() + " Fue creado en el momento: " + new Date());
        return t;
    }

    public ArrayList<String> getStats() {
        return stats;
    }
}
