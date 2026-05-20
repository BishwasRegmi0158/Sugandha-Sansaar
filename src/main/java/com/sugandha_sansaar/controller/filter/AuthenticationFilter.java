package com.sugandha_sansaar.controller.filter;

import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.utils.SessionUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req         = (HttpServletRequest)  request;
        HttpServletResponse res         = (HttpServletResponse) response;
        String              path        = req.getRequestURI().substring(req.getContextPath().length());
        String              contextPath = req.getContextPath();

        // Always allow static resources
        if (path.startsWith("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        // Public pages — no login needed
        boolean isPublic = path.equals("/home")
                || path.equals("/login")
                || path.equals("/register")
                || path.equals("/about")
                || path.isEmpty()
                || path.equals("/");

        User    loggedUser = (User) SessionUtil.getAttribute(req, "loggedUser");
        boolean isLoggedIn = loggedUser != null;

        // Not logged in — only allow public pages, redirect everything else to login
        if (!isLoggedIn) {
            if (isPublic) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(contextPath + "/login");
            }
            return;
        }

        // Logged in — redirect away from login/register only (home is accessible to all)
        if (path.equals("/login") || path.equals("/register")) {
            res.sendRedirect(loggedUser.getRoleId() == 1
                    ? contextPath + "/dashboard"
                    : contextPath + "/user/dashboard");
            return;
        }

        // Admin accessing /user/* → admin dashboard
        if (loggedUser.getRoleId() == 1 && path.startsWith("/user/")) {
            res.sendRedirect(contextPath + "/dashboard");
            return;
        }

        // Normal user accessing /admin/* → user dashboard
        if (loggedUser.getRoleId() == 2 && path.startsWith("/admin/")) {
            res.sendRedirect(contextPath + "/user/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }
}