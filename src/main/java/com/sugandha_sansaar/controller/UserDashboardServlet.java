package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.*;
import com.sugandha_sansaar.model.CartItem;
import com.sugandha_sansaar.model.Order;
import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private final OrderDao   orderDao   = new OrderDaoImpl();
    private final ProductDao productDao = new ProductDaoImpl();
    private final CartDao    cartDao    = new CartDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (loggedUser.getRoleId() != 2) {
            response.sendRedirect(request.getContextPath() + "/userdashboard");
            return;
        }

        ArrayList<Order>    orders    = orderDao.fetchOrdersByUserId(loggedUser.getId());
        ArrayList<Product>  products  = productDao.fetchActiveProducts();
        ArrayList<CartItem> cartItems = cartDao.fetchCartItems(loggedUser.getId());
        int cartCount = cartDao.getCartItemCount(loggedUser.getId());

        request.setAttribute("orders",    orders);
        request.setAttribute("products",  products);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartCount", cartCount);

        request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp")
                .forward(request, response);
    }
}