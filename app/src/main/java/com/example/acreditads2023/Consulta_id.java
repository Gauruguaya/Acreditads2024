package com.example.acreditads2023;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Consulta_id extends AppCompatActivity {
    ListView lvMiActividad;
    Database db;
    private Button btnVolver;

    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_consulta_id);

    /*protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_consulta_id);
        part = new Participantes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = new Database(view.getContext());
        UsuarioDAO uDAO =new UsuarioDAO(db);



        String[] array = new String[participantes.size()];
        for(int i=0;i<participantes.size();i++){
            array[i] = participantes.get(i).getIdUsuario()+" "+participantes.get(i).getNombreUsuario()+" "+participantes.get(i).getApellidoUsuario()+"\n";
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, array);

        lvPart.setAdapter(adapter);
        return view;
    }
*/
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
