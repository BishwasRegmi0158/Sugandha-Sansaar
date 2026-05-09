<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — ${product.name}" />
  <jsp:param name="cssFile" value="product" />
</jsp:include>
<body>
<div class="product-detail-page">

  <nav class="detail-nav">
    <div class="nav-brand">🌸 Sugandha Sansaar</div>
    <div class="nav-links">
      <a href="${pageContext.request.contextPath}/user/dashboard">Home</a>
      <a href="${pageContext.request.contextPath}/search">Shop</a>
      <a href="${pageContext.request.contextPath}/user/cart">
        🛒 Cart
        <c:if test="${cartCount > 0}">
          <span class="cart-badge">${cartCount}</span>
        </c:if>
      </a>
      <a href="${pageContext.request.contextPath}/user/profile">Profile</a>
      <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
  </nav>

  <div class="breadcrumb">
    <a href="${pageContext.request.contextPath}/search">← Back to Shop</a>
  </div>

  <div class="product-detail-card">

    <div class="product-image-section">
      <img src="${pageContext.request.contextPath}/static/images/products/${product.imageUrl}"
           alt="<c:out value='${product.name}' />"
           class="product-main-image" />
    </div>

    <div class="product-info-section">

      <h1 class="product-title">
        <c:out value="${product.name}" />
      </h1>
      <p class="product-brand">by <c:out value="${product.brand}" /></p>
      <p class="product-price">Rs. <c:out value="${product.price}" /></p>

      <c:if test="${not empty product.volume}">
        <p class="product-volume">Volume: <c:out value="${product.volume}" /> ml</p>
      </c:if>

      <c:if test="${not empty product.gender}">
        <p class="product-gender">
          For:
          <span class="gender-tag gender-${product.gender}">
                        <c:out value="${product.gender}" />
                    </span>
        </p>
      </c:if>

      <p class="product-stock-status">
        <c:choose>
          <c:when test="${product.stock > 0}">
                        <span class="in-stock">
                            ✔ In Stock (<c:out value="${product.stock}" /> left)
                        </span>
          </c:when>
          <c:otherwise>
            <span class="out-of-stock">✘ Out of Stock</span>
          </c:otherwise>
        </c:choose>
      </p>

      <c:if test="${not empty product.description}">
        <div class="product-description">
          <h3>Description</h3>
          <p><c:out value="${product.description}" /></p>
        </div>
      </c:if>

      <c:choose>
        <c:when test="${product.stock > 0}">
          <form action="${pageContext.request.contextPath}/user/cart"
                method="post"
                class="add-to-cart-form">
            <input type="hidden" name="productId" value="${product.id}" />
            <input type="hidden" name="action"    value="add" />
            <div class="qty-row">
              <label for="quantity">Quantity:</label>
              <input type="number"
                     id="quantity"
                     name="quantity"
                     value="1"
                     min="1"
                     max="${product.stock}"
                     class="qty-input" />
            </div>
            <button type="submit" class="btn-add-cart">
              🛒 Add to Cart
            </button>
          </form>
        </c:when>
        <c:otherwise>
          <button class="btn-add-cart btn-disabled" disabled>
            Out of Stock
          </button>
        </c:otherwise>
      </c:choose>

    </div>
  </div>

</div>
</body>
</html>