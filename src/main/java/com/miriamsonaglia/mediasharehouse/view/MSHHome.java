package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.miriamsonaglia.mediasharehouse.dao.CasaDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.dao.UtenteDAO;
import com.miriamsonaglia.mediasharehouse.model.Casa;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.service.HouseManager;

public final class MSHHome {

    private static JFrame frame;
    private JPanel previousPanel;
    private Utente currentUser;
    private static String imagePath;
    private static JPanel homePanel;  // Panel principale per la Home

    public MSHHome(JFrame existingFrame, JPanel previousPanel, String imagePath, Utente currentUser) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;
        this.currentUser = currentUser;
        this.imagePath = imagePath;

        homePanel = createHomePanel(imagePath);

        // Carica le case dell'utente corrente e aggiungi i pulsanti
        loadUserHouses();

        frame.add(homePanel);
        frame.revalidate();
        frame.repaint();
    }

    private ImagePanel createHomePanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
    
        final JLabel titleLabel = new JLabel("Home");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.PINK);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
    
        final CustomButton newHouseButton = new CustomButton("BUILD A HOUSE", customColor, customColor1, 1);
        newHouseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                HouseManager houseManager = new HouseManager(frame, currentUser);
                houseManager.createNewHouse();
            }
        });
        panel.add(newHouseButton);
        panel.add(Box.createVerticalStrut(10));
    
        // Mostra il pulsante "PASSA A MSH PREMIUM!" solo se l'utente ha un abbonamento base
        if (currentUser.getAbbonamento() == 0) {
            System.out.println(currentUser.getAbbonamento());
            final CustomButton premiumButton = new CustomButton("PASSA A MSH PREMIUM!", customColor, customColor1, 1);
            premiumButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(final ActionEvent e) {
                    // Creazione del pannello di dialogo per la conferma
                    JPanel confirmPanel = new JPanel();
                    confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
                    confirmPanel.add(new JLabel("Sei sicuro di voler passare a MSH Premium?"));
                    
                    // Creazione dei pulsanti di conferma e annullamento
                    int result = JOptionPane.showConfirmDialog(frame, confirmPanel, "Conferma Upgrade", JOptionPane.YES_NO_OPTION);
    
                    if (result == JOptionPane.YES_OPTION) {
                        // Codice per aggiornare l'abbonamento dell'utente
                        try (Connection connection = DatabaseConnection.getConnection()) {
                            UtenteDAO utenteDAO = new UtenteDAO(connection);
                            currentUser.setAbbonamento(1); // Aggiorna l'abbonamento a Premium
                            boolean isUpdated = utenteDAO.updateUtente(currentUser); // Metodo per aggiornare l'utente nel database
                            
                            if (isUpdated) {
                                JOptionPane.showMessageDialog(frame, "Congratulazioni! Sei passato a MSH Premium.");
                            } else {
                                JOptionPane.showMessageDialog(frame, "Si è verificato un errore durante l'aggiornamento dell'abbonamento.");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
                        }
                    }
                }
            });
            panel.add(premiumButton);
            panel.add(Box.createVerticalStrut(10));
        }


        // Pulsante per tornare al menu principale
        final CustomButton backButton = new CustomButton("BACK", customColor, customColor1, 1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(previousPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        panel.add(backButton);
        panel.add(Box.createVerticalStrut(10));


        final CustomButton exitButton = new CustomButton("EXIT", customColor, customColor1, 1);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                closeWindow();
            }
        });
        panel.add(exitButton);

        return panel;
    }

    public static void addHouseButtonToPanel(String houseName, int houseId) {
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);

        CustomButton houseButton = new CustomButton(houseName, customColor, customColor1, 1);
        houseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll(); // Rimuovi tutti i componenti precedenti             
                new Room(frame, homePanel, imagePath, houseId); // Passa il frame esistente e il pannello corrente
                frame.revalidate(); // Aggiorna il layout del frame
                frame.repaint(); // Ridisegna il 
            }
        });

        homePanel.add(houseButton);
        homePanel.revalidate();
        homePanel.repaint();
    }

    private void loadUserHouses() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            CasaDao casaDao = new CasaDao(connection);
            List<Casa> userHouses = casaDao.getCaseByUser(currentUser.getUsername());

            for (Casa casa : userHouses) {
                addHouseButtonToPanel(casa.getNome(), casa.getIdCasa());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento delle case dell'utente.");
        }
    }

    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }
}