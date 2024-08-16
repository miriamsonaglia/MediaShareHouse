package com.miriamsonaglia.mediasharehouse.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.CasaDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Casa;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.view.MSHHome;

public class HouseManager {

    private JFrame frame;
    private Utente currentUser;

    public HouseManager(JFrame frame, Utente currentUser) {
        this.frame = frame;
        this.currentUser = currentUser;
    }

    public void createNewHouse() {
        // Creazione della connessione al database
        try (Connection connection = DatabaseConnection.getConnection()) {

            // Crea l'oggetto CasaDao passando la connessione
            CasaDao casaDao = new CasaDao(connection);

            // Controlla il numero di case dell'utente se l'abbonamento è 0
            if (currentUser.getAbbonamento() == 0) {
                int numeroCase = casaDao.getNumeroCaseByUser(currentUser.getUsername());
                if (numeroCase >= 3) {
                    JOptionPane.showMessageDialog(frame, "Non puoi creare più di 3 case con un abbonamento gratuito.");
                    return;  // Esci dal metodo senza creare una nuova casa
                }
            }

            // Creazione del dialogo per inserire nome e stato della casa
            JTextField houseNameField = new JTextField();
            String[] houseStates = {"privata", "pubblica"};
            JComboBox<String> stateComboBox = new JComboBox<>(houseStates);

            JPanel dialogPanel = new JPanel();
            dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
            dialogPanel.add(new JLabel("Nome della casa:"));
            dialogPanel.add(houseNameField);
            dialogPanel.add(Box.createVerticalStrut(10));  // Spaziatura
            dialogPanel.add(new JLabel("Stato della casa:"));
            dialogPanel.add(stateComboBox);

            int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Crea nuova casa", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String houseName = houseNameField.getText().trim();
                String houseState = (String) stateComboBox.getSelectedItem();

                if (!houseName.isEmpty()) {
                    // Creazione di un nuovo oggetto Casa
                    Casa newCasa = new Casa(0, houseName, generateUniqueAccessKey(), houseState, currentUser.getUsername());

                    // Inserimento nel database tramite CasaDao
                    boolean isCreated = casaDao.createCasa(newCasa);

                    if (isCreated) {
                        JOptionPane.showMessageDialog(frame, "Casa creata con successo!");
                        // Passa l'ID della nuova casa al metodo che aggiunge i pulsanti
                        MSHHome.addHouseButtonToPanel(newCasa.getNome(), newCasa.getIdCasa());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore durante la creazione della casa.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Il nome della casa non può essere vuoto.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
    }

    // Funzione per generare una chiave di accesso unica (devi implementarla tu)
    private String generateUniqueAccessKey() {
        // Logica per generare una chiave unica per la casa
        return "unique-key-" + System.currentTimeMillis();  // Esempio semplice
    }
}
