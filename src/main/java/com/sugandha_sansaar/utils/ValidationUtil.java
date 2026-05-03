package com.sugandha_sansaar.utils;

public class ValidationUtil {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
    }

    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;

        // Remove spaces and dashes
        String cleaned = phone.trim().replaceAll("[\\s\\-]", "");

        // With country code: +977XXXXXXXXXX or 977XXXXXXXXXX
        if (cleaned.startsWith("+977") || cleaned.startsWith("977")) {
            String local = cleaned.startsWith("+977")
                    ? cleaned.substring(4)
                    : cleaned.substring(3);
            return isValidNepaliLocal(local);
        }

        // Without country code
        return isValidNepaliLocal(cleaned);
    }

    private static boolean isValidNepaliLocal(String number) {

        // Must be exactly 10 digits
        if (!number.matches("^[0-9]{10}$")) return false;

        // NTC Mobile: 984, 985, 986, 974, 975
        if (number.matches("^9(8[456]|7[45])[0-9]{7}$")) return true;

        // Ncell Mobile: 980, 981, 982, 972
        if (number.matches("^9(8[012]|72)[0-9]{7}$")) return true;

        return false;
    }

    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) return false;
        return password.matches(
                "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$"
        );
    }

    public static boolean doPasswordsMatch(String password,
                                           String confirmPassword) {
        if (isNullOrEmpty(password) || isNullOrEmpty(confirmPassword)) return false;
        return password.equals(confirmPassword);
    }

    public static boolean isValidFullName(String fullName) {
        if (isNullOrEmpty(fullName)) return false;
        return fullName.matches("^[A-Za-z ]{3,150}$");
    }
}
