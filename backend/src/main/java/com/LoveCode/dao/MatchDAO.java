package com.LoveCode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.LoveCode.ConexionDB;

/**
 * DAO para la tabla Matches.
 */
public class MatchDAO {

    // ==================== CREAR MATCH ====================

    /**
     * Crea un match entre dos usuarios.
     * Siempre guarda el ID menor como id_usuario1 para evitar duplicados.
     */
    public void crearMatch(int idUsuario1, int idUsuario2) throws SQLException {
        // Ordenamos los IDs para consistencia en la tabla
        int menor = Math.min(idUsuario1, idUsuario2);
        int mayor = Math.max(idUsuario1, idUsuario2);

        String sql = "INSERT IGNORE INTO Matches (id_usuario1, id_usuario2) VALUES (?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, menor);
            ps.setInt(2, mayor);
            ps.executeUpdate();
        }
    }

    // ==================== OBTENER MATCHES POR USUARIO ====================

    /**
     * Obtiene todos los matches de un usuario, devolviendo los datos del otro usuario
     * y las tecnologías en común.
     */
    public List<Map<String, Object>> obtenerMatchesPorUsuario(int idUsuario) throws SQLException {
        // Buscamos matches donde el usuario sea usuario1 o usuario2
        String sql = "SELECT m.id_match, m.fecha_match, " +
                     "  CASE WHEN m.id_usuario1 = ? THEN u2.id_usuario ELSE u1.id_usuario END AS id_otro, " +
                     "  CASE WHEN m.id_usuario1 = ? THEN u2.nombre ELSE u1.nombre END AS nombre_otro, " +
                     "  CASE WHEN m.id_usuario1 = ? THEN u2.ciudad ELSE u1.ciudad END AS ciudad_otro, " +
                     "  CASE WHEN m.id_usuario1 = ? THEN u2.descripcion ELSE u1.descripcion END AS descripcion_otro " +
                     "FROM Matches m " +
                     "INNER JOIN Usuarios u1 ON m.id_usuario1 = u1.id_usuario " +
                     "INNER JOIN Usuarios u2 ON m.id_usuario2 = u2.id_usuario " +
                     "WHERE m.id_usuario1 = ? OR m.id_usuario2 = ? " +
                     "ORDER BY m.fecha_match DESC";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            ps.setInt(3, idUsuario);
            ps.setInt(4, idUsuario);
            ps.setInt(5, idUsuario);
            ps.setInt(6, idUsuario);

            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> matches = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> match = new LinkedHashMap<>();
                match.put("idMatch", rs.getInt("id_match"));
                match.put("fechaMatch", rs.getString("fecha_match"));

                int idOtro = rs.getInt("id_otro");
                match.put("idOtroUsuario", idOtro);
                match.put("nombreOtro", rs.getString("nombre_otro"));
                match.put("ciudadOtro", rs.getString("ciudad_otro"));
                match.put("descripcionOtro", rs.getString("descripcion_otro"));

                // Obtener tecnologías en común
                match.put("tecnologiasComunes", obtenerTecnologiasComunes(idUsuario, idOtro));

                matches.add(match);
            }

            return matches;
        }
    }

    // ==================== TECNOLOGÍAS EN COMÚN ====================

    /**
     * Devuelve los nombres de las tecnologías que comparten dos usuarios.
     */
    public List<String> obtenerTecnologiasComunes(int idUsuario1, int idUsuario2) throws SQLException {
        String sql = "SELECT t.nombre FROM Tecnologias t " +
                     "INNER JOIN Usuarios_Tecnologias ut1 ON t.id_tecnologia = ut1.id_tecnologia " +
                     "INNER JOIN Usuarios_Tecnologias ut2 ON t.id_tecnologia = ut2.id_tecnologia " +
                     "WHERE ut1.id_usuario = ? AND ut2.id_usuario = ? " +
                     "ORDER BY t.nombre";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario1);
            ps.setInt(2, idUsuario2);
            ResultSet rs = ps.executeQuery();

            List<String> tecnologias = new ArrayList<>();
            while (rs.next()) {
                tecnologias.add(rs.getString("nombre"));
            }
            return tecnologias;
        }
    }
}
