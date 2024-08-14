package com.miriamsonaglia.mediasharehouse.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.DatabaseCreation;
import com.miriamsonaglia.mediasharehouse.view.MSHHome;

public class HouseManager {

    private JFrame frame;

    public HouseManager(JFrame frame) {
        this.frame = frame;
    }

    public void createNewHouse() {
        // Creazione del dialogo per inserire nome e stato della casa
        JTextField houseNameField = new JTextField();
        String[] houseStates = {"privata", "pubblica"};
        JComboBox<String> stateComboBox = new JComboBox<>(houseStates);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.add(new JLabel("Nome della casa:"));
        dialogPanel.add(houseNameField);
        dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
        dialogPanel.add(new JLabel("Stato della casa:"));
        dialogPanel.add(stateComboBox);

        int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Crea nuova casa", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String houseName = houseNameField.getText().trim();
            String houseState = (String) stateComboBox.getSelectedItem();

            if (!houseName.isEmpty()) {
                // Inserimento nel database
                try (Connection connection = new DatabaseCreation().connect()) {
                    String insertQuery = "INSERT INTO Casa (nome, chiave_accesso, stato, id_utente) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setString(1, houseName);
                        insertStmt.setString(2, generateUniqueAccessKey()); // Funzione che genera una chiave unica
                        insertStmt.setString(3, houseState);
                        insertStmt.setString(4, "currentUser");

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(frame, "Casa creata con successo!");
                            MSHHome.addHouseButtonToPanel(houseName);  // Aggiungi il pulsante della casa
                        } else {
                            JOptionPane.showMessageDialog(frame, "Errore durante la creazione della casa.");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Il nome della casa non può essere vuoto.");
            }
        }
    }

    // Funzione per generare una chiave di accesso unica (devi implementarla tu)
    private String generateUniqueAccessKey() {
        // Logica per generare una chiave unica per la casa
        return "unique-key-" + System.currentTimeMillis();  // Esempio semplice
    }
}
