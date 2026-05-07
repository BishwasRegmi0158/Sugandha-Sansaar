package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.ProductDao;
import com.sugandha_sansaar.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Handles the product listing page.
 * URL: /products
 * Supports: search, family filter, gender filter
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = new ProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String search = req.getParameter("search");
        String family = req.getParameter("family");
        String gender = req.getParameter("gender");

        // Trim nulls to empty to simplify checks
        if (search != null) search = search.trim();
        if (family != null) family = family.trim();
        if (gender != null) gender = gender.trim();

        List<Product> products;

        if (search != null && !search.isEmpty()) {
            products = productDao.searchProducts(search);
            req.setAttribute("searchKeyword", search);
        } else if (family != null && !family.isEmpty()) {
            products = productDao.getProductsByFamily(family);
            req.setAttribute("familyFilter", family);
        } else if (gender != null && !gender.isEmpty()) {
            products = productDao.getProductsByGender(gender);
            req.setAttribute("genderFilter", gender);
        } else {
            products = productDao.getAllProducts();
        }

        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, res);
    }

    // Search form posts → redirect to GET
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String search = req.getParameter("search");
        if (search != null && !search.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/products?search=" + search.trim());
        } else {
            res.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
