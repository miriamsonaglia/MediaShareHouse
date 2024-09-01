package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImageDisplayer implements ContentDisplayer {

    private File imageFile;

    public ImageDisplayer(File imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public JPanel displayContent() {
        // Usa Desktop per aprire il file con l'applicazione predefinita
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(imageFile);
            } else {
                JOptionPane.showMessageDialog(null, "Apertura del file non supportata.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore nell'apertura del file.");
        }

        JPanel panel = new JPanel();
        JLabel label = new JLabel("L'immagine è stata aperta nell'applicazione predefinita.");
        panel.add(label);
        return panel;
    }
}