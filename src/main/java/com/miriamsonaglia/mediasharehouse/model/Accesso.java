package com.miriamsonaglia.mediasharehouse.model;

public class Accesso {
    private int idAccesso;
    private String username;
    private int idCasa;

    // Costruttore
    public Accesso(int idAccesso, String username, int idCasa) {
        this.idAccesso = idAccesso;
        this.username = username;
        this.idCasa = idCasa;
    }

    // Getter e Setter
    public int getIdAccesso() {
        return idAccesso;
    }

    public void setIdAccesso(int idAccesso) {
        this.idAccesso = idAccesso;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    // Metodo toString per rappresentare l'oggetto come stringa
    @Override
    public String toString() {
        return "Accesso{" +
                "idAccesso='" + idAccesso + '\'' +
                ", username='" + username + '\'' +
                ", idCasa=" + idCasa +
                '}';
    }
}
