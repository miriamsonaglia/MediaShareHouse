package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.DatabaseCreation;

public final class RegistrationMenu {

    private JFrame frame;
    private JPanel previousPanel;

    public RegistrationMenu(JFrame existingFrame, JPanel previousPanel, String imagePath) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;

        ImagePanel registrationPanel = createRegistrationPanel(imagePath);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(registrationPanel);
        frame.revalidate();
        frame.repaint();
    }

    private ImagePanel createRegistrationPanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);

        final JLabel titleLabel = new JLabel("Register");
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

        // Campo di testo per la mail
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(300, 40));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        emailField.setBackground(Color.CYAN);
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));

        // Campo di testo per la password
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(300, 40));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setBackground(Color.CYAN);
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));

        // Campo di testo per confermare la password
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(300, 40));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPasswordField.setBorder(BorderFactory.createTitledBorder("Confirm Password"));
        confirmPasswordField.setBackground(Color.CYAN);
        panel.add(confirmPasswordField);
        panel.add(Box.createVerticalStrut(20));

        // Pulsante di registrazione
        final CustomButton registerButton = new CustomButton("REGISTER", customColor, customColor1, 1);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

                // Controllo che nessun campo sia vuoto
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Tutti i campi sono obbligatori. Riprova.");
                    return;
                }

                // Espressione regolare per validare l'email
                String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
                Pattern emailPattern = Pattern.compile(emailRegex);

                // Controllo se l'email è valida
                if (!emailPattern.matcher(email).matches()) {
                    JOptionPane.showMessageDialog(frame, "L'indirizzo email non è valido. Riprova.");
                    return;
                }

                // Controllo se le password coincidono
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(frame, "Le password non coincidono. Riprova.");
                    return;
                }

                try (Connection connection = new DatabaseCreation().connect()) {
                    // Verifica se l'username o l'email esistono già
                    String checkQuery = "SELECT COUNT(*) FROM Utente WHERE id_utente = ? OR email = ?";
                    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                        checkStmt.setString(1, username);
                        checkStmt.setString(2, email);
                        try (ResultSet resultSet = checkStmt.executeQuery()) {
                            if (resultSet.next() && resultSet.getInt(1) > 0) {
                                JOptionPane.showMessageDialog(frame, "Username o Email già in uso.");
                            } else {
                                // Se non esistono, inserisci i nuovi dati nel database
                                String insertQuery = "INSERT INTO Utente (id_utente, email, password, id_abbonamento) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                                    insertStmt.setString(1, username);
                                    insertStmt.setString(2, email);
                                    insertStmt.setString(3, password);
                                    insertStmt.setInt(4, 1); // Assume l'abbonamento base per i nuovi utenti

                                    int rowsInserted = insertStmt.executeUpdate();
                                    if (rowsInserted > 0) {
                                        JOptionPane.showMessageDialog(frame, "Registrazione completata con successo!");
                                        // Torna al menu principale o passa a un'altra schermata
                                    } else {
                                        JOptionPane.showMessageDialog(frame, "Errore durante la registrazione. Riprova.");
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
                }
            }
        });
        panel.add(registerButton);
        panel.add(Box.createVerticalStrut(10));

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

        return panel;
    }
}
