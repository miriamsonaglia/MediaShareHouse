package com.miriamsonaglia.mediasharehouse.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

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
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (password.equals(confirmPassword)) {
                    // Implementare logica di registrazione, ex. salvare le informazioni nel
                    // database
                    JOptionPane.showMessageDialog(frame, "Registrazione completata con successo!");
                    // Torna al menu principale o passa a un'altra schermata
                } else {
                    JOptionPane.showMessageDialog(frame, "Le password non coincidono. Riprova.");
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
