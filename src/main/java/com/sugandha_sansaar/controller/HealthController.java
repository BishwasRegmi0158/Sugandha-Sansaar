package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.utils.DatabaseConnection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/health")
public class HealthController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        resp.setHeader("Cache-Control", "no-store");

        // /health          -> app only (fast, no database)
        // /health?db=true  -> app + a tiny database check
        boolean checkDb = "true".equals(req.getParameter("db"));
        if (!checkDb) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("OK");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT 1")) {
                rs.next();
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("OK (database reachable)");
        } catch (Exception e) {
            System.out.println("Health check: database problem: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            resp.getWriter().write("DATABASE DOWN");
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }
}