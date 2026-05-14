package com.LoveCode.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.LoveCode.ConexionDB;

/**
 * DAO para la tabla Likes usando Procedimientos Almacenados.
 */
public class LikeDAO {

    public void darLike(int idEmisor, int idReceptor) throws SQLException {
        String sql = "{call sp_dar_like(?, ?)}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idEmisor);
            cs.setInt(2, idReceptor);
            cs.executeUpdate();
        }
    }

    public void quitarLike(int idEmisor, int idReceptor) throws SQLException {
        // Ejecutamos la consulta directa por si no existe el procedimiento sp_quitar_like
        String sql = "DELETE FROM Likes WHERE id_emisor = ? AND id_receptor = ?";

        try (Connection conn = ConexionDB.conectar();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmisor);
            ps.setInt(2, idReceptor);
            ps.executeUpdate();
        }
    }

    public List<Map<String, String>> listarLikesDados(int idUsuarioActual) throws SQLException {
        String sql = "{call sp_listar_likes_dados(?)}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuarioActual);
            ResultSet rs = cs.executeQuery();
            List<Map<String, String>> usuarios = new ArrayList<>();

            while (rs.next()) {
                Map<String, String> u = new LinkedHashMap<>();
                u.put("id", rs.getString("id"));
                u.put("nombre", rs.getString("nombre"));
                u.put("email", rs.getString("email"));
                u.put("ciudad", rs.getString("ciudad"));
                u.put("descripcion", rs.getString("descripcion"));
                usuarios.add(u);
            }
            return usuarios;
        }
    }
}
