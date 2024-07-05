package com.example.acreditads2023;

import android.widget.Button;

public class Acreditaciones {
    private int idUsuario;
    private String timeStamp;
    private String TxQrCodeResult = "";

    public Acreditaciones(){

    }
    public Acreditaciones(String timeStamp, String TxQrCodeResult) {
        this.timeStamp = timeStamp;
        this.TxQrCodeResult = TxQrCodeResult;
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
