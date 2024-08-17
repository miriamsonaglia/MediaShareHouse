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

import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.dao.StanzaDao;
import com.miriamsonaglia.mediasharehouse.model.Casa;
import com.miriamsonaglia.mediasharehouse.model.Stanza;
import com.miriamsonaglia.mediasharehouse.view.Room;

public class RoomManager {

    private JFrame frame;
    private Casa currentHouse;

    public RoomManager(JFrame frame, Casa currentHouse) {
        this.frame = frame;
        this.currentHouse = currentHouse;
    }

    public void createNewRoom() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            StanzaDao stanzaDao = new StanzaDao(connection);

            JTextField roomNameField = new JTextField();
            String[] roomTypes = {"Music", "Movies", "Books", "Pictures"};
            JComboBox<String> typeComboBox = new JComboBox<>(roomTypes);

            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            dialogPanel.add(new JLabel("Nome della stanza:"));
            dialogPanel.add(roomNameField);
            dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
            dialogPanel.add(new JLabel("Tipo della stanza:"));
            dialogPanel.add(typeComboBox);

            int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Crea nuova stanza", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String roomName = roomNameField.getText().trim();
                String roomType = (String) typeComboBox.getSelectedItem();

                if (!roomName.isEmpty()) {
                    Stanza newStanza = new Stanza(roomName, roomType, currentHouse.getIdCasa());
                    boolean isCreated = stanzaDao.createStanza(newStanza);
                    int idNewStanza = newStanza.getIdStanza();


                    if (isCreated) {
                        JOptionPane.showMessageDialog(frame, "Stanza creata con successo!");
                        Room.addRoomButtonToPanel(roomName, idNewStanza);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore durante la creazione della stanza.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Il nome della stanza non può essere vuoto.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
    }
}
