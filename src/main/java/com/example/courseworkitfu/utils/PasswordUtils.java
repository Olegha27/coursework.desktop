package com.example.courseworkitfu.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean isBcryptHash(String value) {
        return value != null && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }

    public static boolean checkPassword(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null) {
            return false;
        }

        try {
            if (isBcryptHash(storedPassword)) {
                return BCrypt.checkpw(plainPassword, storedPassword);
            }
            return plainPassword.equals(storedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
