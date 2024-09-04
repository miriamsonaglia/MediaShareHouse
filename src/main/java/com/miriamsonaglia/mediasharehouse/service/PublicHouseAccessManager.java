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

import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.view.VisualRoom;

public class PublicHouseAccessManager {

    private JFrame frame;
    private Utente currentUser;
    private JPanel homePanel;  
    private String imagePath;  

    public PublicHouseAccessManager(JFrame frame, Utente currentUser, JPanel homePanel, String imagePath) {
        this.frame = frame;
        this.currentUser = currentUser;
        this.homePanel = homePanel;  
        this.imagePath = imagePath;  
    }

    public void openPublicAccessFrame() {
        JTextField houseNameField = new JTextField();
        JTextField userNameField = new JTextField();
    
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        dialogPanel.add(new JLabel("Nome della Casa:"));
        dialogPanel.add(houseNameField);
        dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
        dialogPanel.add(new JLabel("Nome Utente:"));
        dialogPanel.add(userNameField);
    
        int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Accedi a una Casa Pubblica", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String houseName = houseNameField.getText().trim();
            String userName = userNameField.getText().trim();
    
            if (!houseName.isEmpty() && !userName.isEmpty()) {
                Integer houseId = verifyPublicAccess(houseName, userName);
                if (houseId != null) {
                    frame.getContentPane().removeAll(); // Rimuovi tutti i componenti precedenti             
                    new VisualRoom(frame, homePanel, imagePath, houseId, currentUser); // Passa l'ID della casa
                    frame.revalidate(); // Aggiorna il layout del frame
                    frame.repaint(); // Ridisegna il frame
                } else {
                    JOptionPane.showMessageDialog(frame, "Accesso negato. Casa non pubblica o utente non autorizzato.", "Accesso Negato", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Entrambi i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Integer verifyPublicAccess(String houseName, String userName) {
        String sql = "SELECT id_casa, username FROM Casa WHERE nome = ? AND stato = 'pubblica'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, houseName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbUserName = rs.getString("username");
                    if (dbUserName.equals(userName) || userName.equals(currentUser.getUsername())) {
                        return rs.getInt("id_casa");  // Restituisci l'ID della casa
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}

