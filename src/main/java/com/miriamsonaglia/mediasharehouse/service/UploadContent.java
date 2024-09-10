package com.miriamsonaglia.mediasharehouse.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.miriamsonaglia.mediasharehouse.dao.ContenutoDao;
import com.miriamsonaglia.mediasharehouse.dao.DatabaseConnection;
import com.miriamsonaglia.mediasharehouse.model.Contenuto;
import com.miriamsonaglia.mediasharehouse.model.Stanza;
import com.miriamsonaglia.mediasharehouse.model.Utente;
import com.miriamsonaglia.mediasharehouse.view.Content;

public class UploadContent {

    private JFrame frame;
    private Stanza currentRoom;
    private Utente currentUser; // Aggiunto
    private String path = "src/main/resources/files/";

    public UploadContent(JFrame frame, Stanza currentRoom, Utente currentUser) { // Modificato il costruttore
        this.frame = frame;
        this.currentRoom = currentRoom;
        this.currentUser = currentUser; // Aggiunto
    }

    // Mappa che associa i tipi di stanza con le estensioni dei file compatibili
    private static final Map<String, String[]> fileExtensionsMap = new HashMap<>();

    static {
        fileExtensionsMap.put("Music", new String[]{"mp3", "wav", "aac"});
        fileExtensionsMap.put("Movies", new String[]{"mp4", "mkv", "avi"});
        fileExtensionsMap.put("Books", new String[]{"pdf", "epub", "mobi"});
        fileExtensionsMap.put("Pictures", new String[]{"jpg", "jpeg", "png", "gif"});
    }

    public void uploadFile() {
        // Creazione di un file chooser per selezionare il file dal sistema
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona il file da caricare");

        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String originalFileName = selectedFile.getName();
            String fileExtension = getFileExtension(originalFileName);

            // Verifica se il tipo di file è compatibile con la stanza corrente
            if (isFileExtensionCompatible(fileExtension)) {
                // Chiede all'utente di inserire il nome del contenuto
                JTextField contentNameField = new JTextField();
                JPanel dialogPanel = new JPanel();
                dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
                dialogPanel.add(new JLabel("Nome del contenuto:"));
                dialogPanel.add(contentNameField);

                int result = JOptionPane.showConfirmDialog(frame, dialogPanel, "Inserisci il nome del contenuto", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String contentName = contentNameField.getText().trim();

                    if (!contentName.isEmpty()) {
                        try {
                            // Aggiungi un nuovo record nella tabella Contenuto e ottieni l'ID generato
                            String fileType = determineFileType(fileExtension);
                            int idContenuto = addToDatabase(contentName, fileType, fileExtension);
                            String percorsoFile = path + idContenuto + "." + fileExtension; // aggiungo il percorso del file
                            addPathToDatabase(percorsoFile, idContenuto); // aggiungo il percorso del file al database

                            if (idContenuto > 0) {
                                // Imposta il nuovo nome del file basato sull'id_contenuto
                                String newFileName = idContenuto + "." + fileExtension;

                                // Copia il file nella destinazione con il nuovo nome
                                Path destinationPath = getDestinationPath(newFileName);
                                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                                JOptionPane.showMessageDialog(frame, "File caricato con successo!");
                                Content.addContentButtonToPanel(fileType, percorsoFile, contentName, idContenuto);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Errore durante l'inserimento del contenuto nel database.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Errore durante il caricamento del file.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Il nome del contenuto non può essere vuoto.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Il tipo di file selezionato non è compatibile con la stanza corrente.");
            }
        }
    }

    private int addToDatabase(String contentName, String fileType, String fileExtension) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ContenutoDao contenutoDao = new ContenutoDao(connection);

            // Crea un nuovo oggetto Contenuto senza il percorso del file
            System.out.println("nome" + contentName + " fileType" + fileType + " currentRoom.getIdStanza()" + currentRoom.getIdStanza() + " currentUser.getUsername()" + currentUser.getUsername());
            Contenuto newContenuto = new Contenuto(0, contentName, fileType, "", currentRoom.getIdStanza(), currentUser.getUsername());

            // Inserisci il nuovo contenuto nel database e ottieni l'id generato
            if (contenutoDao.createContenuto(newContenuto)) {
                return newContenuto.getIdContenuto(); // Restituisce l'ID generato
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
        return -1; // Ritorna -1 se c'è stato un errore
    }

    private String determineFileType(String fileExtension) {
        if (fileExtension.equals("png") || fileExtension.equals("jpg") || fileExtension.equals("jpeg")) {
            return "picture";
        } else if (fileExtension.equals("mp3") || fileExtension.equals("wav") || fileExtension.equals("aac")) {
            return "song";
        } else if (fileExtension.equals("mp4") || fileExtension.equals("mkv") || fileExtension.equals("avi")) {
            return "movie";
        } else if (fileExtension.equals("pdf") || fileExtension.equals("epub") || fileExtension.equals("mobi")) {
            return "book";
        }
        return "";
    }

    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0 && lastIndex < fileName.length() - 1) {
            return fileName.substring(lastIndex + 1).toLowerCase();
        }
        return "";
    }

    private boolean isFileExtensionCompatible(String fileExtension) {
        String[] compatibleExtensions = fileExtensionsMap.get(currentRoom.getTipo());
        if (compatibleExtensions != null) {
            for (String extension : compatibleExtensions) {
                if (extension.equals(fileExtension)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Path getDestinationPath(String fileName) {
        // Ottieni il percorso della cartella resources/files
        String resourcesPath = new File("src/main/resources/files").getAbsolutePath();
        // Combina il percorso della cartella con il nome del file
        return new File(resourcesPath, fileName).toPath();
    }

    private int addPathToDatabase(String percorsoFile, int idContenuto) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            ContenutoDao contenutoDao = new ContenutoDao(connection);
            // Aggiorna il percorso del file nel database
            if (contenutoDao.updateContenutoPath(idContenuto, percorsoFile)) {
                return idContenuto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Errore nella connessione al database.");
        }
        return -1;
    }
}
