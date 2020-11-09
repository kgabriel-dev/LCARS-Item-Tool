package panels;

import editor.Storage;
import main.Main;
import main.Type;
import frame.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class ItemListPanel extends JPanel {
    private ArrayList<Icon> iconList;
    private GridBagLayout gridbag;
    private GridBagConstraints constraints;

    public ItemListPanel() {
        super();

        gridbag = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridx = 0; constraints.gridy = 0;
        setLayout(gridbag);

        iconList = new ArrayList<>();

        loadAllIcons();
        createAndAddButtons();
    }

    private void loadAllIcons() {

        //Über alle Bilder im Ordner loopen
        File dir = new File("resources/images/icons");
        File[] iconFiles = dir.listFiles();

        for(File iconFile : iconFiles) {
            Icon icon = new ImageIcon(iconFile.getAbsolutePath());
            iconList.add(icon);
        }

    }

    private void createAndAddButtons() {
        int gridy = 0;
        //mit jedem Icon ein Button erzeugen
        for(Icon icon : iconList) {
            JButton button = new JButton(icon);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //den Typ aus dem IconPfad auslesen
                    File iconFile = new File(icon.toString());
                    String fileName = iconFile.getName();
                    String fileNameWOExtension = removeExtension(fileName);
                    Type type = Type.valueOf(fileNameWOExtension);

                    //den Typ im Storage speichern
                    Storage.setType(type);

                    Frame frame = Main.frame;
                    //im Frame das ConfigPanel zum ChooseColorPanel setzen
                    frame.setConfigPanel(new InputMeasurementsPanel());
                }
            });

            constraints.gridy = gridy;
            add(button, constraints);

            gridy++;
        }
    }

    private String removeExtension(String fileName) {
        if (fileName.indexOf(".") > 0) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }

    }

}
