package com.constructrack.bitacora.dao;

import com.constructrack.bitacora.model.Evidencia;
import com.constructrack.bitacora.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EvidenciaDAO {
    public void guardar(Evidencia e) {
        String sql = "INSERT INTO evidencias (id_bitacora, nombre_archivo, ruta_archivo, content_type, fecha_subida) VALUES (?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, e.getIdBitacora());
            ps.setString(2, e.getNombreArchivo());
            ps.setString(3, e.getRutaArchivo());
            ps.setString(4, e.getContentType());
            LocalDateTime now = e.getFechaSubida() != null ? e.getFechaSubida() : LocalDateTime.now();
            ps.setTimestamp(5, Timestamp.valueOf(now));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                e.setId(keys.getLong(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando evidencia", ex);
        }
    }

    public List<Evidencia> listarPorBitacora(long idBitacora) {
        String sql = "SELECT * FROM evidencias WHERE id_bitacora = ? ORDER BY fecha_subida DESC";
        List<Evidencia> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idBitacora);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(map(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error listando evidencias", ex);
        }
        return lista;
    }

    public void eliminar(long id) {
        String sql = "DELETE FROM evidencias WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error eliminando evidencia", ex);
        }
    }

    private Evidencia map(ResultSet rs) throws SQLException {
        Evidencia e = new Evidencia();
        e.setId(rs.getLong("id"));
        e.setIdBitacora(rs.getLong("id_bitacora"));
        e.setNombreArchivo(rs.getString("nombre_archivo"));
        e.setRutaArchivo(rs.getString("ruta_archivo"));
        e.setContentType(rs.getString("content_type"));
        Timestamp ts = rs.getTimestamp("fecha_subida");
        if (ts != null) {
            e.setFechaSubida(ts.toLocalDateTime());
        }
        return e;
    }
}
