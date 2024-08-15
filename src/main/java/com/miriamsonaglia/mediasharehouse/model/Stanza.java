package com.miriamsonaglia.mediasharehouse.model;

public class Stanza {
    private int idStanza;
    private String tipo;
    private int idCasa;

    // Costruttore completo
    public Stanza(int idStanza, String tipo, int idCasa) {
        this.idStanza = idStanza;
        this.tipo = tipo;
        this.idCasa = idCasa;
    }

    // Costruttore senza idStanza (per nuovi record, idStanza sarà auto-generato dal database)
    public Stanza(String tipo, int idCasa) {
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

}
