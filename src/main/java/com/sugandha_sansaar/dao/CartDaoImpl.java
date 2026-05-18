package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.CartItem;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class CartDaoImpl implements CartDao {

    @Override
    public int getOrCreateCartId(int userId) {
        String select = "SELECT id FROM cart WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Error getting cart: " + e.getMessage());
        }

        String insert = "INSERT INTO cart (user_id) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error creating cart: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public ArrayList<CartItem> fetchCartItems(int userId) {
        ArrayList<CartItem> list = new ArrayList<>();
        String sql =
                "SELECT ci.id, ci.cart_id, ci.product_id, ci.quantity, " +
                        "       ci.unit_price, ci.created_at, ci.updated_at, " +
                        "       p.name AS product_name, p.brand AS product_brand, " +
                        "       p.image_url AS product_image_url, p.stock AS product_stock " +
                        "FROM cart_items ci " +
                        "JOIN cart c ON c.id = ci.cart_id " +
                        "JOIN products p ON p.id = ci.product_id " +
                        "WHERE c.user_id = ? " +
                        "ORDER BY ci.created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setCreatedAt(rs.getTimestamp("created_at"));
                item.setUpdatedAt(rs.getTimestamp("updated_at"));
                item.setProductName(rs.getString("product_name"));
                item.setProductBrand(rs.getString("product_brand"));
                item.setProductImageUrl(rs.getString("product_image_url"));
                item.setProductStock(rs.getInt("product_stock"));
                list.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching cart items: " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean addToCart(int userId, int productId, int quantity) {
        int cartId = getOrCreateCartId(userId);
        if (cartId == -1) return false;

        // Check if already in cart
        String check = "SELECT id, quantity FROM cart_items WHERE cart_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int newQty = rs.getInt("quantity") + quantity;
                String upd = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND product_id = ?";
                try (Connection c2 = DatabaseConnection.getConnection();
                     PreparedStatement ps2 = c2.prepareStatement(upd)) {
                    ps2.setInt(1, newQty);
                    ps2.setInt(2, cartId);
                    ps2.setInt(3, productId);
                    ps2.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error checking cart item: " + e.getMessage());
            return false;
        }

        // Get price and insert
        String priceQ = "SELECT price FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(priceQ)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return false;
            BigDecimal price = rs.getBigDecimal("price");

            String ins = "INSERT INTO cart_items (cart_id, product_id, quantity, unit_price) VALUES (?,?,?,?)";
            try (Connection c2 = DatabaseConnection.getConnection();
                 PreparedStatement ps2 = c2.prepareStatement(ins)) {
                ps2.setInt(1, cartId);
                ps2.setInt(2, productId);
                ps2.setInt(3, quantity);
                ps2.setBigDecimal(4, price);
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding to cart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean removeFromCart(int cartId, int productId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error removing from cart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateQuantity(int cartId, int productId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating quantity: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean clearCart(int userId) {
        String sql = "DELETE ci FROM cart_items ci " +
                "JOIN cart c ON c.id = ci.cart_id WHERE c.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getCartItemCount(int userId) {
        String sql = "SELECT COALESCE(SUM(ci.quantity), 0) " +
                "FROM cart_items ci JOIN cart c ON c.id = ci.cart_id " +
                "WHERE c.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error getting cart count: " + e.getMessage());
        }
        return 0;
    }
}