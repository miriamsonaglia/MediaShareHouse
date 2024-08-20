package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

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
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight()));
    
        // Pannello che conterrà tutti i messaggi
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
    
        // Pannello per i messaggi che sarà inserito nello scrollPane
        JPanel chatContainer = new JPanel(new BorderLayout());
        chatContainer.add(chatPanel, BorderLayout.NORTH);  // Impedisce la compressione dei messaggi
    
        // JScrollPane per rendere il chatPanel scorrevole
        JScrollPane chatScrollPane = new JScrollPane(chatContainer);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.getVerticalScrollBar().setUnitIncrement(16);  // Velocità di scorrimento
    
        panel.add(chatScrollPane, BorderLayout.CENTER);
    
        // Inizializza l'area di input per i nuovi messaggi
        JTextArea inputArea = new JTextArea(3, 20);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
    
        // Pannello per l'input e il pulsante di invio
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputArea, BorderLayout.CENTER);
    
        // Bottone per inviare il messaggio
        JButton sendButton = new JButton("Send");
        inputPanel.add(sendButton, BorderLayout.EAST);
    
        // Aggiungi il pannello di input al pannello principale
        panel.add(inputPanel, BorderLayout.SOUTH);
    
        // Variabile per tenere traccia del commento a cui si risponde
        final Commento[] selectedCommentoPadre = {null};
    
        // Carica i commenti esistenti dal database e popola la chat
        try (Connection connection = DatabaseConnection.getConnection()) {
            CommentoDao commentoDao = new CommentoDao(connection);
            List<Commento> commenti = commentoDao.getCommentiByContenuto(idContenuto);
    
            for (Commento commento : commenti) {
                // Pannello orizzontale per ogni messaggio e il pulsante "Rispondi"
                JPanel messagePanel = new JPanel();
                messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
    
                // Etichetta del messaggio
                JLabel messageLabel = new JLabel(commento.getUsername() + ": " + commento.getTesto());
                messagePanel.add(messageLabel);
    
                // Crea un pulsante "Rispondi" per ogni commento
                JButton replyButton = new JButton("Rispondi");
                replyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        inputArea.setText("@" + commento.getUsername() + " ");
                        selectedCommentoPadre[0] = commento;
                    }
                });
    
                // Aggiungi il pulsante "Rispondi" accanto al messaggio
                messagePanel.add(Box.createHorizontalStrut(10)); // Spazio tra messaggio e pulsante
                messagePanel.add(replyButton);
    
                // Aggiungi il pannello del messaggio al pannello della chat
                chatPanel.add(messagePanel);
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento dei commenti.");
        }
    
        // Aggiungi il listener al bottone di invio
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputArea.getText().trim();
                if (!message.isEmpty()) {
                    // Crea un nuovo commento con l'eventuale commento padre
                    Timestamp data = new Timestamp(System.currentTimeMillis());
                    Integer idCommentoPadre = (selectedCommentoPadre[0] != null) ? selectedCommentoPadre[0].getIdCommento() : null;
                    Commento commento = new Commento(0, message, data, idContenuto, currentUser.getUsername(), idCommentoPadre);
    
                    try (Connection connection = DatabaseConnection.getConnection()) {
                        CommentoDao commentoDao = new CommentoDao(connection);
                        commentoDao.createCommento(commento);
    
                        // Aggiungi il nuovo messaggio alla chat
                        JPanel newMessagePanel = new JPanel();
                        newMessagePanel.setLayout(new BoxLayout(newMessagePanel, BoxLayout.X_AXIS));
    
                        // Se c'è un messaggio padre, visualizzalo
                        if (selectedCommentoPadre[0] != null) {
                            JLabel parentMessageLabel = new JLabel("In risposta a " + selectedCommentoPadre[0].getUsername() + ": " + selectedCommentoPadre[0].getTesto());
                            parentMessageLabel.setFont(parentMessageLabel.getFont().deriveFont(Font.ITALIC)); // Opzionale: stile in corsivo
                            newMessagePanel.add(parentMessageLabel);
                        }
    
                        // Messaggio dell'utente corrente
                        JLabel newMessageLabel = new JLabel("You: " + message);
                        newMessagePanel.add(newMessageLabel);
    
                        // Pulsante "Rispondi" per il nuovo messaggio
                        JButton newReplyButton = new JButton("Rispondi");
                        newReplyButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                inputArea.setText("@" + currentUser.getUsername() + " ");
                                selectedCommentoPadre[0] = commento;
                            }
                        });
    
                        newMessagePanel.add(Box.createHorizontalStrut(10)); // Spazio tra il messaggio e il pulsante
                        newMessagePanel.add(newReplyButton);
    
                        chatPanel.add(newMessagePanel);
                        chatPanel.revalidate();
    
                        // Scorri automaticamente in basso alla chat
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
                            }
                        });
    
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Errore nel caricamento del contenuto della stanza.");
                    }
    
                    inputArea.setText(""); // Pulisce il campo input dopo l'invio del messaggio
                    selectedCommentoPadre[0] = null; // Resetta il commento padre selezionato
                }
            }
        });
    
        return panel;
    }
    
    
}
