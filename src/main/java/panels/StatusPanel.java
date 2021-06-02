package main.java.panels;

import main.java.frame.Frame;
import main.java.main.Main;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatusPanel extends JPanel {
    private Frame frame;
    private static JTextArea statusPanel;

    public StatusPanel() {
        frame = Main.frame;

        //die Aktionen für den Vorwärts- und Rückwärts-Button erstellen
        frame.setBackwardsAction(e -> frame.setConfigPanel(new ChooseLocationPanel()));
        frame.setForwardsAction(e -> {
            //Das Fenster auf den Anfangsstatus zurücksetzen
            frame.setConfigPanel(new JPanel());
            frame.setForwardsAction(null);
            frame.setBackwardsAction(null);
            frame.setInfoText("");
        });


        //Das Layout erstellen und setzen
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = constraints.gridy = 0;
        constraints.weightx = constraints.weighty = 1;
        setLayout(gridbag);


        //Die JTextArea hinzufügen
        statusPanel = new JTextArea();
        statusPanel.setLineWrap(true);
        statusPanel.setEditable(false);
        JScrollPane scroll = new JScrollPane(statusPanel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, constraints);

    }

    public static void addStatusLine(String status) {
        //Die aktuelle Uhrzeit laden
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss:SSS");
        LocalDateTime ldt = LocalDateTime.now();
        String time = "[" + dtf.format(ldt) + "]";

        if (statusPanel.getText().equals("") || statusPanel.getText() == null) {
            statusPanel.setText(time + " " + status + "\n");
            return;
        }

        String currentStatusText = statusPanel.getText();
        statusPanel.setText(currentStatusText + "\n" + time + " " + status + "\n");
    }

}
