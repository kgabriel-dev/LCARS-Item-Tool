package frame;

import main.Main;
import panels.ItemListPanel;
import frame.MenuBar;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

public class Frame extends JFrame {
    private GridBagConstraints constraints;
    private JButton forwardsButton, backwardsButton;
    private JPanel currentConfigPanel = new JPanel();
    private JLabel infoLabel;

    public Frame() {
        super("LCARS Item Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(720, 480));
        setLocationRelativeTo(null);
        setJMenuBar(new MenuBar());

        //das Layout erstellen und setzen
        GridBagLayout gridbag = new GridBagLayout();
        constraints = new GridBagConstraints();
        constraints.gridx = 0; constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 5, 5);
        setLayout(gridbag);

        //die ganzen Panels und Items erstellen und hinzufügen
        JPanel itemListPanel = new ItemListPanel();
        JButton donateButton = createDonateButton();
        infoLabel = new JLabel();
        currentConfigPanel = new JPanel();
        forwardsButton = createForwardsButton();
        backwardsButton = createBackwardsButton();

        //--> die ItemList in ein ScrollPanel packen
        //das ScrollPanel konfigurieren
        JScrollPane scrollItemList = new JScrollPane(itemListPanel);
        scrollItemList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollItemList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //--> itemListPanel hinzufügen
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 3;
        constraints.weightx = 0.3;
        constraints.weighty = 1;
        add(scrollItemList, constraints);

        //--> donateButton hinzufügen
    /*  constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        add(donateButton, constraints); */

        //--> das InfoPanel hinzufügen
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        add(infoLabel, constraints);

        //--> emptyPanel (configPanel) hinzufügen
        setConfigPanel(currentConfigPanel);

        //--> Zurück-Button hinzufügen
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        add(backwardsButton, constraints);

        //--> Vorwärts-Button hinzufügen
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.weighty = 0;
        add(forwardsButton, constraints);

        //den Vorwärts- und Rückwärts-Button deaktivieren
        setForwardsAction(null);
        setBackwardsAction(null);
    }

    private JButton createDonateButton() {
        JButton button = new JButton(Main.config.getLanguageWord("donate"));    //TODO: donation_icon setzen

        button.addActionListener(e -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();

                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        java.net.URI uri = new java.net.URI("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=723LRNKVWWNHC&source=url");
                        desktop.browse(uri);
                    }
                }
            } catch(IOException | URISyntaxException exc) {
                exc.printStackTrace();
            }
        });

        return button;
    }

    private JButton createForwardsButton() {
        JButton button = new JButton(Main.config.getLanguageWord("next") + " >>");
        return button;
    }

    private JButton createBackwardsButton() {
        JButton button = new JButton("<< " + Main.config.getLanguageWord("back"));
        return button;
    }


    private JScrollPane currentScrollPanel;
    public void setConfigPanel(JPanel panel) {
        try {
            remove(currentScrollPanel);
        } catch(NullPointerException e) {
            e.printStackTrace();
        }

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 1;

        JScrollPane scroll = new JScrollPane(panel);

        scroll.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        add(scroll, constraints);

        revalidate();
        currentConfigPanel = panel;
        currentScrollPanel = scroll;
    }

    public void setForwardsAction(ActionListener action) {
        //alle anderen ActionListeners entfernen
        for(ActionListener listener : forwardsButton.getActionListeners())
            forwardsButton.removeActionListener(listener);

        if(action == null) {
            forwardsButton.setEnabled(false);
            return;
        }

        //den neuen ActionListener hinzufügen und den Button aktivieren
        forwardsButton.addActionListener(action);
        forwardsButton.setEnabled(true);
    }

    public void setBackwardsAction(ActionListener action) {
        //alle anderen ActionListeners entfernen
        for(ActionListener listener : backwardsButton.getActionListeners())
            backwardsButton.removeActionListener(listener);

        if(action == null) {
            backwardsButton.setEnabled(false);
            return;
        }

        //den neuen ActionListener hinzufügen und den Button aktivieren
        backwardsButton.addActionListener(action);
        backwardsButton.setEnabled(true);
    }

    public void setInfoText(String info) {
        infoLabel.setText(info);
    }

}
