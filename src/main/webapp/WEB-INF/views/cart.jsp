<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — My Cart" />
  <jsp:param name="cssFile" value="cart" />
</jsp:include>
<body>

<%-- ── Navbar (aura.css .ss-nav) ── --%>
<nav class="ss-nav">
  <div class="ss-nav-logo">Sugandha Sansaar</div>
  <div class="ss-nav-search">
    <form action="${pageContext.request.contextPath}/products" method="GET">
      <input type="text" name="search"
             placeholder="Search fragrances, brands…" />
      <button type="submit">⌕</button>
    </form>
  </div>
  <div class="ss-nav-links">
    <a href="${pageContext.request.contextPath}/home">Home</a>
    <a href="${pageContext.request.contextPath}/products">Product</a>
    <a href="${pageContext.request.contextPath}/about">About</a>
    <a href="${pageContext.request.contextPath}/user/cart" class="active">
      Cart<c:if test="${cartCount > 0}"> (${cartCount})</c:if>
    </a>
    <a href="${pageContext.request.contextPath}/user/order">Orders</a>
    <a href="${pageContext.request.contextPath}/user/profile">Profile</a>
    <a href="${pageContext.request.contextPath}/logout" class="nav-cta">Logout</a>
  </div>
</nav>

<div class="page-body">
  <div class="cart-page">

    <div class="cart-header">
      <h1>My Cart</h1>
      <a href="${pageContext.request.contextPath}/products">← Continue Shopping</a>
    </div>

    <c:choose>
      <c:when test="${empty cartItems}">
        <div class="empty-cart">
          <p>Your cart is empty.</p>
          <a href="${pageContext.request.contextPath}/products" class="btn-gold">
            Browse Collection
          </a>
        </div>
      </c:when>
      <c:otherwise>
        <div class="cart-content">

            <%-- ── Items ── --%>
          <div class="cart-items-list">
            <c:forEach var="item" items="${cartItems}">
              <div class="cart-item">

                <a href="${pageContext.request.contextPath}/product-detail?id=${item.productId}">
                  <img src="${pageContext.request.contextPath}/static/images/product_images/${item.productImageUrl}"
                       alt="<c:out value='${item.productName}' />"
                       onerror="this.style.visibility='hidden'" />
                </a>

                <div class="item-details">
                  <h3><c:out value="${item.productName}" /></h3>
                  <p class="brand"><c:out value="${item.productBrand}" /></p>
                  <p class="unit-price">Rs. <c:out value="${item.unitPrice}" /> each</p>
                </div>

                  <%-- − / + quantity form --%>
                <form action="${pageContext.request.contextPath}/user/cart" method="post" class="qty-form">
                  <input type="hidden" name="productId" value="${item.productId}" />
                  <input type="hidden" name="action"    value="update" />
                  <button type="submit" name="quantity"
                          value="${item.quantity - 1}"
                          class="qty-btn"
                    ${item.quantity <= 1 ? 'disabled' : ''}>−</button>
                  <span class="qty-display">${item.quantity}</span>
                  <button type="submit" name="quantity"
                          value="${item.quantity + 1}"
                          class="qty-btn"
                    ${item.quantity >= item.productStock ? 'disabled' : ''}>+</button>
                </form>

                <p class="line-total">Rs. <c:out value="${item.lineTotal}" /></p>

                <form action="${pageContext.request.contextPath}/user/cart" method="post">
                  <input type="hidden" name="productId" value="${item.productId}" />
                  <input type="hidden" name="action"    value="remove" />
                  <button type="submit" class="btn-remove">✕ Remove</button>
                </form>

              </div>
            </c:forEach>
          </div>

            <%-- ── Summary ── --%>
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
              <span>Rs. ${grandTotal + 100}</span>
            </div>
            <a href="${pageContext.request.contextPath}/user/checkout"
               class="btn-checkout">Proceed to Checkout →</a>
          </div>

        </div>
      </c:otherwise>
    </c:choose>

  </div>

  <%-- ── Footer (aura.css .ss-footer) ── --%>
  <jsp:include page="/WEB-INF/templates/footer.jsp"/>
</div>

</body>
</html>
