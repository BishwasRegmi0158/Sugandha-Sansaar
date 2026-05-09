package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class ProductDaoImpl implements ProductDao {

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getInt("category_id"),
                rs.getString("name"),
                rs.getString("brand"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("stock"),
                rs.getString("image_url"),
                rs.getBigDecimal("volume"),
                rs.getString("gender"),
                rs.getInt("active"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

    @Override
    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, stock, image_url, volume, gender, active) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setBigDecimal(5, p.getPrice());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getImageUrl());
            ps.setBigDecimal(8, p.getVolume());
            ps.setString(9, p.getGender());
            ps.setInt(10, p.getActive());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting product: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Product> fetchAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return list;
    }

    @Override
    public ArrayList<Product> fetchActiveProducts() {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE active = 1 ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching active products: " + e.getMessage());
        }
        return list;
    }

    @Override
    public ArrayList<Product> fetchProductsByCategory(int categoryId) {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ? AND active = 1 ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching by category: " + e.getMessage());
        }
        return list;
    }

    @Override
    public ArrayList<Product> searchProducts(String keyword) {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE active = 1 AND (name LIKE ? OR brand LIKE ? OR description LIKE ?) ORDER BY name";
        String p = "%" + keyword + "%";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p);
            ps.setString(2, p);
            ps.setString(3, p);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error searching products: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Product findProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Error finding product: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateProduct(Product p) {
        String sql = "UPDATE products SET category_id=?, name=?, brand=?, description=?, price=?, stock=?, image_url=?, volume=?, gender=?, active=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setBigDecimal(5, p.getPrice());
            ps.setInt(6, p.getStock());
            ps.setString(7, p.getImageUrl());
            ps.setBigDecimal(8, p.getVolume());
            ps.setString(9, p.getGender());
            ps.setInt(10, p.getActive());
            ps.setInt(11, p.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateActiveStatus(int id, int active) {
        String sql = "UPDATE products SET active = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, active);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating product status: " + e.getMessage());
            return false;
        }
    }
}