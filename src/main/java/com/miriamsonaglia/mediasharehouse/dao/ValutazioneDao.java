package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miriamsonaglia.mediasharehouse.model.Valutazione;

public class ValutazioneDao {
    
    private Connection connection;

    // Costruttore che riceve una connessione al database
    public ValutazioneDao(Connection connection) {
        this.connection = connection;
    }

    // Metodo per creare una nuova Valutazione
    public boolean createValutazione(Valutazione valutazione) {
        String sql = "INSERT INTO Valutazione (n_stelle, id_contenuto) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, valutazione.getNStelle());
            pstmt.setInt(2, valutazione.getIdContenuto());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        valutazione.setIdValutazione(rs.getInt(1));  // Imposta l'ID generato
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per leggere una Valutazione dal database tramite il suo ID
    public Valutazione getValutazioneById(int idValutazione) {
        String sql = "SELECT * FROM Valutazione WHERE id_valutazione = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idValutazione);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Valutazione(
                        rs.getInt("id_valutazione"),
                        rs.getInt("n_stelle"),
                        rs.getInt("id_contenuto")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere tutte le valutazioni di un contenuto
    public List<Valutazione> getValutazioniByContenuto(int idContenuto) throws SQLException {
        List<Valutazione> valutazioni = new ArrayList<>();
    
        String query = "SELECT id_valutazione, n_stelle, id_contenuto FROM Valutazione WHERE id_contenuto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idContenuto);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_valutazione");
                    int nStelle = rs.getInt("n_stelle");
                    int contenutoId = rs.getInt("id_contenuto");
    
                    Valutazione valutazione = new Valutazione(id, nStelle, contenutoId);
                    valutazioni.add(valutazione);
                }
            }
        }
        return valutazioni;
    }

    // Metodo per aggiornare una Valutazione esistente
    public boolean updateValutazione(Valutazione valutazione) {
        String sql = "UPDATE Valutazione SET n_stelle = ?, id_contenuto = ? WHERE id_valutazione = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, valutazione.getNStelle());
            pstmt.setInt(2, valutazione.getIdContenuto());
            pstmt.setInt(3, valutazione.getIdValutazione());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare una Valutazione dal database tramite il suo ID
    public boolean deleteValutazione(int idValutazione) {
        String sql = "DELETE FROM Valutazione WHERE id_valutazione = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idValutazione);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare tutte le valutazioni di un contenuto
    public boolean deleteValutazioniByContenuto(int idContenuto) {
        String sql = "DELETE FROM Valutazione WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createValutazione(int idContenuto, int nStelle) throws SQLException {
        String sql = "INSERT INTO Valutazione (n_stelle, id_contenuto) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, nStelle);
            pstmt.setInt(2, idContenuto);
            pstmt.executeUpdate();
        }
    }

    public Integer getUserRating(int idContenuto, String username) throws SQLException {
        String query = "SELECT n_stelle FROM Valutazione WHERE id_contenuto = ? AND username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idContenuto);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("n_stelle");
            }
        }
        return null;
    }

    public void saveOrUpdateRating(int idContenuto, int rating, String username) throws SQLException {
        String querySelect = "SELECT id_valutazione FROM Valutazione WHERE id_contenuto = ? AND username = ?";
        String queryInsert = "INSERT INTO Valutazione (n_stelle, id_contenuto, username) VALUES (?, ?, ?)";
        String queryUpdate = "UPDATE Valutazione SET n_stelle = ? WHERE id_valutazione = ?";
        
        try (PreparedStatement selectStmt = connection.prepareStatement(querySelect)) {
            selectStmt.setInt(1, idContenuto);
            selectStmt.setString(2, username);
            
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    int idValutazione = rs.getInt("id_valutazione");
                    try (PreparedStatement updateStmt = connection.prepareStatement(queryUpdate)) {
                        updateStmt.setInt(1, rating);
                        updateStmt.setInt(2, idValutazione);
                        updateStmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insertStmt = connection.prepareStatement(queryInsert)) {
                        insertStmt.setInt(1, rating);
                        insertStmt.setInt(2, idContenuto);
                        insertStmt.setString(3, username);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
    }

    // Metodo per ottenere la media dei voti per un contenuto
    public double getAverageRating(int idContenuto) {
        String sql = "SELECT AVG(n_stelle) AS average FROM Valutazione WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
