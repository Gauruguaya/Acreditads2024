package com.example.acreditads2023;

import androidx.appcompat.app.AppCompatActivity;

public class Evento extends AppCompatActivity {
    private int idEvento;
    private  String tituloEvento;
    private  String autorEvento;
    private  int tipoEvento;
    private String fechaHoraEvento;
    private  String administradorEvento;
    private double latitudEvento;
    private double longitudEvento;

    public Evento(){
        this.idEvento= -1;
        this.tituloEvento="";
        this.autorEvento="";
        this.tipoEvento=-1;
        this.fechaHoraEvento="";
        this.administradorEvento="";
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    public String getAutorEvento() {
        return autorEvento;
    }

    public void setAutorEvento(String autorEvento) {
        this.autorEvento = autorEvento;
    }

    public int getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(int tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getFechaHoraEvento() {
        return fechaHoraEvento;
    }

    public double getLatitudEvento() { return latitudEvento; }

    public double getLongitudEvento() { return longitudEvento; }

    public void setFechaHoraEvento(String fechaHoraEvento) {
        this.fechaHoraEvento = fechaHoraEvento;
    }

    public String getAdministradorEvento() {
        return administradorEvento;
    }

    public void setAdministradorEvento(String AdministradorEvento) {
        this.administradorEvento = administradorEvento;
    }

    public void setLatitudEvento(double latitudEvento) {
        this.latitudEvento = latitudEvento;
    }

    public void setLongitudEvento(double longitudEvento) {
        this.longitudEvento = longitudEvento;
    }
}
