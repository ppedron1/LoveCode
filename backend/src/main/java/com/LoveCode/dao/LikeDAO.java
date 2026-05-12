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

public class LikeDAO {

    /**
     * Registra un "like" en la base de datos.
     * @param idEmisor ID del usuario que da el like.
     * @param idReceptor ID del usuario que recibe el like.
     * @throws SQLException Si hay un error en la base de datos.
     */
    public void darLike(int idEmisor, int idReceptor) throws SQLException {
        String sql = "INSERT INTO Likes (id_usuario_da, id_usuario_recibe) VALUES (?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmisor);
            ps.setInt(2, idReceptor);

            ps.executeUpdate();
        }
    }

    /**
     * Obtiene los usuarios a los que el usuario actual ha dado like.
     */
    public List<Map<String, String>> listarLikesDados(int idUsuarioActual) throws SQLException {
        String sql = "SELECT u.id_usuario as id, u.nombre, u.email, u.ciudad, u.descripcion " +
                     "FROM Usuarios u " +
                     "INNER JOIN Likes l ON u.id_usuario = l.id_usuario_recibe " +
                     "WHERE l.id_usuario_da = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioActual);
            ResultSet rs = ps.executeQuery();
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
