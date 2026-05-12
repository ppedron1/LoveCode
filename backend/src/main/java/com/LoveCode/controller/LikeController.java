package com.LoveCode.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.LoveCode.dao.LikeDAO;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private LikeDAO likeDAO = new LikeDAO();

    @PostMapping
    public ResponseEntity<?> darLike(@RequestBody Map<String, Integer> request) {
        try {
            int idEmisor = request.get("idEmisor");
            int idReceptor = request.get("idReceptor");

            if (idEmisor == idReceptor) {
                return ResponseEntity.status(400).body(Map.of("error", "No puedes darte like a ti mismo"));
            }

            likeDAO.darLike(idEmisor, idReceptor);
            return ResponseEntity.ok(Map.of("mensaje", "Like registrado correctamente"));

        } catch (Exception e) {
            // Manejar violación de clave única (ya le dio like)
            if (e.getMessage().contains("Duplicate entry")) {
                return ResponseEntity.status(400).body(Map.of("error", "Ya has dado like a este usuario"));
            }
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al registrar like: " + e.getMessage()));
        }
    }
}
