package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.miriamsonaglia.mediasharehouse.model.Utente;


public class VisualContentViewer {

    private JFrame frame;
    private JPanel contentPanel;
    private JPanel chatPanel;
    private File contentFile;
    private Utente currentUser;
    private int idContenuto;
    private JPanel previousPanel;


    public VisualContentViewer(JFrame frame, String filePath, String contentType, Utente currentUser, int idContenuto, JPanel previousPanel) {
        this.frame = frame;
        this.contentFile = new File(filePath);
        this.currentUser = currentUser;
        this.idContenuto = idContenuto;
        this.previousPanel = previousPanel;


        // Imposta il layout del frame come BorderLayout
        frame.setLayout(new BorderLayout());

        // Aggiungi il pulsante "INDIETRO" al frame
        addBackButton(frame);

        // Crea i pannelli per la visualizzazione del contenuto e la chat
        contentPanel = createContentPanel(contentType);

        // Aggiunge i pannelli al frame
        frame.add(contentPanel, BorderLayout.CENTER);


        frame.revalidate();
        frame.repaint();
    }

    private JPanel createContentPanel(String contentType) {
        ContentDisplayer displayer;

        switch (contentType) {
            case "song":
                displayer = new AudioDisplayer(contentFile);
                break;
            case "movie":
                displayer = new VideoDisplayer(contentFile);
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

    private void addBackButton(JFrame frame) {
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
        final CustomButton backButton = new CustomButton("BACK", customColor, customColor1, 1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(previousPanel);  // Torna al pannello precedente
                frame.revalidate();
                frame.repaint();
            }
        });
        frame.add(backButton, BorderLayout.SOUTH);
    }

}
