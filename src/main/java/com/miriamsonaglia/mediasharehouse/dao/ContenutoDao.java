package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.miriamsonaglia.mediasharehouse.model.Contenuto;

public class ContenutoDao {
    
    private Connection connection;

    // Costruttore che riceve una connessione al database
    public ContenutoDao(Connection connection) {
        this.connection = connection;
    }

    // Metodo per creare un nuovo Contenuto
    public boolean createContenuto(Contenuto contenuto) {
        String sql = "INSERT INTO Contenuto (nome, tipo, percorso_file, id_stanza, username) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, contenuto.getNome());
            pstmt.setString(2, contenuto.getTipo());
            pstmt.setString(3, contenuto.getPercorsoFile());
            pstmt.setInt(4, contenuto.getIdStanza());
            pstmt.setString(5, contenuto.getUsername());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        contenuto.setIdContenuto(rs.getInt(1));  // Imposta l'ID generato
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per leggere un Contenuto dal database tramite il suo ID
    public Contenuto getContenutoById(int idContenuto) {
        String sql = "SELECT * FROM Contenuto WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Contenuto(
                        rs.getInt("id_contenuto"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("percorso_file"),
                        rs.getInt("id_stanza"),
                        rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere tutti i contenuti di una specifica stanza
    public List<Contenuto> getContenutiByStanza(int idStanza) {
        List<Contenuto> contenutiList = new ArrayList<>();
        String sql = "SELECT * FROM Contenuto WHERE id_stanza = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idStanza);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contenuto contenuto = new Contenuto(
                        rs.getInt("id_contenuto"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("percorso_file"),
                        rs.getInt("id_stanza"),
                        rs.getString("username")
                    );
                    contenutiList.add(contenuto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contenutiList;
    }

    // Metodo per ottenere il nome di un Contenuto dato il suo ID
    public String getNomeById(int idContenuto) {
        String sql = "SELECT nome FROM Contenuto WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Metodo per aggiornare un Contenuto esistente
    public boolean updateContenuto(Contenuto contenuto) {
        String sql = "UPDATE Contenuto SET nome = ?, tipo = ?, percorso_file = ?, id_stanza = ?, username = ? WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, contenuto.getNome());
            pstmt.setString(2, contenuto.getTipo());
            pstmt.setString(3, contenuto.getPercorsoFile());
            pstmt.setInt(4, contenuto.getIdStanza());
            pstmt.setString(5, contenuto.getUsername());
            pstmt.setInt(6, contenuto.getIdContenuto());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per eliminare un Contenuto dal database tramite il suo ID
    public boolean deleteContenuto(int idContenuto) {
        String sql = "DELETE FROM Contenuto WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Metodo per aggiornare il percorso di un Contenuto esistente
    public boolean updateContenutoPath(int idContenuto, String percorsoFile) {
        String sql = "UPDATE Contenuto SET percorso_file = ? WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, percorsoFile);
            pstmt.setInt(2, idContenuto);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getFilePathFromDatabase(int idContenuto) {
        String sql = "SELECT percorso_file FROM Contenuto WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("percorso_file");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Metodo per ottenere l'ID della casa dato l'ID del contenuto
    public Integer getIdCasa(int idContenuto) {
        String sql = "SELECT id_casa FROM Contenuto WHERE id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_casa");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Ritorna null se non trova il contenuto o in caso di errore
    }

    public Integer getIdCasaByIdContenuto(int idContenuto) {
        String sql = "SELECT s.id_casa " +
                     "FROM Contenuto c " +
                     "JOIN Stanza s ON c.id_stanza = s.id_stanza " +
                     "WHERE c.id_contenuto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idContenuto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_casa");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
