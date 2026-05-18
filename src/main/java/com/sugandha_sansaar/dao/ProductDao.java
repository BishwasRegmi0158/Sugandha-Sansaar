package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the `products` table.
 *
 * ALL SELECT queries LEFT JOIN `categories` so Product.categoryName
 * is always populated — no extra query needed in JSPs.
 *
 * Replaces the old PerfumeDAO (which queried a `perfumes` table that
 * does not exist in the actual SQL schema).
 *
 * Used by:
 *   ProductServlet        — public listing
 *   ProductDetailServlet  — public detail page
 *   ProductCrudServlet    — admin CRUD
 *   DashboardServlet      — admin stats
 */
public class ProductDao {

    private static final String SELECT =
            "SELECT p.id, p.category_id, c.name AS category_name, " +
                    "p.name, p.brand, p.description, p.price, p.stock, " +
                    "p.image_url, p.volume, p.gender, p.active, " +
                    "p.created_at, p.updated_at " +
                    "FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id ";

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        BigDecimal price = rs.getBigDecimal("price");
        p.setPrice(price != null ? price : BigDecimal.ZERO);
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setVolume(rs.getBigDecimal("volume"));
        p.setGender(rs.getString("gender"));
        p.setActive(rs.getBoolean("active"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    // ── Public listing ────────────────────────────────────────────────────────

    /** All active products, alphabetical. For public /products page. */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE p.active = 1 ORDER BY p.name ASC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ProductDao.getAllProducts: " + e.getMessage());
        }
        return list;
    }

    /** All products including inactive. For admin panel. */
    public List<Product> getAllProductsAdmin() {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT + "ORDER BY p.created_at DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ProductDao.getAllProductsAdmin: " + e.getMessage());
        }
        return list;
    }

    /** Single product by ID (active or inactive). Returns null if not found. */
    public Product getProductById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT + "WHERE p.id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("ProductDao.getProductById: " + e.getMessage());
        }
        return null;
    }

    /** Keyword search across name, brand, description. Active products only. */
    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE p.active = 1 AND " +
                "(p.name LIKE ? OR p.brand LIKE ? OR p.description LIKE ?) ORDER BY p.name ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like); ps.setString(2, like); ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("ProductDao.searchProducts: " + e.getMessage());
        }
        return list;
    }

    /** Filter active products by category ID. */
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     SELECT + "WHERE p.active = 1 AND p.category_id = ? ORDER BY p.name ASC")) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("ProductDao.getProductsByCategory: " + e.getMessage());
        }
        return list;
    }

    /** Filter active products by gender ('male' | 'female'). */
    public List<Product> getProductsByGender(String gender) {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     SELECT + "WHERE p.active = 1 AND p.gender = ? ORDER BY p.name ASC")) {
            ps.setString(1, gender);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("ProductDao.getProductsByGender: " + e.getMessage());
        }
        return list;
    }

    /** Up to `limit` related products — same category, different ID. */
    public List<Product> getRelatedProducts(int categoryId, int excludeId, int limit) {
        List<Product> list = new ArrayList<>();
        String sql = SELECT + "WHERE p.active = 1 AND p.category_id = ? AND p.id != ? " +
                "ORDER BY p.stock DESC LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId); ps.setInt(2, excludeId); ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("ProductDao.getRelatedProducts: " + e.getMessage());
        }
        return list;
    }

    // ── Admin CRUD ────────────────────────────────────────────────────────────

    /** Insert new product. @throws SQLException propagated for servlet error handling */
    public boolean addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products " +
                "(category_id, name, brand, description, price, stock, image_url, volume, gender, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setInt(6, product.getStock());
            ps.setString(7, product.getImageUrl());
            ps.setBigDecimal(8, product.getVolume());
            ps.setString(9, product.getGender());
            ps.setBoolean(10, product.isActive());
            return ps.executeUpdate() > 0;
        }
    }

    /** Update existing product. Product ID must be set. @throws SQLException */
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET category_id=?, name=?, brand=?, description=?, " +
                "price=?, stock=?, image_url=?, volume=?, gender=?, active=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getBrand());
            ps.setString(4, product.getDescription());
            ps.setBigDecimal(5, product.getPrice());
            ps.setInt(6, product.getStock());
            ps.setString(7, product.getImageUrl());
            ps.setBigDecimal(8, product.getVolume());
            ps.setString(9, product.getGender());
            ps.setBoolean(10, product.isActive());
            ps.setInt(11, product.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Soft-delete: sets active = 0.
     * Preferred over hard DELETE to keep order_items referential integrity.
     * @throws SQLException propagated for servlet error handling
     */
    public boolean deactivateProduct(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE products SET active = 0 WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Dashboard statistics ──────────────────────────────────────────────────

    /** Count of active products. */
    public int getTotalActiveProducts() { return count("WHERE active = 1"); }

    /** Count of active products with zero stock. */
    public int getOutOfStockCount()     { return count("WHERE active = 1 AND stock = 0"); }

    /** Count of active products with 1–5 units remaining. */
    public int getLowStockCount()       { return count("WHERE active = 1 AND stock BETWEEN 1 AND 5"); }

    /** Count of distinct brands across all active products. */
    public int getTotalBrands() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(DISTINCT brand) FROM products WHERE active = 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("ProductDao.getTotalBrands: " + e.getMessage());
        }
        return 0;
    }

    private int count(String where) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM products " + where);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("ProductDao.count: " + e.getMessage());
        }
        return 0;
    }
}
