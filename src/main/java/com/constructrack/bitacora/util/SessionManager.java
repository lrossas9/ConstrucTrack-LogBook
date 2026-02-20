package com.constructrack.bitacora.util;

import com.constructrack.bitacora.model.Usuario;

public class SessionManager {
    private static Usuario usuario;

    private SessionManager() {
    }

    public static void setUsuario(Usuario u) {
        usuario = u;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static boolean isAuthenticated() {
        return usuario != null;
    }

    public static void clear() {
        usuario = null;
    }
}
