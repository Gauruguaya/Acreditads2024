package com.example.acreditads2023;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
public class Invitados extends Fragment {
    ListView lvInv;
    Database db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_invitados, container, false);
        db = new Database(view.getContext());
        UsuarioDAO uDAO =new UsuarioDAO(db);

        List<Usuario> invitados = uDAO.buscarPorTipo(1);
        lvInv = view.findViewById(R.id.lvInvitados);
        String[] array = new String[invitados.size()];
        for(int i=0;i<invitados.size();i++){
            array[i] = invitados.get(i).getIdUsuario()+" "+invitados.get(i).getNombreUsuario()+" "+invitados.get(i).getApellidoUsuario()+"\n";
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, array);

        lvInv.setAdapter(adapter);
        return view;
    }
}