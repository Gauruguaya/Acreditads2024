package com.example.acreditads2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AccesoAdministradores extends AppCompatActivity {
    private String claveAdm;//no sé si esta variable es necesaria aquí
    private String tareaOrg;

    public AccesoAdministradores(String claveAdm, String tareaUsuario) {
        this.claveAdm = claveAdm;
        this.tareaOrg = tareaOrg;
    }

    public String getClaveAdm() {
        return claveAdm;
    }

    public void setClaveAdm(String claveAdm) {
        this.claveAdm = claveAdm;
    }

    public String getTareaOrg() {
        return tareaOrg;
    }

    public void setTareaOrg(String tareaUsuario) {
        this.tareaOrg = tareaOrg;
    }

}
