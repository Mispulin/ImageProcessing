/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class WhileProcessingThread extends Thread {

    /*
    * This is a thread to use interrupt() on.
    * This thread keeps printing while there are images being processed by other threads.
    * When images are finished processing, this thread is interrupted.
    * */

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
