package com.example.acreditads2023;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
public class Consulta_id extends AppCompatActivity {
    Organizadores org;
    Participantes part;
    Invitados inv;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.consulta_id,menu);
        return true;
    }

    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_consulta_id);
        org = new Organizadores();
        part = new Participantes();
        inv = new Invitados();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.organizadores){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameId,org).commit();
            return true;
        }
        else  if (item.getItemId() == R.id.palestrantes){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameId,inv).commit();
            return true;
        }
        else if (item.getItemId() == R.id.asistentes){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameId,part).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
