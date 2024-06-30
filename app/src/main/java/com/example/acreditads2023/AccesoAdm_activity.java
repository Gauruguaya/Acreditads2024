package com.example.acreditads2023;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.acceso_administradores);

        edtClaveAdm = findViewById(R.id.edtClaveAdm);
        edtTareaOrg = findViewById(R.id.edtTareaOrg);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnVolver = findViewById(R.id.btnVolver);

        requestQueue = Volley.newRequestQueue(this);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String claveAdm = edtClaveAdm.getText().toString();
                String tareaOrg = edtTareaOrg.getText().toString();

                // URL para verificar la clave en Google Sheets
                String urlVerificarClave = "https://script.google.com/macros/s/AKfycbxX1EInivW85EH1IGNZzh6bWJTLm6K9-ZRr3-wduHYFd34d1SW0dG2bAndm8CZvYPE/exec?action=verificarClave&clave=" + claveAdm;

                StringRequest verificarClaveRequest = new StringRequest(Request.Method.GET, urlVerificarClave, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("VALIDO")) {
                            // Si la clave es válida, guardar la tarea en Google Sheets
                            String urlGuardarTarea = "https://script.google.com/macros/s/AKfycbxX1EInivW85EH1IGNZzh6bWJTLm6K9-ZRr3-wduHYFd34d1SW0dG2bAndm8CZvYPE/exec?action=guardarTarea&tarea=" + tareaOrg;

                            StringRequest guardarTareaRequest = new StringRequest(Request.Method.GET, urlGuardarTarea, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("EXITO")) {
                                        Toast.makeText(AccesoAdm_activity.this, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show();
                                        verificarPermisoNotificaciones();
                                    } else {
                                        Toast.makeText(AccesoAdm_activity.this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error", error.toString());
                                    Toast.makeText(AccesoAdm_activity.this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show();
                                }
                            });

                            requestQueue.add(guardarTareaRequest);
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
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verificarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                configurarCanalNotificacion();
                enviarNotificacion();
            }
        } else {
            configurarCanalNotificacion();
            enviarNotificacion();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configurarCanalNotificacion();
                enviarNotificacion();
            } else {
                Toast.makeText(this, "Permiso de notificación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void configurarCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Canal de notificaciones", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void enviarNotificacion() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Tarea de Organización")
                .setContentText("Tarea registrada exitosamente")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}



