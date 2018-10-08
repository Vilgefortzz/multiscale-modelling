package vilgefortzz.edu.grain_growth.grid;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class ColorGenerator {

    private static Map<Integer, Color> mapWithColors = new HashMap<>();

    public static Color getColor(int value) {

        Color color = mapWithColors.get(value);

        if (color == null) {
            color = randomColor();
            setColor(value, color);
        }

        return color;
    }

    public static void setColor(int value, Color color) {
        mapWithColors.put(value, color);
    }

    private static Color randomColor(){
        return Color.color(Math.random(), Math.random(), Math.random());
    }
}
