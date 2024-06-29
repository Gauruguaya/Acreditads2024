package com.example.acreditads2023;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AccesoAdm_activity extends AppCompatActivity {
    private EditText claveAdm;
    private EditText tareaUsuario;
    private Button btnAgregar;
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.acceso_administradores);

        //introducir claveAdm
    //guardar tareaUsuario en BD y notificar que se guardó
        //si claveAdm coincide con una de la BD al hacer click conduce a Evento_activity, si no Toast de error


        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
}
}

