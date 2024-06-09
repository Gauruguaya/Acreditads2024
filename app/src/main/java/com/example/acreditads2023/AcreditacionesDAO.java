package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class AcreditacionesDAO {
    private final Database db;
    private int idInstalacion;
    public AcreditacionesDAO(Database db){
        this.db = db;
    }
    public int salvar(Acreditaciones a){

        SQLiteDatabase db2 = db.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            //values.put(("idInstalacion", a.getIdInstalacion());
            values.put("idUsuario", a.getIdUsuario());
            values.put("lecturaQR",a.getLecturaQR());
            values.put("timeStamp", a.getTimeStamp());

            //idInstalacion = db2.insert("Acreditaciones", null, values);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return idInstalacion;
    }
    @SuppressLint("Range")
    public List<Acreditaciones> buscarTodos(){
        List<Acreditaciones> todos = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        Cursor c = null;
        try{
            c = db2.rawQuery("select * from Acreditaciones", null);
            if (c.moveToFirst()){
                do {
                    Acreditaciones a = new Acreditaciones();
                    a.setIdInstalacion(c.getInt(c.getColumnIndex("idInstalación")));
                    a.setIdUsuario(c.getInt(c.getColumnIndex("idUsuario")));
                    a.setLecturaQR(c.getString(c.getColumnIndex("autorEvento")));
                    a.setTimeStamp(c.getString(c.getColumnIndex("timeStamp")));
                    todos.add(a);
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

    public List<Evento> buscarUsuario(int idInstBuscada){
        List<Evento> usuario = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        Cursor u = null;
        try{
            u = db2.rawQuery("select e.tituloEvento FROM Evento e INNER JOIN Acreditaciones a ON+" +
                    "e.idEvento = a.idEvento INNER JOIN Usuario u ON a.idUsuario = u.idUsuario WHERE+" +
                    " u.idInstalacion ="+idInstBuscada, null);
            if (u.moveToFirst()){
                do {
                    Evento au = new Evento();
                    int idInstalacionIndex = u.getColumnIndex("idInstalación");
                    if (idInstalacionIndex != -1) {
                   //     au.setIdInstalacion(u.getInt(idInstalacionIndex));
                    }
                 //   au.setTituloEvento(u.getString(u.getColumnIndex("tituloEvento")));
                    usuario.add(au);
                }while (u.moveToNext());
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }finally {
            u.close();
        }
        return usuario;
    }

}
