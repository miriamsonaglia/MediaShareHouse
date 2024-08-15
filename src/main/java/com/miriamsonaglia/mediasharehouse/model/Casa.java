package com.miriamsonaglia.mediasharehouse.model;

public class Casa {
    private int idCasa;
    private String nome;
    private String chiaveAccesso;
    private String stato;
    private String username;

    // Costruttore
    public Casa(int idCasa, String nome, String chiaveAccesso, String stato, String username) {
        this.idCasa = idCasa;
        this.nome = nome;
        this.chiaveAccesso = chiaveAccesso;
        this.stato = stato;
        this.username = username;
    }

    // Getter e Setter
    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChiaveAccesso() {
        return chiaveAccesso;
    }

    public void setChiaveAccesso(String chiaveAccesso) {
        this.chiaveAccesso = chiaveAccesso;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}