package com.LoveCode.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.ConexionDB;
import com.LoveCode.Usuario;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario u) {
        String sql = "INSERT INTO Usuarios (nombre, email, password, ciudad) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword()); // Deberías hashear esto con BCrypt
            ps.setString(4, u.getCiudad());
            
            ps.executeUpdate();
            return ResponseEntity.ok(Map.of("mensaje", "Registro exitoso en Debian"));
            
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error en BD: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String sql = "SELECT * FROM Usuarios WHERE email = ? AND password = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, credenciales.get("email"));
            ps.setString(2, credenciales.get("password"));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return ResponseEntity.ok(Map.of("status", "ok", "user", rs.getString("nombre")));
            } else {
                return ResponseEntity.status(401).body("Credenciales incorrectas");
            }
            
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}