package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.ProductDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = new ProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Load 3 featured products for the highlighted section
        req.setAttribute("featuredProducts",
                productDao.getAllProducts()
                        .stream()
                        .limit(3)
                        .collect(Collectors.toList()));
        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, res);
    }
}