package panels;

import editor.Storage;
import main.Main;
import frame.Frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChooseOrientationPanel extends JPanel {

    public ChooseOrientationPanel() {
        super();

        Frame frame = Main.frame;

        //die Aktionen für den Zurück- und den Vorwärtsbutton setzen
        frame.setBackwardsAction(e -> frame.setConfigPanel(new ChooseColorPanel(true)));
        frame.setForwardsAction(null);  //wird aktiviert, wenn man eine Ausrichtung auswählt

        //den InfoText über dem Panel setzen
        frame.setInfoText(Main.config.getLanguageWord("inputOrientation"));

        //das Layout erstellen und setzen
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0; constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        setLayout(gridbag);


        try {
            //das Bild des ausgewählten Typs laden
            File imageFile = new File("resources/images/icons/" + Storage.getType().toString() + ".png");
            BufferedImage image = ImageIO.read(imageFile);

            //die Farben im Bild verändern, sodass es zur ausgewählten Farbe passt
            image = recolorImage(image, new Color(255, 153, 0, 255), Storage.getColor());

            //das Bild 3x drehen, sodass jede Drehung vorhanden ist
            BufferedImage image1, image2, image3, image4;
            image1 = image;
            image2 = rotateImage(image, 90.0d);
            image3 = rotateImage(image, 180.0d);
            image4 = rotateImage(image, 270.0d);

            //die 4 Buttons erstellen
            JButton button1, button2, button3, button4;
            button1 = new JButton(new ImageIcon(image1));
            button2 = new JButton(new ImageIcon(image2));
            button3 = new JButton(new ImageIcon(image3));
            button4 = new JButton(new ImageIcon(image4));

            //die ActionListener so setzen, dass sie die Rotation im Storage auf 1, 2, 3 oder 4 setzen
            button1.addActionListener(createActionListener(1));
            button2.addActionListener(createActionListener(2));
            button3.addActionListener(createActionListener(3));
            button4.addActionListener(createActionListener(4));

            //die 4 Buttons hinzufügen
            //--> der 1. Button
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            add(button1, constraints);

            //--> der 2. Button
            constraints.gridx = 1;
            add(button2, constraints);

            //--> der 3. Button
            constraints.gridx = 0;
            constraints.gridy = 2;
            add(button3, constraints);

            //der 4. Button
            constraints.gridx = 1;
            add(button4, constraints);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage rotateImage(BufferedImage image, double degrees) {
        // Calculate the new size of the image based on the angle of rotaion
        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        at.translate(x, y);
        g2d.setTransform(at);
        // Paint the originl image
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotate;
    }

    private BufferedImage recolorImage(BufferedImage image, Color oldColor, Color newColor) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < newImage.getHeight(); y++) {
            for(int x = 0; x < newImage.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));

                if(pixelColor.getRGB() == oldColor.getRGB()) {  //die neue Farbe dort setzen, wo die alte Farbe ist
                    newImage.setRGB(x, y, newColor.getRGB());
                } else {    //Transparent dort setzen, wo nicht die alte Farbe ist
                    newImage.setRGB(x, y, pixelColor.getRGB());
                }
            }
        }

        return newImage;
    }

    private ActionListener createActionListener(int rotationCode) {
        return e -> {
            Storage.setOrientation(rotationCode);

            //den Vorwärts-Button konfigurieren
            Main.frame.setForwardsAction(e1 -> Main.frame.setConfigPanel(new ChooseLocationPanel()));
        };
    }

}
