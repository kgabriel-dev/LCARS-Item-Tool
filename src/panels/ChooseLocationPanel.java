package panels;

import editor.Editor;
import editor.Storage;
import main.Main;
import frame.Frame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ChooseLocationPanel extends JPanel implements ActionListener {
    private Frame frame;
    private JFileChooser fc;

    public ChooseLocationPanel() {
        frame = Main.frame;

        //Die Aktionen für den Vorwärts- und den Rückwärts-Button setzen
        frame.setBackwardsAction((ActionEvent e) -> frame.setConfigPanel(new ChooseOrientationPanel()));
        frame.setForwardsAction(null);

        //den InfoText hinzufügen
        frame.setInfoText(Main.config.getLanguageWord("inputLocation"));

        //Das Layout erstellen und setzen
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = constraints.weighty = 1;
        setLayout(gridbag);

        fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("Accepted Files (*.png)", "png"));

        fc.addActionListener(this);

        add(fc, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("ApproveSelection")) {
            frame.setForwardsAction((ActionEvent e1) -> {
                File selectedFile = fc.getSelectedFile();

                //Die Dateiendung falls nötig hinzufügen
                if(!selectedFile.getAbsolutePath().endsWith(".png"))
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".png");

                //Die nächsten Schritte vorbereiten und durchführen
                Storage.setLocation(selectedFile);
                frame.setConfigPanel(new StatusPanel());
                StatusPanel.addStatusLine(Main.config.getLanguageWord("statusStart"));
                Editor.editFile();
            });
        } else if(e.getActionCommand().equals("CancelSelection")) {
            frame.setForwardsAction(null);
        }

    }
}
