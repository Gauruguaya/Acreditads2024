package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
public class UsuarioDAO extends AppCompatActivity {
    private Database db;
    public UsuarioDAO(Database db){
        this.db = db;
    }
    public long salvar(Usuario u){
        long idUsuario = -1;
        SQLiteDatabase db2 = db.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("nombreUsuario", u.getNombreUsuario());
            values.put("apellidoUsuario",u.getApellidoUsuario());
            values.put("docUsuario", u.getDocUsuario());
            values.put("emailUsuario", u.getEmailUsuario());
            values.put("tipoUsuario", u.getTipoUsuario());
            idUsuario = db2.insert("Usuario", null, values);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return idUsuario;
    }
    @SuppressLint("Range")
    public List<Usuario> buscarTodos(){
        List<Usuario> todos = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        try{
            Cursor c = db2.rawQuery("select * from Usuario", null);
            if (c.moveToFirst()){
                do {
                    Usuario u = new Usuario();
                    u.setIdUsuario(c.getInt(c.getColumnIndex("idUsuario")));
                    u.setNombreUsuario(c.getString(c.getColumnIndex("nombreUsuario")));
                    u.setApellidoUsuario(c.getString(c.getColumnIndex("apellidoUsuario")));
                    u.setDocUsuario(c.getString(c.getColumnIndex("docUsuario")));
                    u.setEmailUsuario(c.getString(c.getColumnIndex("emailUsuario")));
                    u.setTipoUsuario(c.getInt(c.getColumnIndex("tipoUsuario")));
                    todos.add(u);
                }while (c.moveToNext());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return todos;
    }
    @SuppressLint("Range")
    public List<Usuario> buscarPorTipo(int tipo) {
        List<Usuario> usuarios = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        try {
            String sql = "SELECT * FROM Usuario WHERE tipoUsuario="+tipo;
            Cursor c = db2.rawQuery(sql,null);
            if (c.moveToFirst()) {
                do {
                    Usuario u = new Usuario();
                    u.setIdUsuario(c.getInt(c.getColumnIndex("idUsuario")));
                    u.setNombreUsuario(c.getString(c.getColumnIndex("nombreUsuario")));
                    u.setApellidoUsuario(c.getString(c.getColumnIndex("apellidoUsuario")));
                    usuarios.add(u);
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return usuarios;
    }
}
