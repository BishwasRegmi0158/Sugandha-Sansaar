package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.OrderDao;
import com.sugandha_sansaar.dao.OrderDaoImpl;
import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Admin Order Management.
 * Allows admin to view all orders and update order status.
 *
 * URL Mapping:
 *   GET  /admin/orders              → list all orders
 *   POST /admin/orders?action=updateStatus → update order status
 *
 * @author Member 4 - Admin Dashboard
 */
@WebServlet("/admin/orders")
public class OrderManagementServlet extends HttpServlet {

    private OrderDao orderDao;

    @Override
    public void init() {
        orderDao = new OrderDaoImpl();
    }

    // ─── Helper: check admin session ─────────────────────────────────────────

    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    // ─── GET: show all orders ─────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) return;

        // Load flash messages from session
        HttpSession session = request.getSession(false);
        if (session != null) {
            String success = (String) session.getAttribute("successMessage");
            String error   = (String) session.getAttribute("errorMessage");
            if (success != null) { request.setAttribute("successMessage", success); session.removeAttribute("successMessage"); }
            if (error   != null) { request.setAttribute("errorMessage",   error);   session.removeAttribute("errorMessage"); }
        }

        List<Order> orders = orderDao.getAllOrders();
        request.setAttribute("orders", orders);
        request.setAttribute("totalOrders",   orderDao.getTotalOrders());
        request.setAttribute("pendingOrders", orderDao.getPendingOrders());

        request.getRequestDispatcher("/WEB-INF/views/orderList.jsp")
                .forward(request, response);
    }

    // ─── POST: update order status ────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) return;

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("updateStatus".equals(action)) {
            try {
                int orderId    = Integer.parseInt(request.getParameter("orderId"));
                String newStatus = request.getParameter("status");

                // Validate status value
                if (!isValidStatus(newStatus)) {
                    session.setAttribute("errorMessage", "Invalid status value.");
                    response.sendRedirect(request.getContextPath() + "/admin/orders");
                    return;
                }

                boolean success = orderDao.updateOrderStatus(orderId, newStatus);
                if (success) {
                    session.setAttribute("successMessage",
                            "Order #" + orderId + " status updated to " + newStatus + ".");
                } else {
                    session.setAttribute("errorMessage", "Failed to update order status.");
                }

            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid order ID.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }

    /**
     * Only allow valid status values.
     */
    private boolean isValidStatus(String status) {
        if (status == null) return false;
        switch (status) {
            case "pending":
            case "approved":
            case "processing":
            case "shipped":
            case "delivered":
            case "cancelled":
                return true;
            default:
                return false;
        }
    }
}