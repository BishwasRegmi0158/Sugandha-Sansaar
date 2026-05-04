<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard – Essence Perfume</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>

<%-- ── Sidebar Navigation ──────────────────────────────────────────────────── --%>
<div class="admin-wrapper">
    <aside class="sidebar">
        <div class="sidebar-logo">
            <span class="logo-icon">Admin</span>
            <span class="logo-text">Essence Admin</span>
        </div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link active">
                📊 Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/perfumes" class="nav-link">
                🧴 Manage Perfumes
            </a>
            <a href="${pageContext.request.contextPath}/admin/perfumes?action=add" class="nav-link">
                ➕ Add Perfume
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">
                🚪 Logout
            </a>
        </nav>
    </aside>

    <%-- ── Main Content ──────────────────────────────────────────────────────── --%>
    <main class="main-content">
        <header class="page-header">
            <h1>Admin Dashboard</h1>
            <p>Welcome back, <strong>${sessionScope.username}</strong></p>
        </header>

        <%-- Error message --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <%-- ── Stat Cards ─────────────────────────────────────────────────────── --%>
        <section class="stats-grid">
            <div class="stat-card stat-blue">
                <div class="stat-icon">🧴</div>
                <div class="stat-info">
                    <h3>${totalPerfumes}</h3>
                    <p>Total Perfumes</p>
                </div>
            </div>
            <div class="stat-card stat-green">
                <div class="stat-icon">🏷️</div>
                <div class="stat-info">
                    <h3>${totalBrands}</h3>
                    <p>Brands</p>
                </div>
            </div>
            <div class="stat-card stat-yellow">
                <div class="stat-icon">⚠️</div>
                <div class="stat-info">
                    <h3>${lowStock}</h3>
                    <p>Low Stock (≤5)</p>
                </div>
            </div>
            <div class="stat-card stat-red">
                <div class="stat-icon">❌</div>
                <div class="stat-info">
                    <h3>${outOfStock}</h3>
                    <p>Out of Stock</p>
                </div>
            </div>
        </section>

        <%-- ── Recent Products Table ──────────────────────────────────────────── --%>
        <section class="recent-section">
            <div class="section-header">
                <h2>Recent Perfumes</h2>
                <a href="${pageContext.request.contextPath}/admin/perfumes" class="btn-secondary">View All</a>
            </div>

            <table class="admin-table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Brand</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty recentPerfumes}">
                        <tr><td colspan="7" class="text-center">No perfumes found.</td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${recentPerfumes}" begin="0" end="4">
                            <tr>
                                <td>${p.id}</td>
                                <td>${p.name}</td>
                                <td>${p.brand}</td>
                                <td>${p.category}</td>
                                <td>$<fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.stock == 0}">
                                            <span class="badge badge-red">${p.stock}</span>
                                        </c:when>
                                        <c:when test="${p.stock <= 5}">
                                            <span class="badge badge-yellow">${p.stock}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-green">${p.stock}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.active}">
                                            <span class="badge badge-green">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-red">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </section>
    </main>
</div>

</body>
</html>