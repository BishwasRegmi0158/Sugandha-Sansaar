package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Category;
import com.sugandha_sansaar.model.Perfume;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the 'products' table (aligned to new SQL schema).
 * Joins categories table to fetch categoryName for display.
 */
public class PerfumeDAO {

    // ── INSERT ──────────────────────────────────────────────────────────────────

    public boolean addPerfume(Perfume p) throws SQLException {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, " +
                "stock, image_url, volume, gender, active) VALUES (?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1,     p.getCategoryId());
            ps.setString(2,  p.getName());
            ps.setString(3,  p.getBrand());
            ps.setString(4,  p.getDescription());
            ps.setDouble(5,  p.getPrice());
            ps.setInt(6,     p.getStock());
            ps.setString(7,  p.getImageUrl());
            ps.setDouble(8,  p.getVolume());
            ps.setString(9,  p.getGender());
            ps.setBoolean(10,p.isActive());
            return ps.executeUpdate() > 0;
        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ── SELECT ALL (admin – includes inactive) ──────────────────────────────────

    public List<Perfume> getAllPerfumes() throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p JOIN categories c ON p.category_id = c.id " +
                "ORDER BY p.id DESC";
        List<Perfume> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ── SELECT ACTIVE (public shop listing) ─────────────────────────────────────

    public List<Perfume> getActivePerfumes() throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p JOIN categories c ON p.category_id = c.id " +
                "WHERE p.active = 1 ORDER BY p.id DESC";
        List<Perfume> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ── SELECT BY ID ─────────────────────────────────────────────────────────────

    public Perfume getPerfumeById(int id) throws SQLException {
        String sql = "SELECT p.*, c.name AS category_name " +
                "FROM products p JOIN categories c ON p.category_id = c.id " +
                "WHERE p.id = ?";
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────────

    public boolean updatePerfume(Perfume p) throws SQLException {
        String sql = "UPDATE products SET category_id=?, name=?, brand=?, description=?, " +
                "price=?, stock=?, image_url=?, volume=?, gender=?, active=? WHERE id=?";
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1,     p.getCategoryId());
            ps.setString(2,  p.getName());
            ps.setString(3,  p.getBrand());
            ps.setString(4,  p.getDescription());
            ps.setDouble(5,  p.getPrice());
            ps.setInt(6,     p.getStock());
            ps.setString(7,  p.getImageUrl());
            ps.setDouble(8,  p.getVolume());
            ps.setString(9,  p.getGender());
            ps.setBoolean(10,p.isActive());
            ps.setInt(11,    p.getId());
            return ps.executeUpdate() > 0;
        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ── UPDATE STOCK ──────────────────────────────────────────────────────────────

    public boolean updateStock(int productId, int newStock) throws SQLException {
        String sql = "UPDATE products SET stock = ? WHERE id = ?";
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────────

    public boolean deletePerfume(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection conn = null; PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // ── DASHBOARD STATS ───────────────────────────────────────────────────────────

    public int getTotalPerfumes()   throws SQLException { return countQuery("SELECT COUNT(*) FROM products"); }
    public int getOutOfStockCount() throws SQLException { return countQuery("SELECT COUNT(*) FROM products WHERE stock = 0"); }
    public int getLowStockCount()   throws SQLException { return countQuery("SELECT COUNT(*) FROM products WHERE stock BETWEEN 1 AND 5"); }
    public int getTotalBrands()     throws SQLException { return countQuery("SELECT COUNT(DISTINCT brand) FROM products"); }

    // ── CATEGORIES LIST (for dropdowns) ──────────────────────────────────────────

    public List<Category> getAllCategories() throws SQLException {
        String sql = "SELECT id, name, description FROM categories ORDER BY name";
        List<Category> list = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
            }
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    // ── BRANDS LIST (distinct, for dropdowns) ────────────────────────────────────

    public List<String> getAllBrands() throws SQLException {
        String sql = "SELECT DISTINCT brand FROM products ORDER BY brand";
        List<String> brands = new ArrayList<>();
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            while (rs.next()) brands.add(rs.getString("brand"));
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return brands;
    }

    // ── PRIVATE HELPERS ───────────────────────────────────────────────────────────

    private Perfume mapRow(ResultSet rs) throws SQLException {
        Perfume p = new Perfume();
        p.setId(rs.getInt("id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setVolume(rs.getDouble("volume"));
        p.setGender(rs.getString("gender"));
        p.setActive(rs.getBoolean("active"));
        return p;
    }

    private int countQuery(String sql) throws SQLException {
        Connection conn = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(sql);
            rs   = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }
}
