package com.miriamsonaglia.mediasharehouse.model;

public class Stanza {
    private int idStanza;
    private String nome;  // Nuovo campo
    private String tipo;
    private int idCasa;

    // Costruttore completo
    public Stanza(int idStanza, String nome, String tipo, int idCasa) {
        this.idStanza = idStanza;
        this.nome = nome;
        this.tipo = tipo;
        this.idCasa = idCasa;
    }

    // Costruttore senza idStanza (per nuovi record, idStanza sarà auto-generato dal database)
    public Stanza(String nome, String tipo, int idCasa) {
        this.nome = nome;
        this.tipo = tipo;
        this.idCasa = idCasa;
    }

    // Getter e Setter
    public int getIdStanza() {
        return idStanza;
    }

    public void setIdStanza(int idStanza) {
        this.idStanza = idStanza;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    @Override
    public String toString() {
        return nome; // Ritorna solo il nome della casa
    }
}
