package com.LoveCode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
