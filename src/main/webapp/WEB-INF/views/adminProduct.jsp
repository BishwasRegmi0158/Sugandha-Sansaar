<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Products – Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>

<div class="admin-wrapper">

    <aside class="sidebar">
        <div class="sidebar-logo">
            <span class="logo-icon">🌸</span>
            <span class="logo-text">Sugandha Admin</span>
        </div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard"           class="nav-link">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products"            class="nav-link active">📦 Manage Products</a>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="nav-link">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/logout"                    class="nav-link nav-logout">🚪 Logout</a>
        </nav>
    </aside>

    <main class="main-content">

        <header class="page-header">
            <h1>Manage Products</h1>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="btn-primary">+ Add New Product</a>
        </header>

        <%-- Flash messages --%>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <%-- Quick stats --%>
        <section class="stats-grid">
            <div class="stat-card stat-blue">
                <div class="stat-icon">📦</div>
                <div class="stat-info"><h3>${totalProducts}</h3><p>Active Products</p></div>
            </div>
            <div class="stat-card stat-red">
                <div class="stat-icon">❌</div>
                <div class="stat-info"><h3>${outOfStock}</h3><p>Out of Stock</p></div>
            </div>
            <div class="stat-card stat-yellow">
                <div class="stat-icon">⚠️</div>
                <div class="stat-info"><h3>${lowStock}</h3><p>Low Stock (≤ 5)</p></div>
            </div>
        </section>

        <%-- Product table --%>
        <div class="table-container">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th><th>Image</th><th>Name</th><th>Brand</th>
                    <th>Category</th><th>Price</th><th>Vol(ml)</th>
                    <th>Stock</th><th>Gender</th><th>Status</th><th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty products}">
                        <tr>
                            <td colspan="11" class="text-center">
                                No products yet.
                                <a href="${pageContext.request.contextPath}/admin/products?action=add">Add your first product.</a>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="p" items="${products}">
                            <tr class="${not p.active ? 'row-inactive' : ''}">
                                <td>${p.id}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty p.imageUrl}">
                                            <img src="${pageContext.request.contextPath}/static/images/${p.imageUrl}"
                                                 alt="${p.name}" class="product-thumb">
                                        </c:when>
                                        <c:otherwise><span class="no-image">🌸</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${p.name}</td>
                                <td>${p.brand}</td>
                                <td>${not empty p.categoryName ? p.categoryName : '—'}</td>
                                <td>Rs <fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                                <td>${not empty p.volume ? p.volume : '—'}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.stock == 0}"><span class="badge badge-red">0</span></c:when>
                                        <c:when test="${p.stock <= 5}"><span class="badge badge-yellow">${p.stock}</span></c:when>
                                        <c:otherwise>${p.stock}</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.gender == 'male'}">Men</c:when>
                                        <c:when test="${p.gender == 'female'}">Women</c:when>
                                        <c:otherwise>—</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.active}"><span class="badge badge-green">Active</span></c:when>
                                        <c:otherwise><span class="badge badge-red">Inactive</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="action-cell">
                                    <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.id}"
                                       class="btn-edit">Edit</a>

                                    <c:if test="${p.active}">
                                        <form method="post" action="${pageContext.request.contextPath}/admin/products"
                                              style="display:inline"
                                              onsubmit="return confirm('Deactivate \'${p.name}\'?')">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id"     value="${p.id}">
                                            <button type="submit" class="btn-delete">Deactivate</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </main>
</div>

</body>
</html>
