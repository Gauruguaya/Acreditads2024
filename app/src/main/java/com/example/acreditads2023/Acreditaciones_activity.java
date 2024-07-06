package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Size;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Acreditaciones_activity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private boolean isQRCodeDetected = false, isInside;
    private Button btnAgregar, btnVolver;
    private View view;
    private TextView txQrCodeResult;
    private ProcessCameraProvider cameraProvider;
    public String rawValue, Latitud, Longitud;
    public AcreditacionesDAO aDAO;
    private double LatitudPersona, LongitudPersona, NumLatitud, NumLongitud;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acreditaciones_activity);

        requestQueue = Volley.newRequestQueue(this);

        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();

        txQrCodeResult = findViewById(R.id.txQrCodeResult);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }

        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
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

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Erro ao inicializar a câmera: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        try {
            Preview preview = new Preview.Builder()
                    .build();

            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .setTargetResolution(new Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

            imageAnalysis.setAnalyzer(cameraExecutor, new QRCodeAnalyzer());

            preview.setSurfaceProvider(previewView.getSurfaceProvider());

            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao exibir a prévia: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class QRCodeAnalyzer implements ImageAnalysis.Analyzer {
        private final BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();

        @Override
        public void analyze(@NonNull ImageProxy image) {

            if (isQRCodeDetected) {
                image.close();
                return;
            }

            //@androidx.camera.core.ExperimentalGetImage
            @OptIn(markerClass = ExperimentalGetImage.class)
            ImageProxy.PlaneProxy[] planes = image.getPlanes();
            if (planes.length == 0) {
                return;
            }

            @OptIn(markerClass = ExperimentalGetImage.class)
            InputImage inputImage = InputImage.fromMediaImage(image.getImage(), image.getImageInfo().getRotationDegrees());

            BarcodeScanner scanner = BarcodeScanning.getClient(options);
            scanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            isQRCodeDetected = true;
                            rawValue = barcode.getRawValue();
                            handleQRCodeData(rawValue);
                            break; // Para o processamento de futuros códigos.
                        }
                    })
                    .addOnFailureListener(Throwable::printStackTrace)
                    .addOnCompleteListener(task -> image.close());
        }
    }

    private void handleQRCodeData(String data) {
        // Executar aqui as ações com os dados lidos do QR Code.
        // Mostrando um Toast apenas para demonstração do funcionamento.

        Acreditaciones a = new Acreditaciones();
        a.setTxQrCodeResult(rawValue);
        a.setTimeStamp(LocalDateTime.now().toString());
        //aDAO.salvar(a);
        btnAgregar.setVisibility(View.VISIBLE);

        //Toast.makeText(this, "QR Code Data: " + data, Toast.LENGTH_LONG).show();

        // Finaliza a prévia da câmera depois de fazer a leitura do código
        stopCameraPreview();

        txQrCodeResult.setText("Dados do código: "+data + "\n\n Timestamp: "+LocalDateTime.now());
        previewView.setVisibility(View.INVISIBLE);
        txQrCodeResult.setVisibility(TextView.VISIBLE);

        // Google Sheets URL
        String urlSheets = "https://script.google.com/macros/s/AKfycbz3wNphwxMkuqRkB1rryK2uhv0QMQkrXhSxWgzFjUraC4I6w2sN3aAwPMISRVLCd04/exec?action=buscarEventoPorCodigo"
                + "&codigoQr=" + "18";

        showBlockingDialog("Executando...");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSheets, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // Procesar la respuesta del servidor
                dismissBlockingDialog();
                retGetEvento(s);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(com.android.volley.VolleyError error) {
                dismissBlockingDialog();
                Log.e("Error", error.toString());
                Toast.makeText(getApplicationContext(), "Evento no existe!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissões não concedidas pelo usuário.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void stopCameraPreview() {
        if (cameraProvider != null) {
            cameraExecutor.shutdown();
            cameraProvider.unbindAll();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    LatitudPersona = location.getLatitude();
                    LongitudPersona = location.getLongitude();

                    checkIfInsideArea();
                } else {
                    Toast.makeText(Acreditaciones_activity.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkIfInsideArea() {
        pushLocalidadEvento();

        try {
            NumLatitud = Double.parseDouble(Latitud);
            NumLongitud = Double.parseDouble(Longitud);

            // Conversión de 30 metros a grados
            double metersToDegrees = 30.0 / 111000.0;
            double deltaLat = metersToDegrees;
            double deltaLong = metersToDegrees / Math.cos(Math.toRadians(NumLatitud));

            // Coordenadas límite del área
            double minLat = NumLatitud - deltaLat;
            double maxLat = NumLatitud + deltaLat;
            double minLong = NumLongitud - deltaLong;
            double maxLong = NumLongitud + deltaLong;

            // Verificación de si la persona está dentro del área
            if (LatitudPersona >= minLat && LatitudPersona <= maxLat && LongitudPersona >= minLong && LongitudPersona <= maxLong){
                isInside = true;
            } else {
                isInside = false;
            }

            String message = isInside ? "La persona está dentro del área." : "La persona está fuera del área.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Error al convertir la latitud o longitud", Toast.LENGTH_SHORT).show();
        }
    }

    private void pushLocalidadEvento(){
        // Aca debe de estar los valores de la ubicacion del evento los cuales estan en la sheet
        // y settear los valores a estas dos variables
        Latitud = "-12.122345";
        Longitud = "0.000000";
    }

    protected void showBlockingDialog(String message) {

        dismissBlockingDialog();
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    protected void dismissBlockingDialog() {

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    protected void retGetEvento(String s){
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        Log.i("Retorno evento: ", s);
    }

}
