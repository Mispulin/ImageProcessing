/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class WhileProcessingThread extends Thread {

    @Override
    public void run() {
        while (this.isInterrupted() == false) {
            try {
                Thread.sleep(250);
                System.out.println("...");
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
