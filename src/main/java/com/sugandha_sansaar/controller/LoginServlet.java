package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.UserDao;
import com.sugandha_sansaar.dao.UserDaoImpl;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.CookieUtil;
import com.sugandha_sansaar.utils.PasswordUtil;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // If already logged in redirect to appropriate dashboard
        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser != null) {
            if (loggedUser.getRoleId() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/dashboard");
            }
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // Find user by email
        User user = userDao.findUserByEmail(email);

        if (user == null) {
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Check if account is active
        if (user.getIsActive() != 1) {
            request.setAttribute("error", "Your account is pending admin approval.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Verify password using BCrypt
        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            request.setAttribute("error", "Invalid email or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        // Store user in session
        SessionUtil.setAttribute(request, "loggedUser", user);

        // Store email in cookie for 24 hours
        CookieUtil.addCookie(response, "userEmail", user.getEmail(), 24 * 60 * 60);

        // Redirect based on role_id: 1 = admin, 2 = normal user
        if (user.getRoleId() == 1) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/user/dashboard");
        }
    }
}