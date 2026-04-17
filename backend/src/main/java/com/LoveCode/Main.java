package com.LoveCode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando mi aplicación LoveCode...\n");
        
        Connection conn = ConexionDB.conectar();
        
        if (conn != null) {
            try {
    
                Statement stmt = conn.createStatement();
                String sql = "SELECT id_usuario, nombre, email FROM Usuarios";
                
                ResultSet rs = stmt.executeQuery(sql);
                
                System.out.println("\n--- 📝 LISTA DE USUARIOS EN LA BASE DE DATOS ---");
                
                while (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    
                    System.out.println("ID: " + id + " | Nombre: " + nombre + " | Email: " + email);
                }
                
                System.out.println("------------------------------------------------\n");
                
                rs.close();
                stmt.close();
                
            } catch (Exception e) {
                System.out.println("Error al hacer la consulta: " + e.getMessage());
            }
        }
    }
}