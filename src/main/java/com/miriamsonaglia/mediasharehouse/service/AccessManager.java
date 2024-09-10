package com.miriamsonaglia.mediasharehouse.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.AccessoDao;
import com.miriamsonaglia.mediasharehouse.dao.CasaDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Accesso;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.view.MSHHome;

public class AccessManager {

    private JFrame frame;
    private Utente currentUser;


    public AccessManager(JFrame frame, Utente currentUser) {
        this.frame = frame;
        this.currentUser = currentUser;
    }

    public void openAccessFrame() {
        // Creazione del dialogo per inserire nome della casa e chiave di accesso
        JTextField houseNameField = new JTextField();
        JTextField houseKeyField = new JTextField();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.add(new JLabel("Nome della Casa:"));
        dialogPanel.add(houseNameField);
        dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
        dialogPanel.add(new JLabel("Chiave di Accesso:"));
        dialogPanel.add(houseKeyField);

        int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Accedi a una Casa", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String houseName = houseNameField.getText().trim();
            String houseKey = houseKeyField.getText().trim();

            if (!houseName.isEmpty() && !houseKey.isEmpty()) {
                boolean accessGranted = verifyAccess(houseName, houseKey);
                if (accessGranted) {

                    try (Connection connection = DatabaseConnection.getConnection()) {
                        CasaDao casaDao = new CasaDao(connection);
                        AccessoDao accessoDao = new AccessoDao(connection);
                        int IdCasa=casaDao.getCasaIdByNomeAndChiave(houseName, houseKey);
                        Accesso accesso = new Accesso(0, currentUser.getUsername(), IdCasa);
                        accessoDao.createAccesso(accesso);

                        // Aggiungo la casa alla lista delle case dell'utente
                        
                        MSHHome.addHouseButtonToPanel(houseName, IdCasa);
                        JOptionPane.showMessageDialog(frame, "Accesso concesso!");


                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Errore nel caricamento delle case dell'utente.");
                    }



                    

                } else {
                    JOptionPane.showMessageDialog(frame, "Nome della casa o chiave di accesso errati.", "Accesso Negato", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Entrambi i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean verifyAccess(String houseName, String houseKey) {
        String sql = "SELECT chiave_accesso FROM Casa WHERE nome = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, houseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbHouseKey = rs.getString("chiave_accesso");
                    return dbHouseKey.equals(houseKey);
                } else {
                    return false;  // Nome della casa non trovato
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
