package com.sugandha_sansaar.utils;

/**
 * Shared form-validation utility.
 *
 * Contains TWO method groups:
 *   1. validatePerfumeForm  — kept for backward-compatibility with PerfumeService
 *                             (queries the `perfumes` table via PerfumeDAO)
 *   2. validateProductForm  — new, for ProductCrudServlet (queries the `products` table)
 *
 * Both groups share the same private helpers (isEmpty, isValidPrice, etc.)
 * so there is NO duplicated logic.
 *
 * If your team removes PerfumeDAO and PerfumeService entirely, you can also
 * remove validatePerfumeForm — but it is safe to leave it in.
 */
public class productValidation {

    // ── Shared helpers ────────────────────────────────────────────────────────

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

    // ── Perfume form validation (legacy — kept for PerfumeService) ────────────

    /**
     * Validates admin form fields for the Perfumes feature (PerfumeDAO / PerfumeService).
     * DO NOT rename or remove — PerfumeService calls this directly.
     *
     * @return null if all valid, or a user-facing error message
     */
    public static String validatePerfumeForm(String name, String brand, String category,
                                             String price, String stock,
                                             String volume, String gender) {
        if (isEmpty(name))     return "Perfume name is required.";
        if (isEmpty(brand))    return "Brand is required.";
        if (isEmpty(category)) return "Category is required.";
        if (isEmpty(gender))   return "Gender is required.";
        if (!isValidPrice(price))   return "Price must be a positive number.";
        if (!isValidStock(stock))   return "Stock must be zero or a positive integer.";
        if (!isValidVolume(volume)) return "Volume must be a positive number (ml).";
        return null;
    }

    // ── Product form validation (new — for ProductCrudServlet) ────────────────

    /**
     * Validates admin form fields for the Products CRUD feature (ProductDao).
     *
     * Key differences from validatePerfumeForm:
     *   - categoryId is a numeric FK (int), not a free-text string
     *   - volume is optional (nullable DECIMAL in DB)
     *   - gender is optional (nullable ENUM in DB — 'male' | 'female')
     *
     * @param name        product name (required)
     * @param brand       brand (required)
     * @param categoryId  FK as string (required, must be positive integer)
     * @param price       price (required, must be > 0)
     * @param stock       stock (required, must be >= 0)
     * @param volume      volume in ml (optional — pass "" to skip)
     * @param gender      'male' | 'female' | "" (optional)
     * @return null if valid, or a user-facing error message string
     */
    public static String validateProductForm(String name, String brand, String categoryId,
                                             String price, String stock,
                                             String volume, String gender) {
        if (isEmpty(name))  return "Product name is required.";
        if (name.trim().length() > 150) return "Product name must be 150 characters or fewer.";

        if (isEmpty(brand)) return "Brand is required.";
        if (brand.trim().length() > 100) return "Brand must be 100 characters or fewer.";

        if (isEmpty(categoryId)) return "Category is required.";
        try {
            if (Integer.parseInt(categoryId.trim()) <= 0) return "Please select a valid category.";
        } catch (NumberFormatException e) { return "Please select a valid category."; }

        if (!isValidPrice(price)) return "Price must be a positive number (e.g. 1299.00).";
        if (!isValidStock(stock)) return "Stock must be zero or a positive whole number.";

        // Volume optional — validate only if provided
        if (!isEmpty(volume) && !isValidVolume(volume)) {
            return "Volume must be a positive number in ml (e.g. 50.00).";
        }

        // Gender optional — if provided must match DB ENUM
        if (!isEmpty(gender) &&
                !gender.trim().equals("male") &&
                !gender.trim().equals("female")) {
            return "Gender must be 'male' or 'female'.";
        }

        return null;
    }
}
