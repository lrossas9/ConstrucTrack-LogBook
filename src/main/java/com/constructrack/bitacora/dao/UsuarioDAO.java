package com.constructrack.bitacora.dao;

import com.constructrack.bitacora.model.Usuario;
import com.constructrack.bitacora.util.DBConnection;
import com.constructrack.bitacora.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;

public class UsuarioDAO {
    public Usuario findByCorreoAndPassword(String correo, String rawPassword) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String stored = rs.getString("contrasena");
                if (PasswordUtil.matches(rawPassword, stored)) {
                    return map(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error validando usuario", e);
        }
    }

    public void createIfNotExists(String nombre, String correo, String rawPassword) {
        String existsSql = "SELECT id FROM usuarios WHERE correo = ?";
        String insertSql = "INSERT INTO usuarios (nombre, correo, contrasena, fecha_creacion) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement check = conn.prepareStatement(existsSql)) {
            check.setString(1, correo);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                return;
            }
            try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                insert.setString(1, nombre);
                insert.setString(2, correo);
                insert.setString(3, PasswordUtil.hash(rawPassword));
                insert.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now()));
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creando usuario admin", e);
        }
    }

    public boolean existsByCorreo(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error validando correo", e);
        }
    }

    public Usuario crear(String nombre, String correo, String rawPassword) {
        String sql = "INSERT INTO usuarios (nombre, correo, contrasena, fecha_creacion) VALUES (?,?,?,?)";
        String hashed = PasswordUtil.hash(rawPassword);
        Timestamp ahora = Timestamp.valueOf(java.time.LocalDateTime.now());
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, hashed);
            ps.setTimestamp(4, ahora);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setCorreo(correo);
            u.setContrasena(hashed);
            u.setFechaCreacion(ahora.toLocalDateTime());
            if (keys.next()) {
                u.setId(keys.getLong(1));
            }
            return u;
        } catch (SQLException e) {
            throw new RuntimeException("Error creando usuario", e);
        }
    }

    private Usuario map(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setContrasena(rs.getString("contrasena"));
        Timestamp ts = rs.getTimestamp("fecha_creacion");
        if (ts != null) {
            u.setFechaCreacion(ts.toLocalDateTime());
        }
        return u;
    }
}
