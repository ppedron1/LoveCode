package com.LoveCode.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.Usuario;
import com.LoveCode.dao.UsuarioDAO;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite que el navegador acepte la respuesta del servidor
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario u) {
        try {
            // Encriptamos la contraseña con BCrypt antes de guardarla
            String passwordEncriptada = passwordEncoder.encode(u.getPassword());
            u.setPassword(passwordEncriptada); // Se guarda el hash, NO el texto plano

            usuarioDAO.registrar(u);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));

        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(500).body(Map.of("error", "Error en BD: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            // Buscamos el usuario por email usando el DAO
            Map<String, String> datosUsuario = usuarioDAO.buscarPorEmail(credenciales.get("email"));

            if (datosUsuario != null) {
                String hashGuardado = datosUsuario.get("password");

                // BCrypt compara la contraseña en texto plano con el hash guardado
                if (passwordEncoder.matches(credenciales.get("password"), hashGuardado)) {
                    return ResponseEntity.ok(Map.of("status", "ok", "nombre", datosUsuario.get("nombre")));
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