import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class Image {
    public static final char DIRECTORY_SEPARATOR = '\\';

    private final String fileName;
    private final BufferedImage image;

    public Image(Image image) {
        this.fileName = image.getFileName();
        this.image = image.getImage();
    }

    private Image(String fileName, BufferedImage image) {
        this.fileName = fileName;
        this.image = image;
    }

    public static Image read(String fileName) {
        BufferedImage img;
        Image ret;

        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read image from file: " + fileName, ex);
        }

        ret = new Image(fileName, img);

        return ret;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getFileName() {
        int i = this.fileName.lastIndexOf(DIRECTORY_SEPARATOR);
        String ret = this.fileName.substring(i + 1);
        return ret;
    }

    public String getExtension() {
        String ret = Image.getExtension(fileName);
        return ret;
    }

    private static String getExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        String ret = fileName.substring(i + 1);
        return ret;
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public Color getPixel(int x, int y) {
        if (x < 0 || x > this.getWidth() - 1) {
            throw new IndexOutOfBoundsException("X axis value is out of range.");
        }
        if (y < 0 || y > this.getHeight() - 1) {
            throw new IndexOutOfBoundsException("Y axis value is out of range.");
        }

        int rgb = this.image.getRGB(x, y);
        Color ret = new Color(rgb);
        return ret;
    }

    public void setPixel(int x, int y, Color c) {
        if (x < 0 || x > this.getWidth() - 1) {
            throw new IndexOutOfBoundsException("X axis value is out of range.");
        }
        if (y < 0 || y > this.getHeight() - 1) {
            throw new IndexOutOfBoundsException("Y axis value is out of range.");
        }

        int rgb = c.getRGB();
        this.image.setRGB(x, y, rgb);
    }

}
