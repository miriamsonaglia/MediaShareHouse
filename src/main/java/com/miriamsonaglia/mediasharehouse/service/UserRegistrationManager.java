package com.miriamsonaglia.mediasharehouse.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.miriamsonaglia.mediasharehouse.dao.DatabaseCreation;

public class UserRegistrationManager {

    private JFrame frame;

    public UserRegistrationManager(JFrame frame) {
        this.frame = frame;
    }

    public void registerUser(String username, String email, String password, String confirmPassword) {
        // Controllo che nessun campo sia vuoto
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Tutti i campi sono obbligatori. Riprova.");
            return;
        }

        // Espressione regolare per validare l'email
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        // Controllo se l'email è valida
        if (!emailPattern.matcher(email).matches()) {
            JOptionPane.showMessageDialog(frame, "L'indirizzo email non è valido. Riprova.");
            return;
        }

        // Controllo se le password coincidono
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Le password non coincidono. Riprova.");
            return;
        }

        try (Connection connection = new DatabaseCreation().connect()) {
            // Verifica se l'username o l'email esistono già
            String checkQuery = "SELECT COUNT(*) FROM Utente WHERE id_utente = ? OR email = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, email);
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(frame, "Username o Email già in uso.");
                    } else {
                        // Se non esistono, inserisci i nuovi dati nel database
                        String insertQuery = "INSERT INTO Utente (id_utente, email, password, id_abbonamento) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                            insertStmt.setString(1, username);
                            insertStmt.setString(2, email);
                            insertStmt.setString(3, password);
                            insertStmt.setInt(4, 1); // Assume l'abbonamento base per i nuovi utenti

                            int rowsInserted = insertStmt.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(frame, "Registrazione completata con successo!");
                                // Torna al menu principale o passa a un'altra schermata
                            } else {
                                JOptionPane.showMessageDialog(frame, "Errore durante la registrazione. Riprova.");
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
    }
}
