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

/**
 * Authentication and Authorization filter.
 *
 * CHANGED from original:
 *   - /products and /product-detail are now PUBLIC — no login needed.
 *     Customers can browse the catalogue without an account.
 *   - All /admin/* routes still require roleId == 1.
 *   - All /user/*  routes still require roleId == 2.
 *   - /login and /register remain open as before.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse res  = (HttpServletResponse) response;

        String uri         = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path        = uri.substring(contextPath.length());

        // 1. Always allow static resources
        if (path.startsWith("/static/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Public pages — no login required
        boolean isPublic = "/login".equals(path)
                || "/register".equals(path)
                || path.equals("/products")
                || path.startsWith("/products?")
                || path.equals("/product-detail")
                || path.startsWith("/product-detail?");

        User    loggedUser = (User) SessionUtil.getAttribute(req, "loggedUser");
        boolean isLoggedIn = loggedUser != null;

        // 3. Not logged in — allow public, block everything else
        if (!isLoggedIn) {
            if (isPublic) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(contextPath + "/login");
            }
            return;
        }

        // 4. Already logged in — don't let them back to login/register
        if ("/login".equals(path) || "/register".equals(path)) {
            res.sendRedirect(loggedUser.getRoleId() == 1
                    ? contextPath + "/admin/dashboard"
                    : contextPath + "/user/dashboard");
            return;
        }

        // 5. Admin accessing /user/* — redirect to admin dashboard
        if (loggedUser.getRoleId() == 1 && path.startsWith("/user/")) {
            res.sendRedirect(contextPath + "/admin/dashboard");
            return;
        }

        // 6. Normal user accessing /admin/* — redirect to user dashboard
        if (loggedUser.getRoleId() == 2 && path.startsWith("/admin/")) {
            res.sendRedirect(contextPath + "/user/dashboard");
            return;
        }

        // 7. All clear
        chain.doFilter(request, response);
    }
}
