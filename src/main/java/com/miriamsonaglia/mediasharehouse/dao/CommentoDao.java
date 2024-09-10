package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miriamsonaglia.mediasharehouse.model.Commento;

public class CommentoDao {
    
    private Connection connection;

    // Costruttore che riceve una connessione al database
    public CommentoDao(Connection connection) {
        this.connection = connection;
    }

    // Metodo per creare un nuovo Commento
    public boolean createCommento(Commento commento) {
        String sql = "INSERT INTO Commento (testo, data_commento, id_contenuto, username, id_commento_padre) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, commento.getTesto());
            pstmt.setTimestamp(2, commento.getDataCommento());
            pstmt.setInt(3, commento.getIdContenuto());
            pstmt.setString(4, commento.getUsername());
            pstmt.setObject(5, commento.getIdCommentoPadre(), java.sql.Types.INTEGER);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        commento.setIdCommento(rs.getInt(1));  // Imposta l'ID generato
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per leggere un Commento dal database tramite il suo ID
    public Commento getCommentoById(int idCommento) {
        String sql = "SELECT * FROM Commento WHERE id_commento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCommento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Commento(
                        rs.getInt("id_commento"),
                        rs.getString("testo"),
                        rs.getTimestamp("data_commento"),
                        rs.getInt("id_contenuto"),
                        rs.getString("username"),
                        rs.getObject("id_commento_padre", Integer.class)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere tutti i commenti di un contenuto
    public List<Commento> getCommentiByContenuto(int idContenuto) throws SQLException {
        List<Commento> commenti = new ArrayList<>();
    
        String query = "SELECT id_commento, testo, data_commento, id_contenuto, username FROM commento WHERE id_contenuto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idContenuto);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_commento");
                    String testo = rs.getString("testo");
                    java.sql.Timestamp data = rs.getTimestamp("data_commento");
                    int contenutoId = rs.getInt("id_contenuto");
                    String username = rs.getString("username");
    
                    Commento commento = new Commento(id, testo, data, contenutoId, username, null);
                    commenti.add(commento);
                }
            }
        }
        return commenti;
    }




    // Metodo per aggiornare un Commento esistente
    public boolean updateCommento(Commento commento) {
        String sql = "UPDATE Commento SET testo = ?, data_commento = ?, id_contenuto = ?, username = ?, id_commento_padre = ? WHERE id_commento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, commento.getTesto());
            pstmt.setTimestamp(2, commento.getDataCommento());
            pstmt.setInt(3, commento.getIdContenuto());
            pstmt.setString(4, commento.getUsername());
            pstmt.setObject(5, commento.getIdCommentoPadre(), java.sql.Types.INTEGER);
            pstmt.setInt(6, commento.getIdCommento());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare un Commento dal database tramite il suo ID
    public boolean deleteCommento(int idCommento) {
        String sql = "DELETE FROM Commento WHERE id_commento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCommento);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCommentiByContenuto(int idContenuto) {
        String sql = "DELETE FROM Commento WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Metodo per ottenere l'id del commento padre dato l'id di un commento
    public Integer getParentCommentId(int commentId) {
        String sql = "SELECT id_commento_padre FROM Commento WHERE id_commento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_commento_padre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere il testo di un Commento dato il suo ID
    public String getCommentTextById(int idCommento) {
        String sql = "SELECT testo FROM Commento WHERE id_commento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idCommento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("testo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
