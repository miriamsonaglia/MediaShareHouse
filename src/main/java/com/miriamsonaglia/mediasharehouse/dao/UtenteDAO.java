package com.miriamsonaglia.mediasharehouse.dao;

import com.miriamsonaglia.mediasharehouse.model.Abbonamento;
import com.miriamsonaglia.mediasharehouse.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {

    private Connection conn;

    // Costruttore per inizializzare la connessione
    public UtenteDAO(Connection conn) {
        this.conn = conn;
    }

    // Metodo per ottenere un utente dal database usando l'id_utente
    public Utente getUtenteById(String idUtente) {
        Utente utente = null;
        String sql = "SELECT u.id_utente, u.email, u.password, a.id_abbonamento, a.tipo, a.limite_case "
                + "FROM Utente u "
                + "JOIN Abbonamento a ON u.id_abbonamento = a.id_abbonamento "
                + "WHERE u.id_utente = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUtente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Abbonamento abbonamento = new Abbonamento(
                            rs.getInt("id_abbonamento"),
                            rs.getString("tipo"),
                            rs.getInt("limite_case")
                    );
                    utente = new Utente(
                            rs.getString("id_utente"),
                            rs.getString("email"),
                            rs.getString("password"),
                            abbonamento
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dell'utente: " + e.getMessage());
        }
        return utente;
    }

    // Metodo per inserire un nuovo utente nel database
    public boolean insertUtente(Utente utente) {
        String sql = "INSERT INTO Utente (id_utente, email, password, id_abbonamento) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utente.getIdUtente());
            pstmt.setString(2, utente.getEmail());
            pstmt.setString(3, utente.getPassword());
            pstmt.setInt(4, utente.getAbbonamento().getIdAbbonamento());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento dell'utente: " + e.getMessage());
            return false;
        }
    }

    // Metodo per aggiornare le informazioni di un utente esistente
    public boolean updateUtente(Utente utente) {
        String sql = "UPDATE Utente SET email = ?, password = ?, id_abbonamento = ? WHERE id_utente = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utente.getEmail());
            pstmt.setString(2, utente.getPassword());
            pstmt.setInt(3, utente.getAbbonamento().getIdAbbonamento());
            pstmt.setString(4, utente.getIdUtente());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento dell'utente: " + e.getMessage());
            return false;
        }
    }

    // Metodo per cancellare un utente dal database
    public boolean deleteUtente(String idUtente) {
        String sql = "DELETE FROM Utente WHERE id_utente = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, idUtente);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Errore durante la cancellazione dell'utente: " + e.getMessage());
            return false;
        }
    }
}
