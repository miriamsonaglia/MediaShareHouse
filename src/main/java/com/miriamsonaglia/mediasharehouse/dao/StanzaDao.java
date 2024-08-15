package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miriamsonaglia.mediasharehouse.model.Stanza;

public class StanzaDao {
    
    private Connection connection;

    // Costruttore che riceve una connessione al database
    public StanzaDao(Connection connection) {
        this.connection = connection;
    }

    // Metodo per creare una nuova Stanza
    public boolean createStanza(Stanza stanza) {
        String sql = "INSERT INTO Stanza (tipo, id_casa) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, stanza.getTipo());
            pstmt.setInt(2, stanza.getIdCasa());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        stanza.setIdStanza(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per leggere una Stanza dal database tramite il suo ID
    public Stanza getStanzaById(int idStanza) {
        String sql = "SELECT * FROM Stanza WHERE id_stanza = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idStanza);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Stanza(
                        rs.getInt("id_stanza"),
                        rs.getString("tipo"),
                        rs.getInt("id_casa")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere tutte le Stanze di una Casa dal database
    public List<Stanza> getStanzeByCasa(int idCasa) {
        List<Stanza> stanzeList = new ArrayList<>();
        String sql = "SELECT * FROM Stanza WHERE id_casa = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCasa);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Stanza stanza = new Stanza(
                        rs.getInt("id_stanza"),
                        rs.getString("tipo"),
                        rs.getInt("id_casa")
                    );
                    stanzeList.add(stanza);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stanzeList;
    }

    // Metodo per ottenere tutte le Stanze dal database
    public List<Stanza> getAllStanze() {
        List<Stanza> stanzeList = new ArrayList<>();
        String sql = "SELECT * FROM Stanza";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Stanza stanza = new Stanza(
                    rs.getInt("id_stanza"),
                    rs.getString("tipo"),
                    rs.getInt("id_casa")
                );
                stanzeList.add(stanza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stanzeList;
    }

    // Metodo per aggiornare una Stanza esistente
    public boolean updateStanza(Stanza stanza) {
        String sql = "UPDATE Stanza SET tipo = ?, id_casa = ? WHERE id_stanza = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, stanza.getTipo());
            pstmt.setInt(2, stanza.getIdCasa());
            pstmt.setInt(3, stanza.getIdStanza());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare una Stanza dal database tramite il suo ID
    public boolean deleteStanza(int idStanza) {
        String sql = "DELETE FROM Stanza WHERE id_stanza = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idStanza);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

