package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class AcreditacionesDAO {
    private final Database db;
    int idUsuario;
    public AcreditacionesDAO(Database db){
        this.db = db;
    }
    public void salvar(Acreditaciones a){

        SQLiteDatabase db2 = db.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("lecturaQR",a.getTxQrCodeResult());
            values.put("timeStamp", a.getTimeStamp());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return;
    }
    @SuppressLint("Range")
    public List<Acreditaciones> buscarAcreditaciones(){
        List<Acreditaciones> todos = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        Cursor c = db2.rawQuery("SELECT * FROM Acreditaciones WHERE idUsuario = ?",
                new String[]{String.valueOf(idUsuario)});
        try {
            if (c.moveToFirst()) {
                do {
                    Acreditaciones a = new Acreditaciones();a.setTxQrCodeResult(c.getString(c.getColumnIndex("lecturaQR")));
                    a.setTimeStamp(c.getString(c.getColumnIndex("timeStamp")));
                    todos.add(a);
                } while (c.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return todos;
    }
}