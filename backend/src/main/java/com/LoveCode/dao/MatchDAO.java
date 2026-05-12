package com.LoveCode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.LoveCode.ConexionDB;

public class MatchDAO {

    /**
     * Comprueba si el usuario2 ya ha dado like al usuario1.
     */
    public boolean existeLikeMutuo(int idUsuario1, int idUsuario2) throws SQLException {
        String sql = "SELECT id_like FROM Likes WHERE id_usuario_da = ? AND id_usuario_recibe = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario2); // El otro usuario
            ps.setInt(2, idUsuario1); // Yo
            
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    /**
     * Comprueba si dos usuarios comparten al menos una tecnología.
     */
    public boolean compartenTecnologia(int id1, int id2) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Usuarios_Tecnologias ut1 " +
                     "JOIN Usuarios_Tecnologias ut2 ON ut1.id_tecnologia = ut2.id_tecnologia " +
                     "WHERE ut1.id_usuario = ? AND ut2.id_usuario = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id1);
            ps.setInt(2, id2);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    /**
     * Registra un match en la base de datos.
     */
    public void crearMatch(int id1, int id2) throws SQLException {
        String sql = "INSERT IGNORE INTO Matches (id_usuario1, id_usuario2) VALUES (?, ?)";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Ordenamos los IDs para evitar duplicados invertidos si la DB no lo maneja
            int menor = Math.min(id1, id2);
            int mayor = Math.max(id1, id2);
            
            ps.setInt(1, menor);
            ps.setInt(2, mayor);
            
            ps.executeUpdate();
        }
    }
}
