package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Desktop;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AudioDisplayer implements ContentDisplayer {

    private File audioFile;

    public AudioDisplayer(File audioFile) {
        this.audioFile = audioFile;
    }

    @Override
    public JPanel displayContent() {
        // Usa Desktop per aprire il file con l'applicazione predefinita
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(audioFile);
            } else {
                JOptionPane.showMessageDialog(null, "Apertura del file non supportata.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore nell'apertura del file.");
        }
        
        // Restituisce un pannello vuoto o un messaggio per confermare l'apertura
        JPanel panel = new JPanel();
        JLabel label = new JLabel("L'audio è stato aperto nell'applicazione predefinita.");
        panel.add(label);
        return panel;
    }
}

