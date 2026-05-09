<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — Search" />
  <jsp:param name="cssFile" value="search" />
</jsp:include>
<body>
<div class="search-page">

  <nav class="search-nav">
    <div class="nav-brand">🌸 Sugandha Sansaar</div>
    <div class="nav-links">
      <a href="${pageContext.request.contextPath}/user/dashboard">Home</a>
      <a href="${pageContext.request.contextPath}/search" class="active">Shop</a>
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

  <div class="search-header">
    <h1>Search Perfumes</h1>
  </div>

  <form action="${pageContext.request.contextPath}/search"
        method="get" class="search-bar">
    <input type="text"
           name="keyword"
           placeholder="Search by name or brand..."
           value="<c:out value='${keyword}' default='' />" />
    <button type="submit">Search</button>
  </form>

  <div class="results-section">
    <c:choose>
      <c:when test="${empty results}">
        <div class="no-data">
          <p>No perfumes found
            <c:if test="${not empty keyword}">
              for "<c:out value='${keyword}' />"
            </c:if>
          </p>
        </div>
      </c:when>
      <c:otherwise>
        <p class="results-count">
          Found <strong>${results.size()}</strong> result(s)
          <c:if test="${not empty keyword}">
            for "<c:out value='${keyword}' />"
          </c:if>
        </p>
        <div class="product-grid">
          <c:forEach var="product" items="${results}">
            <div class="product-card">
              <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                 class="product-link">
                <img src="${pageContext.request.contextPath}/static/images/products/${product.imageUrl}"
                     alt="<c:out value='${product.name}' />" />
                <div class="product-info">
                  <h3><c:out value="${product.name}" /></h3>
                  <p class="brand"><c:out value="${product.brand}" /></p>
                  <p class="price">Rs. <c:out value="${product.price}" /></p>
                  <c:if test="${not empty product.volume}">
                    <p class="volume">
                      <c:out value="${product.volume}" /> ml
                    </p>
                  </c:if>
                  <p class="stock-status">
                    <c:choose>
                      <c:when test="${product.stock > 0}">
                        <span class="in-stock">In Stock</span>
                      </c:when>
                      <c:otherwise>
                        <span class="out-of-stock">Out of Stock</span>
                      </c:otherwise>
                    </c:choose>
                  </p>
                </div>
              </a>
              <c:if test="${product.stock > 0}">
                <form action="${pageContext.request.contextPath}/user/cart"
                      method="post">
                  <input type="hidden" name="productId" value="${product.id}" />
                  <input type="hidden" name="action"    value="add" />
                  <input type="hidden" name="quantity"  value="1" />
                  <button type="submit" class="btn-add-cart">
                    🛒 Add to Cart
                  </button>
                </form>
              </c:if>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>

</div>
</body>
</html>