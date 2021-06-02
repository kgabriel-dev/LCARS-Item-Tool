package main.java.panels;

import main.java.editor.Editor;
import main.java.editor.Storage;
import main.java.main.Main;
import main.java.frame.Frame;

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

        //set the action for the forwards and backwards button
        frame.setBackwardsAction((ActionEvent e) -> frame.setConfigPanel(new ChooseOrientationPanel()));
        frame.setForwardsAction(null);

        //add the text for more information
        frame.setInfoText(Main.config.getLanguageWord("inputLocation"));

        //create and set the layout
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

                //add the file extension if necessary
                if(!selectedFile.getAbsolutePath().endsWith(".png"))
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".png");

                //prepare and do the next steps
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
