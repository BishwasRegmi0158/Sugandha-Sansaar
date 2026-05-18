package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.CategoryDao;
import com.sugandha_sansaar.dao.ProductDao;
import com.sugandha_sansaar.model.Category;
import com.sugandha_sansaar.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Public product listing page.
 *
 * URL: /products
 *
 * GET parameters (all optional):
 *   search   — keyword search across name, brand, description
 *   category — category ID filter (int)
 *   gender   — 'male' | 'female'
 *
 * Forwards to: /WEB-INF/views/product.jsp
 *
 * REPLACES old ProductServlet which used the wrong column names.
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDao  productDao;
    private CategoryDao categoryDao;

    @Override
    public void init() {
        productDao  = new ProductDao();
        categoryDao = new CategoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String search        = sanitize(req.getParameter("search"));
        String categoryParam = sanitize(req.getParameter("category"));
        String gender        = sanitize(req.getParameter("gender"));

        List<Product> products;

        if (!search.isEmpty()) {
            products = productDao.searchProducts(search);
            req.setAttribute("searchKeyword", search);

        } else if (!categoryParam.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryParam);
                products = productDao.getProductsByCategory(categoryId);
                req.setAttribute("categoryFilter", categoryId);
            } catch (NumberFormatException e) {
                products = productDao.getAllProducts();
            }

        } else if (!gender.isEmpty()) {
            products = productDao.getProductsByGender(gender);
            req.setAttribute("genderFilter", gender);

        } else {
            products = productDao.getAllProducts();
        }

        List<Category> categories = categoryDao.getAllCategories();

        req.setAttribute("products",   products);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(req, res);
    }

    /** POST search form → redirect to GET to prevent re-submit on refresh */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String search = sanitize(req.getParameter("search"));
        res.sendRedirect(req.getContextPath() + "/products" +
                (search.isEmpty() ? "" : "?search=" + search));
    }

    private String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
}