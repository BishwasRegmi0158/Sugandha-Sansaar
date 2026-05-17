package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.UserDao;
import com.sugandha_sansaar.dao.UserDaoImpl;
import com.sugandha_sansaar.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/users")
public class UserManagementServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDaoImpl();
    }

    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) return;


        List<User> users = new ArrayList<>(userDao.fetchAllUsers());
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/userList.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request, response)) return;

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            if ("changeRole".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                int newRoleId = Integer.parseInt(request.getParameter("roleId"));
                User u = userDao.findUserById(userId);
                u.setRoleId(newRoleId);
                boolean success = userDao.updateUser(u);
                if (success) {
                    session.setAttribute("successMessage", "User role updated successfully.");
                } else {
                    session.setAttribute("errorMessage", "Failed to update user role.");
                }
            } else if ("toggleActive".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                User u = userDao.findUserById(userId);
                int newStatus = u.getIsActive() == 1 ? 0 : 1;
                boolean success = userDao.updateActiveStatus(userId, newStatus);
                if (success) {
                    session.setAttribute("successMessage", "User status updated successfully.");
                } else {
                    session.setAttribute("errorMessage", "Failed to update user status.");
                }
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid user ID.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}