package com.miriamsonaglia.mediasharehouse.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {

    private String url;

    public DatabaseCreation() {
        // Determina il percorso assoluto del database nella cartella "resources"
        File file = new File("src/main/resources/MediaShareHouse.db");
        this.url = "jdbc:sqlite:" + file.getAbsolutePath();
    }

    public Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connessione al database stabilita.");
            return conn;
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
            return null;
        }
    }

    public void createTables() {
        String[] tableCreationQueries = {
            "CREATE TABLE IF NOT EXISTS Utente (" +
                "username TEXT PRIMARY KEY," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "abbonamento INTEGER DEFAULT 0" +
            ")",
            "CREATE TABLE IF NOT EXISTS Accesso (" +
                "id_accesso INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "id_casa INTEGER," +
                "FOREIGN KEY (username) REFERENCES Utente(username)," +
                "FOREIGN KEY (id_casa) REFERENCES Casa(id_casa)" +
            ")",
            "CREATE TABLE IF NOT EXISTS Casa (" +
                "id_casa INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "chiave_accesso TEXT NOT NULL UNIQUE," +
                "stato TEXT CHECK (stato IN ('privata', 'pubblica')) DEFAULT 'privata'," +
                "username TEXT," +
                "FOREIGN KEY (username) REFERENCES Utente(username)" +
            ")",
            "CREATE TABLE IF NOT EXISTS Stanza (" +
                "id_stanza INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "tipo TEXT CHECK (tipo IN ('Music', 'Movies', 'Books', 'Pictures'))," +
                "id_casa INTEGER," +
                "FOREIGN KEY (id_casa) REFERENCES Casa(id_casa)" +
            ")",
            "CREATE TABLE IF NOT EXISTS Contenuto (" +
                "id_contenuto INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "tipo TEXT CHECK (tipo IN ('song', 'movie', 'book', 'picture'))," +
                "percorso_file TEXT NOT NULL," +
                "id_stanza INTEGER," +
                "username TEXT," +
                "FOREIGN KEY (id_stanza) REFERENCES Stanza(id_stanza)," +
                "FOREIGN KEY (username) REFERENCES Utente(username)" +
            ")",
            "CREATE TABLE IF NOT EXISTS Commento (" +
                "id_commento INTEGER PRIMARY KEY AUTOINCREMENT," +
                "testo TEXT NOT NULL," +
                "data_commento TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "id_contenuto INTEGER," +
                "username TEXT," +
                "id_commento_padre INTEGER DEFAULT NULL," +
                "FOREIGN KEY (id_contenuto) REFERENCES Contenuto(id_contenuto)," +
                "FOREIGN KEY (username) REFERENCES Utente(username)," +
                "FOREIGN KEY (id_commento_padre) REFERENCES Commento(id_commento)" +
            ")",
            "CREATE TABLE IF NOT EXISTS Valutazione (" +
                "id_valutazione INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "n_stelle INTEGER CHECK (n_stelle >= 1 AND n_stelle <= 5), " +
                "id_contenuto INTEGER, " +
                "username TEXT, " +
                "FOREIGN KEY (id_contenuto) REFERENCES Contenuto(id_contenuto), " +
                "FOREIGN KEY (username) REFERENCES Utente(username), " +
                "UNIQUE (id_contenuto, username)" +
            ")"
        };

        // Query di inserimento dei dati
        String[] dataInsertionQueries = {
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('bisi', 'bisi@mimi.bm', 'mimi', 0);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('mimi', 'mimi@bisi.mb', 'bisi', 1);",
            
            // Nuovi utenti
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('john_doe', 'john@example.com', 'password123', 1);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('jane_doe', 'jane@example.com', 'password456', 0);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('alice_wonder', 'alice@example.com', 'alicepass', 1);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('bob_builder', 'bob@example.com', 'bobpass', 0);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('charlie_brown', 'charlie@example.com', 'charliepass', 1);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('diana_prince', 'diana@example.com', 'dianapass', 0);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('edward_snow', 'edward@example.com', 'edwardpass', 1);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('frank_castle', 'frank@example.com', 'frankpass', 0);",
            "INSERT INTO Utente (username, email, password, abbonamento) VALUES ('grace_hopper', 'grace@example.com', 'gracepass', 1);",

            // Case
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di John', 'johnkey123', 'privata', 'john_doe');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Jane', 'janekey456', 'pubblica', 'jane_doe');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Alice', 'alicekey789', 'privata', 'alice_wonder');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Bob', 'bobkey101', 'pubblica', 'bob_builder');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Charlie', 'charliekey111', 'privata', 'charlie_brown');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Diana', 'dianakey121', 'pubblica', 'diana_prince');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Edward', 'edwardkey131', 'privata', 'edward_snow');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Frank', 'frankkey141', 'pubblica', 'frank_castle');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Grace', 'gracekey151', 'privata', 'grace_hopper');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Mimi', 'mimi', 'privata', 'mimi');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Bisi', 'bisi', 'privata', 'bisi');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Mimi 2', 'mimi2', 'privata', 'mimi');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Mimi 3', 'mimi3', 'privata', 'mimi');",
            "INSERT INTO Casa (nome, chiave_accesso, stato, username) VALUES ('Casa di Bisi 2', 'bisi2', 'privata', 'bisi');",

            "INSERT INTO Accesso (username, id_casa) VALUES ('john_doe', 1);", // Casa di John
            "INSERT INTO Accesso (username, id_casa) VALUES ('jane_doe', 2);", // Casa di Jane
            "INSERT INTO Accesso (username, id_casa) VALUES ('alice_wonder', 3);", // Casa di Alice
            "INSERT INTO Accesso (username, id_casa) VALUES ('bob_builder', 4);", // Casa di Bob
            "INSERT INTO Accesso (username, id_casa) VALUES ('charlie_brown', 5);", // Casa di Charlie
            "INSERT INTO Accesso (username, id_casa) VALUES ('diana_prince', 6);", // Casa di Diana
            "INSERT INTO Accesso (username, id_casa) VALUES ('edward_snow', 7);", // Casa di Edward
            "INSERT INTO Accesso (username, id_casa) VALUES ('frank_castle', 8);", // Casa di Frank
            "INSERT INTO Accesso (username, id_casa) VALUES ('grace_hopper', 9);", // Casa di Grace
            "INSERT INTO Accesso (username, id_casa) VALUES ('mimi', 10);", // Casa di Mimi
            "INSERT INTO Accesso (username, id_casa) VALUES ('bisi', 11);", // Casa di Bisi
            "INSERT INTO Accesso (username, id_casa) VALUES ('mimi', 12);", // Casa di Mimi 2
            "INSERT INTO Accesso (username, id_casa) VALUES ('mimi', 13);", // Casa di Mimi 3
            "INSERT INTO Accesso (username, id_casa) VALUES ('bisi', 14);", // Casa di Bisi 2

            // Accesso basato su interazioni (commenti e valutazioni)
            // Assumendo che il contenuto con id_contenuto 2 sia associato a una casa con id_casa 10
            "INSERT INTO Accesso (username, id_casa) VALUES ('john_doe', 10);", // Interazione con contenuti in casa di Mimi
            "INSERT INTO Accesso (username, id_casa) VALUES ('jane_doe', 10);", // Interazione con contenuti in casa di Mimi
            "INSERT INTO Accesso (username, id_casa) VALUES ('alice_wonder', 10);", // Interazione con contenuti in casa di Mimi
            "INSERT INTO Accesso (username, id_casa) VALUES ('bob_builder', 10);", // Interazione con contenuti in casa di Mimi
            "INSERT INTO Accesso (username, id_casa) VALUES ('charlie_brown', 10);", // Interazione con contenuti in casa di Mimi
            "INSERT INTO Accesso (username, id_casa) VALUES ('mimi', 10);", // Interazione con contenuti in casa di Mimi

            // Stanze
            "INSERT INTO Stanza (nome, tipo, id_casa) VALUES ('Stanza Immagini di Mimi', 'Pictures', 10);",
            "INSERT INTO Stanza (nome, tipo, id_casa) VALUES ('Stanza Musica di Bisi', 'Music', 11);",

            // Contenuti (solo mimi e bisi possiedono contenuti)
            //"INSERT INTO Contenuto (nome, tipo, percorso_file, id_stanza, username) VALUES ('Canzone', 'song', 'path/to/rocksong.mp3', 2, 'bisi');",  // di bisi
            //"INSERT INTO Contenuto (nome, tipo, percorso_file, id_stanza, username) VALUES ('Immagine', 'picture', 'path/to/comedy.mp4', 1, 'mimi');",  // di mimi

            // Commenti (solo per il contenuto di mimi, ad es. "Film Commedia")
            "INSERT INTO Commento (testo, id_contenuto, username) VALUES ('Ottima!', 2, 'john_doe');",
            "INSERT INTO Commento (testo, id_contenuto, username) VALUES ('Mi piace tantissimo!', 2, 'jane_doe');",
            "INSERT INTO Commento (testo, id_contenuto, username) VALUES ('Che colori!', 2, 'alice_wonder');",
            "INSERT INTO Commento (testo, id_contenuto, username) VALUES ('Fatta con la macchina fotografica?', 2, 'bob_builder');",
            "INSERT INTO Commento (testo, id_contenuto, username) VALUES ('Wow!', 2, 'charlie_brown');",
            "INSERT INTO Commento (testo, id_contenuto, username, id_commento_padre) VALUES ('Esatto!', 2, 'mimi', 4);",

            // Valutazioni (solo per il contenuto di mimi e bisi)
            "INSERT INTO Valutazione (n_stelle, id_contenuto, username) VALUES (5, 1, 'john_doe');",  // Valutazione per la Canzone Rock (di bisi)
            "INSERT INTO Valutazione (n_stelle, id_contenuto, username) VALUES (4, 2, 'jane_doe');",  // Valutazione per il Film Commedia (di mimi)
            "INSERT INTO Valutazione (n_stelle, id_contenuto, username) VALUES (5, 2, 'alice_wonder');",  // Valutazione per il Film Commedia (di mimi)
            "INSERT INTO Valutazione (n_stelle, id_contenuto, username) VALUES (4, 1, 'bob_builder');",  // Valutazione per la Canzone Rock (di bisi)
            "INSERT INTO Valutazione (n_stelle, id_contenuto, username) VALUES (5, 2, 'charlie_brown');",  // Valutazione per il Film Commedia (di mimi)

        };

        Connection conn = connect();
        if (conn != null) {
            try (Statement stmt = conn.createStatement()) {
                // Creazione tabelle
                for (String sql : tableCreationQueries) {
                    stmt.execute(sql);
                }
                System.out.println("Tabelle create con successo.");

                // Inserimento dati
                for (String sql : dataInsertionQueries) {
                    stmt.execute(sql);
                }
                conn.commit();
                System.out.println("Dati inseriti con successo.");
            } catch (SQLException e) {
                System.out.println("Errore durante la creazione delle tabelle o l'inserimento dei dati: " + e.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Errore durante la chiusura della connessione: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Connessione non stabilita, tabelle e dati non creati.");
        }
    }

    public static void main(String[] args) {
        DatabaseCreation dbCreation = new DatabaseCreation();
        dbCreation.createTables();
    }
}
