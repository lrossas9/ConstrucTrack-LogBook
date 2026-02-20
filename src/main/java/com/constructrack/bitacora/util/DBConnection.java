package com.constructrack.bitacora.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream("/application.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                throw new IllegalStateException("No se encontro application.properties");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error cargando configuracion", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                props.getProperty("db.host"),
                props.getProperty("db.port"),
                props.getProperty("db.name"));
        return DriverManager.getConnection(url, props.getProperty("db.user"), props.getProperty("db.password"));
    }
}
