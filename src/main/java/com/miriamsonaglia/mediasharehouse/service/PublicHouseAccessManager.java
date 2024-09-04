package com.miriamsonaglia.mediasharehouse.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Utente;

public class PublicHouseAccessManager {

    private JFrame frame;
    private Utente currentUser;

    public PublicHouseAccessManager(JFrame frame, Utente currentUser) {
        this.frame = frame;
        this.currentUser = currentUser;
    }

    public void openPublicAccessFrame() {
        // Creazione del dialogo per inserire nome della casa e nome dell'utente
        JTextField houseNameField = new JTextField();
        JTextField userNameField = new JTextField();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.add(new JLabel("Nome della Casa:"));
        dialogPanel.add(houseNameField);
        dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
        dialogPanel.add(new JLabel("Nome Utente:"));
        dialogPanel.add(userNameField);

        int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Accedi a una Casa Pubblica", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String houseName = houseNameField.getText().trim();
            String userName = userNameField.getText().trim();

            if (!houseName.isEmpty() && !userName.isEmpty()) {
                boolean accessGranted = verifyPublicAccess(houseName, userName);
                if (accessGranted) {
                    JOptionPane.showMessageDialog(frame, "Accesso concesso!");

                    // Logica aggiuntiva per gestire l'accesso alla casa pubblica
                    // ...

                } else {
                    JOptionPane.showMessageDialog(frame, "Accesso negato. Casa non pubblica o utente non autorizzato.", "Accesso Negato", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Entrambi i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean verifyPublicAccess(String houseName, String userName) {
        String sql = "SELECT * FROM Casa WHERE nome = ? AND stato = 'pubblica'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, houseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbUserName = rs.getString("username");
                    return dbUserName.equals(userName) || userName.equals(currentUser.getUsername());
                } else {
                    return false;  // Casa non trovata o non pubblica
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}

