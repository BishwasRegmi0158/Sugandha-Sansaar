package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.UserDao;
import com.sugandha_sansaar.dao.UserDaoImpl;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.PasswordUtil;
import com.sugandha_sansaar.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String fullName        = request.getParameter("fullName");
        String email           = request.getParameter("email");
        String phone           = request.getParameter("phone");
        String password        = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        StringBuilder errors = new StringBuilder();

        // Validate full name
        if (ValidationUtil.isNullOrEmpty(fullName) || fullName.trim().length() < 3) {
            errors.append("Full name must be at least 3 characters. ");
        }

        // Validate email
        if (!ValidationUtil.isValidEmail(email)) {
            errors.append("Invalid email format. ");
        }

        // Validate phone
        if (!ValidationUtil.isValidPhone(phone)) {
            errors.append("Please enter a valid NTC or Ncell number. ");
        }

        // Validate password
        if (!ValidationUtil.isValidPassword(password)) {
            errors.append("Password must be 8+ characters with uppercase, number, and symbol. ");
        }

        // Validate confirm password
        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
            errors.append("Passwords do not match. ");
        }

        // Return errors if any
        if (!errors.isEmpty()) {
            request.setAttribute("error", errors.toString().trim());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Check duplicate email
        if (userDao.findUserByEmail(email) != null) {
            request.setAttribute("error", "Email already registered.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Check duplicate phone
        if (userDao.findUserByPhone(phone) != null) {
            request.setAttribute("error", "Phone number already registered.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Hash password
        String hashedPassword = PasswordUtil.getHashPassword(password);

        // role_id = 2 (user)
        // is_active = 1 — auto approved, no admin approval needed
        User user = new User(2, fullName, email, phone, hashedPassword, null, 1);

        boolean success = userDao.insertUser(user);

        if (!success) {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Redirect to login with success message
        response.sendRedirect(request.getContextPath() + "/login?registered=true");
    }
}