package test.java;

import main.java.editor.Editor;
import main.java.editor.Storage;
import main.java.main.Type;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

class EditorTest {

    @Test
    void editFileTest() {
        //set the type
        Storage.setType(Type.Button_1Corner);

        //configure and set the measurements
        HashMap<String, Integer> measurements = new HashMap<>();
        measurements.put("width", 255);
        measurements.put("height", 63);

        Storage.setMeasurements(measurements);

        //set the colors
        Storage.setBaseColor(new Color(30, 150, 49));
        Storage.setClickedColor(new Color(55, 174, 83));

        //set the orientation
        Storage.setOrientation(1);

        //set the storage path
        Storage.setLocation(new File(System.getProperty("user.home") + "\\test.png"));

        //test the editFile()-method
        Editor.editFile();
    }
}