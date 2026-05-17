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
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req         = (HttpServletRequest)  request;
        HttpServletResponse res         = (HttpServletResponse) response;

        String uri         = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path        = uri.substring(contextPath.length());

        // Always allow static resources to pass through
        if (path.startsWith("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        // Get logged in user from session
        User loggedUser = (User) SessionUtil.getAttribute(req, "loggedUser");
        boolean isLoggedIn = loggedUser != null;

        // Pages that do not require login
        boolean isAuthPage = "/login".equals(path) || "/register".equals(path);

        // If not logged in and trying to access a protected page → redirect to login
        if (!isLoggedIn && !isAuthPage) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        // If already logged in and trying to access login or register → redirect to dashboard
        if (isLoggedIn && isAuthPage) {
            if (loggedUser.getRoleId() == 1) {
                res.sendRedirect(contextPath + "/admin/dashboard");
            } else {
                res.sendRedirect(contextPath + "/products");
            }
            return;
        }

        // Admin trying to access /user pages → block and redirect to admin dashboard
        if (isLoggedIn && loggedUser.getRoleId() == 1 && path.startsWith("/user/")) {
            res.sendRedirect(contextPath + "/admin/dashboard");
            return;
        }

        // User trying to access /admin pages → block and redirect to user dashboard
        if (isLoggedIn && loggedUser.getRoleId() == 2 && path.startsWith("/admin/")) {
            res.sendRedirect(contextPath + "/products");
            return;
        }

        // All good — continue
        chain.doFilter(request, response);
    }
}