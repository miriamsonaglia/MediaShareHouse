package com.miriamsonaglia.mediasharehouse.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.miriamsonaglia.mediasharehouse.dao.AccessoDao;
import com.miriamsonaglia.mediasharehouse.dao.CommentoDao;
import com.miriamsonaglia.mediasharehouse.dao.ContenutoDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.dao.ValutazioneDao;
import com.miriamsonaglia.mediasharehouse.model.Commento;
import com.miriamsonaglia.mediasharehouse.model.Utente;


public class ContentViewer {

    private JFrame frame;
    private JPanel contentPanel;
    private JPanel chatPanel;
    private File contentFile;
    private Utente currentUser;
    private int idContenuto;
    private JPanel previousPanel;


    public ContentViewer(JFrame frame, String filePath, String contentType, Utente currentUser, int idContenuto, JPanel previousPanel) {
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
        chatPanel = createChatPanel();

        // Aggiunge i pannelli al frame
        //frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(chatPanel);

        // Crea il pannello di valutazione e aggiungilo in alto (NORTH) nel frame
        JPanel ratingPanel = createRatingPanel();
        frame.add(ratingPanel, BorderLayout.NORTH);

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

    private JPanel createChatPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight()));
        panel.setBackground(Color.CYAN); // Imposta lo sfondo ciano per l'intero pannello
    
        // Pannello che conterrà tutti i messaggi
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.CYAN); // Imposta lo sfondo ciano per il pannello dei messaggi
    
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
                messagePanel.setBackground(Color.CYAN); // Imposta lo sfondo ciano per il pannello del messaggio
    
                // Etichetta del messaggio
                int idCommento = commento.getIdCommento();
                System.out.println("ID commento: " + idCommento);
                int idCommentoPadre = commentoDao.getParentCommentId(idCommento);
                System.out.println("Commento padre: " + idCommentoPadre);

                if (idCommentoPadre != -1) {
                    System.out.println("Commento padre trovato");
                    System.out.println("ID commento padre: " + idCommentoPadre);
                    String testoCommentoPadre = commentoDao.getCommentTextById(idCommentoPadre);
                    JLabel parentLabel = new JLabel(commento.getUsername() + ": In risposta a ");
                    JLabel italicLabel = new JLabel(testoCommentoPadre);
                    italicLabel.setFont(italicLabel.getFont().deriveFont(Font.ITALIC));
                    JLabel messageLabel = new JLabel(": " + commento.getTesto());

                    messagePanel.add(parentLabel);
                    messagePanel.add(italicLabel);
                    messagePanel.add(messageLabel);
                    
                } else {
                    System.out.println("Commento padre non trovato");
                    System.out.println("ID commento padre: " + idCommentoPadre);
                    // System.out.println("ID commento padre: " + commento.getIdCommentoPadre());
                    JLabel messageLabel = new JLabel(commento.getUsername() + ": " + commento.getTesto());
                    messagePanel.add(messageLabel);
                }
    

                // Crea un pulsante "Rispondi" per ogni commento
                JButton replyButton = new JButton("Rispondi");
                replyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //inputArea.setText("@" + commento.getUsername() + " ");
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
                    Integer idCommentoPadre = (selectedCommentoPadre[0] != null) ? selectedCommentoPadre[0].getIdCommento() : -1;
                    Commento commento = new Commento(0, message, data, idContenuto, currentUser.getUsername(), idCommentoPadre);
    
                    try (Connection connection = DatabaseConnection.getConnection()) {
                        CommentoDao commentoDao = new CommentoDao(connection);
                        commentoDao.createCommento(commento);
    
                        // Aggiungi il nuovo messaggio alla chat
                        JPanel newMessagePanel = new JPanel();
                        newMessagePanel.setLayout(new BoxLayout(newMessagePanel, BoxLayout.X_AXIS));
                        newMessagePanel.setBackground(Color.CYAN); // Imposta lo sfondo ciano per il nuovo pannello del messaggio
    
                        // Se c'è un messaggio padre, visualizzalo
                        if (selectedCommentoPadre[0] != null) {
                            System.out.println("In risposta a: " + selectedCommentoPadre[0].getTesto());
                            JLabel parentLabel = new JLabel(commento.getUsername() + ": In risposta a ");
                            JLabel parentMessageLabel = new JLabel(selectedCommentoPadre[0].getTesto() + "    ");
                            parentMessageLabel.setFont(parentMessageLabel.getFont().deriveFont(Font.ITALIC)); // Opzionale: stile in corsivo
                            newMessagePanel.add(parentLabel);
                            newMessagePanel.add(parentMessageLabel);
                        }
    
                        // Messaggio dell'utente corrente
                        JLabel newMessageLabel = new JLabel(message);
                        newMessagePanel.add(newMessageLabel);
    
                        // Pulsante "Rispondi" per il nuovo messaggio
                        JButton newReplyButton = new JButton("Rispondi");
                        newReplyButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // inputArea.setText("@" + currentUser.getUsername() + " ");
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
    

    private boolean canUserRate(int idContenuto, String username) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AccessoDao accessoDao = new AccessoDao(connection);
            ContenutoDao contenutoDao = new ContenutoDao(connection);
    
            // Ottieni l'ID della casa attraverso la relazione tra Contenuto e Stanza
            Integer idCasa = contenutoDao.getIdCasaByIdContenuto(idContenuto);
            if (idCasa == null) {
                return false; // Il contenuto non è associato a una casa valida
            }
    
            // Verifica se l'utente ha accesso alla casa
            return accessoDao.hasAccess(username, idCasa);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore durante il controllo dell'accesso.");
            return false;
        }
    }

    private JPanel createRatingPanel() {
        JPanel ratingPanel = new JPanel();
    
        JLabel ratingLabel = new JLabel("Valutazione: ");
        String[] ratings = { "1", "2", "3", "4", "5" };
        JComboBox<String> ratingComboBox = new JComboBox<>(ratings);
    
        // Etichetta per visualizzare la media dei voti
        JLabel averageRatingLabel = new JLabel();
        updateAverageRating(averageRatingLabel); // Inizializza la media
    
        // Controlla se l'utente può votare
        if (!canUserRate(idContenuto, currentUser.getUsername())) {
            ratingComboBox.setEnabled(false);
            ratingLabel.setText("Non hai accesso per votare.");
        } else {
            ratingComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        int selectedRating = Integer.parseInt((String) ratingComboBox.getSelectedItem());
                        saveRating(selectedRating, averageRatingLabel); // Passa l'etichetta della media
                    }
                }
            });
        }
    
        ratingPanel.add(ratingLabel);
        ratingPanel.add(ratingComboBox);
        ratingPanel.add(averageRatingLabel);
    
        return ratingPanel;
    }
    

    private void saveRating(int rating, JLabel averageRatingLabel) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ValutazioneDao valutazioneDao = new ValutazioneDao(connection);
    
            // Aggiorna o inserisce la valutazione
            valutazioneDao.saveOrUpdateRating(idContenuto, rating, currentUser.getUsername());
            JOptionPane.showMessageDialog(frame, "Valutazione salvata con successo!");
    
            // Aggiorna la media dei voti
            updateAverageRating(averageRatingLabel); // Passa l'etichetta della media da aggiornare
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel salvataggio della valutazione.");
        }
    }

    private void updateAverageRating(JLabel averageRatingLabel) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ValutazioneDao valutazioneDao = new ValutazioneDao(connection);
            double average = valutazioneDao.getAverageRating(idContenuto);
            averageRatingLabel.setText(String.format("Media: %.2f", average));
        } catch (SQLException ex) {
            ex.printStackTrace();
            averageRatingLabel.setText("Errore nel calcolo della media.");
        }
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
