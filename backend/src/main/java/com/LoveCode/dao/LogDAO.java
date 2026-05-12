package com.LoveCode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.LoveCode.ConexionDB;

public class LogDAO {

    /**
     * Inserta un nuevo registro en la tabla Logs.
     * 
     * @param email El email del usuario que realiza la acción (puede ser null si es un fallo de alguien no registrado).
     * @param accion La acción realizada (ej: "LOGIN", "REGISTRO", "LIKE", "MATCH").
     * @param resultado El resultado de la acción ("EXITO" o "FALLO").
     */
    public void registrarLog(String email, String accion, String resultado) {
        String sql = "INSERT INTO Logs (email, accion, resultado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, accion);
            ps.setString(3, resultado);

            ps.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println(" [*ERROR*] No se pudo guardar el log en la BD: " + e.getMessage());
        }
    }
}
