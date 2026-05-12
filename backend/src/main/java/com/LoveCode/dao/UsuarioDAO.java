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
import com.LoveCode.Usuario;

/**
 * DAO (Data Access Object) para la tabla Usuarios.
 * Toda la lógica de acceso a base de datos relacionada con usuarios
 * se centraliza aquí, separándola de los controllers.
 */
public class UsuarioDAO {

    // ==================== REGISTRO ====================

    /**
     * Inserta un nuevo usuario en la base de datos.
     * @param u El usuario con los datos a insertar (la password ya debe venir encriptada).
     * @throws SQLException si hay un error en la base de datos.
     */
    public int registrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO Usuarios (nombre, email, password, ciudad, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getCiudad());
            ps.setString(5, u.getDescripcion());

            ps.executeUpdate();

            // Obtener el ID generado
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }

    // ==================== LOGIN ====================

    /**
     * Busca un usuario por su email y devuelve su nombre y hash de password.
     * @param email El email del usuario.
     * @return Un Map con "nombre" y "password" (hash), o null si no existe.
     * @throws SQLException si hay un error en la base de datos.
     */
    public Map<String, String> buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id_usuario, nombre, password FROM Usuarios WHERE email = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Map<String, String> datos = new LinkedHashMap<>();
                datos.put("id", String.valueOf(rs.getInt("id_usuario")));
                datos.put("nombre", rs.getString("nombre"));
                datos.put("password", rs.getString("password"));
                return datos;
            }
            return null; // No se encontró el usuario
        }
    }

    // ==================== LISTAR USUARIOS ====================

    /**
     * Obtiene todos los usuarios de la base de datos (sin devolver la password).
     * @return Lista de Maps con los datos de cada usuario.
     * @throws SQLException si hay un error en la base de datos.
     */
    public List<Map<String, String>> listarTodos() throws SQLException {
        String sql = "SELECT id_usuario as id, nombre, email, ciudad, descripcion FROM Usuarios";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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

    // ==================== LISTAR PARA DASHBOARD ====================

    /**
     * Obtiene los usuarios que el usuario actual aún no ha "likeado".
     * Excluye al propio usuario logueado.
     */
    public List<Map<String, String>> listarParaDashboard(int idUsuarioActual) throws SQLException {
        String sql = "SELECT id_usuario as id, nombre, email, ciudad, descripcion " +
                     "FROM Usuarios " +
                     "WHERE id_usuario != ? " +
                     "AND id_usuario NOT IN (SELECT id_usuario_recibe FROM Likes WHERE id_usuario_da = ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuarioActual);
            ps.setInt(2, idUsuarioActual);
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
