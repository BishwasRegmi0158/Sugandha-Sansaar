package com.sugandha_sansaar.controller;

import com.sugandha_sansaar.dao.CategoryDao;
import com.sugandha_sansaar.model.Category;
import com.sugandha_sansaar.model.Product;
import com.sugandha_sansaar.model.User;
import com.sugandha_sansaar.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Admin CRUD controller for the `products` table.
 *
 * URL: /admin/products
 *
 *   GET  /admin/products                     — list all products
 *   GET  /admin/products?action=add          — show add form
 *   GET  /admin/products?action=edit&id=5    — show edit form
 *   POST /admin/products?action=add          — insert product
 *   POST /admin/products?action=edit         — update product
 *   POST /admin/products?action=delete&id=5  — soft-delete product
 *
 * Admin guard: roleId == 1 (matches DashboardServlet and AuthenticationFilter).
 *
 * REPLACES PerfumeServlet (/admin/perfumes) because the SQL has
 * no `perfumes` table — only a `products` table.
 */
@WebServlet("/admin/products")
public class ProductCrudServlet extends HttpServlet {

    private ProductService productService;
    private CategoryDao    categoryDao;

    @Override
    public void init() {
        productService = new ProductService();
        categoryDao    = new CategoryDao();
    }

    // ── Admin role guard ──────────────────────────────────────────────────────

    private User requireAdmin(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) { res.sendRedirect(req.getContextPath() + "/login"); return null; }
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || user.getRoleId() != 1) {
            res.sendRedirect(req.getContextPath() + "/login"); return null;
        }
        return user;
    }

    // ── GET ───────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (requireAdmin(req, res) == null) return;
        String action = sanitize(req.getParameter("action"));

        switch (action) {
            case "add"  -> showAddForm(req, res);
            case "edit" -> showEditForm(req, res);
            default     -> showList(req, res);
        }
    }

    private void showList(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setAttribute("products",      productService.getAllProductsAdmin());
        req.setAttribute("totalProducts", productService.getTotalActiveProducts());
        req.setAttribute("outOfStock",    productService.getOutOfStockCount());
        req.setAttribute("lowStock",      productService.getLowStockCount());

        // Flash messages from session
        HttpSession session = req.getSession(false);
        if (session != null) {
            for (String key : new String[]{"successMessage", "errorMessage"}) {
                Object msg = session.getAttribute(key);
                if (msg != null) { req.setAttribute(key, msg); session.removeAttribute(key); }
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/adminProduct.jsp").forward(req, res);
    }

    private void showAddForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("categories", categoryDao.getAllCategories());
        req.getRequestDispatcher("/WEB-INF/views/addProduct.jsp").forward(req, res);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int id = parseId(req.getParameter("id"));
        if (id <= 0) { res.sendRedirect(req.getContextPath() + "/admin/products?error=invalid"); return; }

        Product product = productService.getProductById(id);
        if (product == null) { res.sendRedirect(req.getContextPath() + "/admin/products?error=notfound"); return; }

        req.setAttribute("product",    product);
        req.setAttribute("categories", categoryDao.getAllCategories());
        req.getRequestDispatcher("/WEB-INF/views/editProduct.jsp").forward(req, res);
    }

    // ── POST ──────────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        if (requireAdmin(req, res) == null) return;
        String action = sanitize(req.getParameter("action"));

        switch (action) {
            case "add"    -> handleAdd(req, res);
            case "edit"   -> handleEdit(req, res);
            case "delete" -> handleDelete(req, res);
            default       -> res.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Product product = buildFromRequest(req);
        String error = productService.addProduct(product);

        if (error != null) {
            req.setAttribute("errorMessage", error);
            req.setAttribute("product",      product);
            req.setAttribute("categories",   categoryDao.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/admin/addProduct.jsp").forward(req, res);
            return;
        }
        setFlash(req, "successMessage", "Product added successfully.");
        res.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void handleEdit(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int id = parseId(req.getParameter("id"));
        if (id <= 0) { res.sendRedirect(req.getContextPath() + "/admin/products?error=invalid"); return; }

        Product product = buildFromRequest(req);
        product.setId(id);
        String error = productService.updateProduct(product);

        if (error != null) {
            req.setAttribute("errorMessage", error);
            req.setAttribute("product",      product);
            req.setAttribute("categories",   categoryDao.getAllCategories());
            req.getRequestDispatcher("/WEB-INF/views/admin/editProduct.jsp").forward(req, res);
            return;
        }
        setFlash(req, "successMessage", "Product updated successfully.");
        res.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        int id = parseId(req.getParameter("id"));
        if (id <= 0) { res.sendRedirect(req.getContextPath() + "/admin/products?error=invalid"); return; }

        String error = productService.deactivateProduct(id);
        if (error != null) setFlash(req, "errorMessage", error);
        else               setFlash(req, "successMessage", "Product deactivated.");
        res.sendRedirect(req.getContextPath() + "/admin/products");
    }

    // ── Build Product from HTTP form ──────────────────────────────────────────

    private Product buildFromRequest(HttpServletRequest req) {
        Product p = new Product();
        String cat    = sanitize(req.getParameter("categoryId"));
        String price  = sanitize(req.getParameter("price"));
        String stock  = sanitize(req.getParameter("stock"));
        String volume = sanitize(req.getParameter("volume"));
        String gender = sanitize(req.getParameter("gender"));

        try { p.setCategoryId(Integer.parseInt(cat)); } catch (NumberFormatException ignored) {}
        p.setName(sanitize(req.getParameter("name")));
        p.setBrand(sanitize(req.getParameter("brand")));
        p.setDescription(sanitize(req.getParameter("description")));
        try { p.setPrice(new BigDecimal(price)); }
        catch (Exception ignored) {}
        try { p.setStock(Integer.parseInt(stock)); }
        catch (NumberFormatException ignored) {}
        try { if (!volume.isEmpty()) p.setVolume(new BigDecimal(volume)); }
        catch (Exception ignored) {}
        p.setGender(gender.isEmpty() ? null : gender);
        p.setImageUrl(sanitize(req.getParameter("imageUrl")));
        p.setActive("1".equals(sanitize(req.getParameter("active"))));
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String sanitize(String v) { return v == null ? "" : v.trim(); }

    private int parseId(String v) {
        try { return Integer.parseInt(sanitize(v)); }
        catch (NumberFormatException e) { return -1; }
    }

    private void setFlash(HttpServletRequest req, String key, String value) {
        req.getSession(true).setAttribute(key, value);
    }

    /** Convenience: load categories into a separate List for form re-display */
    private List<Category> categories() {
        return categoryDao.getAllCategories();
    }
}