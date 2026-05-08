package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.ProductDao;
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
 * Public-facing product detail page.
 * URL: /product-detail?id=5
 * Aligned to new SQL schema — uses Perfume model, related by category_id.
 */
@WebServlet("/product-detail")
public class ProductDetailServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = new ProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            Perfume product = productDao.getProductById(productId);
            if (product == null) {
                res.sendRedirect(req.getContextPath() + "/products?error=notfound");
                return;
            }

            // 4 related perfumes from the same category
            List<Perfume> related = productDao.getRelatedProducts(
                    product.getCategoryId(), product.getId(), 4);

            req.setAttribute("product", product);
            req.setAttribute("relatedProducts", related);
            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, res);

        } catch (SQLException e) {
            req.setAttribute("errorMessage", "Could not load product: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, res);
        }
    }
}
