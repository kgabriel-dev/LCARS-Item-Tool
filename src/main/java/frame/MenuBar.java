package main.java.frame;

import main.java.config.Config;
import main.java.main.Main;
import main.java.update.Update;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

import javax.swing.UIManager.LookAndFeelInfo;

public class MenuBar extends JMenuBar {
    private final Config config;

    public MenuBar() {
        super();

        config = Main.config;

        //add the language item
        add(createLanguageItem());
        add(createInfoItem());
        add(createCreditsItem());
        add(createThemeItem());
    }

    private JMenu createLanguageItem() {
        JMenu menu = new JMenu(config.getLanguageWord("language"));

        for (String fileString : config.getCSV("languages")) {
            Properties langProps;

            try {
                InputStream langStream = getClass().getResourceAsStream("/language/" + fileString + ".properties");
                langProps = new Properties();
                langProps.load(langStream);
                langStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            String langName = langProps.getProperty("name");

            JMenuItem item = new JMenuItem(langName + " (" + fileString + ")");
            item.addActionListener(e -> config.setProp("language", fileString));

            menu.add(item);
        }

        return menu;
    }

    private JMenu createInfoItem() {
        JMenu menu = new JMenu(config.getLanguageWord("info"));

        //add the item to show the current version
        JMenuItem version = new JMenuItem(config.getLanguageWord("currentVersion") + ": " + Update.getProgramVersion());
        version.setEnabled(false);
        menu.add(version);
        menu.addSeparator();

        //create the icon for the automatic search for updates
        JMenuItem autoUpdate = new JMenuItem(config.getLanguageWord("autoUpdate"));
        autoUpdate.addActionListener(e -> new Thread(() -> Update.checkForUpdates(true)
        ).start());
        menu.add(autoUpdate);

        //the item for the manual search for updates
        JMenuItem userUpdate = new JMenuItem(config.getLanguageWord("userUpdate"));
        userUpdate.addActionListener(e -> {
            try {
                Update.openBrowser("https://sourceforge.net/projects/lcars-item-tool/");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        menu.add(userUpdate);

        //the item to toggle the automatic search for updates
        JCheckBoxMenuItem toggleUpdateSearch = new JCheckBoxMenuItem(config.getLanguageWord("toggleUpdateSearch"));
        boolean currState = Boolean.parseBoolean(config.getProp("checkUpdates"));
        toggleUpdateSearch.setState(currState);
        toggleUpdateSearch.addActionListener(e -> {
            boolean oldState = Boolean.parseBoolean(config.getProp("checkUpdates"));
            boolean newState = !oldState;

            toggleUpdateSearch.setState(newState);
            config.setProp("checkUpdates", "" + newState);
        });
        menu.add(toggleUpdateSearch);

        return menu;
    }

    private JMenu createCreditsItem() {
        JMenu menu = new JMenu(config.getLanguageWord("credits"));

        //create the item to open the credits.txt file
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

    private JMenu createThemeItem() {
        JMenu menu = new JMenu(config.getLanguageWord("theme"));
        LookAndFeelInfo[] installedLAFs = UIManager.getInstalledLookAndFeels();

        JCheckBoxMenuItem[] checkboxList = new JCheckBoxMenuItem[installedLAFs.length];
        int checkboxesAdded = 0;

        for(LookAndFeelInfo lafInfo : installedLAFs) {

            //load the name of the LAF
            String name = lafInfo.getName();

            //create the JCheckBoxMenuItem
            final JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem();
            checkbox.setText(name);

            //add the item to the checkbox and the list
            menu.add(checkbox);
            checkboxList[checkboxesAdded] = checkbox;
            checkboxesAdded++;

            //set this item to selected if the theme is set in config
            if(config.getProp("theme").equals(lafInfo.getClassName()))
                checkbox.setSelected(true);

            //create the action listener for every JCheckBoxMenuItem
            checkbox.addActionListener(e -> {

                //set every item to not selected
                for(JCheckBoxMenuItem indexBox : checkboxList)
                    indexBox.setSelected(false);

                //set this item to selected
                checkbox.setSelected(true);

                //set the theme in the config
                config.setProp("theme", lafInfo.getClassName());
            });
        }

        return menu;
    }

}
