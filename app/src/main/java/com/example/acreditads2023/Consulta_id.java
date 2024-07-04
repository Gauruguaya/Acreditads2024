package com.example.acreditads2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Consulta_id extends AppCompatActivity {
    ListView lvMiActividad;
    Database db;
    private Button btnVolver;

    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_consulta_id);

        db = new Database(getApplicationContext());
        //el usuario consulta su actividad en la base de datos local SQLite
        //la lista debe contener los eventos que registren la misma lectura QR en dos horarios distintos
       /* int idUsuario = obtenerIdUsuario();
        List<String> miActividad = consultarMiActividad(idUsuario);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, miActividad);
        ListView lvMiActividad = findViewById(R.id.lvMiActividad);
        lvMiActividad.setAdapter(adapter);*/

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
