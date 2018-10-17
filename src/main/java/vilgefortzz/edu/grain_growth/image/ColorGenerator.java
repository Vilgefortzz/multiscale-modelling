package vilgefortzz.edu.grain_growth.image;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class ColorGenerator {

    public static int type = 0;

    private static Map<Integer, Color> mapWithColors = new HashMap<>();
    private static Map<java.awt.Color, Integer> mapWithStates = new HashMap<>();

    public static Color getColor(int value) {

        Color color = mapWithColors.get(value);

        if (color == null) {
            color = randomColor();
            setColor(value, color);
        }

        return color;
    }

    public static int getState(java.awt.Color color) {

        Integer state = mapWithStates.get(color);

        if (state == null) {
            state = ++type;
            setState(color, state);
        }

        return state;
    }

    public static void setColor(int value, Color color) {
        mapWithColors.put(value, color);
    }

    private static Color randomColor(){
        return Color.color(Math.random(), Math.random(), Math.random());
    }

    public static void setState(java.awt.Color color, int value) {
        mapWithStates.put(color, value);
    }
}
