import java.awt.*;

/**
 * Created by Mish.k.a on 18. 6. 2017.
 */
public class Filter {

    public enum FilterEnum {
        SHADES_OF_GREY, BLACK_AND_WHITE, BRIGHTER, DARKER, BRIGHTER_SHADES_OF_GREY, NEGATIVE, CONTRAST,
    }

    private static int CONTRAST_POWER = 30;

    public static Color applyFilter(FilterEnum filter, Color oldColor) {
        switch (filter) {
            case SHADES_OF_GREY:
                return getGrey(oldColor);
            case BLACK_AND_WHITE:
                return getBlackAndWhite(oldColor);
            case BRIGHTER:
                return oldColor.brighter();
            case DARKER:
                return oldColor.darker();
            case BRIGHTER_SHADES_OF_GREY:
                return getGrey(oldColor.brighter());
            case NEGATIVE:
                return getNegative(oldColor);
            case CONTRAST:
                return getContrast(oldColor);
            default:
                return oldColor;
        }
    }

    private static Color getGrey(Color color) {
        double r = 0.15;
        double g = 0.6;
        double b = 0.25;

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int grey = (int) ((red * r + green * g + blue * b));

        return new Color(grey, grey, grey);
    }

    private static Color getBlackAndWhite(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int combined = (red + green + blue) / 3;

        if (combined > 127) return Color.white;
        return Color.black;
    }

    private static Color getNegative(Color color) {
        int red = 255 - color.getRed();
        int green = 255 - color.getGreen();
        int blue = 255 - color.getBlue();

        return new Color(red, green, blue);
    }

    public static Color getContrast(Color color) {
        int red = color.getRed() - CONTRAST_POWER;
        int green = color.getGreen() - CONTRAST_POWER;
        int blue = color.getBlue() - CONTRAST_POWER;

        return new Color(optimize(red), optimize(green), optimize(blue));
    }

    public static int optimize(int channel) {
        if (channel > 255) return 255;
        if (channel < 0) return 0;
        return channel;
    }

}
