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

}
