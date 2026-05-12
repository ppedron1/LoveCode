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
import com.LoveCode.Usuario;

/**
 * DAO (Data Access Object) para la tabla Usuarios usando Procedimientos Almacenados.
 */
public class UsuarioDAO {

    // ==================== REGISTRO ====================
    public int registrar(Usuario u) throws SQLException {
        String sql = "{call sp_registrar_usuario(?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, u.getNombre());
            cs.setString(2, u.getEmail());
            cs.setString(3, u.getPassword());
            cs.setString(4, u.getCiudad());
            cs.setString(5, u.getDescripcion());
            cs.registerOutParameter(6, Types.INTEGER);

            cs.execute();
            return cs.getInt(6);
        }
    }

    // ==================== LOGIN ====================
    public Map<String, String> buscarPorEmail(String email) throws SQLException {
        String sql = "{call sp_buscar_usuario_por_email(?)}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, email);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                Map<String, String> datos = new LinkedHashMap<>();
                datos.put("id", String.valueOf(rs.getInt("id_usuario")));
                datos.put("nombre", rs.getString("nombre"));
                datos.put("password", rs.getString("password"));
                return datos;
            }
            return null;
        }
    }

    // ==================== LISTAR USUARIOS ====================
    public List<Map<String, String>> listarTodos() throws SQLException {
        String sql = "{call sp_listar_todos_usuarios()}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

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

    // ==================== LISTAR PARA DASHBOARD ====================
    public List<Map<String, String>> listarParaDashboard(int idUsuarioActual) throws SQLException {
        String sql = "{call sp_listar_usuarios_dashboard(?)}";

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
