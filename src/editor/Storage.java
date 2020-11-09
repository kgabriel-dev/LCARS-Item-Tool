package editor;

import main.Type;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class Storage {
    private static Type type;
    private static Color color, clickedColor;
    private static String text;
    private static int orientation; //1 = normal, 2 = 90°, 3 = 180°, 4 = 270°
    private static HashMap<String, Integer> measurements;
    private static File location;

    public static File getLocation() {
        return location;
    }

    public static void setLocation(File location) {
        Storage.location = location;
    }

    public static HashMap<String, Integer> getMeasurements() {
        return measurements;
    }

    public static void setMeasurements(HashMap<String, Integer> measurements) {
        Storage.measurements = measurements;
    }

    public static String getText() {
        return text;
    }

    public static void setText(String text) {
        Storage.text = text;
    }

    public static Type getType() {
        return type;
    }

    public static void setType(Type type) {
        Storage.type = type;
    }

    public static Color getColor() {
        return color;
    }

    public static void setColor(Color color) {
        Storage.color = color;
    }

    public static int getOrientation() {
        return orientation;
    }

    public static void setOrientation(int orientation) {
        Storage.orientation = orientation;
    }

    public static Color getClickedColor() {
        return clickedColor;
    }

    public static void setClickedColor(Color clickedColor) {
        Storage.clickedColor = clickedColor;
    }
}
