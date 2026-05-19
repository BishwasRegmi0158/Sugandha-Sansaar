<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — Order ${order.orderNumber}" />
  <jsp:param name="cssFile" value="orderDetail" />
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
  <div class="order-detail-page">

    <%-- ── Breadcrumb ── --%>
    <div class="od-breadcrumb">
      <a href="${pageContext.request.contextPath}/user/order">My Orders</a>
      <span class="crumb-sep">›</span>
      <span class="crumb-active">${order.orderNumber}</span>
    </div>

    <%-- ── Success banner (shown once after placing) ── --%>
    <c:if test="${placed}">
      <div class="od-success-banner">
        <span class="success-icon">✓</span>
        <div>
          <strong>Order placed successfully!</strong>
          <p>Thank you for shopping with Sugandha Sansaar. We'll notify you when your order ships.</p>
        </div>
      </div>
    </c:if>

    <%-- ── Order header ── --%>
    <div class="od-header">
      <div class="od-title-block">
        <h1 class="od-order-number">${order.orderNumber}</h1>
        <span class="od-date">Placed on
          <fmt:formatDate value="${order.orderedAt}" pattern="dd MMMM yyyy, hh:mm a" />
        </span>
      </div>
      <span class="order-status status-${order.status}">${order.status}</span>
    </div>

    <div class="od-layout">

      <%-- ══════════════════════════════════════════════════
           LEFT COL — items + delivery + payment
           ══════════════════════════════════════════════════ --%>
      <div class="od-main">

        <%-- Items ── --%>
        <div class="od-section">
          <h2 class="od-section-title">Items Ordered</h2>
          <div class="od-items-list">
            <c:forEach var="item" items="${order.items}">
              <div class="od-item">
                <div class="od-item-img">
                  <img src="${pageContext.request.contextPath}/static/images/product_images/${item.productImageUrl}"
                       alt="<c:out value='${item.productName}' />"
                       style="width:110px;height:110px;min-width:110px;max-width:110px;max-height:110px;object-fit:cover;display:block;"
                       onerror="this.style.visibility='hidden'" />
                </div>
                <div class="od-item-info">
                  <p class="od-item-name"><c:out value="${item.productName}" /></p>
                  <p class="od-item-brand"><c:out value="${item.productBrand}" /></p>
                  <p class="od-item-meta">
                    Rs. <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" />
                    &times; ${item.quantity}
                  </p>
                </div>
                <p class="od-item-line">
                  Rs. <fmt:formatNumber value="${item.lineTotal}" pattern="#,##0.00" />
                </p>
              </div>
            </c:forEach>
          </div>
        </div>

        <%-- Delivery ── --%>
        <div class="od-section">
          <h2 class="od-section-title">Delivery Address</h2>
          <div class="od-address-box">
            <p class="addr-name">${order.deliveryName}</p>
            <p class="addr-phone">${order.deliveryPhone}</p>
            <p class="addr-line">
              ${order.deliveryStreet},
              ${order.deliveryCity},
              ${order.deliveryState} – ${order.deliveryPinCode}
            </p>
          </div>
        </div>

        <%-- Payment ── --%>
        <c:if test="${order.payment != null}">
          <div class="od-section">
            <h2 class="od-section-title">Payment</h2>
            <div class="od-payment-row">
              <span class="pay-method-label">${order.payment.method}</span>
              <span class="pay-status-badge pay-${order.payment.status}">${order.payment.status}</span>
            </div>
          </div>
        </c:if>

      </div>

      <%-- ══════════════════════════════════════════════════
           RIGHT COL — price summary
           ══════════════════════════════════════════════════ --%>
      <aside class="od-summary">
        <h2 class="od-summary-title">Price Summary</h2>

        <div class="od-sum-row">
          <span>Subtotal</span>
          <span>Rs. <fmt:formatNumber value="${order.subtotal}" pattern="#,##0.00" /></span>
        </div>
        <div class="od-sum-row">
          <span>Shipping</span>
          <span>Rs. <fmt:formatNumber value="${order.shippingFee}" pattern="#,##0.00" /></span>
        </div>
        <div class="od-sum-divider"></div>
        <div class="od-sum-row od-sum-total">
          <span>Total</span>
          <span>Rs. <fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" /></span>
        </div>

        <a href="${pageContext.request.contextPath}/user/order" class="od-back-btn">
          ← Back to Orders
        </a>
        <a href="${pageContext.request.contextPath}/products" class="od-shop-btn">
          Continue Shopping
        </a>
      </aside>

    </div><%-- /.od-layout --%>
  </div><%-- /.order-detail-page --%>

  <footer class="ss-footer">
    <div class="ss-footer-inner">
      <div class="ss-footer-brand">Sugandha Sansaar</div>
      <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
      <div class="ss-footer-links">
        <a href="${pageContext.request.contextPath}/about">About</a>
        <a href="${pageContext.request.contextPath}/products">Shop</a>
      </div>
    </div>
  </footer>
</div>

</body>
</html>