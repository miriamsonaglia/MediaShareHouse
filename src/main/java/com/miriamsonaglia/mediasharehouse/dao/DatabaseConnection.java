package com.miriamsonaglia.mediasharehouse.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:src/main/resources/MediaShareHouse.db"; // Sostituisci con il percorso corretto del database

    public static Connection getConnection() throws SQLException {
        try {
            // Carica il driver JDBC (non sempre necessario con JDBC 4.0 o superiore)
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver SQLite non trovato: " + e.getMessage());
        }
        return DriverManager.getConnection(URL);
    }
}