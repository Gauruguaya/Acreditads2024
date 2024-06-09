package com.example.acreditads2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AccesoAdministradores extends AppCompatActivity {
    private String claveAdm;
    private String tareaUsuario;
    private Button btnVolver;

    public AccesoAdministradores(String claveAdm, String tareaUsuario) {
        this.claveAdm = claveAdm;
        this.tareaUsuario = tareaUsuario;
    }

    public String getClaveAdm() {
        return claveAdm;
    }

    public void setClaveAdm(String claveAdm) {
        this.claveAdm = claveAdm;
    }

    public String getTareaUsuario() {
        return tareaUsuario;
    }

    public void setTareaUsuario(String tareaUsuario) {
        this.tareaUsuario = tareaUsuario;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceso_administradores);

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
