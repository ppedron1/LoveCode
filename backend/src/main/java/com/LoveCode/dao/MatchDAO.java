package com.LoveCode.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.LoveCode.ConexionDB;

/**
 * DAO para la gestión de Matches usando Procedimientos Almacenados.
 */
public class MatchDAO {

    public boolean existeLikeMutuo(int idUsuario1, int idUsuario2) throws SQLException {
        String sql = "{call sp_comprobar_like_mutuo(?, ?, ?)}";
        
        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setInt(1, idUsuario1);
            cs.setInt(2, idUsuario2);
            cs.registerOutParameter(3, Types.INTEGER);
            
            cs.execute();
            return cs.getInt(3) > 0;
        }
    }

    public boolean compartenTecnologia(int id1, int id2) throws SQLException {
        String sql = "{call sp_comparten_tecnologia(?, ?, ?)}";
        
        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setInt(1, id1);
            cs.setInt(2, id2);
            cs.registerOutParameter(3, Types.INTEGER);
            
            cs.execute();
            return cs.getInt(3) > 0;
        }
    }

    public void crearMatch(int id1, int id2) throws SQLException {
        String sql = "{call sp_crear_match(?, ?)}";
        
        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setInt(1, id1);
            cs.setInt(2, id2);
            cs.executeUpdate();
        }
    }

    public boolean comprobarSiHayMatch(int id1, int id2) throws SQLException {
        String sql = "{call sp_comprobar_si_hay_match(?, ?, ?)}";
        
        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setInt(1, id1);
            cs.setInt(2, id2);
            cs.registerOutParameter(3, Types.INTEGER);
            
            cs.execute();
            return cs.getInt(3) > 0;
        }
    }

    public List<Map<String, String>> listarMatches(int idUsuarioActual) throws SQLException {
        String sql = "{call sp_listar_matches(?)}";

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
