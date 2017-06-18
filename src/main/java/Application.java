import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {

    public static int threadCount = 5;
    private static final String imagesFolder = "M:\\Documents\\OSU\\Mgr\\2 LS\\VVPAP\\ImageProcessing\\src\\main\\resources\\input";
    private static final String outputFolder = "M:\\Documents\\OSU\\Mgr\\2 LS\\VVPAP\\ImageProcessing\\src\\main\\resources\\output";
    private static long totalTimeParallel = 0;
    private static long totalTimeSerial = 0;
    private static Counter counter = new Counter();
    private static PrintBuffer printBuffer = new PrintBuffer(threadCount);
    private static final Filter.FilterEnum filter = Filter.FilterEnum.CONTRAST;

    public static void main(String[] args) throws InterruptedException {
        initInfo();

        clearFolder(outputFolder);

        // parallelProcessing();

        runBothProcessings();
    }

    private static void runBothProcessings() throws InterruptedException {
        parallelProcessing();

        clearFolder(outputFolder);

        counter.resetCounter();

        serialProcessing();

        compareTimes();
    }

    private static void parallelProcessing() throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println("Parallel processing has started.");
        System.out.println("----------------------------");

        File[] imageFiles = getFiles(imagesFolder);
        int blockLength = imageFiles.length / threadCount;

        List<ImageProcessingThread> threads = new ArrayList<>(threadCount);
        WhileProcessingThread whileProcessingThread = new WhileProcessingThread();
        printBuffer.start();
        whileProcessingThread.start();

        for (int i = 0; i < threadCount; i++) {
            int startPos = i * blockLength;
            int actualBlockLength = (i == threadCount - 1) ? (imageFiles.length - startPos) : blockLength;

            File[] threadFiles = new File[actualBlockLength];
            System.arraycopy(imageFiles, startPos, threadFiles, 0, actualBlockLength);

            ImageProcessingThread imageProcessThread = new ImageProcessingThread(threadFiles);
            threads.add(imageProcessThread);
            imageProcessThread.start();
        }

        for (ImageProcessingThread thread : threads ) {
            thread.join();
        }
        whileProcessingThread.interrupt();
        whileProcessingThread.join();

        printBuffer.printAndErase();

        System.out.println("----------------------------");
        System.out.println("Parallel processing has finished.");
        long end = System.currentTimeMillis();
        totalTimeParallel = end - start;
        System.out.println("Total pixels: " + counter.getPixelsProcesses());
        System.out.println("Total time: " + totalTimeParallel);
        System.out.println();
    }

    private static void serialProcessing() throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println("Serial processing has started.");
        System.out.println("----------------------------");
        WhileProcessingThread whileProcessingThread = new WhileProcessingThread();
        whileProcessingThread.start();

        File[] imageFiles = getFiles(imagesFolder);

        for (File file : imageFiles) {
            Image image = Image.read(file.getAbsolutePath());
            for (int i = 0; i < image.getHeight(); i++) {
                for (int j = 0; j < image.getWidth(); j++) {
                    Color oldColor = image.getPixel(j, i);
                    Color newColor = Filter.applyFilter(filter, oldColor);
                    image.setPixel(j, i, newColor);
                    counter.addUpPixels();
                }
            }
            saveImageToFile(image);
            System.out.println("Image " + image.getFileName() + " done.");
        }

        whileProcessingThread.interrupt();
        whileProcessingThread.join();
        System.out.println("----------------------------");
        System.out.println("Serial processing has finished.");
        long end = System.currentTimeMillis();
        totalTimeSerial = end - start;
        System.out.println("Total pixels: " + counter.getPixelsProcesses());
        System.out.println("Total time: " + totalTimeSerial);
    }

    private static File[] getFiles(String imagesFolderName) {
        File folder = new File(imagesFolderName);
        return folder.listFiles();
    }

    private static void clearFolder(String folderPath) {
        File[] files = getFiles(folderPath);
        for (File file : files) {
            file.delete();
        }
    }

    public static void saveImageToFile(Image image) {
        File output = new File(outputFolder + "\\" + image.getFileName());
        try {
            ImageIO.write(image.getImage(), image.getExtension(), output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void compareTimes() {
        System.out.println();
        long diff = Math.abs(totalTimeParallel - totalTimeSerial);
        if (totalTimeSerial > totalTimeParallel) System.out.println("Parallel processing was faster by " + diff + ".");
        if (totalTimeSerial < totalTimeParallel) System.out.println("Serial processing was faster " + diff + ".");
    }

    private static void initInfo() {
        System.out.println("Image count: " + getFiles(imagesFolder).length);
        System.out.println("Thread count: " + threadCount);
        System.out.println();
    }

    public static Counter getCounter() {
        return counter;
    }

    public static PrintBuffer getPrintBuffer() {
        return printBuffer;
    }

    public static Filter.FilterEnum getFilter() {
        return filter;
    }
}
