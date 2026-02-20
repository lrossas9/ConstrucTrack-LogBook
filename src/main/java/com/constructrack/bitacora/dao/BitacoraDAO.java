package com.constructrack.bitacora.dao;

import com.constructrack.bitacora.model.Bitacora;
import com.constructrack.bitacora.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {
    public List<Bitacora> listarPorProyecto(long idProyecto) {
        String sql = "SELECT * FROM bitacoras WHERE id_proyecto = ? ORDER BY fecha DESC";
        List<Bitacora> lista = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idProyecto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando bitacoras", e);
        }
        return lista;
    }

    public void guardar(Bitacora b) {
        String sql = "INSERT INTO bitacoras (id_proyecto, fecha, clima, jornada, actividades, personal, equipos, materiales, porcentaje_avance, observaciones, firma, ruta_imagen, fecha_registro) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, b.getIdProyecto());
            ps.setDate(2, b.getFecha() != null ? Date.valueOf(b.getFecha()) : null);
            ps.setString(3, b.getClima());
            ps.setString(4, b.getJornada());
            ps.setString(5, b.getActividades());
            ps.setString(6, b.getPersonal());
            ps.setString(7, b.getEquipos());
            ps.setString(8, b.getMateriales());
            ps.setInt(9, b.getPorcentajeAvance() != null ? b.getPorcentajeAvance() : 0);
            ps.setString(10, b.getObservaciones());
            ps.setString(11, b.getFirma());
            ps.setString(12, b.getRutaImagen());
            LocalDateTime now = b.getFechaRegistro() != null ? b.getFechaRegistro() : LocalDateTime.now();
            ps.setTimestamp(13, Timestamp.valueOf(now));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                b.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando bitacora", e);
        }
    }

    public void actualizar(Bitacora b) {
        String sql = "UPDATE bitacoras SET fecha=?, clima=?, jornada=?, actividades=?, personal=?, equipos=?, materiales=?, porcentaje_avance=?, observaciones=?, firma=?, ruta_imagen=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, b.getFecha() != null ? Date.valueOf(b.getFecha()) : null);
            ps.setString(2, b.getClima());
            ps.setString(3, b.getJornada());
            ps.setString(4, b.getActividades());
            ps.setString(5, b.getPersonal());
            ps.setString(6, b.getEquipos());
            ps.setString(7, b.getMateriales());
            ps.setInt(8, b.getPorcentajeAvance() != null ? b.getPorcentajeAvance() : 0);
            ps.setString(9, b.getObservaciones());
            ps.setString(10, b.getFirma());
            ps.setString(11, b.getRutaImagen());
            ps.setLong(12, b.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando bitacora", e);
        }
    }

    public void eliminar(long id) {
        String sql = "DELETE FROM bitacoras WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando bitacora", e);
        }
    }

    private Bitacora map(ResultSet rs) throws SQLException {
        Bitacora b = new Bitacora();
        b.setId(rs.getLong("id"));
        b.setIdProyecto(rs.getLong("id_proyecto"));
        Date f = rs.getDate("fecha");
        if (f != null) {
            b.setFecha(f.toLocalDate());
        }
        b.setClima(rs.getString("clima"));
        b.setJornada(rs.getString("jornada"));
        b.setActividades(rs.getString("actividades"));
        b.setPersonal(rs.getString("personal"));
        b.setEquipos(rs.getString("equipos"));
        b.setMateriales(rs.getString("materiales"));
        b.setPorcentajeAvance(rs.getInt("porcentaje_avance"));
        b.setObservaciones(rs.getString("observaciones"));
        b.setFirma(rs.getString("firma"));
        b.setRutaImagen(rs.getString("ruta_imagen"));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) {
            b.setFechaRegistro(ts.toLocalDateTime());
        }
        return b;
    }
}
