package com.LoveCode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.dao.TecnologiaDAO;

@RestController
@RequestMapping("/api/tecnologias")
@CrossOrigin(origins = "*")
public class TecnologiaController {

    private TecnologiaDAO tecnologiaDAO = new TecnologiaDAO();

    @GetMapping
    public ResponseEntity<?> listarTecnologias() {
        try {
            List<Map<String, Object>> tecnologias = tecnologiaDAO.listarTodas();
            return ResponseEntity.ok(tecnologias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al obtener tecnologías: " + e.getMessage()));
        }
    }
}
