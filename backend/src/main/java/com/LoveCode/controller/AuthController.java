package com.LoveCode.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.ConexionDB;
import com.LoveCode.Usuario;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite que el navegador acepte la respuesta del servidor
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario u) {
        String sql = "INSERT INTO Usuarios (nombre, email, password, ciudad, descripcion) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Encriptamos la contraseña con BCrypt antes de guardarla
            String passwordEncriptada = passwordEncoder.encode(u.getPassword());
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, passwordEncriptada); // Se guarda el hash, NO el texto plano
            ps.setString(4, u.getCiudad());
            ps.setString(5, u.getDescripcion());
            
            ps.executeUpdate();
            return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(500).body(Map.of("error", "Error en BD: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        // Ahora buscamos solo por email, ya NO comparamos la contraseña en SQL
        String sql = "SELECT nombre, password FROM Usuarios WHERE email = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, credenciales.get("email"));
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String hashGuardado = rs.getString("password");
                
                // BCrypt compara la contraseña en texto plano con el hash guardado
                if (passwordEncoder.matches(credenciales.get("password"), hashGuardado)) {
                    return ResponseEntity.ok(Map.of("status", "ok", "nombre", rs.getString("nombre")));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
                }
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}