package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.utils.CookieUtil;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Invalidate the session (removes loggedUser and all session attributes)
        SessionUtil.invalidateSession(request);

        // Delete the email cookie that was set during login
        CookieUtil.deleteCookie(response, "userEmail");

        // Redirect to login page with logout success message
        response.sendRedirect(request.getContextPath() + "/login?logout");
    }
}