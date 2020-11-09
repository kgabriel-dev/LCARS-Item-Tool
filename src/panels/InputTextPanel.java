package panels;

import main.Main;
import frame.Frame;
import editor.Storage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputTextPanel extends JPanel {

    public InputTextPanel() {
        super();

        Frame frame = Main.frame;
        JTextField textInput = new JTextField();

        //die Funktionen für den Zurück- und den Vorwärts-Button setzen
        frame.setBackwardsAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setConfigPanel(new ChooseOrientationPanel());
            }
        });
        frame.setForwardsAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Storage.setText(textInput.getText());
                frame.setConfigPanel(new InputMeasurementsPanel());
            }
        });

        //das Layout erstellen und setzen
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = constraints.weighty = 1;
        constraints.insets = new Insets(5,5,5,5);
        setLayout(gridbag);

        //das JLabel mit dem Infotext erstellen
        JLabel infoText = new JLabel("Gebe hier den Text ein, welcher auf dem LCARS-Item stehen soll");
        constraints.gridx = constraints.gridy = 0;
        add(infoText, constraints);

        //das Textfeld für die Texteingabe hinzufügen
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = constraints.weighty = 0;
        add(textInput, constraints);
    }

}
