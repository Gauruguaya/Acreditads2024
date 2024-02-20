package com.example.acreditads2023;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    private static final String TAG = "BANCO";
    private static final String NOME_BANCO = "banco2.sqlite";
    private static final int VERSAO = 1;
    public Database(Context context){
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creando tablas de la base de datos.");
        db.execSQL("create table if not exists Usuario(idUsuario " +
                "integer primary key autoincrement, nombreUsuario text, apellidoUsuario text," +
                "docUsuario text, emailUsuario text, tipoUsuario integer, tareaUsuario text, fechaHora text);");
        db.execSQL("create table if not exists Evento(idEvento " +
                "integer primary key autoincrement, tituloEvento text, autorEvento text," +
                "tipoEvento integer, fechaHoraEvento text, idAdminEvento integer, qrApertura blob, qrCierre blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nao utilizaremos este salvo si tuviéramos en el futuro que cambiar la base de datos
    }

}
