package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.ProductDao;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = new ProductDao();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        User loggedUser = (User) SessionUtil.getAttribute(request, "loggedUser");
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (loggedUser.getRoleId() == 1) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        // Load products into request so JSP can render "Our Collection"
        request.setAttribute("products", productDao.getAllProducts());

        request.getRequestDispatcher("/WEB-INF/views/userDashboard.jsp")
                .forward(request, response);
    }
}