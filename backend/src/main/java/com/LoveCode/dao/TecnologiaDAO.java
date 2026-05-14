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
 * DAO para la tabla Tecnologias usando Procedimientos Almacenados.
 */
public class TecnologiaDAO {

    public List<Map<String, Object>> listarTodas() throws SQLException {
        String sql = "{call sp_listar_tecnologias()}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            ResultSet rs = cs.executeQuery();
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

    public List<Map<String, Object>> obtenerPorUsuario(int idUsuario) throws SQLException {
        String sql = "{call sp_obtener_tecnologias_usuario(?)}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setInt(1, idUsuario);
            ResultSet rs = cs.executeQuery();
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

    public void asignarTecnologias(int idUsuario, List<Integer> idsTecnologias) throws SQLException {
        String sql = "{call sp_asignar_tecnologia_usuario(?, ?)}";

        try (Connection conn = ConexionDB.conectar();
             CallableStatement cs = conn.prepareCall(sql)) {

            for (int idTec : idsTecnologias) {
                cs.setInt(1, idUsuario);
                cs.setInt(2, idTec);
                cs.addBatch();
            }
            cs.executeBatch();
        }
    }

    public void rellenarTecnologias(List<Map<String, String>> usuarios) throws SQLException {
        for (Map<String, String> u : usuarios) {
            String idStr = u.get("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                List<Map<String, Object>> techs = obtenerPorUsuario(id);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < techs.size(); i++) {
                    sb.append(techs.get(i).get("nombre").toString());
                    if (i < techs.size() - 1) sb.append(", ");
                }
                u.put("tecnologias", sb.length() > 0 ? sb.toString() : "Ninguna");
            }
        }
    }
}
