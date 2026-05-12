package com.LoveCode.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LoveCode.dao.LikeDAO;
import com.LoveCode.dao.MatchDAO;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private LikeDAO likeDAO = new LikeDAO();
    private MatchDAO matchDAO = new MatchDAO();

    /**
     * POST /api/likes
     * Body: { "idUsuarioDa": 1, "idUsuarioRecibe": 2 }
     * 
     * Lógica:
     * 1. Registra el like
     * 2. Comprueba si hay like mutuo
     * 3. Si hay like mutuo Y tecnología en común → crea match
     */
    @PostMapping
    public ResponseEntity<?> darLike(@RequestBody Map<String, Integer> datos) {
        try {
            int idDa = datos.get("idUsuarioDa");
            int idRecibe = datos.get("idUsuarioRecibe");

            // 1. Registrar el like
            likeDAO.darLike(idDa, idRecibe);

            // 2. Comprobar si existe like mutuo (el otro usuario ya le dio like antes)
            boolean likeMutuo = likeDAO.existeLikeMutuo(idDa, idRecibe);

            if (likeMutuo) {
                // 3. Comprobar si tienen tecnología en común
                boolean techComun = likeDAO.tienenTecnologiaComun(idDa, idRecibe);

                if (techComun) {
                    // ¡MATCH! Crear el match
                    matchDAO.crearMatch(idDa, idRecibe);

                    // Obtener las tecnologías en común para mostrarlas
                    List<String> techsComunes = matchDAO.obtenerTecnologiasComunes(idDa, idRecibe);

                    return ResponseEntity.ok(Map.of(
                        "status", "match",
                        "mensaje", "¡Es un Match!",
                        "tecnologiasComunes", techsComunes
                    ));
                } else {
                    return ResponseEntity.ok(Map.of(
                        "status", "like_mutuo_sin_match",
                        "mensaje", "Like mutuo, pero no comparten tecnologías en común"
                    ));
                }
            }

            return ResponseEntity.ok(Map.of(
                "status", "like",
                "mensaje", "Like registrado"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al dar like: " + e.getMessage()));
        }
    }
}
