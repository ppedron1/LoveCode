package com.LoveCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando mi aplicación LoveCode...\n");
        
        Connection conn = ConexionDB.conectar(); 
        
        if (conn != null) {
            Scanner teclado = new Scanner(System.in);
            
            // INSERCIÓN
            System.out.println("--- REGISTRO DE NUEVO USUARIO ---");
            
            System.out.print("Nombre completo: ");
            String nombre = teclado.nextLine();
            
            System.out.print("Email: ");
            String email = teclado.nextLine();
            
            System.out.print("Contraseña: ");
            String password = teclado.nextLine();
            
            System.out.print("Descripción: ");
            String descripcion = teclado.nextLine();
            
            System.out.print("Ciudad: ");
            String ciudad = teclado.nextLine();
            
            //inserción
            insertarUsuario(conn, nombre, email, password, descripcion, ciudad);
            
            // SECCIÓN DE CONSULTA (SELECT)
            listarUsuarios(conn);
            
            try {
                conn.close();
                teclado.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para insertar el usuario (INSERT)
     */
    public static void insertarUsuario(Connection conn, String nombre, String email, String password, String descripcion, String ciudad) {
        String sql = "INSERT INTO Usuarios (nombre, email, password, descripcion, ciudad) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, descripcion);
            pstmt.setString(5, ciudad);
            
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("\n [OK] ¡Usuario insertado con éxito!");
            }
        } catch (SQLException e) {
            System.out.println("\n [ERROR] Al insertar: " + e.getMessage());
        }
    }

    /**
     * Método para mostrar todos los usuarios (SELECT)
     */
    public static void listarUsuarios(Connection conn) {
        String sql = "SELECT id_usuario, nombre, email, ciudad FROM Usuarios";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- LISTA ACTUALIZADA DE USUARIOS ---");
            
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String ciudad = rs.getString("ciudad");
                
                System.out.println("ID: " + id + " | Nombre: " + nombre + " | Email: " + email + " | Ciudad: " + (ciudad != null ? ciudad : "N/A"));
            }
            System.out.println("----------------------------------------\n");
            
        } catch (SQLException e) {
            System.out.println(" [ERROR] Al listar usuarios: " + e.getMessage());
        }
    }
}