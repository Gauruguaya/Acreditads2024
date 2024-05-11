package com.example.acreditads2023;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Participantes extends Fragment {
    ListView lvPart;
    Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_participantes, container, false);
        db = new Database(view.getContext());
        UsuarioDAO uDAO =new UsuarioDAO(db);

        List<Usuario> participantes = uDAO.buscarPorTipo(3);
        lvPart = view.findViewById(R.id.lvParticipantes);
        String[] array = new String[participantes.size()];
        for(int i=0;i<participantes.size();i++){
            array[i] = participantes.get(i).getIdUsuario()+" "+participantes.get(i).getNombreUsuario()+" "+participantes.get(i).getApellidoUsuario()+"\n";
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, array);

        lvPart.setAdapter(adapter);
        return view;
    }
}