package com.example.acreditads2023;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import android.util.Log;
import android.widget.Toast;

public class AcreditacionesDAO {
    private final Database db;
    int idUsuario;
    public AcreditacionesDAO(Database db){
        this.db = db;
    }
    public void salvar(Acreditaciones nuevaAcreditacion) {
        SQLiteDatabase db2 = db.getWritableDatabase();
        try {// 1. Check if a scan for the same QR code already exists
            String selection = "lecturaQR = ?";
            String[] selectionArgs = { nuevaAcreditacion.getTxQrCodeResult() };
            Cursor existingScanCursor = db2.query("Acreditaciones",
                    new String[]{"timeStamp"},
                    selection,
                    selectionArgs,
                    null, null, null);

            if (existingScanCursor.moveToFirst()) {
                // 2. Get timeStamp
                String existingTimeStamp = existingScanCursor.getString(existingScanCursor.getColumnIndex("timeStamp"));

                // 3. Calcular a diferença de tempo
                long timeDifference = calculateTimeDifference(existingTimeStamp, nuevaAcreditacion.getTimeStamp());

                // 4. si la diferencia de tiempo es al menos una hora
                long oneHourInMillis = 60 * 60 * 1000;
                if (timeDifference >= oneHourInMillis) {
                    insertAccreditation(db2, nuevaAcreditacion);
                }
            } else {
                // 5. Si no hay una lectura anterior, no insertamos la nueva
               // Toast.makeText(getApplicationContext(), "No hay una lectura anterior", Toast.LENGTH_SHORT).show();
            }

            existingScanCursor.close(); // Close the cursor

        } catch (Exception ex) {

            Log.e("Database Error", "Error al guardar la acreditación: ", ex);
        }
    }

    private long calculateTimeDifference(String timeStamp1, String timeStamp2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try {
            LocalDateTime dateTime1 = LocalDateTime.parse(timeStamp1, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(timeStamp2, formatter);

            return ChronoUnit.MILLIS.between(dateTime1, dateTime2);
        } catch (Exception e) {
            // Handle parsing exceptions, e.g., log the error and return a default value
            System.err.println("Error parsing time strings: " + e.getMessage());
            return 0;
        }
    }

    // método para inserir a la base de datos
    private void insertAccreditation(SQLiteDatabase db, Acreditaciones acreditacion) {
        ContentValues values = new ContentValues();
        values.put("lecturaQR", acreditacion.getTxQrCodeResult());
        values.put("timeStamp", acreditacion.getTimeStamp());
        db.insert("Acreditaciones", null, values);
    }

    @SuppressLint("Range")
    public List<Acreditaciones> buscarAcreditaciones(){
        List<Acreditaciones> todos = new ArrayList<>();
        SQLiteDatabase db2 = db.getReadableDatabase();
        Cursor c = db2.rawQuery("SELECT * FROM Acreditaciones WHERE idUsuario = ?",
                new String[]{String.valueOf(idUsuario)});
        try {
            if (c.moveToFirst()) {int lecturaQRIndex = c.getColumnIndex("lecturaQR"); // Get column index
                int timeStampIndex = c.getColumnIndex("timeStamp");

                do {
                    Acreditaciones a = new Acreditaciones();
                    if (lecturaQRIndex >= 0) { // Check if column exists
                        a.setTxQrCodeResult(c.getString(lecturaQRIndex));
                    }
                    if (timeStampIndex >= 0) {
                        a.setTimeStamp(c.getString(timeStampIndex));
                    }
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