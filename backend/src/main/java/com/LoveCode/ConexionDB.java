package com.LoveCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    private static final String URL = "jdbc:mariadb://192.168.204.129:3306/lovecode_db";
    private static final String USUARIO = "administrador";
    private static final String PASSWORD = "administradorpablo"; 

    public static Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println(" [*CONECTADO*] ¡Éxito! Conectado a MariaDB desde Java.");
        } catch (SQLException e) {
            System.out.println(" [*ERROR*] Error al conectar: " + e.getMessage());
        }
        return conexion;
    }
}