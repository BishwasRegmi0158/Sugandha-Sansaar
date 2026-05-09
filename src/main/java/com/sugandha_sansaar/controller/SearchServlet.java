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
import java.util.ArrayList;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDaoImpl();
    private final CartDao    cartDao    = new CartDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String keyword  = request.getParameter("keyword");
        String category = request.getParameter("category");

        ArrayList<Product> results;

        if (keyword != null && !keyword.trim().isEmpty()) {
            results = productDao.searchProducts(keyword.trim());
        } else if (category != null && !category.trim().isEmpty()) {
            results = productDao.fetchProductsByCategory(
                    Integer.parseInt(category));
        } else {
            results = productDao.fetchActiveProducts();
        }

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser != null) {
            request.setAttribute("cartCount",
                    cartDao.getCartItemCount(loggedUser.getId()));
        }

        request.setAttribute("results", results);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/WEB-INF/views/search.jsp")
                .forward(request, response);
    }
}