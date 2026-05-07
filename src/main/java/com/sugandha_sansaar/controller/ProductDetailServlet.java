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
 * Handles the single product detail page.
 * URL: /product-detail?id=5
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

        // Validate id parameter
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

        // Load product
        Product product = productDao.getProductById(productId);
        if (product == null) {
            res.sendRedirect(req.getContextPath() + "/products?error=notfound");
            return;
        }

        // Load 4 related products (same family)
        List<Product> related = productDao.getRelatedProducts(
                product.getFragranceFamily(), product.getProductId(), 4);

        req.setAttribute("product", product);
        req.setAttribute("relatedProducts", related);
        req.getRequestDispatcher("/WEB-INF/views/product-detail.jsp").forward(req, res);
    }
}
