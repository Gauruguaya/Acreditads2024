package com.example.acreditads2023;

import androidx.appcompat.app.AppCompatActivity;

public class Usuario extends AppCompatActivity {
    private int idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String docUsuario;
    private String emailUsuario;
    private int tipoUsuario;
    private String tareaUsuario;
    private String fechaHora;

    public Usuario() {
        this.idUsuario = -1;
        this.nombreUsuario = "";
        this.apellidoUsuario = "";
        this.docUsuario = "";
        this.emailUsuario = "";
        this.tipoUsuario = -1;
        this.tareaUsuario = "";
        this.fechaHora = "";
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;

    }
    public String getDocUsuario() {
        return docUsuario;
    }

    public void setDocUsuario(String docUsuario) {
        this.docUsuario = docUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTareaUsuario() {
        return tareaUsuario;
    }

    public void setTareaUsuario(String tareaUsuario) {
        this.tareaUsuario = tareaUsuario;
    }

    public String getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
