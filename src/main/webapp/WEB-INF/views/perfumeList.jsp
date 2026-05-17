<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manage Perfumes – Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>

<div class="admin-wrapper">
  <%-- Sidebar --%>
  <aside class="sidebar">
    <div class="sidebar-logo">
      <span class="logo-icon"></span>
      <span class="logo-text">Sugandha Sansaar</span>
    </div>
    <nav class="sidebar-nav">
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
        📊 Dashboard
      </a>
      <a href="${pageContext.request.contextPath}/admin/perfumes" class="nav-link active">
        🧴 Manage Perfumes
      </a>
      <a href="${pageContext.request.contextPath}/admin/perfumes?action=add" class="nav-link">
        ➕ Add Perfume
      </a>
      <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">
        👤 Manage Users
      </a>
      <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">
        🚪 Logout
      </a>
    </nav>
  </aside>

  <main class="main-content">
    <header class="page-header">
      <h1>Manage Perfumes</h1>
      <a href="${pageContext.request.contextPath}/admin/perfumes?action=add" class="btn-primary">+ Add New Perfume</a>
    </header>

    <%-- Flash Messages --%>
    <c:if test="${not empty successMessage}">
      <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <%-- Perfume Table --%>
    <div class="table-container">
      <table class="admin-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Image</th>
          <th>Name</th>
          <th>Brand</th>
          <th>Category</th>
          <th>Price</th>
          <th>Vol (ml)</th>
          <th>Stock</th>
          <th>Gender</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
          <c:when test="${empty perfumes}">
            <tr>
              <td colspan="11" class="text-center">
                No perfumes found.
                <a href="${pageContext.request.contextPath}/admin/perfumes?action=add">Add your first perfume.</a>
              </td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="p" items="${perfumes}">
              <tr>
                <td>${p.id}</td>
                <td>
                  <c:choose>
                    <c:when test="${not empty p.imageUrl}">
                      <img src="${pageContext.request.contextPath}/${p.imageUrl}"
                           alt="${p.name}" class="product-thumb">
                    </c:when>
                    <c:otherwise>
                      <span class="no-image">No Image</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>${p.name}</td>
                <td>${p.brand}</td>
                <td>${p.category}</td>
                <td>Rs. <fmt:formatNumber value="${p.price}" pattern="#,##0.00"/></td>
                <td>${p.volume}</td>
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
                <td>${p.gender}</td>
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
                <td class="action-cell">
                    <%-- Edit Button --%>
                  <a href="${pageContext.request.contextPath}/admin/perfumes?action=edit&id=${p.id}"
                     class="btn-edit">Edit</a>

                    <%-- Update Stock Form (inline) --%>
                  <form method="post"
                        action="${pageContext.request.contextPath}/admin/perfumes"
                        class="inline-form">
                    <input type="hidden" name="action" value="updateStock">
                    <input type="hidden" name="id" value="${p.id}">
                    <input type="number" name="stock" value="${p.stock}"
                           min="0" class="stock-input" required>
                    <button type="submit" class="btn-stock">Update Stock</button>
                  </form>

                    <%-- Delete Form --%>
                  <form method="post"
                        action="${pageContext.request.contextPath}/admin/perfumes"
                        class="inline-form"
                        onsubmit="return confirmDelete('${p.name}');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id"     value="${p.id}">
                    <button type="submit" class="btn-delete">Delete</button>
                  </form>
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

<script>
  /**
   * Asks admin to confirm before deleting a perfume.
   * @param {string} name - the perfume name to display
   * @returns {boolean} true to proceed with delete
   */
  function confirmDelete(name) {
    return confirm('Are you sure you want to delete "' + name + '"? This action cannot be undone.');
  }
</script>

</body>
</html>
