<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Sugandha Sansaar — ${product.name}</title>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<nav class="ss-nav">
  <div class="ss-nav-logo">Sugandha Sansaar</div>
  <div class="ss-nav-links">
    <a href="${pageContext.request.contextPath}/products">PRODUCT</a>
    <a href="${pageContext.request.contextPath}/user/cart">CART</a>
    <a href="${pageContext.request.contextPath}/logout">LOGOUT</a>
  </div>
</nav>

<div class="page-body">
  <div class="breadcrumb">
    <a href="${pageContext.request.contextPath}/products">Collection</a>
    <span>›</span>
    <c:if test="${not empty product.categoryName}">
      <a href="${pageContext.request.contextPath}/products?category=${product.categoryId}">${product.categoryName}</a>
      <span>›</span>
    </c:if>
    <span>${product.name}</span>
  </div>

  <main class="detail-main">
    <div class="detail-layout">
      <div>
        <div class="detail-img-wrap">
          <c:choose>
            <c:when test="${not empty product.imageUrl}">
              <img src="${pageContext.request.contextPath}/static/images/product_images/${product.imageUrl}" alt="${product.name}" onerror="this.parentElement.innerHTML='<div class=\'detail-img-placeholder\'>🌸</div>'"/>
            </c:when>
            <c:otherwise><div class="detail-img-placeholder">🌸</div></c:otherwise>
          </c:choose>
          <c:if test="${not product.inStock}"><div class="oos-overlay">Out of Stock</div></c:if>
        </div>
        <a href="${pageContext.request.contextPath}/products" class="back-link">← Back to Collection</a>
      </div>

      <div>
        <p class="detail-brand">${product.brand}</p>
        <h1 class="detail-name">${product.name}</h1>
        <div class="detail-price-row">
          <span class="detail-price">${product.formattedPrice}</span>
          <c:choose>
            <c:when test="${product.inStock}"><span class="stock-badge in-stock">✓ In Stock</span></c:when>
            <c:otherwise><span class="stock-badge out-of-stock">✗ Out of Stock</span></c:otherwise>
          </c:choose>
        </div>

        <div class="detail-info-box">
          <h3>Product Details</h3>
          <table class="info-table">
            <c:if test="${not empty product.categoryName}">
              <tr><td>Category</td><td>${product.categoryName}</td></tr>
            </c:if>
            <c:if test="${not empty product.gender}">
              <tr><td>For</td><td>${product.gender == 'male' ? 'Men' : 'Women'}</td></tr>
            </c:if>
            <c:if test="${product.volume != null}">
              <tr><td>Volume</td><td>${product.volume} ml</td></tr>
            </c:if>
            <tr><td>Availability</td>
              <td><c:choose>
                <c:when test="${product.inStock}">${product.stock} units available</c:when>
                <c:otherwise>Currently unavailable</c:otherwise>
              </c:choose></td>
            </tr>
          </table>
        </div>

        <c:if test="${not empty product.description}">
          <div class="detail-desc">
            <h3>About This Product</h3>
            <p>${product.description}</p>
          </div>
        </c:if>

        <div class="detail-actions">
          <c:choose>
            <c:when test="${product.inStock}">
              <form action="${pageContext.request.contextPath}/user/cart" method="post">
                <input type="hidden" name="productId" value="${product.id}"/>
                <input type="hidden" name="action" value="add"/>
                <input type="hidden" name="quantity" value="1"/>
                <button type="submit" class="btn-gold" style="padding:13px 28px;font-size:0.75rem;">Add to Cart</button>
              </form>
            </c:when>
            <c:otherwise>
              <button class="btn-gold" disabled style="opacity:0.4;cursor:not-allowed;padding:13px 28px;">Out of Stock</button>
            </c:otherwise>
          </c:choose>
          <a href="${pageContext.request.contextPath}/products" class="btn-outline" style="padding:13px 28px;font-size:0.75rem;">Continue Shopping</a>
        </div>
      </div>
    </div>

    <c:if test="${not empty relatedProducts}">
      <div style="padding-top:56px; border-top:1px solid rgba(255,255,255,0.07); margin-top:16px;">
        <p style="font-family:'Cormorant Garamond',serif;font-size:1.8rem;font-weight:300;color:#f5ede0;margin-bottom:8px;">You May Also Like</p>
        <p style="font-size:0.75rem;color:#7a7a72;margin-bottom:28px;letter-spacing:0.5px;">More from the ${product.categoryName} category</p>
        <div class="product-grid">
          <c:forEach var="r" items="${relatedProducts}">
            <div class="product-card">
              <div class="card-img">
                <c:choose>
                  <c:when test="${not empty r.imageUrl}"><img src="${pageContext.request.contextPath}/static/images/product_images/${r.imageUrl}" alt="${r.name}"/></c:when>
                  <c:otherwise><div class="img-ph">🌸</div></c:otherwise>
                </c:choose>
              </div>
              <div class="card-body">
                <p class="card-brand">${r.brand}</p>
                <h3 class="card-name">${r.name}</h3>
                <div class="card-footer">
                  <span class="card-price">${r.formattedPrice}</span>
                  <a href="${pageContext.request.contextPath}/product-detail?id=${r.id}" class="btn-ghost">View →</a>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
    </c:if>
  </main>

  <footer class="ss-footer">
    <div class="ss-footer-inner">
      <div class="ss-footer-brand">Sugandha Sansaar</div>
      <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
    </div>
  </footer>
</div>
</body></html>
