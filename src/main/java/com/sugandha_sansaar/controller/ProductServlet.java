package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.PerfumeDAO;
import com.sugandha_sansaar.dao.ProductDao;
import com.sugandha_sansaar.model.Category;
import com.sugandha_sansaar.model.Perfume;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Public-facing product listing page.
 * URL: /products
 * Supports: search, category filter, gender filter
 * Aligned to new SQL schema (category_id FK, ENUM gender: male/female).
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDao productDao;
    private PerfumeDAO perfumeDAO;

    @Override
    public void init() {
        productDao = new ProductDao();
        perfumeDAO = new PerfumeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String search     = trim(req.getParameter("search"));
        String categoryId = trim(req.getParameter("category"));
        String gender     = trim(req.getParameter("gender"));

        List<Perfume> products;
        try {
            if (!search.isEmpty()) {
                products = productDao.searchProducts(search);
                req.setAttribute("searchKeyword", search);
            } else if (!categoryId.isEmpty()) {
                products = productDao.getProductsByCategory(Integer.parseInt(categoryId));
                req.setAttribute("categoryFilter", categoryId);
            } else if (!gender.isEmpty()) {
                products = productDao.getProductsByGender(gender);
                req.setAttribute("genderFilter", gender);
            } else {
                products = productDao.getAllProducts();
            }

            // Load categories for the filter dropdown
            List<Category> categories = perfumeDAO.getAllCategories();
            req.setAttribute("products", products);
            req.setAttribute("categories", categories);

        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Could not load products: " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/product.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String search = trim(req.getParameter("search"));
        if (!search.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/products?search=" + search);
        } else {
            res.sendRedirect(req.getContextPath() + "/products");
        }
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
