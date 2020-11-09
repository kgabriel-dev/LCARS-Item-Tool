package panels;

import editor.Storage;
import frame.Frame;
import main.Main;
import main.Type;
import org.apache.batik.swing.JSVGCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

public class InputMeasurementsPanel extends JPanel {
    private GridBagConstraints mainConstraints;

    public InputMeasurementsPanel() {
        super();

        Frame frame = Main.frame;

        //die Funktionen für den Zurück- und den Vorwärts-Button setzen
        frame.setBackwardsAction(null);
        frame.setForwardsAction(null);

        //den InfoText über dem Panel setzen
        frame.setInfoText(Main.config.getLanguageWord("inputMeasure"));

        //das Layout erstellen und setzen
        GridBagLayout gridbag = new GridBagLayout();
        mainConstraints = new GridBagConstraints();
        mainConstraints.fill = GridBagConstraints.BOTH;
        mainConstraints.weighty = 1;
        mainConstraints.weightx = 0.8;
        mainConstraints.insets = new Insets(20,30,20,5);
        mainConstraints.gridx = mainConstraints.gridy = 0;
        setLayout(gridbag);

        //das korrekte Bild laden
//        BufferedImage image = loadMeasurementImage();
//        JLabel imageLabel = new JLabel(new ImageIcon(image));
//        JScrollPane imageScroll = new JScrollPane(imageLabel);
//        imageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        imageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setBackground(this.getBackground());
        try {
            svgCanvas.setURI(new File("resources/svg/" + Storage.getType().toString() + "_Measurement.svg").toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //das Bild hinzufügen
        mainConstraints.gridx = mainConstraints.gridy = 0;
        add(svgCanvas, mainConstraints);

        //das dem Type entsprechende Panel für die Eingaben hinzufügen
        addMeasurementPanel(Storage.getType());

        //Das DropDown-Menu für die Skalierung hinzufügen
//        JPanel scalePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,5));
//        scalePanel.add(new JLabel("Skalierung des Bildes:"));
//        String[] scaleList = {"0.25x", "0.5x", "0.75x", "1x", "2x", "3x", "4x"};
//        JComboBox scaleChoice = new JComboBox(scaleList);
//        scaleChoice.setSelectedIndex(3);
//        scaleChoice.addActionListener(e -> {
//            String choiceStr = scaleList[scaleChoice.getSelectedIndex()];
//            double choice = Double.parseDouble(choiceStr.replace("x", ""));
//
//            imageLabel.setIcon(new ImageIcon(image.getScaledInstance((int) (image.getWidth() * choice),
//                    (int) (image.getHeight() * choice),
//                            Image.SCALE_AREA_AVERAGING)));
//        });
//        scalePanel.add(scaleChoice);
//        mainConstraints.gridx = 0;
//        mainConstraints.gridy = 1;
//        mainConstraints.weighty = 0;
//        add(scalePanel, mainConstraints);

        JScrollPane scroll = new JScrollPane(this);
    }

    private BufferedImage loadMeasurementImage() {
        try {
            File imageFile = new File("resources/images/measurements/" + Storage.getType().toString() + ".png");
            BufferedImage image = ImageIO.read(imageFile);

            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addMeasurementPanel(Type type) {
        //das Panel und das Layout erstellen
//        GridLayout grid = new GridLayout(type.getMeasurements().length, 2);
//        grid.setHgap(0); grid.setVgap(5);
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 25, 10);
        JPanel measurePanel = new JPanel(flow);

        String[] measurementNames = type.getMeasurements();
        JTextField[] textFields = new JTextField[measurementNames.length];
        int currentIndex = 0;

        for(String currentMeasurement : measurementNames) {

            //das Panel für die momentane Eingabe erstellen
            JPanel currMeasurePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 2, 0));

            //das Font mit der größeren Schrift erstellen
            Font font = new JLabel().getFont().deriveFont(20.0f);

            //das Label mit dem Infotext erstellen und hinzufügen
            JLabel textLabel = new JLabel(currentMeasurement + ": ");
            textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            textLabel.setFont(font);
            currMeasurePanel.add(textLabel);

            //das Textfeld zum Eingeben erstellen und hinzufügen
            JTextField input = new JTextField(6);
            input.setFont(font);
            input.setHorizontalAlignment(SwingConstants.RIGHT);
            currMeasurePanel.add(input);

            //das Label mit dem "px"-Text hinzufügen
            JLabel px = new JLabel("px");
            px.setFont(font);
            currMeasurePanel.add(px);

            //das currMeasurePanel zum measurePanel hinzufügen
            measurePanel.add(currMeasurePanel);

            textFields[currentIndex] = input;
            currentIndex++;
        }

        //Den ActionListener hinzufügen
        Main.frame.setForwardsAction(e -> {

            //dem Storage alle eingegebenen Maße geben
            HashMap<String, Integer> measurements = new HashMap<>();
            for(int i = 0; i < textFields.length; i++) {
                String measurementName = measurementNames[i];
                JTextField textField = textFields[i];

                measurements.put(measurementName, Integer.parseInt(textField.getText()));
            }

            Storage.setMeasurements(measurements);

            Main.frame.setConfigPanel(new ChooseColorPanel(false));
        });

        //das Panel zum Frame hinzufügen
        mainConstraints.weightx = 0;
        mainConstraints.weighty = 0;
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 1;
        add(measurePanel, mainConstraints);
    }

}
