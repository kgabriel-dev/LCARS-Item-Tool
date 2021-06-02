package main.java.panels;

import main.java.editor.Storage;
import main.java.frame.Frame;
import main.java.main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ChooseColorPanel extends JPanel {

    private int rows = 3, cols = 11, colors = 32;
    private Color[] colorList = new Color[colors];
    private boolean clickedColor;


    public ChooseColorPanel(boolean clickedColor) {
        super();
        JPanel colorPanel = new JPanel();   //the panel that will show the chosen color later
        Frame frame = Main.frame;
        this.clickedColor = clickedColor;

        //configure the forwards and the backwards button
        if(clickedColor)
            frame.setBackwardsAction(e -> frame.setConfigPanel(new ChooseColorPanel(false)));
        else
            frame.setBackwardsAction(e -> frame.setConfigPanel(new InputMeasurementsPanel()));

        frame.setForwardsAction(null);  //will be set when a color is chosen

        //set the text for information above the panel
        if(clickedColor)
            frame.setInfoText(Main.config.getLanguageWord("inputColorClicked"));
        else
            frame.setInfoText(Main.config.getLanguageWord("inputColorNormal"));

        //configure and set the layout
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);
        constraints.weightx = 1; constraints.weighty = 1;
        setLayout(gridbag);

        //create the array which contains all the colors
        createColorArray();

        //create and add a button for every color
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.weighty = 1;

        for (Color color : colorList) {
            if (constraints.gridx == cols) {
                constraints.gridx = 0;
                constraints.gridy = constraints.gridy + 1;
            }

            JButton button = new JButton();
            button.setBackground(color);
            button.setForeground(color);

            button.setFont(button.getFont().deriveFont(Font.BOLD));
            button.setText(String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));

            button.addActionListener(e -> {
                colorPanel.setBackground(color);

                if(!clickedColor) {
                    Storage.setBaseColor(color);

                    Main.frame.setForwardsAction(e1 -> Main.frame.setConfigPanel(new ChooseColorPanel(true)));
                }
                else {
                    Storage.setClickedColor(color);

                    Main.frame.setForwardsAction(e1 -> Main.frame.setConfigPanel(new ChooseOrientationPanel()));
                }

            });

            add(button, constraints);
            constraints.gridx++;
        }

        //den Button für die eigene Farbe hinzufügen
        JButton ownColor = new JButton(Main.config.getLanguageWord("ownColor"));
        ownColor.setFont(ownColor.getFont().deriveFont(Font.BOLD));
        ownColor.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser();
            Color chosenColor = colorChooser.showDialog(Main.frame, Main.config.getLanguageWord("chooseColor"), new Color(0x000096));

            if(chosenColor == null)
                return;

            colorPanel.setBackground(chosenColor);

            if(!clickedColor) {
                Storage.setBaseColor(chosenColor);

                Main.frame.setForwardsAction(e1 -> Main.frame.setConfigPanel(new ChooseColorPanel(true)));
            }
            else {
                Storage.setClickedColor(chosenColor);

                Main.frame.setForwardsAction(e1 -> Main.frame.setConfigPanel(new ChooseOrientationPanel()));
            }
        });
        add(ownColor, constraints);

        //das Panel hinzufügen, dass die gegenwärtige Farbe anzeigt
        constraints.gridx = 0;
        constraints.gridy = constraints.gridy + 1;
        constraints.gridwidth = cols;
        add(colorPanel, constraints);

    }

    private void createColorArray() {
        //File colorFile = new File("resources/images/colors.txt");
        InputStream colorFile = getClass().getResourceAsStream("/images/colors.txt");
        try {
            //BufferedReader reader = new BufferedReader(new FileReader(colorFile));
            InputStreamReader streamReader = new InputStreamReader(colorFile);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            int currentIndex = 0;

            while((line = reader.readLine()) != null) {
                String[] rgb = line.split(" ");
                Color color = new Color(Integer.parseInt(rgb[0]),
                                        Integer.parseInt(rgb[1]),
                                        Integer.parseInt(rgb[2]));

                colorList[currentIndex] = color;
                currentIndex++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
