package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.miriamsonaglia.mediasharehouse.dao.CommentoDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Commento;
import com.miriamsonaglia.mediasharehouse.model.Utente;


public class ContentViewer {

    private JFrame frame;
    private JPanel contentPanel;
    private JPanel chatPanel;
    private File contentFile;
    private Utente currentUser;
    private int idContenuto;


    public ContentViewer(JFrame frame, String filePath, String contentType, Utente currentUser, int idContenuto) {
        this.frame = frame;
        this.contentFile = new File(filePath);
        this.currentUser = currentUser;
        this.idContenuto = idContenuto;

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
                displayer = new AudioDisplayer(contentFile);
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(frame.getWidth()/2, frame.getHeight()));

        JTextArea chatArea = new JTextArea();
        chatArea.setEditable(false); // Rende l'area di chat non modificabile
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setPreferredSize(new Dimension(300, frame.getHeight() - 150));
        panel.add(chatScrollPane);

        try (Connection connection = DatabaseConnection.getConnection()) {
            CommentoDao commentoDao = new CommentoDao(connection);
            List<Commento> commenti = commentoDao.getCommentiByContenuto(idContenuto);
            System.out.println(commenti);
            // System.out.println(idContenuto);

            for (Commento commento : commenti) {
                chatArea.append(commento.getUsername() + ": " + commento.getTesto() + "\n");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento dei commenti.");
        }

        JTextArea inputArea = new JTextArea();
        inputArea.setMaximumSize(new Dimension(frame.getWidth()/2, frame.getHeight())); // Imposta altezza massima per l'area input
        panel.add(inputArea);

        

        JButton sendButton = new JButton("Send");
        // sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputArea.getText().trim();
                if (!message.isEmpty()) {
                    chatArea.append("You: " + message + "\n");

                    Timestamp data = new Timestamp(System.currentTimeMillis());
                    Commento commento = new Commento(0, message, data, idContenuto, currentUser.getUsername() , null);



                    try (Connection connection = DatabaseConnection.getConnection()) {
                        
                        CommentoDao commentoDao = new CommentoDao(connection);
                        commentoDao.createCommento(commento);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Errore nel caricamento del contenuto della stanza.");
                    }

                    inputArea.setText(""); // Pulisce il campo input dopo l'invio del messaggio
                }
            }
        });
        panel.add(Box.createVerticalStrut(10)); // Aggiungi uno spazio verticale tra l'input e il pulsante
        panel.add(sendButton);

        return panel;
    }

    
}
