package com.miriamsonaglia.mediasharehouse.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.CommentoDao;
import com.miriamsonaglia.mediasharehouse.dao.ContenutoDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Commento;
import com.miriamsonaglia.mediasharehouse.model.Contenuto;
import com.miriamsonaglia.mediasharehouse.model.Stanza;
import com.miriamsonaglia.mediasharehouse.view.Content;

public class ContentManager {

    private JFrame frame;
    private Stanza currentRoom;

    public ContentManager(JFrame frame, Stanza currentRoom) {
        this.frame = frame;
        this.currentRoom = currentRoom;
    }

    public void createNewContent() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ContenutoDao contenutoDao = new ContenutoDao(connection);

            JTextField contentNameField = new JTextField();
            JTextField contentPathField = new JTextField();
            String[] contentTypes = {"song", "movie", "book", "picture"};
            JComboBox<String> typeComboBox = new JComboBox<>(contentTypes);

            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            dialogPanel.add(new JLabel("Nome del contenuto:"));
            dialogPanel.add(contentNameField);
            dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
            dialogPanel.add(new JLabel("Percorso del file:"));
            dialogPanel.add(contentPathField);
            dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
            dialogPanel.add(new JLabel("Tipo di contenuto:"));
            dialogPanel.add(typeComboBox);

            int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Aggiungi nuovo contenuto", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String contentName = contentNameField.getText().trim();
                String contentPath = contentPathField.getText().trim();
                String contentType = (String) typeComboBox.getSelectedItem();

                if (!contentName.isEmpty() && !contentPath.isEmpty()) {
                    Contenuto newContenuto = new Contenuto(0, contentName, contentType, contentPath, currentRoom.getIdStanza(), null);
                    int idContenuto = newContenuto.getIdContenuto();
                    
                    boolean isCreated = contenutoDao.createContenuto(newContenuto);

                    if (isCreated) {
                        JOptionPane.showMessageDialog(frame, "Contenuto aggiunto con successo!");
                        Content.addContentButtonToPanel(contentType, contentPath, contentName, idContenuto);

                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore durante l'aggiunta del contenuto.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Il nome del contenuto e il percorso del file non possono essere vuoti.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
    }

    public void deleteContent(int idContenuto) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        connection.setAutoCommit(false);  // Inizia una transazione

        ContenutoDao contenutoDao = new ContenutoDao(connection);
        CommentoDao commentoDao = new CommentoDao(connection);

        // Conferma l'eliminazione
        int confirm = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare questo contenuto e tutti i suoi commenti?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Ottieni il percorso del file associato al contenuto
            String percorsoFile = contenutoDao.getFilePathFromDatabase(idContenuto);
            
            if (percorsoFile != null && !percorsoFile.isEmpty()) {
                // Elimina fisicamente il file dal filesystem
                File file = new File(percorsoFile);
                if (file.exists()) {
                    if (!file.delete()) {
                        JOptionPane.showMessageDialog(frame, "Errore durante l'eliminazione del file dal filesystem.");
                        connection.rollback();  // Rollback in caso di errore
                        return;
                    }
                }
            }

            // Ottieni tutti i commenti associati al contenuto
            List<Commento> commenti = commentoDao.getCommentiByContenuto(idContenuto);

            // Elimina i commenti
            for (Commento commento : commenti) {
                commentoDao.deleteCommento(commento.getIdCommento());
            }

            // Elimina il contenuto
            boolean isDeleted = contenutoDao.deleteContenuto(idContenuto);
            if (isDeleted) {
                connection.commit();  // Conferma la transazione
                JOptionPane.showMessageDialog(frame, "Contenuto e commenti eliminati con successo.");
                Content.removeContentButtonFromPanel(idContenuto);
            } else {
                connection.rollback();  // Annulla la transazione in caso di errore
                JOptionPane.showMessageDialog(frame, "Errore durante l'eliminazione del contenuto.");
            }
        } else {
            connection.rollback();  // Annulla la transazione se l'utente annulla
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        try {
            // Rollback in caso di errore
            DatabaseConnection.getConnection().rollback();
        } catch (SQLException rollbackEx) {
            rollbackEx.printStackTrace();
        }
        JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
    }
}

}
