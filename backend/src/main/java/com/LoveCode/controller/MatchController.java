package com.LoveCode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.dao.MatchDAO;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
public class MatchController {

    private MatchDAO matchDAO = new MatchDAO();

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> listarMatches(@PathVariable int idUsuario) {
        try {
            List<Map<String, String>> matches = matchDAO.listarMatches(idUsuario);
            new com.LoveCode.dao.TecnologiaDAO().rellenarTecnologias(matches);
            return ResponseEntity.ok(matches);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al obtener matches: " + e.getMessage()));
        }
    }
}
