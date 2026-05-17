<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Manage Products — Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<div class="admin-wrap">
    <aside class="admin-sidebar">
        <div class="admin-sidebar-logo"><span>Sugandha Admin</span></div>
        <nav class="admin-sidebar-nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="admin-nav-link">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link active">📦 Products</a>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="admin-nav-link">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/logout" class="admin-nav-link logout">🚪 Logout</a>
        </nav>
    </aside>
    <main class="admin-main">
        <div class="admin-page-header">
            <div><h1>Manage Products</h1></div>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="btn-gold">+ Add Product</a>
        </div>
        <c:if test="${not empty successMessage}"><div class="admin-alert ok">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="admin-alert err">${errorMessage}</div></c:if>

        <div class="stats-grid" style="margin-bottom:24px;">
            <div class="stat-card stat-blue"><div class="stat-icon">📦</div><div><div class="stat-num">${totalProducts}</div><div class="stat-lbl">Active</div></div></div>
            <div class="stat-card stat-red"><div class="stat-icon">❌</div><div><div class="stat-num">${outOfStock}</div><div class="stat-lbl">Out of Stock</div></div></div>
            <div class="stat-card stat-yellow"><div class="stat-icon">⚠️</div><div><div class="stat-num">${lowStock}</div><div class="stat-lbl">Low Stock</div></div></div>
        </div>

        <div class="admin-table-wrap">
            <table class="admin-table">
                <thead><tr><th>ID</th><th>Image</th><th>Name</th><th>Brand</th><th>Category</th><th>Price</th><th>Vol</th><th>Stock</th><th>Gender</th><th>Status</th><th>Actions</th></tr></thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr><td colspan="11" style="text-align:center;color:#7a7a72;padding:32px;">No products yet. <a href="${pageContext.request.contextPath}/admin/products?action=add" style="color:#c9a84c;">Add your first.</a></td></tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${products}">
                            <tr style="${not p.active ? 'opacity:0.5' : ''}">
                                <td>${p.id}</td>
                                <td><c:choose>
                                    <c:when test="${not empty p.imageUrl}"><img src="${pageContext.request.contextPath}/static/images/product_images/${p.imageUrl}" class="thumb" alt="${p.name}"/></c:when>
                                    <c:otherwise><span style="font-size:1.4rem;">🌸</span></c:otherwise>
                                </c:choose></td>
                                <td>${p.name}</td><td>${p.brand}</td>
                                <td>${not empty p.categoryName ? p.categoryName : '—'}</td>
                                <td>Rs <fmt:formatNumber value="${p.price}" pattern="#,##0"/></td>
                                <td>${not empty p.volume ? p.volume : '—'}</td>
                                <td><c:choose>
                                    <c:when test="${p.stock == 0}"><span class="badge badge-red">0</span></c:when>
                                    <c:when test="${p.stock <= 5}"><span class="badge badge-yellow">${p.stock}</span></c:when>
                                    <c:otherwise><span class="badge badge-green">${p.stock}</span></c:otherwise>
                                </c:choose></td>
                                <td>${not empty p.gender ? p.gender : '—'}</td>
                                <td><c:choose>
                                    <c:when test="${p.active}"><span class="badge badge-green">Active</span></c:when>
                                    <c:otherwise><span class="badge badge-red">Inactive</span></c:otherwise>
                                </c:choose></td>
                                <td><div class="action-cell">
                                    <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.id}" class="btn-edit">Edit</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/products" onsubmit="return confirm('Delete this product?')">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="id" value="${p.id}"/>
                                        <button type="submit" class="btn-delete">Delete</button>
                                    </form>
                                </div></td>
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
