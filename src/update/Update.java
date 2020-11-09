package update;

import main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class Update {
    public static final String VERSION = "0.1.0";

    public static void checkForUpdates(boolean showInfo) {
        String onlineVersion = getOnlineVersion();

//        System.out.println("installed: " + VERSION);
//        System.out.println("online: " + onlineVersion);

        //check if there is a new version available
        if(!VERSION.equals(onlineVersion)) {
            String message = Main.config.getLanguageWord("newVersion").toString();

            String download = Main.config.getLanguageWord("download");
            String cancel = Main.config.getLanguageWord("cancel");

            int result = JOptionPane.showOptionDialog(Main.frame,
                    message,
                    Main.config.getLanguageWord("updateAvailable"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{download, cancel},
                    "default");

            //check if the "Download" button was pressed
            if(result == 0) {
                try {
                    //Den Downloadlink im Browser öffnen //TODO: funktioniert wahrscheinlich nur unter Windows
                    openBrowser(new URI("https://github.com"));

                    //Das Programm beenden, da es neu installiert wird
                    System.exit(0);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
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
            return VERSION;
        }
    }

    private static void openBrowser(URI uri) throws IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(uri);
        }
    }

    public static String getProgramVersion() {
        return VERSION;
    }

}
