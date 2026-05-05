package com.sugandha_sansaar.service;



import com.sugandha_sansaar.dao.PerfumeDAO;
import com.sugandha_sansaar.model.Perfume;
import com.sugandha_sansaar.utils.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for Perfume-related business logic.
 * Acts as the bridge between AdminController and PerfumeDAO.
 * Handles validation before passing data to the DAO.
 *
 * @author Member 4 - Admin Dashboard
 */
public class PerfumeService{

    private final PerfumeDAO perfumeDAO;

    public PerfumeService() {
        this.perfumeDAO = new PerfumeDAO();
    }

    // ─── ADD ─────────────────────────────────────────────────────────────────────

    /**
     * Validates and adds a new perfume.
     *
     * @param perfume the Perfume to add
     * @return null on success, or an error message string on failure
     */
    public String addPerfume(Perfume perfume) {
        // Business-level validation
        String validationError = ValidationUtil.validatePerfumeForm(
                perfume.getName(), perfume.getBrand(), perfume.getCategory(),
                String.valueOf(perfume.getPrice()), String.valueOf(perfume.getStock()),
                String.valueOf(perfume.getVolume()), perfume.getGender()
        );
        if (validationError != null) return validationError;

        try {
            boolean success = perfumeDAO.addPerfume(perfume);
            return success ? null : "Failed to add perfume. Please try again.";
        } catch (SQLException e) {
            System.err.println("PerfumeService.addPerfume error: " + e.getMessage());
            return "Database error while adding perfume.";
        }
    }

    // ─── FETCH ALL ────────────────────────────────────────────────────────────────

    /**
     * Returns all perfumes (for admin – includes inactive).
     */
    public List<Perfume> getAllPerfumes() throws SQLException {
        return perfumeDAO.getAllPerfumes();
    }

    // ─── FETCH ONE ────────────────────────────────────────────────────────────────

    /**
     * Returns a single perfume by ID, or null if not found.
     */
    public Perfume getPerfumeById(int id) throws SQLException {
        return perfumeDAO.getPerfumeById(id);
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    /**
     * Validates and updates an existing perfume.
     *
     * @param perfume the Perfume with updated fields
     * @return null on success, or an error message string on failure
     */
    public String updatePerfume(Perfume perfume) {
        String validationError = ValidationUtil.validatePerfumeForm(
                perfume.getName(), perfume.getBrand(), perfume.getCategory(),
                String.valueOf(perfume.getPrice()), String.valueOf(perfume.getStock()),
                String.valueOf(perfume.getVolume()), perfume.getGender()
        );
        if (validationError != null) return validationError;

        try {
            boolean success = perfumeDAO.updatePerfume(perfume);
            return success ? null : "Failed to update perfume. It may no longer exist.";
        } catch (SQLException e) {
            System.err.println("PerfumeService.updatePerfume error: " + e.getMessage());
            return "Database error while updating perfume.";
        }
    }

    // ─── UPDATE STOCK ─────────────────────────────────────────────────────────────

    /**
     * Updates only the stock quantity for a given perfume.
     *
     * @param perfumeId the target perfume's ID
     * @param newStock  the new stock value (must be >= 0)
     * @return null on success, or an error message string on failure
     */
    public String updateStock(int perfumeId, int newStock) {
        if (newStock < 0) return "Stock cannot be negative.";

        try {
            boolean success = perfumeDAO.updateStock(perfumeId, newStock);
            return success ? null : "Failed to update stock.";
        } catch (SQLException e) {
            System.err.println("PerfumeService.updateStock error: " + e.getMessage());
            return "Database error while updating stock.";
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    /**
     * Deletes a perfume by ID.
     *
     * @param id the perfume's primary key
     * @return null on success, or an error message string on failure
     */
    public String deletePerfume(int id) {
        try {
            boolean success = perfumeDAO.deletePerfume(id);
            return success ? null : "Perfume not found or already deleted.";
        } catch (SQLException e) {
            System.err.println("PerfumeService.deletePerfume error: " + e.getMessage());
            return "Database error while deleting perfume.";
        }
    }

    // ─── DASHBOARD STATS ──────────────────────────────────────────────────────────

    public int getTotalPerfumes() throws SQLException  { return perfumeDAO.getTotalPerfumes(); }
    public int getOutOfStockCount() throws SQLException { return perfumeDAO.getOutOfStockCount(); }
    public int getLowStockCount() throws SQLException   { return perfumeDAO.getLowStockCount(); }
    public int getTotalBrands() throws SQLException     { return perfumeDAO.getTotalBrands(); }

    // ─── CATEGORY / BRAND HELPERS ─────────────────────────────────────────────────

    public List<String> getAllCategories() throws SQLException { return perfumeDAO.getAllCategories(); }
    public List<String> getAllBrands() throws SQLException     { return perfumeDAO.getAllBrands(); }
}

