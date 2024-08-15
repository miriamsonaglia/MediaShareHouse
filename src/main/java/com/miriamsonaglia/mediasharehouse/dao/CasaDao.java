package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miriamsonaglia.mediasharehouse.model.Casa;


public class CasaDao {
    
    private Connection connection;

    // Costruttore che riceve una connessione al database
    public CasaDao(Connection connection) {
        this.connection = connection;
    }

    // Metodo per creare una nuova Casa
    public boolean createCasa(Casa casa) {
        String sql = "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, casa.getNome());
            pstmt.setString(2, casa.getChiaveAccesso());
            pstmt.setString(3, casa.getStato());
            pstmt.setString(4, casa.getUsername());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        casa.setIdCasa(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per leggere una Casa dal database tramite il suo ID
    public Casa getCasaById(int idCasa) {
        String sql = "SELECT * FROM Casa WHERE id_casa = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCasa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Casa(
                        rs.getInt("id_casa"),
                        rs.getString("nome"),
                        rs.getString("chiave_accesso"),
                        rs.getString("stato"),
                        rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Metodo per ottenere il numero di Case di un utente dal database
    public int getNumeroCaseByUser(String username) {
        String sql = "SELECT COUNT(*) FROM Casa WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    

    // Metodo per ottenere tutte le Case di un utente dal database
    public List<Casa> getCaseByUser(String username) {
        List<Casa> caseList = new ArrayList<>();
        String sql = "SELECT * FROM Casa WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Casa casa = new Casa(
                        rs.getInt("id_casa"),
                        rs.getString("nome"),
                        rs.getString("chiave_accesso"),
                        rs.getString("stato"),
                        rs.getString("username")
                    );
                    caseList.add(casa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caseList;
    }




    // Metodo per ottenere tutte le Case dal database
    public List<Casa> getAllCase() {
        List<Casa> caseList = new ArrayList<>();
        String sql = "SELECT * FROM Casa";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Casa casa = new Casa(
                    rs.getInt("id_casa"),
                    rs.getString("nome"),
                    rs.getString("chiave_accesso"),
                    rs.getString("stato"),
                    rs.getString("username")
                );
                caseList.add(casa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caseList;
    }

    // Metodo per aggiornare una Casa esistente
    public boolean updateCasa(Casa casa) {
        String sql = "UPDATE Casa SET nome = ?, chiave_accesso = ?, stato = ?, username = ? WHERE id_casa = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, casa.getNome());
            pstmt.setString(2, casa.getChiaveAccesso());
            pstmt.setString(3, casa.getStato());
            pstmt.setString(4, casa.getUsername());
            pstmt.setInt(5, casa.getIdCasa());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare una Casa dal database tramite il suo ID
    public boolean deleteCasa(int idCasa) {
        String sql = "DELETE FROM Casa WHERE id_casa = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCasa);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}