package main.java.panels;

import main.java.config.Config;
import main.java.editor.Storage;
import main.java.main.Main;
import main.java.main.Type;
import main.java.frame.Frame;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ItemListPanel extends JPanel {
    private ArrayList<Icon> iconList;
    private ArrayList<String> iconNameList;
    private GridBagLayout gridbag;
    private GridBagConstraints constraints;
    private Config config = Main.config;

    public ItemListPanel() {
        super();

        gridbag = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(5,5,5,5);
        constraints.gridx = 0; constraints.gridy = 0;
        setLayout(gridbag);

        iconList = new ArrayList<>();
        iconNameList = new ArrayList<>();

        loadAllIcons();
        createAndAddButtons();
    }

    private void loadAllIcons() {
        for(String iconFileName : config.getCSV("icons")) {
            //Icon icon = new ImageIcon("/images/icons/" + iconFileName + ".png");
            BufferedImage icon = null;
            try {
                icon = ImageIO.read(getClass().getResourceAsStream("/images/icons/" + iconFileName + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            iconNameList.add(iconFileName);
            iconList.add(new ImageIcon(icon));
        }
    }

    private void createAndAddButtons() {
        int gridy = 0;

        //mit jedem Icon ein Button erzeugen
//        for(Icon icon : iconList) {
//            JButton button = new JButton(icon);
//
//            button.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    //den Typ aus dem IconPfad auslesen
//                    File iconFile = new File(icon.toString());
//                    String fileName = iconFile.getName();
//                    String fileNameWOExtension = removeExtension(fileName);
//                    System.out.println(icon.toString());
//                    Type type = Type.valueOf(fileNameWOExtension);
//
//                    //den Typ im Storage speichern
//                    Storage.setType(type);
//
//                    Frame frame = Main.frame;
//                    //im Frame das ConfigPanel zum ChooseColorPanel setzen
//                    frame.setConfigPanel(new InputMeasurementsPanel());
//                }
//            });
//
//            constraints.gridy = gridy;
//            add(button, constraints);
//
//            gridy++;
//        }

        for(int i = 0; i < iconList.size(); i++) {
            Icon icon = iconList.get(i);
            JButton button = new JButton(icon);

            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String fileName = iconNameList.get(finalI);
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
