package com.usk.security;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hash(String plaintext) {
        return BCrypt.hashpw(plaintext, BCrypt.gensalt(12));
    }
    public static boolean verify(String plaintext, String hashed) {
        return BCrypt.checkpw(plaintext, hashed);
    }
}