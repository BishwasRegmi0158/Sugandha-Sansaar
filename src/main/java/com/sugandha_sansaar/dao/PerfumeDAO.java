package com.sugandha_sansaar.dao;


import com.sugandha_sansaar.model.Perfume;
import com.sugandha_sansaar.utils.DatabaseConnection;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Perfume entities.
 * Handles all CRUD operations against the MySQL database.
 * Used by PerfumeService (never called directly from controllers).
 *
 * @author Member 4 - Admin Dashboard
 */
public class PerfumeDAO {


    /**
     * Inserts a new perfume record into the database.
     *
     * @param perfume the Perfume object to insert
     * @return true if insertion was successful
     * @throws SQLException on database error
     */
    public boolean addPerfume(Perfume perfume) throws SQLException {
        String sql = "INSERT INTO perfumes (name, brand, category, description, price, stock, " +
                "image_url, volume, gender, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, perfume.getName());
            ps.setString(2, perfume.getBrand());
            ps.setString(3, perfume.getCategory());
            ps.setString(4, perfume.getDescription());
            ps.setDouble(5, perfume.getPrice());
            ps.setInt(6, perfume.getStock());
            ps.setString(7, perfume.getImageUrl());
            ps.setDouble(8, perfume.getVolume());
            ps.setString(9, perfume.getGender());
            ps.setBoolean(10, perfume.isActive());

            return ps.executeUpdate() > 0;

        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }



    /**
     * Retrieves all perfumes from the database (admin view – includes inactive).
     *
     * @return list of all Perfume objects
     * @throws SQLException on database error
     */
    public List<Perfume> getAllPerfumes() throws SQLException {
        String sql = "SELECT * FROM perfumes ORDER BY id DESC";
        List<Perfume> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }



    /**
     * Retrieves a single perfume by its ID.
     *
     * @param id the perfume's primary key
     * @return Perfume object, or null if not found
     * @throws SQLException on database error
     */
    public Perfume getPerfumeById(int id) throws SQLException {
        String sql = "SELECT * FROM perfumes WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }



    /**
     * Updates an existing perfume record in the database.
     *
     * @param perfume the Perfume object with updated fields (must have valid id)
     * @return true if update was successful
     * @throws SQLException on database error
     */
    public boolean updatePerfume(Perfume perfume) throws SQLException {
        String sql = "UPDATE perfumes SET name=?, brand=?, category=?, description=?, " +
                "price=?, stock=?, image_url=?, volume=?, gender=?, active=? WHERE id=?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, perfume.getName());
            ps.setString(2, perfume.getBrand());
            ps.setString(3, perfume.getCategory());
            ps.setString(4, perfume.getDescription());
            ps.setDouble(5, perfume.getPrice());
            ps.setInt(6, perfume.getStock());
            ps.setString(7, perfume.getImageUrl());
            ps.setDouble(8, perfume.getVolume());
            ps.setString(9, perfume.getGender());
            ps.setBoolean(10, perfume.isActive());
            ps.setInt(11, perfume.getId());

            return ps.executeUpdate() > 0;

        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }


    /**
     * Updates only the stock quantity for a given perfume.
     * Used by cart/order module integration as well as admin stock management.
     *
     * @param perfumeId the perfume's ID
     * @param newStock  the new stock quantity
     * @return true if update was successful
     * @throws SQLException on database error
     */
    public boolean updateStock(int perfumeId, int newStock) throws SQLException {
        String sql = "UPDATE perfumes SET stock = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, newStock);
            ps.setInt(2, perfumeId);
            return ps.executeUpdate() > 0;

        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }



    /**
     * Deletes a perfume record by ID.
     *
     * @param id the perfume's primary key
     * @return true if deletion was successful
     * @throws SQLException on database error
     */
    public boolean deletePerfume(int id) throws SQLException {
        String sql = "DELETE FROM perfumes WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }



    /**
     * Returns total number of perfume products in the system.
     */
    public int getTotalPerfumes() throws SQLException {
        return countQuery("SELECT COUNT(*) FROM perfumes");
    }

    /**
     * Returns number of perfumes with stock = 0 (out of stock).
     */
    public int getOutOfStockCount() throws SQLException {
        return countQuery("SELECT COUNT(*) FROM perfumes WHERE stock = 0");
    }

    /**
     * Returns number of perfumes with stock between 1 and 5 (low stock alert).
     */
    public int getLowStockCount() throws SQLException {
        return countQuery("SELECT COUNT(*) FROM perfumes WHERE stock BETWEEN 1 AND 5");
    }

    /**
     * Returns number of distinct brands in the system.
     */
    public int getTotalBrands() throws SQLException {
        return countQuery("SELECT COUNT(DISTINCT brand) FROM perfumes");
    }



    /**
     * Returns a distinct list of all category names.
     */
    public List<String> getAllCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM perfumes ORDER BY category";
        List<String> categories = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return categories;
    }

    /**
     * Returns a distinct list of all brand names.
     */
    public List<String> getAllBrands() throws SQLException {
        String sql = "SELECT DISTINCT brand FROM perfumes ORDER BY brand";
        List<String> brands = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                brands.add(rs.getString("brand"));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return brands;
    }



    /**
     * Maps a ResultSet row to a Perfume object.
     */
    private Perfume mapRow(ResultSet rs) throws SQLException {
        Perfume p = new Perfume();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setCategory(rs.getString("category"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setVolume(rs.getDouble("volume"));
        p.setGender(rs.getString("gender"));
        p.setActive(rs.getBoolean("active"));
        return p;
    }

    /**
     * Helper for COUNT queries – returns the integer result.
     */
    private int countQuery(String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }
}