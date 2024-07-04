package com.example.acreditads2023;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Usuario_activity extends AppCompatActivity {
    private EditText edtNombreUsuario, edtApellidoUsuario, edtDocUsuario, edtEmailUsuario;
    private Spinner spnrTipoUsuario;
    private Database db;
    private Button btnRegistro, btnVolver;

    private CheckBox checkboxTerminos;
    private String idPlanilha = "1g6dIJTiR-4eIzbnwKUK6sXDjC6GYMCRElsbr_bVxPDQ", idInstalacion;
    private RequestQueue requestQueue;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final String CHANNEL_ID = "canal";

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    private boolean usuarioCreado = false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_activity);
        requestQueue = Volley.newRequestQueue(this);

        edtDocUsuario = findViewById(R.id.edtDocUsuario);
        edtEmailUsuario = findViewById(R.id.edtEmailUsuario);
        edtNombreUsuario = findViewById(R.id.edtNombreUsuario);
        edtApellidoUsuario = findViewById(R.id.edtApellidoUsuario);
        checkboxTerminos = findViewById(R.id.checkbox_terminos);

        spnrTipoUsuario = findViewById(R.id.spnrTipoUsuario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipo_usuario,
                R.layout.spinner_item_tipo_usuario
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrTipoUsuario.setAdapter(adapter);


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

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkboxTerminos.isChecked()) {
                    // Mostrar mensaje si no se aceptan los términos y condiciones
                    Toast.makeText(getApplicationContext(), "Debe aceptar los términos y condiciones", Toast.LENGTH_LONG).show();
                    return;
                }
                Usuario u = new Usuario();
                u.setNombreUsuario(edtNombreUsuario.getText().toString());
                u.setApellidoUsuario(edtApellidoUsuario.getText().toString());
                u.setDocUsuario(edtDocUsuario.getText().toString());
                u.setEmailUsuario(edtEmailUsuario.getText().toString());
                u.setTipoUsuario(spnrTipoUsuario.getSelectedItemPosition()+1);

                // Google Sheets URL
                String urlSheets = "https://script.google.com/macros/s/AKfycbyQyPbRkjrPCyy8mLro7-yrRdFxVR5Tgzt7liTI2JrVx53enlPlbFtxvDXQLLfCliId/exec?action=inserir"
                        + "&nome=" + u.getNombreUsuario()
                        + "&sobrenome=" + u.getApellidoUsuario()
                        + "&documento=" + u.getDocUsuario()
                        + "&email=" + u.getEmailUsuario()
                        + "&tipoUsuario=" + u.getTipoUsuario()
                        + "&idInstalacion=" + idInstalacion;


                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSheets, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Response", s);
                        Toast.makeText(getApplicationContext(), "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                        usuarioCreado = true;
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        Log.e("Error", error.toString());
                        usuarioCreado = false;
                    }
                });
                if(usuarioCreado == true)
                    verificarPermisoNotificaciones();
                else
                    Toast.makeText(getApplicationContext(), "Error en la notificacion", Toast.LENGTH_SHORT).show();
                requestQueue.add(stringRequest);
            }
        });

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Usuario_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //private void VerificacionDeUsuarioCreado(){
    //vaya a la sheet

    //si se encuentra el usuario
    //notificacion usuario creado
    //  cedulaAux = edtDocUsuario.getText().toString();
    //     if(cedulaAux.equals(/varible de Fabricio/)){
    //         verificarPermisoNotificaciones();
    //     }else{
    //         Toast.makeText(getApplicationContext(), "Error al crear Usuario! ", Toast.LENGTH_LONG).show();
    //     }
    //sino notoficacion error
    //     verificarPermisoNotificaciones();
    // }

    private void verificarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar si el permiso de notificación está concedido
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso de notificación
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                // Permiso ya concedido
                configurarCanalNotificacion();
                enviarNotificacion();
            }
        } else {
            // No es necesario solicitar permisos en versiones anteriores a Android 13
            configurarCanalNotificacion();
            enviarNotificacion();
        }
    }

    private void configurarCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Canal de notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            // Configurar otras propiedades del canal, si es necesario
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void enviarNotificacion() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // Icono de la notificación
                .setContentTitle("Creacion de Usuario")
                .setContentText("Usuario creado con exito!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    @SuppressLint("HardwareIds")
    private void initializeIdInstalacion() {
        // Ya no usamos getDeviceId() porque requiere READ_PRIVILEGED_PHONE_STATE
        // Utilizamos ANDROID_ID como una alternativa menos privilegiada
        idInstalacion = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);  // Asegúrate de llamar a la superclase
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeIdInstalacion();
            } else {
                Toast.makeText(this, "Permission denied to read phone state", Toast.LENGTH_SHORT).show();
                idInstalacion = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, configurar el canal de notificación
                configurarCanalNotificacion();
                enviarNotificacion();
            } else {
                // Permiso denegado
                Toast.makeText(getApplicationContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void descargarPDF() {
        // URL del PDF para descargar
        String urlPDF = "https://drive.google.com/file/d/178com7W9Ct-O2i3kSdeBHNHrtFdOOHvo/view";

        // Crear un Intent para abrir el navegador y descargar el PDF desde la URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlPDF));
        startActivity(intent);
    }
}