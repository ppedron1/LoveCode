package com.LoveCode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.LoveCode.ConexionDB;

/**
 * DAO para la tabla Likes.
 * Gestiona la lógica de dar likes y comprobar likes mutuos.
 */
public class LikeDAO {

    // ==================== DAR LIKE ====================

    /**
     * Registra un like de un usuario hacia otro.
     * @param idUsuarioDa El usuario que da el like.
     * @param idUsuarioRecibe El usuario que recibe el like.
     */
    public void darLike(int idUsuarioDa, int idUsuarioRecibe) throws SQLException {
        String sql = "INSERT IGNORE INTO Likes (id_usuario_da, id_usuario_recibe) VALUES (?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioDa);
            ps.setInt(2, idUsuarioRecibe);
            ps.executeUpdate();
        }
    }

    // ==================== COMPROBAR LIKE MUTUO ====================

    /**
     * Comprueba si existe un like del usuario2 hacia el usuario1.
     * (Se llama después de que usuario1 haya dado like a usuario2)
     */
    public boolean existeLikeMutuo(int idUsuario1, int idUsuario2) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Likes WHERE id_usuario_da = ? AND id_usuario_recibe = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Comprobamos si usuario2 ya le dio like a usuario1
            ps.setInt(1, idUsuario2);
            ps.setInt(2, idUsuario1);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    // ==================== TECNOLOGÍAS EN COMÚN ====================

    /**
     * Comprueba si dos usuarios comparten al menos una tecnología.
     */
    public boolean tienenTecnologiaComun(int idUsuario1, int idUsuario2) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Usuarios_Tecnologias ut1 " +
                     "INNER JOIN Usuarios_Tecnologias ut2 ON ut1.id_tecnologia = ut2.id_tecnologia " +
                     "WHERE ut1.id_usuario = ? AND ut2.id_usuario = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario1);
            ps.setInt(2, idUsuario2);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }
}
