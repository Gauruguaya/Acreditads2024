package com.example.acreditads2023;

import android.widget.Button;

public class Acreditaciones {
    private int idInstalacion;
    private int idUsuario;
    private String timeStamp;
    private String lecturaQR = "";


    public Acreditaciones(){

    }
    public Acreditaciones(int idUsuario, String timeStamp, String lecturaQR) {
        this.idInstalacion = idInstalacion;
        this.idUsuario = idUsuario;
        this.timeStamp = timeStamp;
        this.lecturaQR = lecturaQR;
    }

    public int getIdInstalacion() {
        return idInstalacion;
    }

    public void setIdInstalacion(int idInstalacion) {
        this.idInstalacion = idInstalacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLecturaQR() {
        return lecturaQR;
    }

    public void setLecturaQR(String lecturaQR) {
        this.lecturaQR = lecturaQR;
    }
}
