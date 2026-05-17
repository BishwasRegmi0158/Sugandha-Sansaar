<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Admin Dashboard — Sugandha Sansaar</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<div class="admin-wrap">
    <aside class="admin-sidebar">
        <div class="admin-sidebar-logo"><span>Sugandha Admin</span></div>
        <nav class="admin-sidebar-nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="admin-nav-link active">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link">📦 Products</a>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="admin-nav-link">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/logout" class="admin-nav-link logout">🚪 Logout</a>
        </nav>
    </aside>
    <main class="admin-main">
        <div class="admin-page-header">
            <div><h1>Admin Dashboard</h1><p>Welcome back, <strong>${sessionScope.loggedUser.fullName}</strong></p></div>
        </div>
        <c:if test="${not empty successMessage}"><div class="admin-alert ok">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="admin-alert err">${errorMessage}</div></c:if>

        <div class="stats-grid">
            <div class="stat-card stat-blue"><div class="stat-icon">📦</div><div><div class="stat-num">${totalProducts}</div><div class="stat-lbl">Active Products</div></div></div>
            <div class="stat-card stat-green"><div class="stat-icon">🏷️</div><div><div class="stat-num">${totalBrands}</div><div class="stat-lbl">Brands</div></div></div>
            <div class="stat-card stat-yellow"><div class="stat-icon">⚠️</div><div><div class="stat-num">${lowStock}</div><div class="stat-lbl">Low Stock</div></div></div>
            <div class="stat-card stat-red"><div class="stat-icon">❌</div><div><div class="stat-num">${outOfStock}</div><div class="stat-lbl">Out of Stock</div></div></div>
        </div>

        <div style="display:flex;align-items:center;justify-content:space-between;margin-bottom:14px;">
            <span style="font-family:'Cormorant Garamond',serif;font-size:1.3rem;color:#f5ede0;">Recent Products</span>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn-ghost">View All</a>
        </div>
        <div class="admin-table-wrap">
            <table class="admin-table">
                <thead><tr><th>#</th><th>Name</th><th>Brand</th><th>Category</th><th>Price</th><th>Stock</th><th>Status</th></tr></thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty recentProducts}"><tr><td colspan="7" style="text-align:center;color:#7a7a72;padding:24px;">No products found.</td></tr></c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${recentProducts}" begin="0" end="4">
                            <tr>
                                <td>${p.id}</td><td>${p.name}</td><td>${p.brand}</td>
                                <td>${not empty p.categoryName ? p.categoryName : '—'}</td>
                                <td>Rs <fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                <td><c:choose>
                                    <c:when test="${p.stock == 0}"><span class="badge badge-red">${p.stock}</span></c:when>
                                    <c:when test="${p.stock <= 5}"><span class="badge badge-yellow">${p.stock}</span></c:when>
                                    <c:otherwise><span class="badge badge-green">${p.stock}</span></c:otherwise>
                                </c:choose></td>
                                <td><c:choose>
                                    <c:when test="${p.active}"><span class="badge badge-green">Active</span></c:when>
                                    <c:otherwise><span class="badge badge-red">Inactive</span></c:otherwise>
                                </c:choose></td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </main>
</div>
</body></html>
