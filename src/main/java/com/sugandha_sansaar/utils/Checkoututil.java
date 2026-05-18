package com.sugandha_sansaar.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Validation helpers for the checkout form.
 * All methods return null on success, or an error message string on failure.
 */
public class Checkoututil {

    /** Valid Nepal province values — must match the dropdown options in checkout.jsp */
    private static final Set<String> VALID_PROVINCES = Set.of(
            "Koshi", "Madhesh", "Bagmati", "Gandaki",
            "Lumbini", "Karnali", "Sudurpashchim"
    );

    /**
     * Generates a unique order number from the DB-assigned order id.
     *
     * Called AFTER the order row is inserted so we have a real, unique id.
     * Example: SS-20260518-00042
     *
     * Replaces the old AtomicInteger approach which reset to 0 on every
     * Tomcat restart and caused Duplicate entry errors in the database.
     */
    public static String generateOrderNumber(int orderId) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("SS-%s-%05d", date, orderId);
    }

    /** Delivery full name — required, 2–150 chars */
    public static String validateDeliveryName(String v) {
        if (isBlank(v))              return "Delivery name is required.";
        if (v.trim().length() < 2)   return "Delivery name must be at least 2 characters.";
        if (v.trim().length() > 150) return "Delivery name is too long.";
        return null;
    }

    /** Nepal phone — 10 digits, starts with 97/98 (NTC/Ncell) */
    public static String validateDeliveryPhone(String v) {
        if (isBlank(v)) return "Delivery phone is required.";
        String clean = v.trim();
        if (!clean.matches("^(97|98)\\d{8}$"))
            return "Enter a valid NTC or Ncell number (98XXXXXXXX).";
        return null;
    }

    /** Street address — required */
    public static String validateStreet(String v) {
        if (isBlank(v)) return "Street address is required.";
        return null;
    }

    /** City — required */
    public static String validateCity(String v) {
        if (isBlank(v)) return "City is required.";
        return null;
    }

    /**
     * Province — must be one of Nepal's 7 provinces (from the dropdown).
     * Now validates against the fixed set instead of just non-blank,
     * so tampered form submissions are rejected server-side too.
     */
    public static String validateState(String v) {
        if (isBlank(v)) return "Province is required.";
        if (!VALID_PROVINCES.contains(v.trim()))
            return "Please select a valid Province from the list.";
        return null;
    }

    /** Nepal PIN — 5 digits */
    public static String validatePinCode(String v) {
        if (isBlank(v)) return "PIN code is required.";
        if (!v.trim().matches("^\\d{5}$")) return "PIN code must be 5 digits.";
        return null;
    }

    /** Payment method must be one of the schema ENUMs */
    public static String validatePaymentMethod(String v) {
        if (isBlank(v)) return "Please select a payment method.";
        return switch (v.trim()) {
            case "cash_on_delivery", "esewa", "khalti", "bank_transfer" -> null;
            default -> "Invalid payment method selected.";
        };
    }

    /** Collect all errors into one string. Returns empty string if all valid. */
    public static String validateAll(String name, String phone, String street,
                                     String city, String state, String pin, String method) {
        StringBuilder sb = new StringBuilder();
        append(sb, validateDeliveryName(name));
        append(sb, validateDeliveryPhone(phone));
        append(sb, validateStreet(street));
        append(sb, validateCity(city));
        append(sb, validateState(state));
        append(sb, validatePinCode(pin));
        append(sb, validatePaymentMethod(method));
        return sb.toString().trim();
    }

    private static void append(StringBuilder sb, String msg) {
        if (msg != null) sb.append(msg).append(" ");
    }

    private static boolean isBlank(String v) {
        return v == null || v.trim().isEmpty();
    }
}