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

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        StringBuilder errors = new StringBuilder();

        // Validation
        if (ValidationUtil.isNullOrEmpty(fullName) || fullName.trim().length() < 3) {
            errors.append("Full name must be at least 3 characters. ");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            errors.append("Invalid email format. ");
        }

        if (!ValidationUtil.isValidPassword(password)) {
            errors.append("Password must be 8+ characters with uppercase, number, and symbol. ");
        }
        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
            errors.append("Passwords do not match. ");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("error", errors.toString().trim());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Check if email or phone already exists
        if (userDao.findUserByEmail(email) != null) {
            request.setAttribute("error", "Email already registered.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }
        if (userDao.findUserByPhone(phone) != null) {
            request.setAttribute("error", "Phone number already registered.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Hash password
        String hashedPassword = PasswordUtil.getHashPassword(password);

        // Create user: role_id = 2 (normal user), is_active = 0 (pending admin approval)
        User user = new User(2, fullName, email, phone, hashedPassword, null, 0);

        boolean success = userDao.insertUser(user);

        if (!success) {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                    .forward(request, response);
            return;
        }

        // Redirect to log in with success message (optional)
        response.sendRedirect(request.getContextPath() + "/login?registered=true");
    }
}