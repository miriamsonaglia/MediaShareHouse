package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.miriamsonaglia.mediasharehouse.model.Utente;

public class UtenteDAO {

    private Connection conn;

    // Costruttore per inizializzare la connessione
    public UtenteDAO(Connection conn) {
        this.conn = conn;
    }


    // Metodo per ottenere l'email di un utente dal database usando l'username
    public String getEmailById(String username) {
        String email = null; // Email da restituire
        String sql = "SELECT email FROM Utente WHERE username = ?"; // Query per selezionare l'email usando l'username

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Creazione dello statement
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    email = rs.getString("email");
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dell'email utente: " + e.getMessage());
        }
        return email;
    }

    public int getAbbonamentoById(String username) {
        int abbonamento = 0; // Abbonamento da restituire, di default è 0
        String sql = "SELECT abbonamento FROM Utente WHERE username = ?"; // Query per selezionare l'email usando l'username

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Creazione dello statement
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    abbonamento = rs.getInt("abbonamento");
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dell'abbonamento utente: " + e.getMessage());
        }
        return abbonamento;
    }


    // Metodo per inserire un nuovo utente nel database
    public boolean insertUtente(Utente utente) {
        String sql = "INSERT INTO Utente (username, email, password, abbonamento) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utente.getUsername());
            pstmt.setString(2, utente.getEmail());
            pstmt.setString(3, utente.getPassword());
            pstmt.setInt(4, utente.getAbbonamento());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento dell'utente: " + e.getMessage());
            return false;
        }
    }

    // Metodo per aggiornare le informazioni di un utente esistente
    public boolean updateUtente(Utente utente) {
        String sql = "UPDATE Utente SET email = ?, password = ?, abbonamento = ? WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utente.getEmail());
            pstmt.setString(2, utente.getPassword());
            pstmt.setInt(3, utente.getAbbonamento());
            pstmt.setString(4, utente.getUsername());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
            return false;
        }
    }

    // Metodo per cancellare un utente dal database
    public boolean deleteUtente(String username) {
        String sql = "DELETE FROM Utente WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore durante la cancellazione dell'utente: " + e.getMessage());
            return false;
        }
    }
}
