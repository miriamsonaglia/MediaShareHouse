package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ContentViewer {

    private JFrame frame;
    private JPanel contentPanel;
    private JPanel chatPanel;
    private File contentFile;

    public ContentViewer(JFrame frame, String filePath, String contentType) {
        this.frame = frame;
        this.contentFile = new File(filePath);

        // Imposta il layout del frame come BorderLayout
        frame.setLayout(new BorderLayout());

        // Crea i pannelli per la visualizzazione del contenuto e la chat
        contentPanel = createContentPanel(contentType);
        chatPanel = createChatPanel();

        // Aggiunge i pannelli al frame
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(chatPanel, BorderLayout.EAST);

        frame.revalidate();
        frame.repaint();
    }

    private JPanel createContentPanel(String contentType) {
        ContentDisplayer displayer;

        switch (contentType) {
            case "song":
                displayer = new AudioDisplayer();
                break;
            case "movie":
                displayer = new VideoDisplayer();
                break;
            case "picture":
                displayer = new ImageDisplayer(contentFile);
                break;
            case "book":
                displayer = new TextDisplayer(contentFile);
                break;
            default:
                throw new IllegalArgumentException("Tipo di contenuto non supportato: " + contentType);
        }

        return displayer.displayContent();
    }

    private JPanel createChatPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, frame.getHeight())); // Setta la dimensione preferita

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false); // Rende l'area di chat non modificabile
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        panel.add(chatScrollPane, BorderLayout.CENTER);

        JTextArea inputArea = new JTextArea();
        inputArea.setPreferredSize(new Dimension(300, 50)); // Imposta altezza area input
        panel.add(inputArea, BorderLayout.SOUTH);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputArea.getText().trim();
                if (!message.isEmpty()) {
                    chatArea.append("You: " + message + "\n");
                    inputArea.setText(""); // Pulisce il campo input dopo l'invio del messaggio
                }
            }
        });
        panel.add(sendButton, BorderLayout.NORTH);

        return panel;
    }
}

