package frame;

import config.Config;
import main.Main;
import update.Update;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MenuBar extends JMenuBar {
    private Config config;

    public MenuBar() {
        super();

        config = Main.config;

        //Das SprachItem hinzufügen
        add(createLanguageItem());
        add(createInfoItem());
    }

    private JMenu createLanguageItem() {
        JMenu menu = new JMenu(config.getLanguageWord("language"));

        File langDir = new File("resources/language");
        for(File langFile : langDir.listFiles()) {
            String fileName = langFile.getName().replace(".txt", "");
            String langName;
            Properties langProps;

            try {
                FileReader reader = new FileReader(langFile);
                langProps = new Properties();
                langProps.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            langName = langProps.getProperty("name");

            JMenuItem item = new JMenuItem(langName + " (" + fileName + ")");
            item.addActionListener(e -> config.setProp("language", fileName));

            menu.add(item);
        }

        return menu;
    }

    private JMenu createInfoItem() {
        JMenu menu = new JMenu(config.getLanguageWord("info"));

        //Das Item für die Versionsanzeige erstellen
        JMenuItem version = new JMenuItem(config.getLanguageWord("currentVersion") + ": " + Update.getProgramVersion());
        version.setEnabled(false);
        menu.add(version);
        menu.addSeparator();

        //Das Item für die automatische Update-Suche erstellen
        JMenuItem autoUpdate = new JMenuItem(config.getLanguageWord("autoUpdate"));
        autoUpdate.addActionListener(e -> new Thread(() -> Update.checkForUpdates(true)
        ).start());
        menu.add(autoUpdate);

        //Das Item für eine manuelle Updatesuche
        JMenuItem userUpdate = new JMenuItem(config.getLanguageWord("userUpdate"));
        userUpdate.addActionListener(e -> {
            try {
                Update.openBrowser("https://sourceforge.net/projects/lcars-item-tool/");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        menu.add(userUpdate);

        return menu;
    }

}
