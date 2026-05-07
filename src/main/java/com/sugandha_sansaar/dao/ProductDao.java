package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all database queries for products.
 * Uses the existing DatabaseConnection from utils package.
 */
public class ProductDao {

    // Reusable SELECT columns
    private static final String SELECT =
            "SELECT product_id, product_name, brand, description, fragrance_family, " +
                    "scent_strength, size_ml, price, stock_quantity, sold_count, " +
                    "image_url, gender, is_active, created_at FROM products ";

    // Map a ResultSet row → Product object
    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setProductName(rs.getString("product_name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setFragranceFamily(rs.getString("fragrance_family"));
        p.setScentStrength(rs.getString("scent_strength"));
        p.setSizeMl(rs.getInt("size_ml"));
        BigDecimal price = rs.getBigDecimal("price");
        p.setPrice(price != null ? price : BigDecimal.ZERO);
        p.setStockQuantity(rs.getInt("stock_quantity"));
        p.setSoldCount(rs.getInt("sold_count"));
        p.setImageUrl(rs.getString("image_url"));
        p.setGender(rs.getString("gender"));
        p.setActive(rs.getBoolean("is_active"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        return p;
    }

    /** Get all active products */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE is_active = 1 ORDER BY product_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("ProductDao.getAllProducts error: " + e.getMessage());
        }
        return list;
    }

    /** Get one product by ID */
    public Product getProductById(int id) {
        String sql = SELECT + "WHERE is_active = 1 AND product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("ProductDao.getProductById error: " + e.getMessage());
        }
        return null;
    }

    /** Search by keyword (name, brand, or description) */
    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE is_active = 1 AND " +
                "(product_name LIKE ? OR brand LIKE ? OR description LIKE ?) " +
                "ORDER BY product_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String p = "%" + keyword + "%";
            ps.setString(1, p); ps.setString(2, p); ps.setString(3, p);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("ProductDao.searchProducts error: " + e.getMessage());
        }
        return list;
    }

    /** Filter by fragrance family */
    public List<Product> getProductsByFamily(String family) {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE is_active = 1 AND fragrance_family = ? ORDER BY product_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, family);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("ProductDao.getProductsByFamily error: " + e.getMessage());
        }
        return list;
    }

    /** Filter by gender */
    public List<Product> getProductsByGender(String gender) {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE is_active = 1 AND gender = ? ORDER BY product_name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gender);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("ProductDao.getProductsByGender error: " + e.getMessage());
        }
        return list;
    }

    /** Get related products (same family, different product) — used on detail page */
    public List<Product> getRelatedProducts(String family, int excludeId, int limit) {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE is_active = 1 AND fragrance_family = ? AND product_id != ? " +
                "ORDER BY sold_count DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, family);
            ps.setInt(2, excludeId);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("ProductDao.getRelatedProducts error: " + e.getMessage());
        }
        return list;
    }
}
