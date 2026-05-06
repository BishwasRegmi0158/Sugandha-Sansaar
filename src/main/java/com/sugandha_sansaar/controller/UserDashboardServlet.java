package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Serves the user dashboard page.
 * URL: /user/dashboard
 *
 * doGet: checks session, forwards to dashboard.jsp
 */
@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Block if not logged in
        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Block admin from user dashboard
        if (loggedUser.getRoleId() == 1) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp")
                .forward(request, response);
    }
}