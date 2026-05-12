package com.LoveCode.controller;

import java.util.List;
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
import com.LoveCode.dao.TecnologiaDAO;
import com.LoveCode.dao.UsuarioDAO;
import com.LoveCode.dao.LogDAO;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite que el navegador acepte la respuesta del servidor
public class AuthController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private TecnologiaDAO tecnologiaDAO = new TecnologiaDAO();
    private LogDAO logDAO = new LogDAO();

    @SuppressWarnings("unchecked")
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Map<String, Object> datos) {
        try {
            // Crear el objeto Usuario con los datos recibidos
            Usuario u = new Usuario();
            u.setNombre((String) datos.get("nombre"));
            u.setEmail((String) datos.get("email"));
            u.setCiudad((String) datos.get("ciudad"));
            u.setDescripcion((String) datos.get("descripcion"));

            // Encriptamos la contraseña con BCrypt antes de guardarla
            String passwordEncriptada = passwordEncoder.encode((String) datos.get("password"));
            u.setPassword(passwordEncriptada); // Se guarda el hash, NO el texto plano

            // Registrar usuario y obtener su ID generado
            int idUsuario = usuarioDAO.registrar(u);

            // Asignar tecnologías si se enviaron
            List<Integer> tecnologias = (List<Integer>) datos.get("tecnologias");
            if (tecnologias != null && !tecnologias.isEmpty()) {
                tecnologiaDAO.asignarTecnologias(idUsuario, tecnologias);
            }

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
                    logDAO.registrarLog(credenciales.get("email"), "LOGIN", "EXITO");
                    return ResponseEntity.ok(Map.of(
                        "status", "ok",
                        "nombre", datosUsuario.get("nombre"),
                        "id", datosUsuario.get("id")
                    ));
                } else {
                    logDAO.registrarLog(credenciales.get("email"), "LOGIN", "FALLO");
                    return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
                }
            } else {
                logDAO.registrarLog(credenciales.get("email"), "LOGIN", "FALLO");
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
            }
        } catch (Exception e) {
            logDAO.registrarLog(credenciales.get("email"), "LOGIN", "FALLO");
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}