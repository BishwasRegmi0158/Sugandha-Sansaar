package com.sugandha_sansaar.dao;

import com.sugandha_sansaar.model.Order;
import java.util.ArrayList;

public interface OrderDao {
    boolean insertOrder(Order order);
    ArrayList<Order> fetchAllOrders();
    ArrayList<Order> fetchOrdersByUserId(int userId);
    Order findOrderById(int id);
    boolean updateOrderStatus(int id, String status);
    boolean deleteOrder(int id);
}