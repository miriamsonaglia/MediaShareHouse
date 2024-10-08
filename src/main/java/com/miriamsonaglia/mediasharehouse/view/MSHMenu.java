package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.DatabaseCreation;
import com.miriamsonaglia.mediasharehouse.dao.UtenteDAO;
import com.miriamsonaglia.mediasharehouse.model.Utente;

// import sun.security.provider.DSAKeyPairGenerator;

public final class MSHMenu {

    private JFrame frame;
    private JPanel loginPanel;

    public MSHMenu(final String title) {
        // Definizione dei colori personalizzati
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
        final ImageIcon imageIcon = new ImageIcon("src/main/resources/Icon.png");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame(title);
        frame.setIconImage(imageIcon.getImage());
        frame.setSize(screenSize.width, screenSize.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final String imagePath = "src/main/resources/Background.jpg";
        loginPanel = createLoginPanel(imagePath, customColor, customColor1);

        frame.add(loginPanel);
        frame.setVisible(true);
    }

    private ImagePanel createLoginPanel(String imagePath, Color customColor, Color customColor1) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final JLabel titleLabel = new JLabel("MediaShare House");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.PINK);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Campo di testo per lo username
        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300, 40));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        usernameField.setBackground(Color.CYAN);
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(10));

        // Campo di testo per la password
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setBackground(Color.CYAN);
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));

        // Pulsante LOGIN
        final CustomButton loginButton = new CustomButton("ACCEDI", customColor, customColor1, 1);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Connessione al database e verifica delle credenziali
                try (Connection connection = new DatabaseCreation().connect()) { // Usa la connessione dal
                    // DatabaseCreation
                    String query = "SELECT COUNT(*) FROM Utente WHERE username = ? AND password = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next() && resultSet.getInt(1) > 0) {
                                // Credenziali valide
                                frame.getContentPane().removeAll(); // Rimuovi tutti i componenti precedenti
                                
                                UtenteDAO inizializeUser = new UtenteDAO(connection);


                                int abbonamentoCurrentUser = inizializeUser.getAbbonamentoById(username);
                                String emailCurrentUser = inizializeUser.getEmailById(username);

                                
                                Utente currentUser = new Utente(username, emailCurrentUser, password, abbonamentoCurrentUser);

                                System.out.println("1" + currentUser.getUsername());
                                System.out.println("1" + currentUser.getAbbonamento());
                                System.out.println("1" + currentUser.getEmail());
                                System.out.println("1" + currentUser.getPassword());
                                
                                new MSHHome(frame, panel, imagePath, currentUser); // Passa il frame esistente e il pannello corrente
                                frame.revalidate(); // Aggiorna il layout del frame
                                frame.repaint(); // Ridisegna il 

                            } else {
                                // Credenziali non valide
                                JOptionPane.showMessageDialog(frame, "Username o Password errati.");
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
                }
            }
        });
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));

        

        // Pulsante SIGN IN
        final CustomButton signInButton = new CustomButton("REGISTRATI", customColor, customColor1, 1);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new RegistrationMenu(frame, panel, imagePath); // Passa il pannello di login
            }
        });
        panel.add(signInButton);
        panel.add(Box.createVerticalStrut(10));

        // Pulsante EXIT
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

    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }
}
