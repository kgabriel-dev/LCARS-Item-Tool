package main.java.main;

import main.java.config.Config;
import main.java.frame.Frame;
import main.java.update.Update;

import javax.swing.*;

public class Main {
    public static Frame frame;
    public static Config config;

    public static void main(String[] args) {
        config = new Config();

        setLookAndFeel();

        frame = new Frame();
        frame.setVisible(true);

        //check for updates in a new thread
        if (Boolean.parseBoolean(config.getProp("checkUpdates"))) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Update.checkForUpdates(false);
                }
            }).start();
        }
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(config.getProp("theme"));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

}
