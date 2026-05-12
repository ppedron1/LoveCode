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
 * DAO para la tabla Tecnologias y la relación Usuarios_Tecnologias.
 */
public class TecnologiaDAO {

    // ==================== LISTAR TODAS ====================

    /**
     * Devuelve todas las tecnologías disponibles en el catálogo.
     */
    public List<Map<String, Object>> listarTodas() throws SQLException {
        String sql = "SELECT id_tecnologia, nombre FROM Tecnologias ORDER BY nombre";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> tecnologias = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> t = new LinkedHashMap<>();
                t.put("id", rs.getInt("id_tecnologia"));
                t.put("nombre", rs.getString("nombre"));
                tecnologias.add(t);
            }

            return tecnologias;
        }
    }

    // ==================== OBTENER POR USUARIO ====================

    /**
     * Devuelve las tecnologías asociadas a un usuario específico.
     */
    public List<Map<String, Object>> obtenerPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT t.id_tecnologia, t.nombre " +
                     "FROM Tecnologias t " +
                     "INNER JOIN Usuarios_Tecnologias ut ON t.id_tecnologia = ut.id_tecnologia " +
                     "WHERE ut.id_usuario = ? " +
                     "ORDER BY t.nombre";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            List<Map<String, Object>> tecnologias = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> t = new LinkedHashMap<>();
                t.put("id", rs.getInt("id_tecnologia"));
                t.put("nombre", rs.getString("nombre"));
                tecnologias.add(t);
            }

            return tecnologias;
        }
    }

    // ==================== ASIGNAR TECNOLOGÍAS A USUARIO ====================

    /**
     * Asocia una lista de tecnologías a un usuario (INSERT en Usuarios_Tecnologias).
     */
    public void asignarTecnologias(int idUsuario, List<Integer> idsTecnologias) throws SQLException {
        String sql = "INSERT INTO Usuarios_Tecnologias (id_usuario, id_tecnologia) VALUES (?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int idTec : idsTecnologias) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idTec);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }
}
