package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.model.OrderItem;

import java.util.List;

public interface OrderDao {

    /**
     * Insert a new order + its items + payment in one transaction.
     * Generates the order number internally from the DB auto-increment id
     * so it is always unique across server restarts.
     * Returns the generated order ID, or -1 on failure, or -2 if out of stock.
     */
    int placeOrder(Order order, String paymentMethod);

    /** All orders for one user, newest first. */
    List<Order> getOrdersByUserId(int userId);

    /** Single order by ID. Includes items and payment. */
    Order getOrderById(int orderId);

    /** Order items for a given order, with product name/brand/image. */
    List<OrderItem> getOrderItems(int orderId);

    /** All orders — admin use. */
    List<Order> getAllOrders();

    /** Update order status — admin use. */
    boolean updateOrderStatus(int orderId, String status);

    // ── Dashboard stats ──
    int getTotalOrders();
    int getPendingOrders();
}