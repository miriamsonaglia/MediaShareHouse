package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TextDisplayer implements ContentDisplayer {

    private File textFile;

    public TextDisplayer(File textFile) {
        this.textFile = textFile;
    }

    @Override
    public JPanel displayContent() {
        // Usa Desktop per aprire il file con l'applicazione predefinita
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(textFile);
            } else {
                JOptionPane.showMessageDialog(null, "Apertura del file non supportata.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore nell'apertura del file.");
        }

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Il file di testo è stato aperto nell'applicazione predefinita.");
        panel.add(label);
        return panel;
    }
}