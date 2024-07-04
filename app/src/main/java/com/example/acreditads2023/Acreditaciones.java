package com.example.acreditads2023;

import android.widget.Button;

public class Acreditaciones {
    private int idInstalacion;
    private int idUsuario;
    private String timeStamp;
    private String TxQrCodeResult = "";

    public Acreditaciones(){

    }
    public Acreditaciones(int idUsuario, String timeStamp, String lecturaQR) {
        this.idInstalacion = idInstalacion;
        this.idUsuario = idUsuario;
        this.timeStamp = timeStamp;
        this.TxQrCodeResult = TxQrCodeResult;
    }

    public int getIdInstalacion() {
        return idInstalacion;
    }

    public void setIdInstalacion(int idInstalacion) {
        this.idInstalacion = idInstalacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTxQrCodeResult() {
        return TxQrCodeResult;
    }

    public void setTxQrCodeResult(String TxQrCodeResult) {
        this.TxQrCodeResult = TxQrCodeResult;
    }
}
