package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.model.User;



import com.sugandha_sansaar.service.PerfumeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the Admin Dashboard.
 * Handles the main dashboard landing page with statistics.
 * All admin routes require ADMIN role – checked via session.
 *
 * URL: /admin/dashboard
 *
 * @author Member 4 - Admin Dashboard
 */
@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private PerfumeService perfumeService;

    @Override
    public void init() {
        perfumeService = new PerfumeService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Role check: only ADMIN users can access
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        //  Load dashboard statistics
        try {
            request.setAttribute("totalPerfumes",  perfumeService.getTotalPerfumes());
            request.setAttribute("outOfStock",      perfumeService.getOutOfStockCount());
            request.setAttribute("lowStock",        perfumeService.getLowStockCount());
            request.setAttribute("totalBrands",     perfumeService.getTotalBrands());
            request.setAttribute("recentPerfumes",  perfumeService.getAllPerfumes());

        } catch (SQLException e) {
            System.err.println("AdminDashboardController error: " + e.getMessage());
            request.setAttribute("errorMessage", "Unable to load dashboard data.");
        }

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
                .forward(request, response);
    }
}
