package com.LoveCode.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.LoveCode.dao.LikeDAO;
import com.LoveCode.dao.MatchDAO;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private LikeDAO likeDAO = new LikeDAO();
    private MatchDAO matchDAO = new MatchDAO();

    @PostMapping
    public ResponseEntity<?> darLike(@RequestBody Map<String, Integer> request) {
        try {
            int idEmisor = request.get("idEmisor");
            int idReceptor = request.get("idReceptor");

            if (idEmisor == idReceptor) {
                return ResponseEntity.status(400).body(Map.of("error", "No puedes darte like a ti mismo"));
            }

            likeDAO.darLike(idEmisor, idReceptor);

            // --- Lógica de Match ---
            boolean hayMatch = false;
            
            // 1. ¿El otro ya me había dado like?
            if (matchDAO.existeLikeMutuo(idEmisor, idReceptor)) {
                // 2. ¿Compartimos al menos una tecnología?
                if (matchDAO.compartenTecnologia(idEmisor, idReceptor)) {
                    matchDAO.crearMatch(idEmisor, idReceptor);
                    hayMatch = true;
                }
            }

            return ResponseEntity.ok(Map.of(
                "mensaje", "Like registrado correctamente",
                "match", hayMatch
            ));

        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                return ResponseEntity.status(400).body(Map.of("error", "Ya has dado like a este usuario"));
            }
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al registrar like: " + e.getMessage()));
        }
    }
}
