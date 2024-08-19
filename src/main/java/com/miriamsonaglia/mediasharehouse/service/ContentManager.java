package com.miriamsonaglia.mediasharehouse.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.ContenutoDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
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
}
