package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.CartDao;
import com.sugandha_sansaar.dao.CartDaoImpl;
import com.sugandha_sansaar.dao.OrderDao;
import com.sugandha_sansaar.dao.OrderDaoImpl;
import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * GET /user/order        → order list
 * GET /user/order?id=N   → single order detail
 *                          (add ?placed=true to show success banner)
 */
@WebServlet("/user/order")
public class Orderservlet extends HttpServlet {

    private final OrderDao orderDao = new OrderDaoImpl();
    private final CartDao  cartDao  = new CartDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("cartCount", cartDao.getCartItemCount(user.getId()));

        String idParam = request.getParameter("id");

        if (idParam != null) {
            // ── Single order detail ──────────────────────────────────────────
            try {
                int   orderId = Integer.parseInt(idParam);
                Order order   = orderDao.getOrderById(orderId);

                // Security: make sure this order belongs to this user
                if (order == null || order.getUserId() != user.getId()) {
                    response.sendRedirect(request.getContextPath() + "/user/order");
                    return;
                }

                request.setAttribute("order",  order);
                request.setAttribute("placed", request.getParameter("placed") != null);
                request.getRequestDispatcher("/WEB-INF/views/orderDetail.jsp")
                        .forward(request, response);

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/user/order");
            }

        } else {
            // ── Order list ───────────────────────────────────────────────────
            List<Order> orders = orderDao.getOrdersByUserId(user.getId());
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp")
                    .forward(request, response);
        }
    }
}

