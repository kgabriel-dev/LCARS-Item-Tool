package main;

import config.Config;
import frame.Frame;
import update.Update;

import javax.swing.*;

public class Main {
    public static Frame frame;
    public static Config config;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        config = new Config();

        frame = new Frame();
        frame.setVisible(true);

        //Auf Updates in einem neuen Thread prüfen
        new Thread(new Runnable() {
            @Override
            public void run() {
                Update.checkForUpdates(false);
            }
        }).start();
    }

}
