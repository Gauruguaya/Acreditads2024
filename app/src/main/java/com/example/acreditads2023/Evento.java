package com.example.acreditads2023;

import androidx.appcompat.app.AppCompatActivity;

public class Evento extends AppCompatActivity {
    private int idEvento;
    private  String tituloEvento;
    private  String autorEvento;
    private  int tipoEvento;
    private String fechaHoraEvento;
    private  int idAdminEvento;

    public Evento(){
        this.idEvento= -1;
        this.tituloEvento="";
        this.autorEvento="";
        this.tipoEvento=-1;
        this.fechaHoraEvento="";
        this.idAdminEvento=-1;
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

    public void setFechaHoraEvento(String fechaHoraEvento) {
        this.fechaHoraEvento = fechaHoraEvento;
    }

    public int getIdAdminEvento() {
        return idAdminEvento;
    }

    public void setIdAdminEvento(int idAdminEvento) {
        this.idAdminEvento = idAdminEvento;
    }
}
