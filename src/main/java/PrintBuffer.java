import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class PrintBuffer extends Thread {
    private static int bufferSize = 1;
    private List<String> inner = new ArrayList();

    public PrintBuffer(int size) {
        bufferSize = size;
    }

    public synchronized void add(String line) {
        inner.add(line);
        this.notifyAll();
    }

    public synchronized void printAndErase() {
        if (inner.size() > 0) {
            for (String item : inner) System.out.println(item);
            inner.clear();
        }
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            synchronized (this) {
                if (inner.size() >= bufferSize) {
                    printAndErase();
                } else {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

}