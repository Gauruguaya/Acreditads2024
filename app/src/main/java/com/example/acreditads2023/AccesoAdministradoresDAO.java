package com.example.acreditads2023;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;

public class AccesoAdministradoresDAO extends AppCompatActivity {
        private Database db;

        public AccesoAdministradoresDAO(Database db) {
            this.db = db;
        }

        // Este método guarda la tarea del organizador en la base de datos local
        public long salvar(AccesoAdministradores accesoAdministradores) {
            SQLiteDatabase db2 = db.getWritableDatabase();

            try {
                // Crear un objeto ContentValues para insertar datos en la base de datos
                ContentValues values = new ContentValues();
                values.put("tareaOrg", accesoAdministradores.getTareaOrg());  // Solo se guarda la tarea del organizador

                // Guardar en la base de datos
                return db2.insert("AccesoAdministradores", null, values);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                db2.close();
            }
            return -1; // Retorno -1 si no se guarda correctamente
        }
    }
