package com.miriamsonaglia.mediasharehouse.dao;

import com.miriamsonaglia.mediasharehouse.model.Accesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccessoDao {

    private Connection connection;

    // Costruttore che riceve una connessione al database
    public AccessoDao(Connection connection) {
        this.connection = connection;
    }

    // Metodo per creare un nuovo Accesso
    public boolean createAccesso(Accesso accesso) {
        String sql = "INSERT INTO Accesso (username, id_casa) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, accesso.getUsername());
            pstmt.setInt(2, accesso.getIdCasa());
            int affectedRows = pstmt.executeUpdate();
    
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        accesso.setIdAccesso(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per ottenere un Accesso dal database tramite il suo ID
    public Accesso getAccessoById(int idAccesso) {
        String sql = "SELECT * FROM Accesso WHERE id_accesso = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idAccesso);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Accesso(
                        rs.getInt("id_accesso"),
                        rs.getString("username"),
                        rs.getInt("id_casa")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere tutti gli Accessi di un utente dal database
    public List<Accesso> getAccessiByUsername(String username) {
        List<Accesso> accessiList = new ArrayList<>();
        String sql = "SELECT * FROM Accesso WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Accesso accesso = new Accesso(
                        rs.getInt("id_accesso"),
                        rs.getString("username"),
                        rs.getInt("id_casa")
                    );
                    accessiList.add(accesso);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessiList;
    }

    // Metodo per ottenere una lista di ID case dato l'username di un utente
    public List<Integer> getIdCaseByUsername(String username) {
        List<Integer> idCaseList = new ArrayList<>();
        String sql = "SELECT id_casa FROM Accesso WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idCasa = rs.getInt("id_casa");
                    idCaseList.add(idCasa);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCaseList;
    }

    // Metodo per aggiornare un Accesso esistente
    public boolean updateAccesso(Accesso accesso) {
        String sql = "UPDATE Accesso SET username = ?, id_casa = ? WHERE id_accesso = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, accesso.getUsername());
            pstmt.setInt(2, accesso.getIdCasa());
            pstmt.setInt(3, accesso.getIdAccesso());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare un Accesso dal database tramite il suo ID
    public boolean deleteAccesso(int idAccesso) {
        String sql = "DELETE FROM Accesso WHERE id_accesso = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idAccesso);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare un Accesso dal database tramite l'id della casa e lo username dell'utente
    public boolean deleteAccessoByCasa(int idCasa, String username) {
        String sql = "DELETE FROM Accesso WHERE id_casa = ? AND username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCasa);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}