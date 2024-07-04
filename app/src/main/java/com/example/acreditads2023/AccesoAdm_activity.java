package com.example.acreditads2023;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AccesoAdm_activity extends AppCompatActivity {

    private EditText edtClaveAdm, edtTareaOrg;
    private Button btnValidar, btnAgregar, btnVolver;
    private static final String CHANNEL_ID = "canal";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private AccesoAdministradoresDAO guardarTarea;
    private Database db;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceso_administradores);

        edtClaveAdm = findViewById(R.id.edtClaveAdm);
        btnValidar = findViewById(R.id.btnValidar);
        edtTareaOrg = findViewById(R.id.edtTareaOrg);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnVolver = findViewById(R.id.btnVolver);

        requestQueue = Volley.newRequestQueue(this);
        db = new Database(getApplicationContext()); // Initialize Database object
        guardarTarea = new AccesoAdministradoresDAO(db);

        btnValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String claveAdm = edtClaveAdm.getText().toString();
                verificarClaveAdm(claveAdm);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tareaOrg = edtTareaOrg.getText().toString();
                guardarTareaLocal(tareaOrg);
                guardarTareaGoogleSheets(tareaOrg);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccesoAdm_activity.this, MainActivity.class);startActivity(intent);
            }
        });
    }

    private void verificarClaveAdm(String claveAdm) {
        String urlVerificarClave = "https://script.google.com/macros/s/YOUR_SCRIPT_ID/exec?action=verificarClave&clave=" + claveAdm;
        StringRequest verificarClaveRequest = new StringRequest(Request.Method.GET, urlVerificarClave,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("VALIDO")) {
                            // Clave válida, navegar a EventoActivity
                            Intent intent = new Intent(AccesoAdm_activity.this, Evento_activity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AccesoAdm_activity.this, "Clave de administrador no válida", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                Toast.makeText(AccesoAdm_activity.this, "Error al verificar la clave", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(verificarClaveRequest);
    }

    private void guardarTareaLocal(String tareaOrg) {
        long result = guardarTarea.salvar(new AccesoAdministradores(tareaOrg));
        if (result != -1) {
            Toast.makeText(this, "Tarea guardada localmente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al guardar la tarea localmente", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarTareaGoogleSheets(String tareaOrg) {
        String urlGuardarTarea = "https://script.google.com/macros/s/YOUR_SCRIPT_ID/exec?action=guardarTarea&tarea=" + tareaOrg;
        StringRequest guardarTareaRequest = new StringRequest(Request.Method.GET, urlGuardarTarea,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("EXITO")) {
                            Toast.makeText(AccesoAdm_activity.this, "Tarea guardada en Google Sheets", Toast.LENGTH_SHORT).show();
                            // Puedes agregar lógica adicional aquí, como mostrar una notificación
                        } else {
                            Toast.makeText(AccesoAdm_activity.this, "Error al guardar la tarea en Google Sheets", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                Toast.makeText(AccesoAdm_activity.this, "Error al guardar la tarea en Google Sheets", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(guardarTareaRequest);
    }

}