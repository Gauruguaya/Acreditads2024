package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Usuario_activity extends AppCompatActivity {
    private EditText edtNombreUsuario;
    private EditText edtApellidoUsuario;
    private EditText edtDocUsuario;
    private EditText edtEmailUsuario;
    private EditText edtTipoUsuario;
    private Database db;
    private Button btnRegistro;
    private Button btnVolver;
    private String idPlanilha = "1g6dIJTiR-4eIzbnwKUK6sXDjC6GYMCRElsbr_bVxPDQ";

    private RequestQueue requestQueue;

    String range = "Sheet1A1:B2";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_activity);
        requestQueue = Volley.newRequestQueue(this);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnVolver = findViewById(R.id.btnVolver);
        edtNombreUsuario = findViewById(R.id.edtNombreUsuario);
        edtApellidoUsuario = findViewById(R.id.edtApellidoUsuario);
        edtDocUsuario = findViewById(R.id.edtDocUsuario);
        edtEmailUsuario = findViewById(R.id.edtEmailUsuario);
        edtTipoUsuario = findViewById(R.id.edtTipoUsuario);

        db = new Database(getApplicationContext());

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario u = new Usuario();
                u.setNombreUsuario(edtNombreUsuario.getText().toString());
                u.setApellidoUsuario(edtApellidoUsuario.getText().toString());
                u.setDocUsuario(edtDocUsuario.getText().toString());
                u.setEmailUsuario(edtEmailUsuario.getText().toString());
                u.setTipoUsuario(Integer.parseInt(edtTipoUsuario.getText().toString()));

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.zerobounce.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Consulta_validacion correo = new Consulta_validacion();
                correo.setEmail(u.getEmailUsuario());

                // Google Sheets URL
                String urlSheets = "https://script.google.com/macros/s/AKfycbwo4OPDNV5K_DdrmZoVrY-Mng3X0zmwW3FVMYdLDAzKBZ35AXifkwl3fdTg9T_A6jTg/exec?action=inserir&nome=" +
                        edtNombreUsuario.getText().toString() + "&sobrenome=" + edtApellidoUsuario.getText().toString() +
                        "&documento=" + edtDocUsuario.getText().toString() + "&email=" + edtEmailUsuario.getText().toString() + "&tipoUsuario=" + edtTipoUsuario.getText().toString();

                //aaaaaaa
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

                api_email request = retrofit.create(api_email.class);
                request.consulta_correo(correo.getClave(), correo.getEmail()).enqueue(new Callback<retorno_validacion>() {
                    @Override
                    public void onResponse(Call<retorno_validacion> call, Response<retorno_validacion> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            retorno_validacion retorno = response.body();
                            Toast.makeText(getApplicationContext(), retorno.getAddress(), Toast.LENGTH_LONG).show();
                            if ("valid".equals(retorno.getStatus())) {
                                UsuarioDAO uDAO = new UsuarioDAO(db);
                                uDAO.salvar(u);
                                Toast.makeText(getApplicationContext(), "Usuario registrado con éxito", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Digite un email válido", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error en la validación del correo", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<retorno_validacion> call, Throwable t) {
                        Log.e("Error", t.getMessage(), t);
                        Toast.makeText(getApplicationContext(), "Error en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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