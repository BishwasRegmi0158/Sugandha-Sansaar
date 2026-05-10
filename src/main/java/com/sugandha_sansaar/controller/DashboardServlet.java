package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Admin Dashboard controller.
 *
 * URL: /admin/dashboard
 *
 * CHANGED: Now uses ProductService (not PerfumeService) because the SQL
 * only has a `products` table. Stats and recent-products list are loaded
 * from the `products` table via ProductDao.
 *
 * Role check: roleId == 1 (admin). All other sessions are redirected to /login.
 */
@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ── Role guard ────────────────────────────────────────────────────────
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ── Load dashboard stats and recent products ──────────────────────────
        request.setAttribute("totalProducts",  productService.getTotalActiveProducts());
        request.setAttribute("outOfStock",     productService.getOutOfStockCount());
        request.setAttribute("lowStock",       productService.getLowStockCount());
        request.setAttribute("totalBrands",    productService.getTotalBrands());
        request.setAttribute("recentProducts", productService.getAllProductsAdmin());

        // Flash messages from redirects
        String success = (String) session.getAttribute("successMessage");
        String error   = (String) session.getAttribute("errorMessage");
        if (success != null) { request.setAttribute("successMessage", success); session.removeAttribute("successMessage"); }
        if (error   != null) { request.setAttribute("errorMessage",   error);   session.removeAttribute("errorMessage"); }

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
