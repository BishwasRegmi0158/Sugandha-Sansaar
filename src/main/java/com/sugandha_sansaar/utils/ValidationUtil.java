package com.sugandha_sansaar.utils;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isAlphanumericStartingWithLetter(String value) {
        return value != null && value.matches("^[a-zA-Z][a-zA-Z0-9]*$");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    public static boolean doPasswordsMatch(String password, String retypePassword) {
        return password != null && password.equals(retypePassword);
    }

    public static String sanitize(String value) {
        return (value == null) ? "" : value.trim();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidPrice(String priceStr) {
        if (isEmpty(priceStr)) return false;
        try {
            double price = Double.parseDouble(priceStr.trim());
            return price > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidStock(String stockStr) {
        if (isEmpty(stockStr)) return false;
        try {
            int stock = Integer.parseInt(stockStr.trim());
            return stock >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidVolume(String volumeStr) {
        if (isEmpty(volumeStr)) return false;
        try {
            double volume = Double.parseDouble(volumeStr.trim());
            return volume > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String validatePerfumeForm(String name, String brand, String category,
                                             String price, String stock,
                                             String volume, String gender) {
        if (isEmpty(name))
            return "Perfume name is required.";
        if (isEmpty(brand))
            return "Brand is required.";
        if (isEmpty(category))
            return "Category is required.";
        if (isEmpty(gender))
            return "Gender classification is required.";
        if (!isValidPrice(price))
            return "Price must be a positive number.";
        if (!isValidStock(stock))
            return "Stock must be a non-negative integer.";
        if (!isValidVolume(volume))
            return "Volume must be a positive number (in ml).";
        return null; // All valid
    }
}
