package com.miriamsonaglia.mediasharehouse.model;

public class Valutazione {
    private int idValutazione;
    private int nStelle;
    private int idContenuto;

    // Costruttore
    public Valutazione(int idValutazione, int nStelle, int idContenuto) {
        this.idValutazione = idValutazione;
        this.nStelle = nStelle;
        this.idContenuto = idContenuto;
    }

    // Getter e Setter
    public int getIdValutazione() {
        return idValutazione;
    }

    public void setIdValutazione(int idValutazione) {
        this.idValutazione = idValutazione;
    }

    public int getNStelle() {
        return nStelle;
    }

    public void setNStelle(int nStelle) {
        this.nStelle = nStelle;
    }

    public int getIdContenuto() {
        return idContenuto;
    }

    public void setIdContenuto(int idContenuto) {
        this.idContenuto = idContenuto;
    }

    @Override
    public String toString() {
        return "Valutazione{" +
                "idValutazione=" + idValutazione +
                ", nStelle=" + nStelle +
                ", idContenuto=" + idContenuto +
                '}';
    }
}
