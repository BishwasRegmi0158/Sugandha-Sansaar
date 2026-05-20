<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — My Orders" />
  <jsp:param name="cssFile" value="orders" />
</jsp:include>
<body>

<nav class="ss-nav">
  <div class="ss-nav-logo">Sugandha Sansaar</div>
  <div class="ss-nav-search">
    <form action="${pageContext.request.contextPath}/products" method="GET">
      <input type="text" name="search" placeholder="Search fragrances, brands…" />
      <button type="submit">⌕</button>
    </form>
  </div>
  <div class="ss-nav-links">
    <a href="${pageContext.request.contextPath}/home">Home</a>
    <a href="${pageContext.request.contextPath}/products">Product</a>
    <a href="${pageContext.request.contextPath}/about">About</a>
    <a href="${pageContext.request.contextPath}/user/cart">
      Cart<c:if test="${cartCount > 0}"> (${cartCount})</c:if>
    </a>
    <a href="${pageContext.request.contextPath}/user/order" class="active">Orders</a>
    <a href="${pageContext.request.contextPath}/user/profile">Profile</a>
    <a href="${pageContext.request.contextPath}/logout" class="nav-cta">Logout</a>
  </div>
</nav>

<div class="page-body">
  <div class="orders-page">

    <div class="orders-header">
      <h1>My Orders</h1>
      <a href="${pageContext.request.contextPath}/products" class="btn-shop-more">
        + Shop More
      </a>
    </div>

    <c:choose>
      <c:when test="${empty orders}">
        <div class="orders-empty">
          <div class="empty-icon">🛍</div>
          <h2>No orders yet</h2>
          <p>Your order history will appear here once you make a purchase.</p>
          <a href="${pageContext.request.contextPath}/products" class="btn-gold">
            Browse Collection
          </a>
        </div>
      </c:when>
      <c:otherwise>
        <div class="orders-list">
          <c:forEach var="o" items="${orders}">
            <a href="${pageContext.request.contextPath}/user/order?id=${o.id}"
               class="order-card">

              <div class="order-card-top">
                <div class="order-meta">
                  <span class="order-number">${o.orderNumber}</span>
                  <span class="order-date">
                    <fmt:formatDate value="${o.orderedAt}" pattern="dd MMM yyyy" />
                  </span>
                </div>
                <span class="order-status status-${o.status}">${o.status}</span>
              </div>

              <div class="order-card-bottom">
                <div class="order-pay">
                  <span class="pay-method-label">
                    <c:choose>
                      <c:when test="${o.payment != null}">${o.payment.method}</c:when>
                      <c:otherwise>—</c:otherwise>
                    </c:choose>
                  </span>
                </div>
                <div class="order-total-wrap">
                  <span class="order-total-label">Total</span>
                  <span class="order-total-amt">Rs. <fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00" /></span>
                </div>
                <span class="order-arrow">›</span>
              </div>

            </a>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>

  </div>

  <jsp:include page="/WEB-INF/templates/footer.jsp"/>
</div>

</body>
</html>
