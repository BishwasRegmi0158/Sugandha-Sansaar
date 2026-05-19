<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Users – Sugandha Sansaar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>
<div class="admin-wrapper">
    <aside class="sidebar">
        <div class="sidebar-logo">
            <span class="logo-icon"></span>
            <span class="logo-text">Sugandha Sansaar</span>
        </div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
                📊 Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/perfumes" class="nav-link">
                🧴 Manage Perfumes
            </a>
            <a href="${pageContext.request.contextPath}/admin/perfumes?action=add" class="nav-link">
                ➕ Add Perfume
            </a>
            <a href="${pageContext.request.contextPath}/admin/users" class="nav-link active">
                👤 Manage Users
            </a>
            <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">
                📦 Manage Orders
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">
                🚪 Logout
            </a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="page-header">
            <h1>Manage Users</h1>
        </header>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <table class="admin-table">
            <thead>
            <tr>
                <th>#</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td>${u.id}</td>
                    <td>${u.fullName}</td>
                    <td>${u.email}</td>
                    <td>${u.phone}</td>
                    <td>
                        <span class="badge ${u.roleId == 1 ? 'badge-purple' : 'badge-green'}">
                                ${u.roleId == 1 ? 'Admin' : 'User'}
                        </span>
                    </td>
                    <td>
                        <span class="badge ${u.isActive == 1 ? 'badge-green' : 'badge-red'}">
                                ${u.isActive == 1 ? 'Active' : 'Inactive'}
                        </span>
                    </td>
                    <td>
                        <!-- Change Role -->
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/users"
                              style="display:inline;">
                            <input type="hidden" name="action" value="changeRole">
                            <input type="hidden" name="userId" value="${u.id}">
                            <select name="roleId">
                                <option value="1" ${u.roleId == 1 ? 'selected' : ''}>Admin</option>
                                <option value="2" ${u.roleId == 2 ? 'selected' : ''}>User</option>
                            </select>
                            <button type="submit" class="btn-small btn-primary">Update</button>
                        </form>

                        <!-- Toggle Active -->
                        <form method="post"
                              action="${pageContext.request.contextPath}/admin/users"
                              style="display:inline;">
                            <input type="hidden" name="action" value="toggleActive">
                            <input type="hidden" name="userId" value="${u.id}">
                            <button type="submit"
                                    class="btn-small ${u.isActive == 1 ? 'btn-danger' : 'btn-success'}">
                                    ${u.isActive == 1 ? 'Deactivate' : 'Activate'}
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </main>
</div>
</body>
</html>