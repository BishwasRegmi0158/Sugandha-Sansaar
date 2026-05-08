package com.sugandha_sansaar.utils;

public class productValidation {

    public static String sanitize(String value) {
        return (value == null) ? "" : value.trim();
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidPrice(String priceStr) {
        if (isEmpty(priceStr)) return false;
        try { return Double.parseDouble(priceStr.trim()) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidStock(String stockStr) {
        if (isEmpty(stockStr)) return false;
        try { return Integer.parseInt(stockStr.trim()) >= 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidVolume(String volumeStr) {
        if (isEmpty(volumeStr)) return false;
        try { return Double.parseDouble(volumeStr.trim()) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidCategoryId(String categoryIdStr) {
        if (isEmpty(categoryIdStr)) return false;
        try { return Integer.parseInt(categoryIdStr.trim()) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidGender(String gender) {
        return "male".equals(gender) || "female".equals(gender);
    }

    /**
     * Validates the Add/Edit Perfume form.
     * categoryId is now an INT foreign key (replaces the old plain-text category).
     *
     * @return null if all fields are valid, or an error message string.
     */
    public static String validatePerfumeForm(String name, String brand, String categoryId,
                                             String price, String stock,
                                             String volume, String gender) {
        if (isEmpty(name))                return "Perfume name is required.";
        if (isEmpty(brand))               return "Brand is required.";
        if (!isValidCategoryId(categoryId)) return "Please select a valid category.";
        if (!isValidGender(gender))       return "Gender must be 'male' or 'female'.";
        if (!isValidPrice(price))         return "Price must be a positive number.";
        if (!isValidStock(stock))         return "Stock must be a non-negative integer.";
        if (!isValidVolume(volume))       return "Volume must be a positive number (in ml).";
        return null;
    }
}
