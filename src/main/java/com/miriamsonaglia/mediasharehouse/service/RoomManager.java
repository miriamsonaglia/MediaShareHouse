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
import com.miriamsonaglia.mediasharehouse.dao.StanzaDao;
import com.miriamsonaglia.mediasharehouse.dao.ValutazioneDao;
import com.miriamsonaglia.mediasharehouse.model.Casa;
import com.miriamsonaglia.mediasharehouse.model.Commento;
import com.miriamsonaglia.mediasharehouse.model.Contenuto;
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

    public void deleteRoom(int idStanza) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            StanzaDao stanzaDao = new StanzaDao(connection);
            ContenutoDao contenutoDao = new ContenutoDao(connection);
            CommentoDao commentoDao = new CommentoDao(connection);
            ValutazioneDao valutazioneDao = new ValutazioneDao(connection);

            // Conferma l'eliminazione
            int confirm = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare questa stanza e tutti i suoi contenuti e commenti?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Ottieni tutti i contenuti della stanza
                List<Contenuto> contenuti = contenutoDao.getContenutiByStanza(idStanza);

                // Elimina i commenti e i contenuti e le valutazioni
                for (Contenuto contenuto : contenuti) {

                    // Elimina le valutazioni associate al contenuto
                    valutazioneDao.deleteValutazioniByContenuto(contenuto.getIdContenuto());

                    // Ottieni e elimina tutti i commenti associati al contenuto
                    List<Commento> commenti = commentoDao.getCommentiByContenuto(contenuto.getIdContenuto());
                    for (Commento commento : commenti) {
                        commentoDao.deleteCommento(commento.getIdCommento());
                    }

                    // Elimina il file associato al contenuto
                    String percorsoFile = contenutoDao.getFilePathFromDatabase(contenuto.getIdContenuto());
                    deleteFileFromSystem(percorsoFile);

                    // Elimina il contenuto
                    contenutoDao.deleteContenuto(contenuto.getIdContenuto());
                }

                // Elimina la stanza
                boolean isDeleted = stanzaDao.deleteStanza(idStanza);

                if (isDeleted) {
                    connection.commit();
                    JOptionPane.showMessageDialog(frame, "Stanza eliminata con successo.");
                    Room.removeRoomButtonFromPanel(idStanza);
                } else {
                    connection.rollback();
                    JOptionPane.showMessageDialog(frame, "Errore durante l'eliminazione della stanza.");
                }
            } else {
                connection.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                DatabaseConnection.getConnection().rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
    }

    // Metodo per eliminare un file dal sistema
    private void deleteFileFromSystem(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("File eliminato: " + filePath);
                } else {
                    System.out.println("Errore nell'eliminazione del file: " + filePath);
                }
            } else {
                System.out.println("File non trovato: " + filePath);
            }
        }
    }
}
