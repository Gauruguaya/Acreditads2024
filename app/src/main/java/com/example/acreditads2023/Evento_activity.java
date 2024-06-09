package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Evento_activity extends AppCompatActivity {
    private EditText edtTituloEvento;
    private EditText edtAutorEvento;
    private EditText edtTipoEvento;
    private EditText edtFechaHoraEvento;
    private EditText edtIdAdminEvento;
    //private EditText edtQRaperturaEvento;
    //private EditText edtQRcierreEvento;
    private Button btnAgregar;
    private Button btnVolver;
    private Database db;
    Button btnMapa;
    private RequestQueue requestQueue;
    String latitude;
    String longitude;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evento_activity);
        requestQueue = Volley.newRequestQueue(this);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnVolver = findViewById(R.id.btnVolver);
        edtTituloEvento = findViewById(R.id.edtTipoEvento);
        edtAutorEvento = findViewById(R.id.edtAutorEvento);
        edtTipoEvento = findViewById(R.id.edtTipoEvento);
        edtFechaHoraEvento = findViewById(R.id.edtFechaHoraEvento);
        edtIdAdminEvento = findViewById(R.id.edtIdAdminEvento);
       // edtQRaperturaEvento = findViewById(R.id.edtQRaperturaEvento);
        //edtQRcierreEvento = findViewById(R.id.edtQRcierreEvento);

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

                // Google Sheets URL
                //Eu preciso puxar as variáveis aqui
                String urlSheets = "https://script.google.com/macros/s/AKfycbxX1EInivW85EH1IGNZzh6bWJTLm6K9-ZRr3-wduHYFd34d1SW0dG2bAndm8CZvYPE/exec?action=evento&tituloEvento=" +
                        edtTituloEvento.getText().toString() + "&autorEvento=" + edtAutorEvento.getText().toString() +
                        "&tipoEvento=" + edtTipoEvento.getText().toString() + "&fechaHoraEvento=" + edtFechaHoraEvento.getText().toString() + "&latitud=" + latitude + "&longitud=" + longitude;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSheets, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Response", s);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                });

                requestQueue.add(stringRequest);
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
        btnMapa = findViewById(R.id.btnAgregarMapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreEvento = edtTituloEvento.getText().toString();
                Intent intent;
                intent = new Intent(Evento_activity.this, UbicarEventoActivity.class);
                intent.putExtra("NombreEvento",nombreEvento);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Obtener los valores de las variables devueltas
            latitude = data.getStringExtra("Latitud");
            longitude = data.getStringExtra("Longitud");
            Log.i("Latitud",latitude);
            Log.i("Longitud",longitude);
        }
    }
}
