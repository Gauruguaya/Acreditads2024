package com.example.acreditads2023;
/*El objetivo es lograr automatizar el registro de los participantes de todas las categorías de la Semana Académica
 que realizan anualmente los estudiantes de la carrera binacional de Tecnólogo en análisis y desarrollo de sistemas
  de Ifsul/Utec, para que permita agilizar la entrega de las certificaciones al finalizar el evento.
 */
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

        Button btnMiActividad = findViewById(R.id.btnMiActividad);
        btnMiActividad.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Consulta_id.class);
            startActivity(intent);
        });

        Button btnAdministracion = findViewById(R.id.btnAdministracion);
        btnAdministracion.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AccesoAdm_activity.class);
            startActivity(intent);
        });
    }
}