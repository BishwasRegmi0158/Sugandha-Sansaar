package com.sugandha_sansaar.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using BCrypt.
 * BCrypt automatically handles salting — no need to store salt separately.
 * Cost factor 12 means 2^12 = 4096 hashing rounds (secure and reasonably fast).
 */
public class PasswordUtil {

    private static final int COST_FACTOR = 12;

    public static String getHashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST_FACTOR));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.out.println("Error checking password: " + e.getMessage());
            return false;
        }
    }

}