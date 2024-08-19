package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextDisplayer implements ContentDisplayer {
    private File file;

    public TextDisplayer(File file) {
        this.file = file;
    }

    @Override
    public JPanel displayContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        try {
            String fileContent = readFileContent();
            textArea.setText(fileContent);
        } catch (IOException e) {
            textArea.setText("Errore durante la lettura del file.");
            e.printStackTrace(); // Stampa l'eccezione per il debug
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 600)); // Imposta dimensioni preferite
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private String readFileContent() throws IOException {
        // Verifica se il file è di testo leggibile
        if (isTextFile()) {
            return new String(Files.readAllBytes(file.toPath()));
        } else {
            throw new IOException("Il file non è un file di testo.");
        }
    }

    private boolean isTextFile() {
        // Implementa una semplice verifica per determinare se il file è di testo
        // Questo è solo un esempio e può essere migliorato
        String mimeType;
        try {
            mimeType = Files.probeContentType(file.toPath());
            return mimeType != null && mimeType.startsWith("text");
        } catch (IOException e) {
            return false;
        }
    }
}
