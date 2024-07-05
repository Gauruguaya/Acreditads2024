package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
    private PreviewView previewView;
    private ExecutorService cameraExecutor;
    private boolean isQRCodeDetected = false;
    private Button btnAgregar, btnVolver;
    private View view;
    private TextView txQrCodeResult;
    private ProcessCameraProvider cameraProvider;
    public String rawValue;
    public AcreditacionesDAO aDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acreditaciones_activity);

        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();

        txQrCodeResult = findViewById(R.id.txQrCodeResult);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
        }

        btnAgregar = findViewById(R.id.btnAgregar);
        btnVolver = findViewById(R.id.btnVolver);

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
                            Acreditaciones a = new Acreditaciones();
                            a.setTxQrCodeResult(rawValue);
                            a.setTimeStamp(LocalDateTime.now().toString());
                            aDAO.salvar(a);
                            handleQRCodeData(rawValue);
                            btnAgregar.setVisibility(View.VISIBLE);
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
        Toast.makeText(this, "QR Code Data: " + data, Toast.LENGTH_LONG).show();

        // Finaliza a prévia da câmera depois de fazer a leitura do código
        stopCameraPreview();

        txQrCodeResult.setText("Dados do código: "+data + "\n\n Timestamp: "+LocalDateTime.now());
        previewView.setVisibility(View.INVISIBLE);
        txQrCodeResult.setVisibility(TextView.VISIBLE);

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

}
