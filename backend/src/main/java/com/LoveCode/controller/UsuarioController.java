package com.LoveCode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.dao.UsuarioDAO;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @GetMapping
    public ResponseEntity<?> listarUsuarios(@RequestParam(required = false) Integer idUsuario) {
        try {
            List<Map<String, String>> usuarios;
            if (idUsuario != null) {
                usuarios = usuarioDAO.listarParaDashboard(idUsuario);
            } else {
                usuarios = usuarioDAO.listarTodos();
            }
            new com.LoveCode.dao.TecnologiaDAO().rellenarTecnologias(usuarios);
            return ResponseEntity.ok(usuarios);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al obtener usuarios: " + e.getMessage()));
        }
    }
}
