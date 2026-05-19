<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Manage Orders – Sugandha Sansaar</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>
<div class="admin-wrapper">

  <%-- ── Sidebar ──────────────────────────────────────────────────────────── --%>
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
      <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">
        👤 Manage Users
      </a>
      <a href="${pageContext.request.contextPath}/admin/orders" class="nav-link active">
        📦 Manage Orders
      </a>
      <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">
        🚪 Logout
      </a>
    </nav>
  </aside>

  <%-- ── Main Content ──────────────────────────────────────────────────────── --%>
  <main class="main-content">
    <header class="page-header">
      <div>
        <h1>Manage Orders</h1>
        <p>Total: <strong>${totalOrders}</strong> &nbsp;|&nbsp;
          Pending: <strong style="color:#e74c3c;">${pendingOrders}</strong></p>
      </div>
    </header>

    <%-- Flash messages --%>
    <c:if test="${not empty successMessage}">
      <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <%-- ── Orders Table ─────────────────────────────────────────────────── --%>
    <div class="table-container">
      <table class="admin-table">
        <thead>
        <tr>
          <th>#</th>
          <th>Order No.</th>
          <th>Customer</th>
          <th>Amount</th>
          <th>Payment</th>
          <th>Ordered At</th>
          <th>Status</th>
          <th>Update Status</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
          <c:when test="${empty orders}">
            <tr><td colspan="8" class="text-center">No orders found.</td></tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="o" items="${orders}">
              <tr>
                <td>${o.id}</td>
                <td><strong>${o.orderNumber}</strong></td>
                <td>${o.deliveryName}<br>
                  <small style="color:#7f8c8d;">${o.deliveryPhone}</small>
                </td>
                <td>Rs. <fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                <td>
                  <c:if test="${not empty o.payment}">
                                        <span class="badge ${o.payment.status == 'paid' ? 'badge-green' : 'badge-yellow'}">
                                            ${o.payment.method} – ${o.payment.status}
                                        </span>
                  </c:if>
                </td>
                <td>
                  <fmt:formatDate value="${o.orderedAt}" pattern="dd MMM yyyy HH:mm"/>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${o.status == 'pending'}">
                      <span class="badge badge-yellow">Pending</span>
                    </c:when>
                    <c:when test="${o.status == 'approved'}">
                      <span class="badge badge-green">Approved</span>
                    </c:when>
                    <c:when test="${o.status == 'processing'}">
                      <span class="badge badge-blue">Processing</span>
                    </c:when>
                    <c:when test="${o.status == 'shipped'}">
                      <span class="badge badge-purple">Shipped</span>
                    </c:when>
                    <c:when test="${o.status == 'delivered'}">
                      <span class="badge badge-green">Delivered</span>
                    </c:when>
                    <c:when test="${o.status == 'cancelled'}">
                      <span class="badge badge-red">Cancelled</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge">${o.status}</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <div class="action-cell">
                    <form method="post"
                          action="${pageContext.request.contextPath}/admin/orders"
                          style="display:inline-flex; align-items:center; gap:6px;">
                    <input type="hidden" name="action" value="updateStatus">
                    <input type="hidden" name="orderId" value="${o.id}">
                    <select name="status" class="stock-input" style="min-width:110px;">
                      <option value="pending"    ${o.status == 'pending'    ? 'selected' : ''}>Pending</option>
                      <option value="approved"   ${o.status == 'approved'   ? 'selected' : ''}>Approved</option>
                      <option value="processing" ${o.status == 'processing' ? 'selected' : ''}>Processing</option>
                      <option value="shipped"    ${o.status == 'shipped'    ? 'selected' : ''}>Shipped</option>
                      <option value="delivered"  ${o.status == 'delivered'  ? 'selected' : ''}>Delivered</option>
                      <option value="cancelled"  ${o.status == 'cancelled'  ? 'selected' : ''}>Cancelled</option>
                    </select>
                    <button type="submit" class="btn-update">Update</button>
                    </form>
                  </div>
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
