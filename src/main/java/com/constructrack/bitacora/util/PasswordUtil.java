package com.constructrack.bitacora.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String raw) {
        return BCrypt.hashpw(raw, BCrypt.gensalt());
    }

    public static boolean matches(String raw, String stored) {
        if (stored == null) {
            return false;
        }
        if (stored.startsWith("$2a") || stored.startsWith("$2b") || stored.startsWith("$2y")) {
            return BCrypt.checkpw(raw, stored);
        }
        return raw.equals(stored);
    }
}
