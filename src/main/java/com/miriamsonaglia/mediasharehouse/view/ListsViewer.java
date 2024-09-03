package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.dao.UtenteDAO;

public final class ListsViewer {
    private static JFrame frame;
    private JPanel previousPanel;
    private static JPanel listsPanel;  // Panel principale per le liste
    private static String imagePath;

    public ListsViewer(JFrame existingFrame, JPanel previousPanel, String imagePath) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;
        this.imagePath = imagePath;

        listsPanel = createListsPanel(imagePath);

        frame.add(listsPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createListsPanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);

        final JLabel titleLabel = new JLabel("Classifiche");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.PINK);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Bottone 1
        final CustomButton housesList = new CustomButton("UTENTI POPOLARI", customColor, customColor1, 1);
        housesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTopCommentersPanel(frame, previousPanel, imagePath);
            }
        });
        panel.add(housesList);
        panel.add(Box.createVerticalStrut(10));

        // Bottone 2
        final CustomButton usersList = new CustomButton("CLASSIFICA UTENTI-CASE", customColor, customColor1, 1);
        usersList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTopUsersPanel(frame, panel, imagePath);
            }
        });
        panel.add(usersList);
        panel.add(Box.createVerticalStrut(10));

        // Pulsante per tornare al menu principale
        final CustomButton backButton = new CustomButton("INDIETRO", customColor, customColor1, 1);
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

        final CustomButton exitButton = new CustomButton("ESCI", customColor, customColor1, 1);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                closeWindow();
            }
        });
        panel.add(exitButton);

        return panel;
    }

    private void showTopCommentersPanel(JFrame existingFrame, JPanel previousPanel, String imagePath) {
        // Creazione del pannello principale con layout verticale
        JPanel topCommentersPanel = new JPanel() {
            // Sovrascrivi il metodo paintComponent per disegnare lo sfondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Carica l'immagine di sfondo
                ImageIcon backgroundIcon = new ImageIcon(imagePath);
                Image backgroundImage = backgroundIcon.getImage();
                // Disegna l'immagine di sfondo che riempie il pannello
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
    
        topCommentersPanel.setLayout(new BoxLayout(topCommentersPanel, BoxLayout.Y_AXIS));
        topCommentersPanel.setOpaque(false); // Rendi il pannello trasparente per mostrare lo sfondo
    
        // Colori personalizzati
        final Color customColor = new Color(218, 165, 32);  // Colore oro
        final Color customColor1 = new Color(101, 67, 33);  // Colore marrone
    
        // Titolo
        JLabel titleLabel = new JLabel("Top Users by Number of Comments");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        titleLabel.setForeground(Color.PINK);
        topCommentersPanel.add(titleLabel);
        topCommentersPanel.add(Box.createVerticalStrut(20));
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            UtenteDAO utenteDao = new UtenteDAO(conn);
            List<String> topUsers = utenteDao.getTopUsersByNumberOfComments(5);
    
            if (topUsers.isEmpty()) {
                JLabel noUsersLabel = new JLabel("Nessun utente trovato.");
                noUsersLabel.setForeground(Color.WHITE);
                topCommentersPanel.add(noUsersLabel);
            } else {
                for (String user : topUsers) {
                    JLabel userLabel = new JLabel(user);
                    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    userLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                    userLabel.setForeground(Color.WHITE);
                    topCommentersPanel.add(userLabel);
                }
            }
        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("Errore nella connessione al database.");
            errorLabel.setForeground(Color.RED);
            topCommentersPanel.add(errorLabel);
            e.printStackTrace();
        }
    
        // Pulsante per tornare al menu principale
        final CustomButton backButton = new CustomButton("INDIETRO", customColor, customColor1, 1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(previousPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        topCommentersPanel.add(Box.createVerticalStrut(20));
        topCommentersPanel.add(backButton);
    
        // Aggiornamento della finestra
        existingFrame.getContentPane().removeAll();
        existingFrame.getContentPane().add(topCommentersPanel);
        existingFrame.revalidate();
        existingFrame.repaint();
    }

    private void showTopUsersPanel(JFrame existingFrame, JPanel previousPanel, String imagePath) {
        // Creazione del pannello principale con layout verticale
        JPanel topUsersPanel = new JPanel() {
            // Sovrascrivi il metodo paintComponent per disegnare lo sfondo
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Carica l'immagine di sfondo
                ImageIcon backgroundIcon = new ImageIcon(imagePath);
                Image backgroundImage = backgroundIcon.getImage();
                // Disegna l'immagine di sfondo che riempie il pannello
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        topUsersPanel.setLayout(new BoxLayout(topUsersPanel, BoxLayout.Y_AXIS));
        topUsersPanel.setOpaque(false); // Rendi il pannello trasparente per mostrare lo sfondo
    
        // Colori personalizzati
        final Color customColor = new Color(218, 165, 32);  // Colore oro
        final Color customColor1 = new Color(101, 67, 33);  // Colore marrone
    
        // Titolo
        JLabel titleLabel = new JLabel("Top Users by Number of Houses");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
        titleLabel.setForeground(Color.PINK);
        topUsersPanel.add(titleLabel);
        topUsersPanel.add(Box.createVerticalStrut(20));
    
        try (Connection conn = DatabaseConnection.getConnection()) {
            UtenteDAO utenteDao = new UtenteDAO(conn);
            List<String> topUsers = utenteDao.getTopUsersByNumberOfHouses(5);
    
            if (topUsers.isEmpty()) {
                JLabel noUsersLabel = new JLabel("Nessun utente trovato.");
                noUsersLabel.setForeground(Color.WHITE);
                topUsersPanel.add(noUsersLabel);
            } else {
                for (String user : topUsers) {
                    JLabel userLabel = new JLabel(user);
                    userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    userLabel.setFont(new Font("Arial", Font.PLAIN, 20));
                    userLabel.setForeground(Color.WHITE);
                    topUsersPanel.add(userLabel);
                }
            }
        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("Errore nella connessione al database.");
            errorLabel.setForeground(Color.RED);
            topUsersPanel.add(errorLabel);
            e.printStackTrace();
        }

    // Pulsante per tornare al menu principale
    final CustomButton backButton = new CustomButton("INDIETRO", customColor, customColor1, 1);
    backButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(previousPanel);
            frame.revalidate();
            frame.repaint();
        }
    });
    topUsersPanel.add(Box.createVerticalStrut(20));
    topUsersPanel.add(backButton);

    // Aggiornamento della finestra
    existingFrame.getContentPane().removeAll();
    existingFrame.getContentPane().add(topUsersPanel);
    existingFrame.revalidate();
    existingFrame.repaint();
}


    private void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }
}
