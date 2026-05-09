package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.CartDao;
import com.sugandha_sansaar.dao.CartDaoImpl;
import com.sugandha_sansaar.model.CartItem;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet("/user/cart")
public class CartServlet extends HttpServlet {

    private final CartDao cartDao = new CartDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        ArrayList<CartItem> cartItems =
                cartDao.fetchCartItems(loggedUser.getId());

        BigDecimal grandTotal = cartItems.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int cartCount = cartDao.getCartItemCount(loggedUser.getId());

        request.setAttribute("cartItems",  cartItems);
        request.setAttribute("grandTotal", grandTotal);
        request.setAttribute("cartCount",  cartCount);

        request.getRequestDispatcher("/WEB-INF/views/cart.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action    = request.getParameter("action");
        int    productId = Integer.parseInt(request.getParameter("productId"));
        int    userId    = loggedUser.getId();

        switch (action) {
            case "add" -> {
                int qty = 1;
                String qp = request.getParameter("quantity");
                if (qp != null && !qp.isEmpty()) qty = Integer.parseInt(qp);
                cartDao.addToCart(userId, productId, qty);
            }
            case "remove" -> {
                int cartId = cartDao.getOrCreateCartId(userId);
                cartDao.removeFromCart(cartId, productId);
            }
            case "update" -> {
                int cartId = cartDao.getOrCreateCartId(userId);
                int newQty = Integer.parseInt(request.getParameter("quantity"));
                if (newQty <= 0) cartDao.removeFromCart(cartId, productId);
                else cartDao.updateQuantity(cartId, productId, newQty);
            }
        }

        String redirect = request.getParameter("redirect");
        if ("dashboard".equals(redirect))
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        else
            response.sendRedirect(request.getContextPath() + "/user/cart");
    }
}