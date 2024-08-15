package com.miriamsonaglia.mediasharehouse.view;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.miriamsonaglia.mediasharehouse.service.HouseManager;
import com.miriamsonaglia.mediasharehouse.service.RoomManager;

public final class Room {
    private static JFrame frame;
    private JPanel previousPanel;
    private static JPanel roomPanel;  // Panel principale per la Stanza

    public Room(JFrame existingFrame, JPanel previousPanel, String imagePath) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;

        roomPanel = createRoomPanel(imagePath);

        frame.add(roomPanel);
        frame.revalidate();
        frame.repaint();
    }

    private ImagePanel createRoomPanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
    
        final JLabel titleLabel = new JLabel("House");   //FAI IN MODO CHE LEGGA IL NOME DELLA CASA SELEZIONATA
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.PINK);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        final CustomButton newRoomButton = new CustomButton("ADD A ROOM", customColor, customColor1, 1);
        newRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                //INSERISCI
            }
        });
        panel.add(newRoomButton);
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

    // Metodo per aggiungere un nuovo pulsante per la stanza creata
    public static void addRoomButtonToPanel(String roomName) {
        JButton roomButton = new JButton(roomName);
        roomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        roomButton.setBackground(new Color(101, 67, 33));
        roomButton.setForeground(Color.WHITE);
        
        roomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logica per aprire la stanza selezionata
                JOptionPane.showMessageDialog(frame, "Stanza selezionata: " + roomName);
            }
        });

        roomPanel.add(roomButton);
        roomPanel.add(Box.createVerticalStrut(10));  // Spaziatura tra i pulsanti
        roomPanel.revalidate();
        roomPanel.repaint();
    }

    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }

}
