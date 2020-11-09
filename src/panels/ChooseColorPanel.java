package panels;

import editor.Storage;
import frame.Frame;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ChooseColorPanel extends JPanel {

    private int rows = 4, cols = 8, colors = 32;
    private Color[] colorList = new Color[colors];
    private boolean clickedColor;


    public ChooseColorPanel(boolean clickedColor) {
        super();
        JPanel colorPanel = new JPanel();   //das Panel, das später die ausgewählte Farbe anzeigt
        Frame frame = Main.frame;
        this.clickedColor = clickedColor;

        //den Zurück- und den Vorwärtsbutton konfigurieren
        if(clickedColor)
            frame.setBackwardsAction(e -> frame.setConfigPanel(new ChooseColorPanel(false)));
        else
            frame.setBackwardsAction(e -> frame.setConfigPanel(new InputMeasurementsPanel()));

        frame.setForwardsAction(null);  //Wird beim Auswählen der Farbe gesetzt

        //den InfoText über dem Panel setzen
        if(clickedColor)
            frame.setInfoText(Main.config.getLanguageWord("inputColorClicked"));
        else
            frame.setInfoText(Main.config.getLanguageWord("inputColorNormal"));

        //das Layout konfigurieren und setzen
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5,5,5,5);
        constraints.weightx = 1; constraints.weighty = 1;
        setLayout(gridbag);

        //das Array mit den Farben erstellen
        createColorArray();

        //für jede Farbe einen Button erstellen und ihn hinzufügen
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
                    Storage.setColor(color);

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

        //das Panel hinzufügen, dass die gegenwärtige Farbe anzeigt
        constraints.gridx = 0;
        constraints.gridy = constraints.gridy + 1;
        constraints.gridwidth = cols;
        add(colorPanel, constraints);

    }

    private void createColorArray() {
        File colorFile = new File("resources/images/colors.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(colorFile));
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
