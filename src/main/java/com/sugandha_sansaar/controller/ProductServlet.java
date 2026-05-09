package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.CartDao;
import com.sugandha_sansaar.dao.CartDaoImpl;
import com.sugandha_sansaar.dao.ProductDao;
import com.sugandha_sansaar.dao.ProductDaoImpl;
import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();
    private final CartDao    cartDao    = new CartDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/search");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/search");
            return;
        }

        Product product = productDao.findProductById(productId);
        if (product == null || product.getActive() == 0) {
            response.sendRedirect(request.getContextPath() + "/search");
            return;
        }

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser != null) {
            request.setAttribute("cartCount",
                    cartDao.getCartItemCount(loggedUser.getId()));
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/views/product.jsp")
                .forward(request, response);
    }
}