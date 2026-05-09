package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class OrderDaoImpl implements OrderDao {

    private Order mapRow(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("order_number"),
                rs.getString("delivery_name"),
                rs.getString("delivery_phone"),
                rs.getString("delivery_street"),
                rs.getString("delivery_city"),
                rs.getString("delivery_state"),
                rs.getString("delivery_pin_code"),
                rs.getBigDecimal("subtotal"),
                rs.getBigDecimal("shipping_fee"),
                rs.getBigDecimal("total_amount"),
                rs.getString("status"),
                rs.getTimestamp("ordered_at"),
                rs.getTimestamp("updated_at")
        );
    }

    @Override
    public boolean insertOrder(Order o) {
        String sql = "INSERT INTO orders (user_id, order_number, delivery_name, delivery_phone, delivery_street, delivery_city, delivery_state, delivery_pin_code, subtotal, shipping_fee, total_amount, status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, o.getUserId());
            ps.setString(2, o.getOrderNumber());
            ps.setString(3, o.getDeliveryName());
            ps.setString(4, o.getDeliveryPhone());
            ps.setString(5, o.getDeliveryStreet());
            ps.setString(6, o.getDeliveryCity());
            ps.setString(7, o.getDeliveryState());
            ps.setString(8, o.getDeliveryPinCode());
            ps.setBigDecimal(9, o.getSubtotal());
            ps.setBigDecimal(10, o.getShippingFee());
            ps.setBigDecimal(11, o.getTotalAmount());
            ps.setString(12, o.getStatus());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting order: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Order> fetchAllOrders() {
        ArrayList<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY ordered_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching orders: " + e.getMessage());
        }
        return list;
    }

    @Override
    public ArrayList<Order> fetchOrdersByUserId(int userId) {
        ArrayList<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY ordered_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.out.println("Error fetching orders by user: " + e.getMessage());
        }
        return list;
    }

    @Override
    public Order findOrderById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.out.println("Error finding order: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateOrderStatus(int id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteOrder(int id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting order: " + e.getMessage());
            return false;
        }
    }
}