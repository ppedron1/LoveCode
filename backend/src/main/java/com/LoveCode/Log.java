package com.LoveCode;

import java.sql.Timestamp;

public class Log {
    private int idLog;
    private String email;
    private String accion;
    private String resultado; // 'EXITO' o 'FALLO'
    private Timestamp fecha;

    public Log() {}

    public Log(String email, String accion, String resultado) {
        this.email = email;
        this.accion = accion;
        this.resultado = resultado;
    }

    // Getters y Setters
    public int getIdLog() { return idLog; }
    public void setIdLog(int idLog) { this.idLog = idLog; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}
