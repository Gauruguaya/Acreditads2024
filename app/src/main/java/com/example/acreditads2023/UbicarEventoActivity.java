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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.events.MapEventsReceiver;

import java.util.ArrayList;

public class UbicarEventoActivity extends AppCompatActivity {

    private final int FINE_PERMISSION_CODE = 123;
    private Polygon circlePrevio;
    private OverlayItem marker;
    private ItemizedOverlayWithFocus<OverlayItem> itemizedOverlay;

    EditText txtLatitud, txtLongitud; //mis variables de Latitud(Norte, Sur) y Longitud(Este, Oeste)
    MapView mMap;
    String nombreEvento;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private String LatitudString;
    private String LongitudString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar Osmdroid ANTES de setContentView
        Configuration.getInstance().setUserAgentValue(getApplicationContext().getPackageName());
        
        setContentView(R.layout.activity_ubicar_evento);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        Button btnConfirmar = findViewById(R.id.Confirmar);
        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);

        nombreEvento = getIntent().getStringExtra("NombreEvento");
        //###########################################################################################
        //###############  Devuelve los valores latitud y longitud   ################################
        //
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creo un nuevo Intent
                Intent intent = new Intent();

                // Obtener los valores de las variables que deseas devolver
                LatitudString = txtLatitud.getText().toString();
                LongitudString = txtLongitud.getText().toString();

                // Agregar las variables al Intent como extras
                intent.putExtra("Latitud", LatitudString);
                intent.putExtra("Longitud", LongitudString);

                // Establecer el código de resultado y finalizar la actividad
                if (!LongitudString.isEmpty() && !LatitudString.isEmpty()){
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Log.e("Error","Algunos de los campos latitud o Longitud van vacios");
                }

            }
        });
        //###########################################################################################
        //###########################################################################################
    }

    //################################### AFUERA DEL onCreate #######################################
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
                    initializeMap();  // Cambio: Llamar a método para inicializar el mapa
                }
            }
        });
    }
    //###########################################################################################

    //###########################################################################################
    private void initializeMap() {
        mMap = findViewById(R.id.map);
        
        // Configurar la fuente de tiles (OpenStreetMap)
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        
        // Obtener el controlador del mapa
        IMapController mapController = mMap.getController();
        mapController.setZoom(10.0);
        
        // Crear punto inicial con la ubicación actual
        GeoPoint startPoint = new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
        mapController.setCenter(startPoint);
        
        // Inicializar overlay para marcadores
        ArrayList<OverlayItem> items = new ArrayList<>();
        itemizedOverlay = new ItemizedOverlayWithFocus<>(this, items, 
            new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    return true;
                }

                @Override
                public boolean onItemLongPress(int index, OverlayItem item) {
                    // Obtener coordenadas del marcador al hacer long press
                    GeoPoint point = item.getPoint();
                    txtLatitud.setText("" + point.getLatitude());
                    txtLongitud.setText("" + point.getLongitude());
                    return true;
                }
            });
        
        mMap.getOverlays().add(itemizedOverlay);
        
        // Configurar listener para clicks en el mapa
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                onMapClick(geoPoint);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false;
            }
        };
        
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        mMap.getOverlays().add(0, mapEventsOverlay);
    }
    //###########################################################################################

    //###########################################################################################
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
    //###########################################################################################

    //###########################################################################################
    private void onMapClick(GeoPoint geoPoint) {
        txtLatitud.setText("" + geoPoint.getLatitude());
        txtLongitud.setText("" + geoPoint.getLongitude());

        String strLatitud = txtLatitud.getText().toString();
        String strLongitud = txtLongitud.getText().toString();
        double latitud = Double.parseDouble(strLatitud);
        double longitud = Double.parseDouble(strLongitud);
        
        GeoPoint nuevaUbicacion = new GeoPoint(latitud, longitud);
        
        // Remover marcador anterior
        if (marker != null) {
            itemizedOverlay.removeItem(marker);
        }
        
        // Agregar nuevo marcador
        marker = new OverlayItem(nombreEvento, nombreEvento, nuevaUbicacion);
        itemizedOverlay.addItem(marker);
        itemizedOverlay.setFocus(marker);
        
        // Mover cámara a la nueva ubicación
        IMapController mapController = mMap.getController();
        mapController.setCenter(nuevaUbicacion);
        
        // Remover círculo anterior
        if (circlePrevio != null) {
            mMap.getOverlays().remove(circlePrevio);
        }
        
        // Dibujar nuevo círculo
        drawCircle(nuevaUbicacion, 30);
        
        // Refrescar el mapa
        mMap.invalidate();
    }
    //###########################################################################################

    //###########################################################################################
    private void drawCircle(GeoPoint center, double radiusMeters) {
        Polygon circle = new Polygon(mMap);
        circle.setPoints(Polygon.pointsAsCircle(center, radiusMeters));
        circle.setStrokeWidth(2);
        circle.setStrokeColor(Color.RED);
        circle.setFillColor(Color.argb(70, 255, 0, 0));
        
        mMap.getOverlays().add(circle);
        circlePrevio = circle;
    }
    //###########################################################################################

    @Override
    protected void onResume() {
        super.onResume();
        // Reanudar el mapa cuando la actividad vuelve al frente
        if (mMap != null) {
            mMap.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pausar el mapa cuando la actividad se pausa
        if (mMap != null) {
            mMap.onPause();
        }
    }
}