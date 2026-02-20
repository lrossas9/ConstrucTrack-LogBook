package com.constructrack.bitacora.dao;

import com.constructrack.bitacora.model.Proyecto;
import com.constructrack.bitacora.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAO {
    public List<Proyecto> listarPorUsuario(long idUsuario) {
        String sql = "SELECT * FROM proyectos WHERE id_usuario = ? ORDER BY fecha_creacion DESC";
        List<Proyecto> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando proyectos", e);
        }
        return lista;
    }

    public void guardar(Proyecto p) {
        String sql = "INSERT INTO proyectos (id_usuario, nombre, ubicacion, constructora, interventoria, contrato, fecha_inicio, fecha_fin, estado, fecha_creacion) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, p.getIdUsuario());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getUbicacion());
            ps.setString(4, p.getConstructora());
            ps.setString(5, p.getInterventoria());
            ps.setString(6, p.getContrato());
            ps.setDate(7, p.getFechaInicio() != null ? Date.valueOf(p.getFechaInicio()) : null);
            ps.setDate(8, p.getFechaFin() != null ? Date.valueOf(p.getFechaFin()) : null);
            ps.setString(9, p.getEstado());
            ps.setTimestamp(10, Timestamp.valueOf(java.time.LocalDateTime.now()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                p.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando proyecto", e);
        }
    }

    public void actualizar(Proyecto p) {
        String sql = "UPDATE proyectos SET nombre=?, ubicacion=?, constructora=?, interventoria=?, contrato=?, fecha_inicio=?, fecha_fin=?, estado=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getUbicacion());
            ps.setString(3, p.getConstructora());
            ps.setString(4, p.getInterventoria());
            ps.setString(5, p.getContrato());
            ps.setDate(6, p.getFechaInicio() != null ? Date.valueOf(p.getFechaInicio()) : null);
            ps.setDate(7, p.getFechaFin() != null ? Date.valueOf(p.getFechaFin()) : null);
            ps.setString(8, p.getEstado());
            ps.setLong(9, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando proyecto", e);
        }
    }

    public void eliminar(long id) {
        String sql = "DELETE FROM proyectos WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando proyecto", e);
        }
    }

    private Proyecto map(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto();
        p.setId(rs.getLong("id"));
        p.setIdUsuario(rs.getLong("id_usuario"));
        p.setNombre(rs.getString("nombre"));
        p.setUbicacion(rs.getString("ubicacion"));
        p.setConstructora(rs.getString("constructora"));
        p.setInterventoria(rs.getString("interventoria"));
        p.setContrato(rs.getString("contrato"));
        Date fi = rs.getDate("fecha_inicio");
        if (fi != null) {
            p.setFechaInicio(fi.toLocalDate());
        }
        Date ff = rs.getDate("fecha_fin");
        if (ff != null) {
            p.setFechaFin(ff.toLocalDate());
        }
        p.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            p.setFechaCreacion(ts.toLocalDateTime());
        }
        return p;
    }
}
