package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Perfume;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PerfumeDao interface.
 * Handles all CRUD operations against the MySQL database.
 * Used by PerfumeService (never called directly from controllers).
 *
 * @author Member 4 - Admin Dashboard
 */
public class PerfumeDaoImpl implements PerfumeDao {

    /**
     * Inserts a new perfume record into the database.
     */
    @Override
    public boolean addPerfume(Perfume perfume) throws SQLException {
        String sql = "INSERT INTO products (category_id, category, name, brand, description, price, stock, " +
                "image_url, volume, gender, active) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, perfume.getCategory());
            ps.setString(2, perfume.getName());
            ps.setString(3, perfume.getBrand());
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
     */
    @Override
    public List<Perfume> getAllPerfumes() throws SQLException {
        String sql = "SELECT * FROM products ORDER BY id DESC";
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
     */
    @Override
    public Perfume getPerfumeById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";

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
     */
    @Override
    public boolean updatePerfume(Perfume perfume) throws SQLException {
        String sql = "UPDATE products SET category=?, name=?, brand=?, description=?, " +
                "price=?, stock=?, image_url=?, volume=?, gender=?, active=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, perfume.getCategory());
            ps.setString(2, perfume.getName());
            ps.setString(3, perfume.getBrand());
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
     */
    @Override
    public boolean updateStock(int perfumeId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";

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
     */
    @Override
    public boolean deletePerfume(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

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
    @Override
    public int getTotalPerfumes() throws SQLException {
        return countQuery("SELECT COUNT(*) FROM products");
    }

    /**
     * Returns number of perfumes with stock = 0 (out of stock).
     */
    @Override
    public int getOutOfStockCount() throws SQLException {
        return countQuery("SELECT COUNT(*) FROM products WHERE stock = 0");
    }

    /**
     * Returns number of perfumes with stock between 1 and 5 (low stock alert).
     */
    @Override
    public int getLowStockCount() throws SQLException {
        return countQuery("SELECT COUNT(*) FROM products WHERE stock BETWEEN 1 AND 5");
    }

    /**
     * Returns number of distinct brands in the system.
     */
    @Override
    public int getTotalBrands() throws SQLException {
        return countQuery("SELECT COUNT(DISTINCT brand) FROM products");
    }

    /**
     * Returns a distinct list of all category names.
     */
    @Override
    public List<String> getAllCategories() throws SQLException {
        String sql = "SELECT name FROM categories ORDER BY name";
        List<String> categories = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("name"));
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
    @Override
    public List<String> getAllBrands() throws SQLException {
        String sql = "SELECT DISTINCT brand FROM products ORDER BY brand";
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