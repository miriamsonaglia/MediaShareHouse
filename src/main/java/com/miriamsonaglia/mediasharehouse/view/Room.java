package com.miriamsonaglia.mediasharehouse.view;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.miriamsonaglia.mediasharehouse.dao.CasaDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.dao.StanzaDao;
import com.miriamsonaglia.mediasharehouse.model.Casa;
import com.miriamsonaglia.mediasharehouse.model.Stanza;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.service.RoomManager;

public final class Room {
    private static JFrame frame;
    private JPanel previousPanel;
    private static JPanel roomPanel;  // Panel principale per la Stanza
    private Casa currentHouse;
    private static String imagePath;
    private static Utente currentUser;

    private static Map<Integer, CustomButton> roomButtonMap = new HashMap<>();

    public Room(JFrame existingFrame, JPanel previousPanel, String imagePath, int houseId, Utente currentUser) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;
        this.imagePath = imagePath;
        this.currentUser = currentUser;

        this.currentHouse = fetchHouseById(houseId);

        roomPanel = createRoomPanel(imagePath);
        loadHouseRooms();

        frame.add(roomPanel);
        frame.revalidate();
        frame.repaint();
    }

    private Casa fetchHouseById(int houseId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            CasaDao casaDao = new CasaDao(connection);
            return casaDao.getCasaById(houseId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
            return null;
        }
    }

    private ImagePanel createRoomPanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
    
        final JLabel titleLabel = new JLabel("House");   //FAI IN MODO CHE LEGGA IL NOME DELLA CASA SELEZIONATA
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.PINK);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        final CustomButton newRoomButton = new CustomButton("AGGIUNGI UNA STANZA", customColor, customColor1, 1);
        newRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                RoomManager roomManager = new RoomManager(frame, currentHouse);
                roomManager.createNewRoom();  
            }
        });
        panel.add(newRoomButton);
        panel.add(Box.createVerticalStrut(10));

        final CustomButton deleteButton = new CustomButton("ELIMINA UNA STANZA", customColor, customColor1, 1);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                showDeleteRoomDialog();
            }
        });
        panel.add(deleteButton);
        panel.add(Box.createVerticalStrut(10));

        // Pulsante per tornare al menu principale
        final CustomButton backButton = new CustomButton("INDIETRO", customColor, customColor1, 1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(previousPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        panel.add(backButton);
        panel.add(Box.createVerticalStrut(10));

        final CustomButton exitButton = new CustomButton("ESCI", customColor, customColor1, 1);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                closeWindow();
            }
        });
        panel.add(exitButton);

        return panel;

    }

    private void showDeleteRoomDialog() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            StanzaDao stanzaDao = new StanzaDao(connection);
            List<Stanza> userRooms = stanzaDao.getStanzeByCasa(currentHouse.getIdCasa());
            if (userRooms.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Non hai stanze da eliminare.");
                return;
            }
    
            // Creazione del dialogo per la selezione della casa da eliminare
            JComboBox<Stanza> roomComboBox = new JComboBox<>(userRooms.toArray(new Stanza[0]));
            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            dialogPanel.add(new JLabel("Seleziona la stanza da eliminare:"));
            dialogPanel.add(roomComboBox);
    
            int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Elimina Stanza", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Stanza selectedRoom = (Stanza) roomComboBox.getSelectedItem();
                if (selectedRoom != null) {
                    RoomManager roomManager = new RoomManager(frame, currentHouse);
                    roomManager.deleteRoom(selectedRoom.getIdStanza());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
    }

    // Metodo per aggiungere un nuovo pulsante per la stanza creata
    public static void addRoomButtonToPanel(String roomName, int roomId) {
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
        
        CustomButton roomButton = new CustomButton(roomName, Color.GREEN, customColor1, 1);
        roomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gestisci l'azione del pulsante
                System.out.println("ID Stanza selezionata: " + roomId);
                frame.getContentPane().removeAll();
                new Content(frame, roomPanel, imagePath, roomId, currentUser);
                frame.revalidate();
                frame.repaint();
            }
        });
    
        roomPanel.add(roomButton, 1);
        roomPanel.add(Box.createVerticalStrut(10));  // Spaziatura tra i pulsanti
    
        // Aggiungi il pannello al frame e aggiorna la visualizzazione
        roomPanel.revalidate();
        roomPanel.repaint();
        roomButtonMap.put(roomId, roomButton);
    }

    public static void removeRoomButtonFromPanel(int roomId) {
        CustomButton buttonToRemove = roomButtonMap.remove(roomId);
        if (buttonToRemove != null) {
            roomPanel.remove(buttonToRemove);
            roomPanel.revalidate();
            roomPanel.repaint();
        }
    }

    private void loadHouseRooms() {
        System.out.println("Caricamento delle stanze della casa: " + currentHouse.getNome());
        try (Connection connection = DatabaseConnection.getConnection()) {
            StanzaDao stanzaDao = new StanzaDao(connection);
            List<Stanza> houseRooms = stanzaDao.getStanzeByCasa(currentHouse.getIdCasa());

            for (Stanza stanza : houseRooms) {
                addRoomButtonToPanel(stanza.getNome(), stanza.getIdStanza());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento delle stanze della casa.");
        }
    }

    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }

}
