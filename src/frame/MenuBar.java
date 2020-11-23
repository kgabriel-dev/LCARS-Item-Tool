package frame;

import config.Config;
import main.Main;
import update.Update;

import javax.swing.*;
import java.awt.*;
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
        add(createCreditsItem());
    }

    private JMenu createLanguageItem() {
        JMenu menu = new JMenu(config.getLanguageWord("language"));

        File langDir = new File("resources/language/");
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

        //Das Item zum Togglen für die Updatesuche hinzufügen
        JCheckBoxMenuItem toggleUpdateSearch = new JCheckBoxMenuItem(config.getLanguageWord("toggleUpdateSearch"));
        boolean currState = Boolean.valueOf(config.getProp("checkUpdates"));
        toggleUpdateSearch.setState(currState);
        toggleUpdateSearch.addActionListener(e -> {
            boolean oldState = Boolean.valueOf(config.getProp("checkUpdates"));
            boolean newState = !oldState;

            toggleUpdateSearch.setState(newState);
            config.setProp("checkUpdates", "" + newState);
        });
        menu.add(toggleUpdateSearch);

        return menu;
    }

    private JMenu createCreditsItem() {
        JMenu menu = new JMenu(config.getLanguageWord("credits"));

        //Das Item zum Öffnen der Credits.txt erzeugen
        JMenuItem openCredits = new JMenuItem(config.getLanguageWord("showCredits"));

        openCredits.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File("resources/credits.txt"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        menu.add(openCredits);

        return menu;
    }

}
