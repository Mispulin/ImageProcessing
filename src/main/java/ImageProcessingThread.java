import java.awt.*;
import java.io.File;

/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class ImageProcessingThread extends Thread {

    private File[] files;

    public ImageProcessingThread(File[] files) {
        this.files = files;
    }

    @Override
    public void run() {
        for (File file : files) {
            try {
                processImage(Image.read(file.getAbsolutePath()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processImage(Image image) throws InterruptedException {
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color oldColor = image.getPixel(j, i);
                Color newColor = Filter.applyFilter(Application.getFilter(), oldColor);
                image.setPixel(j, i, newColor);
                Application.getCounter().addUpPixels();
            }
        }

        Application.saveImageToFile(image);
        Application.getPrintBuffer().add( "Image " + image.getFileName() + " done, thread " + getId() + ".");
    }

}
