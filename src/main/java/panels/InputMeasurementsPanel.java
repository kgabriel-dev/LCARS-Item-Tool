package main.java.panels;

import main.java.editor.Storage;
import main.java.frame.Frame;
import main.java.main.Main;
import main.java.main.Type;
import org.apache.batik.swing.JSVGCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class InputMeasurementsPanel extends JPanel {
    private GridBagConstraints mainConstraints;

    public InputMeasurementsPanel() {
        super();

        Frame frame = Main.frame;

        //set the functions for the forwards and the backwards button
        frame.setBackwardsAction(null);
        frame.setForwardsAction(null);

        //set the text for information above the panel
        frame.setInfoText(Main.config.getLanguageWord("inputMeasure"));

        //create and set the layout
        GridBagLayout gridbag = new GridBagLayout();
        mainConstraints = new GridBagConstraints();
        mainConstraints.fill = GridBagConstraints.BOTH;
        mainConstraints.weighty = 1;
        mainConstraints.weightx = 0.8;
        mainConstraints.insets = new Insets(20,30,20,5);
        mainConstraints.gridx = mainConstraints.gridy = 0;
        setLayout(gridbag);

        //load the correct image
//        BufferedImage image = loadMeasurementImage();
//        JLabel imageLabel = new JLabel(new ImageIcon(image));
//        JScrollPane imageScroll = new JScrollPane(imageLabel);
//        imageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        imageScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setBackground(this.getBackground());

        //svgCanvas.setURI(new File("resources/svg/" + Storage.getType().toString() + "_Measurement.svg").toURI().toString());
        try {
            svgCanvas.setURI(getClass().getResource("/svg/" + Storage.getType().toString() + "_Measurement.svg").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //add the image to the panel
        mainConstraints.gridx = mainConstraints.gridy = 0;
        add(svgCanvas, mainConstraints);

        //add the according panel to the type for the inputs
        addMeasurementPanel(Storage.getType());

        //add the dropdown menu to scale the image
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
        //create the panel and the layout
//        GridLayout grid = new GridLayout(type.getMeasurements().length, 2);
//        grid.setHgap(0); grid.setVgap(5);
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER, 25, 10);
        JPanel measurePanel = new JPanel(flow);

        String[] measurementNames = type.getMeasurements();
        JTextField[] textFields = new JTextField[measurementNames.length];
        int currentIndex = 0;

        for(String currentMeasurement : measurementNames) {

            //create the panel for the inputs
            JPanel currMeasurePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 2, 0));

            //create the font with the bigger size
            Font font = new JLabel().getFont().deriveFont(20.0f);

            //create and add the label with the information text
            JLabel textLabel = new JLabel(currentMeasurement + ": ");
            textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            textLabel.setFont(font);
            currMeasurePanel.add(textLabel);

            //create and add the text field for inputs
            JTextField input = new JTextField(6);
            input.setFont(font);
            input.setHorizontalAlignment(SwingConstants.RIGHT);
            currMeasurePanel.add(input);

            //create the label with the text "px"
            JLabel px = new JLabel("px");
            px.setFont(font);
            currMeasurePanel.add(px);

            //add the currMeasurePanel to the measurePanel
            measurePanel.add(currMeasurePanel);

            textFields[currentIndex] = input;
            currentIndex++;
        }

        //add the ActionListener
        Main.frame.setForwardsAction(e -> {

            //put all inputted measurements into the storage
            HashMap<String, Integer> measurements = new HashMap<>();
            for(int i = 0; i < textFields.length; i++) {
                String measurementName = measurementNames[i];
                JTextField textField = textFields[i];

                measurements.put(measurementName, Integer.parseInt(textField.getText()));
            }

            Storage.setMeasurements(measurements);

            Main.frame.setConfigPanel(new ChooseColorPanel(false));
        });

        //add the panel to the frame
        mainConstraints.weightx = 0;
        mainConstraints.weighty = 0;
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 1;
        add(measurePanel, mainConstraints);
    }

}
