<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Sugandha Sansaar — Collection</title>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>

<!-- ═══════════════════════════════════════════════════════════
     NAVBAR
     ═══════════════════════════════════════════════════════════ -->
<nav class="ss-nav">
  <div class="ss-nav-logo">Sugandha Sansaar</div>

  <%-- Search bar — only useful when logged in and on product page --%>
  <c:if test="${not empty sessionScope.loggedUser}">
    <div class="ss-nav-search">
      <form action="${pageContext.request.contextPath}/products" method="GET">
        <input type="text" name="search"
               placeholder="Search for fragrances, brands, or collections..."
               value="${not empty searchKeyword ? searchKeyword : ''}"/>
        <button type="submit">⌕</button>
      </form>
    </div>
  </c:if>

  <div class="ss-nav-links">

    <!-- Always visible public links -->
    <a href="${pageContext.request.contextPath}/home">HOME</a>
    <a href="${pageContext.request.contextPath}/about">ABOUT US</a>

    <!-- Protected links — only visible when logged in -->
    <c:if test="${not empty sessionScope.loggedUser}">
      <a href="${pageContext.request.contextPath}/products" class="active">PRODUCT</a>
      <a href="${pageContext.request.contextPath}/user/cart">CART</a>
      <a href="${pageContext.request.contextPath}/user/order">ORDERS</a>
    </c:if>

    <!-- LOGIN or PROFILE + LOGOUT -->
    <c:choose>
      <c:when test="${not empty sessionScope.loggedUser}">
        <a href="${pageContext.request.contextPath}/user/profile">PROFILE</a>
        <a href="${pageContext.request.contextPath}/logout" class="nav-cta">LOGOUT</a>
      </c:when>
      <c:otherwise>
        <a href="${pageContext.request.contextPath}/login" class="nav-cta">LOGIN</a>
      </c:otherwise>
    </c:choose>

  </div>
</nav>

<div class="page-body">

  <!-- ═══════════════════════════════════════════════════════════
       COLLECTION HEADER
       ═══════════════════════════════════════════════════════════ -->
  <div class="collection-header">
    <h1>Our Collection</h1>
    <p>Discover our meticulously crafted fragrances, designed to leave a lasting impression.</p>
  </div>

  <!-- ═══════════════════════════════════════════════════════════
       FILTER BAR
       ═══════════════════════════════════════════════════════════ -->
  <div class="filter-bar">
    <div class="filter-inner">

      <div class="filter-group">
        <span class="filter-label">CATEGORY</span>
        <div class="filter-pills">
          <a href="${pageContext.request.contextPath}/products"
             class="pill ${empty categoryFilter && empty genderFilter && empty searchKeyword ? 'active' : ''}">
            All
          </a>
          <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/products?category=${cat.id}"
               class="pill ${categoryFilter == cat.id ? 'active' : ''}">
                ${cat.name}
            </a>
          </c:forEach>
        </div>
      </div>

      <div class="filter-group">
        <span class="filter-label">FOR</span>
        <div class="filter-pills">
          <a href="${pageContext.request.contextPath}/products?gender=male"
             class="pill ${genderFilter == 'male' ? 'active' : ''}">Men</a>
          <a href="${pageContext.request.contextPath}/products?gender=female"
             class="pill ${genderFilter == 'female' ? 'active' : ''}">Women</a>
        </div>
      </div>

      <div class="results-info">
        <c:choose>
          <c:when test="${not empty searchKeyword}">
            <strong>${products.size()}</strong> results for "<em>${searchKeyword}</em>"
            <a href="${pageContext.request.contextPath}/products" class="clear-link">✕ Clear</a>
          </c:when>
          <c:otherwise>
            <strong>${products.size()}</strong> products
          </c:otherwise>
        </c:choose>
      </div>

    </div>
  </div>

  <!-- ═══════════════════════════════════════════════════════════
       PRODUCT GRID
       ═══════════════════════════════════════════════════════════ -->
  <main class="collection">
    <c:choose>

      <c:when test="${empty products}">
        <div class="empty-state">
          <div class="empty-icon"></div>
          <h2>No products found</h2>
          <p>Try a different search or browse all our fragrances.</p>
          <a href="${pageContext.request.contextPath}/products" class="btn-gold">View All</a>
        </div>
      </c:when>

      <c:otherwise>
        <div class="product-grid">
          <c:forEach var="p" items="${products}">
            <div class="product-card">

              <div class="card-img">
                <c:choose>
                  <c:when test="${not empty p.imageUrl}">
                    <img src="${pageContext.request.contextPath}/static/images/product_images/${p.imageUrl}"
                         alt="${p.name}"
                         onerror="this.parentElement.innerHTML='<div class=\'img-ph\'>🌸</div>'"/>
                  </c:when>
                  <c:otherwise>
                    <div class="img-ph">🌸</div>
                  </c:otherwise>
                </c:choose>

                <c:if test="${not empty p.categoryName}">
                  <span class="card-badge">${p.categoryName}</span>
                </c:if>

                <c:if test="${not p.inStock}">
                  <div class="oos-overlay">Out of Stock</div>
                </c:if>
              </div>

              <div class="card-body">
                <p class="card-brand">${p.brand}</p>
                <h3 class="card-name">${p.name}</h3>

                <div class="card-meta">
                  <c:if test="${not empty p.gender}">
                    <span>${p.gender == 'male' ? 'Men' : 'Women'}</span>
                  </c:if>
                  <c:if test="${p.volume != null}">
                    <span>${p.volume}ml</span>
                  </c:if>
                </div>

                <div class="card-footer">
                  <span class="card-price">${p.formattedPrice}</span>
                  <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}"
                     class="btn-ghost">VIEW →</a>
                </div>
              </div>

            </div>
          </c:forEach>
        </div>
      </c:otherwise>

    </c:choose>
  </main>

  <!-- ═══════════════════════════════════════════════════════════
       FOOTER
       ═══════════════════════════════════════════════════════════ -->
  <footer class="ss-footer">
    <div class="ss-footer-inner">
      <div class="ss-footer-brand">Sugandha Sansaar</div>
      <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
    </div>
  </footer>

</div>
</body>
</html>