package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Perfume;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Public-facing DAO for the products table.
 * Used by ProductServlet and ProductDetailServlet (shop pages).
 * Aligned to the new SQL schema (category_id FK, DECIMAL price, ENUM gender).
 */
public class ProductDao {

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

    private static final String BASE_SELECT =
            "SELECT p.*, c.name AS category_name " +
                    "FROM products p JOIN categories c ON p.category_id = c.id ";

    /** All active products for the public shop. */
    public List<Perfume> getAllProducts() throws SQLException {
        String sql = BASE_SELECT + "WHERE p.active = 1 ORDER BY p.name ASC";
        List<Perfume> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Single active product by id. */
    public Perfume getProductById(int id) throws SQLException {
        String sql = BASE_SELECT + "WHERE p.active = 1 AND p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    /** Search active products by name, brand, or description. */
    public List<Perfume> searchProducts(String keyword) throws SQLException {
        String sql = BASE_SELECT +
                "WHERE p.active = 1 AND (p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) " +
                "ORDER BY p.name ASC";
        List<Perfume> list = new ArrayList<>();
        String like = "%" + keyword + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Filter active products by category id. */
    public List<Perfume> getProductsByCategory(int categoryId) throws SQLException {
        String sql = BASE_SELECT + "WHERE p.active = 1 AND p.category_id = ? ORDER BY p.name ASC";
        List<Perfume> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Filter active products by gender (male / female). */
    public List<Perfume> getProductsByGender(String gender) throws SQLException {
        String sql = BASE_SELECT + "WHERE p.active = 1 AND p.gender = ? ORDER BY p.name ASC";
        List<Perfume> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gender);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Related products — same category, excluding current product. */
    public List<Perfume> getRelatedProducts(int categoryId, int excludeId, int limit) throws SQLException {
        String sql = BASE_SELECT +
                "WHERE p.active = 1 AND p.category_id = ? AND p.id != ? " +
                "ORDER BY p.id DESC LIMIT ?";
        List<Perfume> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId); ps.setInt(2, excludeId); ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }
}
