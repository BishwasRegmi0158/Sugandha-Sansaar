package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.model.OrderItem;
import com.sugandha_sansaar.model.Payment;
import com.sugandha_sansaar.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    // ── Place Order (full transaction) ───────────────────────────────────────
    /**
     * Flow (all inside one transaction):
     *  1. INSERT into orders with a temporary placeholder order_number
     *  2. Retrieve the DB auto-increment id
     *  3. Generate the real order number from that id  ← fixes the restart bug
     *  4. UPDATE order_number with the real value
     *  5. INSERT each item into order_items
     *  6. Decrement stock in products for each item (rolls back if out of stock)
     *  7. INSERT into payments
     *  8. DELETE cart_items for this user (clear cart)
     *
     * Generating the number from the DB id means it is always unique even
     * after Tomcat restarts, because AUTO_INCREMENT never resets.
     */
    @Override
    public int placeOrder(Order order, String paymentMethod) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1 ── Insert order with a temporary placeholder ─────────────────
            //      We cannot know the order number yet (we need the DB id first),
            //      so we use a short placeholder that satisfies the NOT NULL
            //      constraint and is immediately overwritten in step 4.
            String sqlOrder =
                    "INSERT INTO orders " +
                            "(user_id, order_number, delivery_name, delivery_phone, " +
                            " delivery_street, delivery_city, delivery_state, delivery_pin_code, " +
                            " subtotal, shipping_fee, total_amount, status) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?,'pending')";

            int orderId;
            try (PreparedStatement ps =
                         conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1,         order.getUserId());
                ps.setString(2,      "PENDING");          // temporary placeholder
                ps.setString(3,      order.getDeliveryName());
                ps.setString(4,      order.getDeliveryPhone());
                ps.setString(5,      order.getDeliveryStreet());
                ps.setString(6,      order.getDeliveryCity());
                ps.setString(7,      order.getDeliveryState());
                ps.setString(8,      order.getDeliveryPinCode());
                ps.setBigDecimal(9,  order.getSubtotal());
                ps.setBigDecimal(10, order.getShippingFee());
                ps.setBigDecimal(11, order.getTotalAmount());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) { conn.rollback(); return -1; }
                orderId = keys.getInt(1);
            }

            // 2 ── Generate the real order number from the DB id ─────────────
            //      AUTO_INCREMENT never resets, so this is always unique.
            //      Example: SS-20260518-00042
            String date        = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String orderNumber = String.format("SS-%s-%05d", date, orderId);

            // 3 ── Write the real order number back (same transaction) ────────
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE orders SET order_number = ? WHERE id = ?")) {
                ps.setString(1, orderNumber);
                ps.setInt(2,    orderId);
                ps.executeUpdate();
            }

            // 4 & 5 ── Insert order items + decrement stock ──────────────────
            String sqlItem =
                    "INSERT INTO order_items " +
                            "(order_id, product_id, quantity, unit_price, line_total) " +
                            "VALUES (?,?,?,?,?)";
            String sqlStock =
                    "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

            for (OrderItem item : order.getItems()) {
                try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                    ps.setInt(1,        orderId);
                    ps.setInt(2,        item.getProductId());
                    ps.setInt(3,        item.getQuantity());
                    ps.setBigDecimal(4, item.getUnitPrice());
                    ps.setBigDecimal(5, item.getLineTotal());
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                    ps.setInt(1, item.getQuantity());
                    ps.setInt(2, item.getProductId());
                    ps.setInt(3, item.getQuantity());
                    int updated = ps.executeUpdate();
                    if (updated == 0) {
                        conn.rollback();
                        return -2;   // caller shows "out of stock" message
                    }
                }
            }

            // 6 ── Insert payment record ─────────────────────────────────────
            String sqlPay =
                    "INSERT INTO payments (order_id, method, amount, status) " +
                            "VALUES (?,?,?,'pending')";
            try (PreparedStatement ps = conn.prepareStatement(sqlPay)) {
                ps.setInt(1,        orderId);
                ps.setString(2,     paymentMethod);
                ps.setBigDecimal(3, order.getTotalAmount());
                ps.executeUpdate();
            }

            // 7 ── Clear cart ────────────────────────────────────────────────
            String sqlClear =
                    "DELETE ci FROM cart_items ci " +
                            "JOIN cart c ON c.id = ci.cart_id " +
                            "WHERE c.user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlClear)) {
                ps.setInt(1, order.getUserId());
                ps.executeUpdate();
            }

            conn.commit();
            return orderId;

        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.placeOrder: " + e.getMessage());
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -1;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ── Get orders by user ───────────────────────────────────────────────────
    @Override
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql =
                "SELECT o.*, p.method AS pay_method, p.status AS pay_status " +
                        "FROM orders o " +
                        "LEFT JOIN payments p ON p.order_id = o.id " +
                        "WHERE o.user_id = ? ORDER BY o.ordered_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = mapOrder(rs);
                o.setPayment(mapPayment(rs, o.getId()));
                list.add(o);
            }
        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.getOrdersByUserId: " + e.getMessage());
        }
        return list;
    }

    // ── Get single order with items ──────────────────────────────────────────
    @Override
    public Order getOrderById(int orderId) {
        String sql =
                "SELECT o.*, p.method AS pay_method, p.status AS pay_status " +
                        "FROM orders o " +
                        "LEFT JOIN payments p ON p.order_id = o.id " +
                        "WHERE o.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order o = mapOrder(rs);
                o.setPayment(mapPayment(rs, o.getId()));
                o.setItems(getOrderItems(orderId));
                return o;
            }
        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.getOrderById: " + e.getMessage());
        }
        return null;
    }

    // ── Get order items (with product info) ──────────────────────────────────
    @Override
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql =
                "SELECT oi.*, p.name AS product_name, p.brand AS product_brand, " +
                        "       p.image_url AS product_image_url " +
                        "FROM order_items oi " +
                        "JOIN products p ON p.id = oi.product_id " +
                        "WHERE oi.order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getBigDecimal("unit_price"));
                item.setLineTotal(rs.getBigDecimal("line_total"));
                item.setProductName(rs.getString("product_name"));
                item.setProductBrand(rs.getString("product_brand"));
                item.setProductImageUrl(rs.getString("product_image_url"));
                list.add(item);
            }
        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.getOrderItems: " + e.getMessage());
        }
        return list;
    }

    // ── Admin: all orders ────────────────────────────────────────────────────
    @Override
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql =
                "SELECT o.*, p.method AS pay_method, p.status AS pay_status " +
                        "FROM orders o LEFT JOIN payments p ON p.order_id = o.id " +
                        "ORDER BY o.ordered_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapOrder(rs);
                o.setPayment(mapPayment(rs, o.getId()));
                list.add(o);
            }
        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.getAllOrders: " + e.getMessage());
        }
        return list;
    }

    // ── Admin: update status ─────────────────────────────────────────────────
    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE orders SET status = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.updateOrderStatus: " + e.getMessage());
            return false;
        }
    }

    // ── Stats ────────────────────────────────────────────────────────────────
    @Override
    public int getTotalOrders() { return countWhere(""); }

    @Override
    public int getPendingOrders() { return countWhere("WHERE status = 'pending'"); }

    private int countWhere(String where) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM orders " + where);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("OrderDaoImpl.countWhere: " + e.getMessage());
        }
        return 0;
    }

    // ── Mappers ──────────────────────────────────────────────────────────────
    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setOrderNumber(rs.getString("order_number"));
        o.setDeliveryName(rs.getString("delivery_name"));
        o.setDeliveryPhone(rs.getString("delivery_phone"));
        o.setDeliveryStreet(rs.getString("delivery_street"));
        o.setDeliveryCity(rs.getString("delivery_city"));
        o.setDeliveryState(rs.getString("delivery_state"));
        o.setDeliveryPinCode(rs.getString("delivery_pin_code"));
        o.setSubtotal(rs.getBigDecimal("subtotal"));
        o.setShippingFee(rs.getBigDecimal("shipping_fee"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setOrderedAt(rs.getTimestamp("ordered_at"));
        o.setUpdatedAt(rs.getTimestamp("updated_at"));
        return o;
    }

    /** Safe — returns null if pay_method column is null (no payment row yet). */
    private Payment mapPayment(ResultSet rs, int orderId) throws SQLException {
        String method = rs.getString("pay_method");
        if (method == null) return null;
        Payment p = new Payment();
        p.setOrderId(orderId);
        p.setMethod(method);
        p.setStatus(rs.getString("pay_status"));
        return p;
    }
}