package com.sugandha_sansaar.controller;



import com.sugandha_sansaar.model.Perfume;
import com.sugandha_sansaar.service.PerfumeService;
import com.sugandha_sansaar.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Admin Perfume CRUD operations.
 *
 * URL Mapping:
 *   GET  /admin/perfumes           → list all perfumes
 *   GET  /admin/perfumes?action=add    → show add form
 *   GET  /admin/perfumes?action=edit&id=X  → show edit form
 *   POST /admin/perfumes?action=add    → process add
 *   POST /admin/perfumes?action=update → process update
 *   POST /admin/perfumes?action=delete → process delete
 *   POST /admin/perfumes?action=updateStock → process stock update
 *
 * @author Member 4 - Admin Dashboard
 */
@WebServlet("/admin/perfumes")
public class PerfumeServlet extends HttpServlet {

    private PerfumeService perfumeService;

    @Override
    public void init() {
        perfumeService = new PerfumeService();
    }

    // ─── Helper: check admin session ─────────────────────────────────────────────

    private boolean isAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    // ─── GET handler ──────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) return;

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                // Show blank Add Perfume form
                loadCategoryAndBrandLists(request);
                request.getRequestDispatcher("/WEB-INF/views/admin/addPerfume.jsp")
                        .forward(request, response);

            } else if ("edit".equals(action)) {
                // Show Edit Perfume form pre-filled with existing data
                int id = parseId(request.getParameter("id"));
                if (id <= 0) {
                    response.sendRedirect(request.getContextPath() + "/admin/perfumes");
                    return;
                }
                Perfume perfume = perfumeService.getPerfumeById(id);
                if (perfume == null) {
                    request.setAttribute("errorMessage", "Perfume not found.");
                    showList(request, response);
                    return;
                }
                request.setAttribute("perfume", perfume);
                loadCategoryAndBrandLists(request);
                request.getRequestDispatcher("/WEB-INF/views/admin/editPerfume.jsp")
                        .forward(request, response);

            } else {
                // Default: list all perfumes
                showList(request, response);
            }

        } catch (SQLException e) {
            System.err.println("AdminPerfumeController.doGet error: " + e.getMessage());
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/perfumeList.jsp")
                    .forward(request, response);
        }
    }

    // ─── POST handler ─────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request, response)) return;

        String action = request.getParameter("action");

        try {
            switch (action != null ? action : "") {

                case "add":
                    handleAdd(request, response);
                    break;

                case "update":
                    handleUpdate(request, response);
                    break;

                case "delete":
                    handleDelete(request, response);
                    break;

                case "updateStock":
                    handleUpdateStock(request, response);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/admin/perfumes");
            }

        } catch (SQLException e) {
            System.err.println("AdminPerfumeController.doPost error: " + e.getMessage());
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            try {
                showList(request, response);  // showList throws SQLException
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // ─── ADD ─────────────────────────────────────────────────────────────────────

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        Perfume perfume = buildPerfumeFromRequest(request);

        String error = perfumeService.addPerfume(perfume);
        if (error != null) {
            // Return to form with error
            request.setAttribute("errorMessage", error);
            request.setAttribute("perfume", perfume); // keep user's input
            loadCategoryAndBrandLists(request);
            request.getRequestDispatcher("/WEB-INF/views/admin/addPerfume.jsp")
                    .forward(request, response);
        } else {
            // Success – redirect to list with success message
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Perfume added successfully!");
            response.sendRedirect(request.getContextPath() + "/admin/perfumes");
        }
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────────

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int id = parseId(request.getParameter("id"));
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/perfumes");
            return;
        }

        Perfume perfume = buildPerfumeFromRequest(request);
        perfume.setId(id);

        String error = perfumeService.updatePerfume(perfume);
        if (error != null) {
            request.setAttribute("errorMessage", error);
            request.setAttribute("perfume", perfume);
            loadCategoryAndBrandLists(request);
            request.getRequestDispatcher("/WEB-INF/views/admin/editPerfume.jsp")
                    .forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("successMessage", "Perfume updated successfully!");
            response.sendRedirect(request.getContextPath() + "/admin/perfumes");
        }
    }

    // ─── DELETE ──────────────────────────────────────────────────────────────────

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException, ServletException {

        int id = parseId(request.getParameter("id"));
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/admin/perfumes");
            return;
        }

        String error = perfumeService.deletePerfume(id);
        HttpSession session = request.getSession();
        if (error != null) {
            session.setAttribute("errorMessage", error);
        } else {
            session.setAttribute("successMessage", "Perfume deleted successfully.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/perfumes");
    }

    // ─── UPDATE STOCK ─────────────────────────────────────────────────────────────

    private void handleUpdateStock(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException, ServletException {

        int id = parseId(request.getParameter("id"));
        int newStock = parseId(request.getParameter("stock")); // reuse parser (checks > 0 only; stock=0 handled in service)

        // Allow stock = 0 (out of stock)
        String stockParam = request.getParameter("stock");
        int stockValue = 0;
        try {
            stockValue = Integer.parseInt(stockParam);
        } catch (NumberFormatException e) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Invalid stock value.");
            response.sendRedirect(request.getContextPath() + "/admin/perfumes");
            return;
        }

        String error = perfumeService.updateStock(id, stockValue);
        HttpSession session = request.getSession();
        if (error != null) {
            session.setAttribute("errorMessage", error);
        } else {
            session.setAttribute("successMessage", "Stock updated successfully.");
        }
        response.sendRedirect(request.getContextPath() + "/admin/perfumes");
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────────

    /**
     * Loads list page with all perfumes.
     */
    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        List<Perfume> perfumes = perfumeService.getAllPerfumes();
        request.setAttribute("perfumes", perfumes);

        // Collect and display flash messages from session
        HttpSession session = request.getSession(false);
        if (session != null) {
            String success = (String) session.getAttribute("successMessage");
            String error   = (String) session.getAttribute("errorMessage");
            if (success != null) { request.setAttribute("successMessage", success); session.removeAttribute("successMessage"); }
            if (error   != null) { request.setAttribute("errorMessage", error);     session.removeAttribute("errorMessage"); }
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/perfumeList.jsp")
                .forward(request, response);
    }

    /**
     * Builds a Perfume object from HTTP request parameters.
     */
    private Perfume buildPerfumeFromRequest(HttpServletRequest request) {
        Perfume p = new Perfume();
        p.setName(ValidationUtil.sanitize(request.getParameter("name")));
        p.setBrand(ValidationUtil.sanitize(request.getParameter("brand")));
        p.setCategory(ValidationUtil.sanitize(request.getParameter("category")));
        p.setDescription(ValidationUtil.sanitize(request.getParameter("description")));
        p.setImageUrl(ValidationUtil.sanitize(request.getParameter("imageUrl")));
        p.setGender(ValidationUtil.sanitize(request.getParameter("gender")));
        p.setActive("true".equals(request.getParameter("active")));

        try { p.setPrice(Double.parseDouble(request.getParameter("price"))); }
        catch (NumberFormatException e) { p.setPrice(0); }

        try { p.setStock(Integer.parseInt(request.getParameter("stock"))); }
        catch (NumberFormatException e) { p.setStock(0); }

        try { p.setVolume(Double.parseDouble(request.getParameter("volume"))); }
        catch (NumberFormatException e) { p.setVolume(0); }

        return p;
    }

    /**
     * Loads categories and brands into request attributes for dropdowns.
     */
    private void loadCategoryAndBrandLists(HttpServletRequest request) throws SQLException {
        request.setAttribute("categories", perfumeService.getAllCategories());
        request.setAttribute("brands",     perfumeService.getAllBrands());
    }

    /**
     * Safely parses a String to a positive int; returns -1 on failure.
     */
    private int parseId(String param) {
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException | NullPointerException e) {
            return -1;
        }
    }
}
