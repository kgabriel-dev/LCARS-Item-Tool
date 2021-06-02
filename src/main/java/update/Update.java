package main.java.update;

import main.java.main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class Update {
    public static final String VERSION = "0.2.1";

    public static void checkForUpdates(boolean showInfo) {
        String onlineVersion = getOnlineVersion();

        //check if there is a new version available
        if(!VERSION.equals(onlineVersion)) {
            String message = Main.config.getLanguageWord("newVersion");
            JDialog dialog = new JDialog();

            JButton htmlDownloadButton = new JButton("<html><a><img alt=\"Download LCARS Item Tool\" src=\"https://a.fsdn.com/con/app/sf-download-button\" width=276 height=48 srcset=\"https://a.fsdn.com/con/app/sf-download-button?button_size=2x 2x\"></a></html>");
            htmlDownloadButton.setFocusPainted(false);
            htmlDownloadButton.setBackground(Color.WHITE);
            htmlDownloadButton.addActionListener(e -> {
                try {
                    Update.openBrowser("https://sourceforge.net/projects/lcars-item-tool/");
                    dialog.setVisible(false);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            JOptionPane.showMessageDialog(Main.frame, htmlDownloadButton, message, JOptionPane.PLAIN_MESSAGE);

        } else {
            if(showInfo) {
                JOptionPane.showMessageDialog(Main.frame, Main.config.getLanguageWord("noUpdateFound"));
            }
        }
    }

    private static String getOnlineVersion() {
        try {
            URL fileURL = new URL("https://pastebin.com/raw/ZPrFJWbD");
            Scanner fileScanner = new Scanner(fileURL.openStream());

            String version = fileScanner.nextLine();
            return version;

        } catch (IOException e) {
            e.printStackTrace();
            return getProgramVersion();
        }
    }

    public static void openBrowser(String uri) throws IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(URI.create(uri));
        }
    }

    public static String getProgramVersion() {
        return VERSION;
    }

}
