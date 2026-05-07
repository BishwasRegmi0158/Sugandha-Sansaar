<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — ${product.productName}" />
    <jsp:param name="cssFile" value="product-detail" />
</jsp:include>

<body>
<div class="detail-page">

    <!-- ===== HEADER ===== -->
    <header class="products-header">
        <div class="header-inner">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/products">🌸 Sugandha Sansaar</a>
            </div>
            <nav>
                <a href="${pageContext.request.contextPath}/products" class="nav-link">Collection</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a>
            </nav>
        </div>
    </header>

    <!-- Breadcrumb -->
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/products">Collection</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/products?family=${product.fragranceFamily}">${product.fragranceFamily}</a>
        <span>›</span>
        <span>${product.productName}</span>
    </div>

    <main class="detail-main">

        <!-- ===== TWO-COLUMN LAYOUT ===== -->
        <div class="detail-layout">

            <!-- LEFT: Image -->
            <div class="detail-image-col">
                <div class="detail-img-wrap">
                    <c:choose>
                        <c:when test="${not empty product.imageUrl}">
                            <img src="${pageContext.request.contextPath}/static/images/${product.imageUrl}"
                                 alt="${product.productName}"
                                 onerror="this.parentElement.innerHTML='<div class=\'detail-img-placeholder\'>🌸</div>'" />
                        </c:when>
                        <c:otherwise>
                            <div class="detail-img-placeholder">🌸</div>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${not product.inStock}">
                        <div class="oos-overlay">Out of Stock</div>
                    </c:if>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="back-link">← Back to Collection</a>
            </div>

            <!-- RIGHT: Info -->
            <div class="detail-info-col">
                <p class="detail-brand">${product.brand}</p>
                <h1 class="detail-name">${product.productName}</h1>

                <div class="detail-price-row">
                    <span class="detail-price">${product.formattedPrice}</span>
                    <c:choose>
                        <c:when test="${product.inStock}">
                            <span class="stock-badge in-stock">✓ In Stock</span>
                        </c:when>
                        <c:otherwise>
                            <span class="stock-badge out-of-stock">✗ Out of Stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Details box -->
                <div class="detail-info-box">
                    <h3>Fragrance Details</h3>
                    <table class="info-table">
                        <tr><td>Family</td><td><span class="badge-family">${product.fragranceFamily}</span></td></tr>
                        <tr><td>Strength</td><td>${product.scentStrength}</td></tr>
                        <tr><td>Size</td><td>${product.sizeMl} ml</td></tr>
                        <tr><td>Gender</td><td>${product.gender}</td></tr>
                        <tr>
                            <td>Stock</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.inStock}">${product.stockQuantity} units available</c:when>
                                    <c:otherwise><span class="text-red">Currently unavailable</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </table>
                </div>

                <!-- Description -->
                <div class="detail-desc">
                    <h3>About This Fragrance</h3>
                    <p>${product.description}</p>
                </div>

                <!-- Buttons -->
                <div class="detail-actions">
                    <c:choose>
                        <c:when test="${product.inStock}">
                            <button class="btn-primary btn-large"
                                    onclick="alert('🛒 Added to cart!\n\n(Cart is handled by Member 5)')">
                                🛒 Add to Cart
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn-primary btn-large btn-disabled" disabled>Out of Stock</button>
                        </c:otherwise>
                    </c:choose>
                    <a href="${pageContext.request.contextPath}/products" class="btn-secondary btn-large">
                        Continue Shopping
                    </a>
                </div>

                <c:if test="${product.soldCount > 0}">
                    <p class="sold-note">🔥 ${product.soldCount} people bought this</p>
                </c:if>
            </div>

        </div>

        <!-- ===== YOU MAY ALSO LIKE ===== -->
        <c:if test="${not empty relatedProducts}">
            <section class="related-section">
                <h2>You May Also Like</h2>
                <p class="related-subtitle">More from the ${product.fragranceFamily} family</p>
                <div class="product-grid related-grid">
                    <c:forEach var="r" items="${relatedProducts}">
                        <div class="product-card">
                            <div class="card-image">
                                <c:choose>
                                    <c:when test="${not empty r.imageUrl}">
                                        <img src="${pageContext.request.contextPath}/static/images/${r.imageUrl}"
                                             alt="${r.productName}"
                                             onerror="this.parentElement.innerHTML='<div class=\'img-placeholder\'>🌸</div>'" />
                                    </c:when>
                                    <c:otherwise>
                                        <div class="img-placeholder">🌸</div>
                                    </c:otherwise>
                                </c:choose>
                                <span class="badge-family">${r.fragranceFamily}</span>
                            </div>
                            <div class="card-body">
                                <p class="card-brand">${r.brand}</p>
                                <h3 class="card-name">${r.productName}</h3>
                                <div class="card-footer">
                                    <span class="card-price">${r.formattedPrice}</span>
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${r.productId}"
                                       class="btn-view">View</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </section>
        </c:if>

    </main>

    <footer class="products-footer">
        <p>© 2024 Sugandha Sansaar — Your Premium Fragrance Destination</p>
    </footer>

</div>
</body>
</html>
