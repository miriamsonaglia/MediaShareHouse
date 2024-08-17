package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageDisplayer implements ContentDisplayer {
    private File imageFile;

    public ImageDisplayer(File imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public JPanel displayContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Verifica se il file esiste
        if (!imageFile.exists()) {
            System.out.println("File non trovato: " + imageFile.getAbsolutePath());
            JLabel errorLabel = new JLabel("Impossibile trovare l'immagine.");
            panel.add(errorLabel, BorderLayout.CENTER);
            return panel;
        }

        // Carica e ridimensiona l'immagine per adattarla al pannello
        try {
            ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
            Image image = imageIcon.getImage();

            // Verifica se l'immagine è stata caricata correttamente
            if (image == null) {
                System.out.println("Immagine non caricata: " + imageFile.getAbsolutePath());
                JLabel errorLabel = new JLabel("Errore durante il caricamento dell'immagine.");
                panel.add(errorLabel, BorderLayout.CENTER);
                return panel;
            }

            // Ridimensiona l'immagine
            Image scaledImage = image.getScaledInstance(-1, 500, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel();
            imageLabel.setIcon(imageIcon);

            JScrollPane scrollPane = new JScrollPane(imageLabel);
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Errore durante la visualizzazione dell'immagine.");
            panel.add(errorLabel, BorderLayout.CENTER);
        }

        return panel;
    }
}
