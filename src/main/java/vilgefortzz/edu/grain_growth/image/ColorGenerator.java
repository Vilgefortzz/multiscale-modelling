package vilgefortzz.edu.grain_growth.image;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by vilgefortzz on 07/10/18
 */
public class ColorGenerator {

    public static int type = 0;
    private static Random random = new Random();

    private static final Color[] redColors = {
            Color.web("#8c1818"),
            Color.web("#bb2323"),
            Color.web("#bf0d0d"),
            Color.web("#a70404"),
            Color.web("#5d0a0a"),
            Color.web("#350707"),
            Color.web("#c50f0c"),
            Color.web("#ee1c1c"),
            Color.web("#fb3333"),
            Color.web("#ff0a0a")
    };

    private static Map<Integer, Color> mapWithColors = new HashMap<>();
    private static Map<java.awt.Color, Integer> mapWithStates = new HashMap<>();

    public static Color getColor(int value, boolean isRecrystallized) {

        Color color = mapWithColors.get(value);

        if (color == null) {
            if (isRecrystallized) {
                color = randomRecrystallizedColor();
            } else {
                color = randomColor();
            }
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

    private static Color randomRecrystallizedColor(){
        return redColors[random.nextInt(redColors.length - 1)];
    }

    public static void setState(java.awt.Color color, int value) {
        mapWithStates.put(color, value);
    }
}
