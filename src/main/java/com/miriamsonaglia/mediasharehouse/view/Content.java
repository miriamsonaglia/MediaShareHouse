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

import com.miriamsonaglia.mediasharehouse.dao.ContenutoDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.dao.StanzaDao;
import com.miriamsonaglia.mediasharehouse.model.Contenuto;
import com.miriamsonaglia.mediasharehouse.model.Stanza;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.service.UploadContent;

public final class Content {

    private static JFrame frame;
    private JPanel previousPanel;
    private static JPanel contentPanel;  // Panel principale per il Contenuto
    private Stanza currentRoom;
    private Utente currentUser;

    public Content(JFrame existingFrame, JPanel previousPanel, String imagePath, int roomId, Utente currentUser) {
        this.frame = existingFrame;
        this.previousPanel = previousPanel;

        this.currentRoom = fetchRoomById(roomId);
        this.currentUser = currentUser;

        contentPanel = createContentPanel(imagePath);
        loadRoomContent();

        frame.add(contentPanel);
        frame.revalidate();
        frame.repaint();
    }

    private Stanza fetchRoomById(int roomId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Presumendo che tu abbia un Dao simile a StanzaDao per ottenere la stanza
            StanzaDao stanzaDao = new StanzaDao(connection);
            return stanzaDao.getStanzaById(roomId);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
            return null;
        }
    }

    private ImagePanel createContentPanel(String imagePath) {
        ImagePanel panel = new ImagePanel(imagePath);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
    
        final JLabel titleLabel = new JLabel(currentRoom.getTipo() + " Room");   // Legge il tipo della stanza selezionata
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.PINK);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        // Pulsante per aggiungere un nuovo contenuto nella stanza
        final CustomButton newContentButton = new CustomButton("AGGIUNGI UN CONTENUTO", customColor, customColor1, 1);
        newContentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                
                UploadContent uploadContent = new UploadContent(frame, currentRoom, currentUser);
                uploadContent.uploadFile();

            }
        });
        panel.add(newContentButton);
        panel.add(Box.createVerticalStrut(10));

        // Pulsante per tornare al menu precedente
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

    public static void addContentButtonToPanel(String contentType, String filePath, String name) {
        System.out.println("Aggiungendo pulsante per il contenuto: " + name);
        final Color customColor = new Color(218, 165, 32);
        final Color customColor1 = new Color(101, 67, 33);
        CustomButton contentButton = new CustomButton(name, Color.GREEN, customColor1, 1);
    
        contentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                // Azione quando il pulsante del contenuto viene cliccato
                new ContentViewer(frame, filePath, contentType);
                frame.revalidate();
                frame.repaint();
            }
        });
    
        contentPanel.add(contentButton, 1);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadRoomContent() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ContenutoDao contenutoDao = new ContenutoDao(connection);
            List<Contenuto> roomContent = contenutoDao.getContenutiByStanza(currentRoom.getIdStanza());

            for (Contenuto contenuto : roomContent) {
                addContentButtonToPanel(contenuto.getTipo(), contenuto.getPercorsoFile(), contenuto.getNome());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nel caricamento del contenuto della stanza.");
        }
    }

    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }
}
