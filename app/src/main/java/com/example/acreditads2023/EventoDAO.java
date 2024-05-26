package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
public class EventoDAO extends AppCompatActivity {
    private final Database db;
    public EventoDAO(Database db){
        this.db = db;
    }
    public long salvar(Evento e){
        long idEvento = -1;
        SQLiteDatabase db2 = db.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("tituloEvento", e.getTituloEvento());
            values.put("autorEvento",e.getAutorEvento());
            values.put("tipoEvento", e.getTipoEvento());
            values.put("administradorEvento", e.getAdministradorEvento());
            values.put("fechaHoraEvento", e.getFechaHoraEvento());
            values.put("latitudEvento", e.getLatitudEvento());
            values.put("longitudEvento", e.getLongitudEvento());
            idEvento = db2.insert("Evento", null, values);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return idEvento;
    }
    @SuppressLint("Range")
    public List<Evento> buscarTodos(){
        List<Evento> todos = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        Cursor c = null;
        try{
            c = db2.rawQuery("select * from Evento", null);
            if (c.moveToFirst()){
                do {
                    Evento e = new Evento();
                    e.setIdEvento(c.getInt(c.getColumnIndex("idEvento")));
                    e.setTituloEvento(c.getString(c.getColumnIndex("tituloEvento")));
                    e.setAutorEvento(c.getString(c.getColumnIndex("autorEvento")));
                    e.setTipoEvento(c.getInt(c.getColumnIndex("tipoEvento")));
                    e.setAdministradorEvento(c.getString(c.getColumnIndex("AdministradorEvento")));
                    e.setFechaHoraEvento(c.getString(c.getColumnIndex("fechaHoraEvento")));
                    e.setLatitudEvento(c.getDouble(c.getColumnIndex("latitudEvento")));
                    e.setLongitudEvento(c.getDouble(c.getColumnIndex("longitudEvento")));
                    todos.add(e);
                }while (c.moveToNext());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }finally {
            c.close();
        }
        return todos;
    }
}
