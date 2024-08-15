package com.miriamsonaglia.mediasharehouse.model;

// import com.sun.org.apache.xpath.internal.operations.Bool;
public class Utente {

    private String username;
    private String email;
    private String password;
    private int abbonamento;

    // Costruttore
    public Utente(String username, String email, String password, int abbonamento) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.abbonamento = abbonamento;
    }

    // Getter e setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAbbonamento() {
        return abbonamento;
    }

    public void setAbbonamento(int abbonamento) {
        this.abbonamento = abbonamento;
    }
}
