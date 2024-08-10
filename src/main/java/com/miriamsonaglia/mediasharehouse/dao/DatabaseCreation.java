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

        try (Connection conn = this.connect()) {
            if (conn != null) {
                for (String sql : tableCreationQueries) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Tabelle create con successo.");
            } else {
                System.out.println("Connessione non stabilita, tabelle non create.");
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la creazione delle tabelle: " + e.getMessage());
        }
    }

    // public static void main(String[] args) {
    // DatabaseCreation dbConnection = new DatabaseCreation();
    // dbConnection.createTables();
    // }
}
