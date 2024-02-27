package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
public class Evento_activity extends AppCompatActivity {
    private EditText edtTituloEvento;
    private EditText edtAutorEvento;
    private EditText edtTipoEvento;
    private EditText edtFechaHoraEvento;
    private EditText edtIdAdminEvento;
    private EditText edtQRaperturaEvento;
    private EditText edtQRcierreEvento;
    private Button btnAgregar;
    private Button btnVolver;
    private Database db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evento_activity);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnVolver = findViewById(R.id.btnVolver);
        edtTituloEvento = findViewById(R.id.edtTipoEvento);
        edtAutorEvento = findViewById(R.id.edtAutorEvento);
        edtTipoEvento = findViewById(R.id.edtTipoEvento);
        edtFechaHoraEvento = findViewById(R.id.edtFechaHoraEvento);
        edtIdAdminEvento = findViewById(R.id.edtIdAdminEvento);
        edtQRaperturaEvento = findViewById(R.id.edtQRaperturaEvento);
        edtQRcierreEvento = findViewById(R.id.edtQRcierreEvento);

        db = new Database(getApplicationContext());

        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Evento e = new Evento();
                e.setTituloEvento(edtTituloEvento.getText().toString());
                e.setAutorEvento(edtAutorEvento.getText().toString());
                e.setTipoEvento(Integer.parseInt(edtTipoEvento.getText().toString()));
                e.setFechaHoraEvento(edtFechaHoraEvento.getText().toString());
                e.setIdAdminEvento(Integer.parseInt(edtIdAdminEvento.getText().toString()));

                EventoDAO eDAO = new EventoDAO(db);
                eDAO.salvar(e);
            }
        });

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
