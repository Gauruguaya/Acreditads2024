package com.example.acreditads2023;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAgenda = findViewById(R.id.btnAgenda);
        btnAgenda.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Agenda_activity.class);
            startActivity(intent);
        });

        Button btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Usuario_activity.class);
            startActivity(intent);
        });

        Button btnAcreditacion = findViewById(R.id.btnAcreditacion);
        btnAcreditacion.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Acreditaciones_activity.class);
            startActivity(intent);
        });

        Button btnAdministracion = findViewById(R.id.btnAdministracion);
        btnAdministracion.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AccesoAdministradores.class);
            startActivity(intent);
        });
    }
}