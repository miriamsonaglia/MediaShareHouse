package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreation {

    // Specifica il percorso del database nella cartella "resources"
    private final String url = "jdbc:sqlite:src/main/resources/MediaShareHouse.db";

    public Connection connect() {
        Connection conn = null;
        try {
            // Caricamento esplicito del driver JDBC per SQLite
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            System.out.println("Connessione al database stabilita.");
        } catch (SQLException e) {
            System.out.println("Errore durante la connessione al database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Driver SQLite non trovato: " + e.getMessage());
        }
        return conn;
    }

    public void createTables() {
        String[] tableCreationQueries = {
                "CREATE TABLE IF NOT EXISTS Abbonamento ("
                        + "id_abbonamento INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "tipo TEXT CHECK (tipo IN ('base', 'premium')) DEFAULT 'base',"
                        + "limite_case INTEGER"
                        + ")",
                "CREATE TABLE IF NOT EXISTS Utente ("
                        + "id_utente TEXT PRIMARY KEY,"
                        + "email TEXT NOT NULL UNIQUE,"
                        + "password TEXT NOT NULL,"
                        + "id_abbonamento INTEGER,"
                        + "FOREIGN KEY (id_abbonamento) REFERENCES Abbonamento(id_abbonamento)"
                        + ")",
                "CREATE TABLE IF NOT EXISTS Casa ("
                        + "id_casa INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "nome TEXT NOT NULL,"
                        + "chiave_accesso TEXT NOT NULL UNIQUE,"
                        + "stato TEXT CHECK (stato IN ('privata', 'pubblica')) DEFAULT 'privata',"
                        + "id_utente TEXT,"
                        + "FOREIGN KEY (id_utente) REFERENCES Utente(id_utente)"
                        + ")",
                "CREATE TABLE IF NOT EXISTS Stanza ("
                        + "id_stanza INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "tipo TEXT CHECK (tipo IN ('music', 'movies', 'books', 'photos')),"
                        + "id_casa INTEGER,"
                        + "FOREIGN KEY (id_casa) REFERENCES Casa(id_casa)"
                        + ")",
                "CREATE TABLE IF NOT EXISTS Contenuto ("
                        + "id_contenuto INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "tipo TEXT CHECK (tipo IN ('song', 'movie', 'book', 'picture')),"
                        + "percorso_file TEXT NOT NULL,"
                        + "id_stanza INTEGER,"
                        + "id_utente TEXT,"
                        + "FOREIGN KEY (id_stanza) REFERENCES Stanza(id_stanza),"
                        + "FOREIGN KEY (id_utente) REFERENCES Utente(id_utente)"
                        + ")",
                "CREATE TABLE IF NOT EXISTS Commento ("
                        + "id_commento INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "testo TEXT NOT NULL,"
                        + "data_commento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                        + "id_contenuto INTEGER,"
                        + "id_utente TEXT,"
                        + "id_commento_padre INTEGER NULL,"
                        + "FOREIGN KEY (id_contenuto) REFERENCES Contenuto(id_contenuto),"
                        + "FOREIGN KEY (id_utente) REFERENCES Utente(id_utente),"
                        + "FOREIGN KEY (id_commento_padre) REFERENCES Commento(id_commento)"
                        + ")",
                "CREATE TABLE IF NOT EXISTS Valutazione ("
                        + "id_valutazione INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "n_stelle INTEGER CHECK (n_stelle >= 1 AND n_stelle <= 5),"
                        + "id_contenuto INTEGER,"
                        + "FOREIGN KEY (id_contenuto) REFERENCES Contenuto(id_contenuto)"
                        + ")"
        };

        String[] dataInsertionQueries = {
                // Inserimento in Abbonamento
                "INSERT INTO Abbonamento (tipo, limite_case) VALUES ('base', 1);",
                "INSERT INTO Abbonamento (tipo, limite_case) VALUES ('premium', 5);",
                // Inserimento in Utente
                "INSERT INTO Utente (id_utente, email, password, id_abbonamento) VALUES ('user1', 'user1@example.com', 'password123', 1);",
                "INSERT INTO Utente (id_utente, email, password, id_abbonamento) VALUES ('user2', 'user2@example.com', 'password456', 2);",
                // Inserimento in Casa
                "INSERT INTO Casa (nome, chiave_accesso, stato, id_utente) VALUES ('Casa1', 'accesso123', 'privata', 'user1');",
                "INSERT INTO Casa (nome, chiave_accesso, stato, id_utente) VALUES ('Casa2', 'accesso456', 'pubblica', 'user2');",
                // Inserimento in Stanza
                "INSERT INTO Stanza (tipo, id_casa) VALUES ('music', 1);",
                "INSERT INTO Stanza (tipo, id_casa) VALUES ('movies', 2);",
                // Inserimento in Contenuto
                "INSERT INTO Contenuto (tipo, percorso_file, id_stanza, id_utente) VALUES ('song', '/media/song1.mp3', 1, 'user1');",
                "INSERT INTO Contenuto (tipo, percorso_file, id_stanza, id_utente) VALUES ('movie', '/media/movie1.mp4', 2, 'user2');",
                // Inserimento in Commento
                "INSERT INTO Commento (testo, id_contenuto, id_utente) VALUES ('Great song!', 1, 'user2');",
                "INSERT INTO Commento (testo, id_contenuto, id_utente, id_commento_padre) VALUES ('Thanks!', 1, 'user1', 1);",
                // Inserimento in Valutazione
                "INSERT INTO Valutazione (n_stelle, id_contenuto) VALUES (5, 1);",
                "INSERT INTO Valutazione (n_stelle, id_contenuto) VALUES (4, 2);"
        };

        try (Connection conn = this.connect()) {
            if (conn != null) {
                // Creazione tabelle
                for (String sql : tableCreationQueries) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Tabelle create con successo.");

                // Inserimento dati
                for (String sql : dataInsertionQueries) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Dati inseriti con successo.");
            } else {
                System.out.println("Connessione non stabilita, tabelle e dati non creati.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la creazione delle tabelle o l'inserimento dei dati: " + e.getMessage());
        }
    }

    // public static void main(String[] args) {
    // DatabaseCreation dbConnection = new DatabaseCreation();
    // dbConnection.createTables();
    // }
}
