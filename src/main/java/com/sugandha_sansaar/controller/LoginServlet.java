package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.UserDao;
import com.sugandha_sansaar.dao.UserDaoImpl;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling user login.
 * URL pattern: /login
 *
 * doGet:  displays the login JSP page.
 * doPost: processes login form submission, validates credentials,
 *         creates session, and redirects based on user role.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // If user is already logged in, redirect to appropriate dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedUser") != null) {
            User user = (User) session.getAttribute("loggedUser");
            if (user.getRoleId() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/dashboard");
            }
            return;
        }
        // Otherwise show login page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember"); // optional "remember me"

        // Validate input presence
        if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Find user by email
        User user = userDao.findUserByEmail(email.trim());
        if (user == null) {
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Check if account is active (is_active = 1)
        if (user.getIsActive() != 1) {
            request.setAttribute("error", "Your account is pending admin approval.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Verify password using BCrypt
        boolean passwordMatches = PasswordUtil.checkPassword(password, user.getPassword());
        if (!passwordMatches) {
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Login successful: create session
        HttpSession session = request.getSession(true); // create new session
        session.setAttribute("loggedUser", user);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes timeout

        // Optional: handle "remember me" with cookie (not implemented here)
        if (remember != null && remember.equals("true")) {
            // Future: set persistent cookie with session token (see sessions table)
        }

        // Redirect based on role_id: 1 = admin, 2 = normal user
        String redirectUrl;
        if (user.getRoleId() == 1) {
            redirectUrl = request.getContextPath() + "/admin/dashboard";
        } else {
            redirectUrl = request.getContextPath() + "/user/dashboard";
        }
        response.sendRedirect(redirectUrl);
    }
}