package com.miriamsonaglia.mediasharehouse.model;

import java.sql.Timestamp;

public class Commento {
    private int idCommento;
    private String testo;
    private Timestamp dataCommento;
    private int idContenuto;
    private String username;
    private Integer idCommentoPadre;

    // Costruttore
    public Commento(int idCommento, String testo, Timestamp dataCommento, int idContenuto, String username, Integer idCommentoPadre) {
        this.idCommento = idCommento;
        this.testo = testo;
        this.dataCommento = dataCommento;
        this.idContenuto = idContenuto;
        this.username = username;
        this.idCommentoPadre = idCommentoPadre;
    }

    // Getter e Setter
    public int getIdCommento() {
        return idCommento;
    }

    public void setIdCommento(int idCommento) {
        this.idCommento = idCommento;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Timestamp getDataCommento() {
        return dataCommento;
    }

    public void setDataCommento(Timestamp dataCommento) {
        this.dataCommento = dataCommento;
    }

    public int getIdContenuto() {
        return idContenuto;
    }

    public void setIdContenuto(int idContenuto) {
        this.idContenuto = idContenuto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getIdCommentoPadre() {
        return idCommentoPadre;
    }

    public void setIdCommentoPadre(Integer idCommentoPadre) {
        this.idCommentoPadre = idCommentoPadre;
    }

    @Override
    public String toString() {
        return "Commento{" +
                "idCommento=" + idCommento +
                ", testo='" + testo + '\'' +
                ", dataCommento=" + dataCommento +
                ", idContenuto=" + idContenuto +
                ", username='" + username + '\'' +
                ", idCommentoPadre=" + idCommentoPadre +
                '}';
    }
}
