<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — My Cart" />
  <jsp:param name="cssFile" value="cart" />
</jsp:include>
<body>
<div class="cart-page">

  <nav class="cart-nav">
    <div class="nav-brand">🌸 Sugandha Sansaar</div>
    <div class="nav-links">
      <a href="${pageContext.request.contextPath}/user/dashboard">Home</a>
      <a href="${pageContext.request.contextPath}/search">Shop</a>
      <a href="${pageContext.request.contextPath}/user/cart" class="active">
        🛒 Cart
        <c:if test="${cartCount > 0}">
          <span class="cart-badge">${cartCount}</span>
        </c:if>
      </a>
      <a href="${pageContext.request.contextPath}/user/profile">Profile</a>
      <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
  </nav>

  <div class="cart-header">
    <h1>🛒 My Cart</h1>
    <a href="${pageContext.request.contextPath}/search">← Continue Shopping</a>
  </div>

  <c:choose>
    <c:when test="${empty cartItems}">
      <div class="empty-cart">
        <p>Your cart is empty.</p>
        <a href="${pageContext.request.contextPath}/search"
           class="btn-shop">Browse Perfumes →</a>
      </div>
    </c:when>
    <c:otherwise>
      <div class="cart-content">

        <div class="cart-items-list">
          <c:forEach var="item" items="${cartItems}">
            <div class="cart-item">
              <a href="${pageContext.request.contextPath}/product?id=${item.productId}">
                <img src="${pageContext.request.contextPath}/static/images/products/${item.productImageUrl}"
                     alt="<c:out value='${item.productName}' />" />
              </a>
              <div class="item-details">
                <h3><c:out value="${item.productName}" /></h3>
                <p class="brand"><c:out value="${item.productBrand}" /></p>
                <p class="unit-price">
                  Rs. <c:out value="${item.unitPrice}" /> each
                </p>
              </div>
              <form action="${pageContext.request.contextPath}/user/cart"
                    method="post" class="qty-form">
                <input type="hidden" name="productId" value="${item.productId}" />
                <input type="hidden" name="action"    value="update" />
                <input type="number"
                       name="quantity"
                       value="${item.quantity}"
                       min="1"
                       max="${item.productStock}"
                       class="qty-input"
                       onchange="this.form.submit()" />
              </form>
              <p class="line-total">
                Rs. <c:out value="${item.lineTotal}" />
              </p>
              <form action="${pageContext.request.contextPath}/user/cart"
                    method="post">
                <input type="hidden" name="productId" value="${item.productId}" />
                <input type="hidden" name="action"    value="remove" />
                <button type="submit" class="btn-remove">✕ Remove</button>
              </form>
            </div>
          </c:forEach>
        </div>

        <div class="cart-summary">
          <h2>Order Summary</h2>
          <div class="summary-row">
            <span>Subtotal</span>
            <span>Rs. <c:out value="${grandTotal}" /></span>
          </div>
          <div class="summary-row">
            <span>Shipping</span>
            <span>Rs. 100.00</span>
          </div>
          <div class="summary-row total">
            <span>Total</span>
            <span>Rs. <c:out value="${grandTotal.add(java.math.BigDecimal.valueOf(100))}" /></span>
          </div>
          <a href="${pageContext.request.contextPath}/user/checkout"
             class="btn-checkout">Proceed to Checkout →</a>
        </div>

      </div>
    </c:otherwise>
  </c:choose>

</div>
</body>
</html>