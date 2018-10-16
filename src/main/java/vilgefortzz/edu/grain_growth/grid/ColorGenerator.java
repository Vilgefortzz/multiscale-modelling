package vilgefortzz.edu.grain_growth.grid;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class ColorGenerator {

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

            if (color.getRGB() == java.awt.Color.WHITE.getRGB()) {
                state = 0;
            } else {
                state = randomState();
            }

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

    private static int randomState() {
        return new Random().nextInt(1000) + 1;
    }

    private static void setState(java.awt.Color color, int value) {
        mapWithStates.put(color, value);
    }
}
