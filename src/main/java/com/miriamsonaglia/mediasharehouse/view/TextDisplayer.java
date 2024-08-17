package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextDisplayer implements ContentDisplayer {
    private File textFile;

    public TextDisplayer(File textFile) {
        this.textFile = textFile;
    }

    @Override
    public JPanel displayContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        try {
            textArea.setText(new String(Files.readAllBytes(textFile.toPath())));
        } catch (IOException e) {
            textArea.setText("Errore durante la lettura del file.");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}  