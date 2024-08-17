package com.miriamsonaglia.mediasharehouse.model;

public class Contenuto {
    private int idContenuto;
    private String nome;
    private String tipo;
    private String percorsoFile;
    private int idStanza;
    private String username;


    // Costruttore
    public Contenuto(int idContenuto, String nome, String tipo, String percorsoFile, int idStanza, String username) {
        this.idContenuto = idContenuto;
        this.nome = nome;
        this.tipo = tipo;
        this.percorsoFile = percorsoFile;
        this.idStanza = idStanza;
        this.username = username;
    }

    // Getter e Setter
    public int getIdContenuto() {
        return idContenuto;
    }

    public void setIdContenuto(int idContenuto) {
        this.idContenuto = idContenuto;
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

    public String getPercorsoFile() {
        return percorsoFile;
    }

    public void setPercorsoFile(String percorsoFile) {
        this.percorsoFile = percorsoFile;
    }

    public int getIdStanza() {
        return idStanza;
    }

    public void setIdStanza(int idStanza) {
        this.idStanza = idStanza;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Metodo toString per rappresentare l'oggetto come stringa
    @Override
    public String toString() {
        return "Contenuto{" +
                "idContenuto=" + idContenuto +
                ", tipo='" + tipo + '\'' +
                ", percorsoFile='" + percorsoFile + '\'' +
                ", idStanza=" + idStanza +
                ", username='" + username + '\'' +
                '}';
    }
}
