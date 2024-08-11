package com.miriamsonaglia.mediasharehouse.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.miriamsonaglia.mediasharehouse.service.HouseManager;

public final class MSHHome {

    private JFrame frame;
    private JPanel previousPanel;
    private static JPanel homePanel;  // Panel principale per la Home
    private static List<JButton> houseButtons = new ArrayList<>();  // Lista di pulsanti per le case

    public MSHHome(JFrame existingFrame, JPanel previousPanel, String imagePath) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;

        homePanel = createHomePanel(imagePath);

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
                HouseManager houseManager = new HouseManager(frame);
                houseManager.createNewHouse();
            }
        });
        panel.add(newHouseButton);
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

    public static void addHouseButtonToPanel(String houseName) {
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);

        CustomButton houseButton = new CustomButton(houseName, customColor, customColor1, 1);
        houseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Azione quando il pulsante della casa viene cliccato
                JOptionPane.showMessageDialog(null, "Hai cliccato su " + houseName);
            }
        });

        homePanel.add(houseButton);
        homePanel.revalidate();
        homePanel.repaint();
        houseButtons.add(houseButton);
    }

    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }
}
