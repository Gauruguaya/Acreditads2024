package com.example.acreditads2023;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class UbicarEventoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private final int FINE_PERMISSION_CODE = 123;
    private Circle circlePrevio;
    private Marker marker;
    EditText txtLatitud, txtLongitud; //mis variables de Latitud(Norte, Sur) y Longitud(Este, Oeste)
    GoogleMap mMap;
    String nombreEvento;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicar_evento);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        Button btnConfirmar = findViewById(R.id.Confirmar);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);

        nombreEvento = getIntent().getStringExtra("NombreEvento");

        //Devuelve los valores latitud y longitud
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de las variables que deseas devolver
                String LatitudString = txtLatitud.getText().toString();
                String LongitudString = txtLongitud.getText().toString();

                // Crear un nuevo Intent
                Intent intent = new Intent();

                // Agregar las variables al Intent como extras
                intent.putExtra("Latitud", LatitudString);
                intent.putExtra("Longitud", LongitudString);

                // Establecer el código de resultado y finalizar la actividad
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(UbicarEventoActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        //aca deberia estar la ubicacion de la persoana
        LatLng uruguay = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()); //ubicacion ifsul -30.9008403,-55.5354775
       // mMap.addMarker(new MarkerOptions().position(uruguay).title("mi ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uruguay));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                getLastLocation();
            } else {
                Toast.makeText(this,"El acceso a la ubicacion fue denegada, permitir acceso!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        txtLatitud.setText(""+latLng.latitude);
        txtLongitud.setText(""+latLng.longitude);

        String strLatitud = txtLatitud.getText().toString();
        String strLongitud = txtLongitud.getText().toString();
        double latitud = Double.parseDouble(strLatitud);
        double longitud = Double.parseDouble(strLongitud);
        //guarda mi nueva ubicacion
        LatLng nuevaUbicacion = new LatLng(latitud, longitud);
        // Si ya hay un marcador en el mapa, elimínalo
        if (marker != null) {
            marker.remove();
        }
        // Agregar un nuevo marcador en la nueva ubicación
        marker = mMap.addMarker(new MarkerOptions().position(nuevaUbicacion).title(nombreEvento));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaUbicacion));
        // cada vez que crea un nuevo ciruclo borra el anterior
        if (circlePrevio != null){
            circlePrevio.remove();
        }
        // dibuja mi circulo
        drawCircle(latLng,30);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtLatitud.setText(""+latLng.latitude);
        txtLongitud.setText(""+latLng.longitude);
    }

    private void drawCircle(LatLng center, double radio){
        circlePrevio = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radio)
                .strokeWidth(2)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(70,255,0,0)));
    }
}