package com.miriamsonaglia.mediasharehouse.model;

public class Utente {

    private String idUtente;
    private String email;
    private String password;
    private Abbonamento abbonamento;

    // Costruttore
    public Utente(String idUtente, String email, String password, Abbonamento abbonamento) {
        this.idUtente = idUtente;
        this.email = email;
        this.password = password;
        this.abbonamento = abbonamento;
    }

    // Getter e setter
    public String getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
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

    public Abbonamento getAbbonamento() {
        return abbonamento;
    }

    public void setAbbonamento(Abbonamento abbonamento) {
        this.abbonamento = abbonamento;
    }
}