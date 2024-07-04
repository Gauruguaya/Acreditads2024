package com.example.acreditads2023;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AccesoAdm_activity extends AppCompatActivity {

    private EditText edtClaveAdm, edtTareaOrg;
    private Button btnAgregar;
    private Button btnVolver;
    private static final String CHANNEL_ID = "canal";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String idInstalacion;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceso_administradores);

        edtClaveAdm = findViewById(R.id.edtClaveAdm);
        edtTareaOrg = findViewById(R.id.edtTareaOrg);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnVolver = findViewById(R.id.btnVolver);

        requestQueue = Volley.newRequestQueue(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            idInstalacion = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            } else {
                initializeIdInstalacion();
            }
        }

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String claveAdm = edtClaveAdm.getText().toString();
                String tareaOrg = edtTareaOrg.getText().toString();

                Log.d("Clave pega", claveAdm);

                if (tareaOrg != null) {
                    // URL para guardar la tarea en Google Sheets
                    String urlGuardarTarea = "https://script.google.com/macros/s/AKfycbwRHeHUHMtCXLe-iyzuVj6MXrxaW-zl5-Q-g8h_yHWYAm8KI9jDwaKcFINKpyX-eA/exec?action=guardarTarea&tarea=" + tareaOrg + "&idInstalacion=" + idInstalacion;
                    StringRequest guardarTareaRequest = new StringRequest(Request.Method.GET, urlGuardarTarea, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("EXITO")) {
                                Toast.makeText(AccesoAdm_activity.this, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show();
                                verificarPermisoNotificaciones();
                            } else {
                                Toast.makeText(AccesoAdm_activity.this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error", "Erro ao guardar a tarefa: " + error.toString());
                            Toast.makeText(AccesoAdm_activity.this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(guardarTareaRequest);
                }

                // URL para verificar la clave en Google Sheets
                String urlVerificarClave = "https://script.google.com/macros/s/AKfycbwXHk8xtcQgeb_1uLDEyLCmt9lb5Mi-wFQWFrQWPu-JgBhBGcVGboHkj0VR_YNS3bNo/exec?action=verificarClave&clave=" + claveAdm;
                Log.d("Url completa", urlVerificarClave);

                StringRequest verificarClaveRequest = new StringRequest(Request.Method.GET, urlVerificarClave, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log para verificar a resposta recebida
                        Log.d("Resposta do Script", "Resposta recebida: " + response);

                        // Garantir que a resposta não contenha espaços em branco extras
                        if (response.trim().equals("true")) {
                            // Se a clave é válida, prosseguir com a tarefa
                            Intent intent = new Intent(getApplicationContext(), Evento_activity.class);
                            startActivity(intent);
                        } else {
                            Log.d("Resposta do Script", "Clave de administrador no válida: " + response);
                            Toast.makeText(AccesoAdm_activity.this, "Clave de administrador no válida", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Erro ao verificar a clave: " + error.toString());
                        Toast.makeText(AccesoAdm_activity.this, "Error al verificar la clave", Toast.LENGTH_SHORT).show();
                    }
                });

                requestQueue.add(verificarClaveRequest);
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("HardwareIds")
    private void initializeIdInstalacion() {
        idInstalacion = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

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
                .setContentTitle("Tarea de Organización")
                .setContentText("Tarea registrada exitosamente")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeIdInstalacion();
            } else {
                Toast.makeText(this, "Permission denied to read phone state", Toast.LENGTH_SHORT).show();
                idInstalacion = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
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
}
