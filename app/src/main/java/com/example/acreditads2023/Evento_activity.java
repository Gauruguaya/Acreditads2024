package com.example.acreditads2023;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.TextView;
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
public class Evento_activity extends AppCompatActivity {
    private Database db;
    private String latitud,longitud;
    private RequestQueue requestQueue;
    private static final int REQUEST_CODE = 1;
    private Button btnAgregar,btnVolver,btnMapa;
    private static final String CHANNEL_ID = "canal";
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private EditText edtTituloEvento,edtAutorEvento,edtTipoEvento,edtFechaHoraEvento,edtIdAdminEvento;
    private TextView latitudView;
    private TextView longitudView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evento_activity);

        requestQueue = Volley.newRequestQueue(this);

        edtTipoEvento = findViewById(R.id.edtTipoEvento);
        edtTituloEvento = findViewById(R.id.edtTipoEvento);
        edtAutorEvento = findViewById(R.id.edtAutorEvento);
        edtIdAdminEvento = findViewById(R.id.edtIdAdminEvento);
        edtFechaHoraEvento = findViewById(R.id.edtFechaHoraEvento);
        latitudView = findViewById(R.id.tvLatitud);
        longitudView = findViewById(R.id.tvLongitud);

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

                // Llamar al método verificarPermisoNotificaciones() después de que el usuario se registre con éxito
                verificarPermisoNotificaciones();


                // Google Sheets URL
                //Eu preciso puxar as variáveis aqui
                String urlSheets = "https://script.google.com/macros/s/AKfycbwYDDtE4okyKpEZ5y1ZmhwJzOqP8FXTCEvjRVTxk846eRw0Ud1RqlgYKlKeIFf9S3KE/exec?action=evento&tituloEvento=" +
                        edtTituloEvento.getText().toString() + "&autorEvento=" + edtAutorEvento.getText().toString() +
                        "&tipoEvento=" + edtTipoEvento.getText().toString() + "&fechaHoraEvento=" + edtFechaHoraEvento.getText().toString() + "&latitud=" + latitud + "&longitud=" + longitud;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSheets, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i("Response", s);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        Log.e("Error", error.toString());
                        Toast.makeText(getApplicationContext(), "Error al crear Evento", Toast.LENGTH_SHORT).show();
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
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    // Recibe valores Latitud y Longitud
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Obtener los valores de las variables devueltas
            String LatitudAux = data.getStringExtra("Latitud");
            String LongitudAux = data.getStringExtra("Longitud");

            // VARIABLES QUE CONTIENEN EL VALOR DEL EVENTO
            latitud = LatitudAux;
            longitud = LongitudAux;
            latitudView.setText(latitud);
            longitudView.setText(longitud);
        }
    }

    private void verificarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar si el permiso de notificación está concedido
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicitar permiso de notificación
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                // Permiso ya concedido
                Toast.makeText(this, "Permisos de notificación ya concedidos", Toast.LENGTH_SHORT).show();
                configurarCanalNotificacion();
                enviarNotificacion();
            }
        } else {
            // No es necesario solicitar permisos en versiones anteriores a Android 13
            configurarCanalNotificacion();
            enviarNotificacion();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, configurar el canal de notificación
                configurarCanalNotificacion();
                enviarNotificacion();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de notificación denegado", Toast.LENGTH_SHORT).show();
            }
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
}
