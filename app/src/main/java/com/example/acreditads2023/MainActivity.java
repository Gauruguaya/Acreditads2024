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

        Button btnParticipacion = findViewById(R.id.btnParticipacion);
        btnParticipacion.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Usuario_activity.class);
            startActivity(intent);
        });

        Button btnEventos = findViewById(R.id.btnEventos);
        btnEventos.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Evento_activity.class);
            startActivity(intent);
        });

        Button btnAcreditaciones = findViewById(R.id.btnAcreditaciones);
        btnAcreditaciones.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Consulta_id.class);
            startActivity(intent);
        });

    }
}