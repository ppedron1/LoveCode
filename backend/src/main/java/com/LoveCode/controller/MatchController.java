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

    /**
     * GET /api/matches/{idUsuario}
     * Devuelve todos los matches del usuario con datos del otro usuario
     * y las tecnologías en común.
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerMatches(@PathVariable int idUsuario) {
        try {
            List<Map<String, Object>> matches = matchDAO.obtenerMatchesPorUsuario(idUsuario);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al obtener matches: " + e.getMessage()));
        }
    }
}
