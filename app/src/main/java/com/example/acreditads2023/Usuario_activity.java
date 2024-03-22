package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_activity);
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

                Retrofit retrofit = new  Retrofit.Builder()
                        .baseUrl("https://api.zerobounce.net/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Consulta_validacion correo = new Consulta_validacion();
                correo.setEmail(u.getEmailUsuario());

                api_email request = retrofit.create(api_email.class);
                request.consulta_correo(correo.getClave(),correo.getEmail()).enqueue(new Callback<retorno_validacion>() {
                    @Override
                    public void onResponse(Call<retorno_validacion> call, Response<retorno_validacion> response) {
                        retorno_validacion retorno = response.body();
                        Toast.makeText(getApplicationContext(),retorno.getAddress().toString(),Toast.LENGTH_LONG).show();
                        if(retorno.getStatus().equals("valid")){
                            UsuarioDAO uDAO = new UsuarioDAO(db);
                            uDAO.salvar(u);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Digite un email válido",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<retorno_validacion> call, Throwable t) {
                        t.getMessage();
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
