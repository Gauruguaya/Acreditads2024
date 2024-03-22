package com.example.acreditads2023;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
public class Organizadores extends Fragment {
    ListView lvOrg;
    Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_organizadores, container, false);
        db = new Database(view.getContext());
        UsuarioDAO uDAO =new UsuarioDAO(db);

        List<Usuario> organizadores = uDAO.buscarPorTipo(2);
        lvOrg = view.findViewById(R.id.lvOrganizadores);
        String[] array = new String[organizadores.size()];
        for(int i=0;i<organizadores.size();i++){
            array[i] = organizadores.get(i).getIdUsuario()+" "+organizadores.get(i).getNombreUsuario()+" "+organizadores.get(i).getApellidoUsuario()+"\n";
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, array);

        lvOrg.setAdapter(adapter);
        return view;
    }
}