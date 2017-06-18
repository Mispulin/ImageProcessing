/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class Counter {

    private long pixelsProcesses = 0;

    public synchronized void addUpPixels() {
        pixelsProcesses++;
    }

    public long getPixelsProcesses() {
        return pixelsProcesses;
    }

    public void resetCounter() {
        pixelsProcesses = 0;
    }

}