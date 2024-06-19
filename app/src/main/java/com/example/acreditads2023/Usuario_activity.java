package com.example.acreditads2023;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
    private String idInstalacion;

    private RequestQueue requestQueue;
    private static final int PERMISSION_REQUEST_CODE = 1;

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            idInstalacion = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            } else {
                initializeIdInstalacion();
            }
        }

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario u = new Usuario();
                u.setNombreUsuario(edtNombreUsuario.getText().toString());
                u.setApellidoUsuario(edtApellidoUsuario.getText().toString());
                u.setDocUsuario(edtDocUsuario.getText().toString());
                u.setEmailUsuario(edtEmailUsuario.getText().toString());
                u.setTipoUsuario(Integer.parseInt(edtTipoUsuario.getText().toString()));

                // Google Sheets URL
                String urlSheets = "https://script.google.com/macros/s/AKfycbyQyPbRkjrPCyy8mLro7-yrRdFxVR5Tgzt7liTI2JrVx53enlPlbFtxvDXQLLfCliId/exec?action=inserir&nome=" +
                        edtNombreUsuario.getText().toString() + "&sobrenome=" + edtApellidoUsuario.getText().toString() +
                        "&documento=" + edtDocUsuario.getText().toString() + "&email=" + edtEmailUsuario.getText().toString() + "&tipoUsuario=" + edtTipoUsuario.getText().toString() + "&idInstalacion=" + idInstalacion;

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

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("HardwareIds")
    private void initializeIdInstalacion() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (telephonyManager != null) {
                idInstalacion = telephonyManager.getDeviceId();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeIdInstalacion();
            } else {
                Toast.makeText(this, "Permission denied to read phone state", Toast.LENGTH_SHORT).show();
                idInstalacion = "PermissionDenied";
            }
        }
    }
}
z