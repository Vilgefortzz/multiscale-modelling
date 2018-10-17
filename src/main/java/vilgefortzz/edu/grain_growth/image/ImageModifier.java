package vilgefortzz.edu.grain_growth.image;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by vilgefortzz on 17/10/18
 */
public class ImageModifier {

    public static BufferedImage resize(BufferedImage image, int width, int height) {

        Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }
}
