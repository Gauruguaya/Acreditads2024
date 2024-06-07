package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class Agenda_activity extends AppCompatActivity {
    private Button btnInscripcion;
    private Button btnVolver;
    private View view;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_activity);

        WebView webViewAgenda = findViewById(R.id.webViewAgenda);
        WebSettings webSettings = webViewAgenda.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewAgenda.loadUrl("https://www.even3.com.br/semabi-2023/");

        btnInscripcion = findViewById(R.id.btnInscripcion);
        btnInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Usuario_activity.class);
                startActivity(intent);
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
