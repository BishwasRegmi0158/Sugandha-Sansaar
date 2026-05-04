package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.utils.PasswordUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/generate-hash")
public class HashGeneratorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        if (password == null || password.isEmpty()) {
            out.println("<h3>Usage: /generate-hash?password=YourPassword</h3>");
            return;
        }

        String hash = PasswordUtil.getHashPassword(password); // ✅ fixed

        out.println("<h2>Password: " + password + "</h2>");
        out.println("<h2>Hash: " + hash + "</h2>");
        out.println("<hr>");
        out.println("<p>Run this SQL:</p>");
        out.println("<pre>UPDATE users SET password = '" + hash + "' WHERE email = 'bishwash@sugandha.com';</pre>");
    }
}