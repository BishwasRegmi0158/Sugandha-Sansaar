<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — ${product.name}" />
    <jsp:param name="cssFile" value="product" />
</jsp:include>

<body>
<div class="detail-page">

    <header class="products-header">
        <div class="header-inner">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/products">🌸 Sugandha Sansaar</a>
            </div>
            <nav>
                <a href="${pageContext.request.contextPath}/products" class="nav-link">Collection</a>
                <a href="${pageContext.request.contextPath}/login"    class="nav-link">Login</a>
            </nav>
        </div>
    </header>

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

            <%-- LEFT: image --%>
            <div class="detail-image-col">
                <div class="detail-img-wrap">
                    <c:choose>
                        <c:when test="${not empty product.imageUrl}">
                            <img src="${pageContext.request.contextPath}/static/images/${product.imageUrl}"
                                 alt="${product.name}"
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

            <%-- RIGHT: info --%>
            <div class="detail-info-col">
                <p class="detail-brand">${product.brand}</p>
                <h1 class="detail-name">${product.name}</h1>

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

                <div class="detail-info-box">
                    <h3>Product Details</h3>
                    <table class="info-table">
                        <c:if test="${not empty product.categoryName}">
                            <tr>
                                <td>Category</td>
                                <td><span class="badge-family">${product.categoryName}</span></td>
                            </tr>
                        </c:if>
                        <c:if test="${not empty product.gender}">
                            <tr>
                                <td>Gender</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.gender == 'male'}">Men</c:when>
                                        <c:when test="${product.gender == 'female'}">Women</c:when>
                                        <c:otherwise>${product.gender}</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${product.volume != null}">
                            <tr><td>Volume</td><td>${product.volume} ml</td></tr>
                        </c:if>
                        <tr>
                            <td>Availability</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.inStock}">${product.stock} units available</c:when>
                                    <c:otherwise><span class="text-red">Currently unavailable</span></c:otherwise>
                                </c:choose>
                            </td>
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
                            <button class="btn-primary btn-large"
                                    onclick="alert('🛒 Added to cart!\n\n(Cart feature handled separately)')">
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
            </div>

        </div>

        <%-- Related products --%>
        <c:if test="${not empty relatedProducts}">
            <section class="related-section">
                <h2>You May Also Like</h2>
                <p class="related-subtitle">More from the ${product.categoryName} category</p>
                <div class="product-grid related-grid">
                    <c:forEach var="r" items="${relatedProducts}">
                        <div class="product-card">
                            <div class="card-image">
                                <c:choose>
                                    <c:when test="${not empty r.imageUrl}">
                                        <img src="${pageContext.request.contextPath}/static/images/${r.imageUrl}"
                                             alt="${r.name}"
                                             onerror="this.parentElement.innerHTML='<div class=\'img-placeholder\'>🌸</div>'" />
                                    </c:when>
                                    <c:otherwise><div class="img-placeholder">🌸</div></c:otherwise>
                                </c:choose>
                                <c:if test="${not empty r.categoryName}">
                                    <span class="badge-family">${r.categoryName}</span>
                                </c:if>
                            </div>
                            <div class="card-body">
                                <p class="card-brand">${r.brand}</p>
                                <h3 class="card-name">${r.name}</h3>
                                <div class="card-footer">
                                    <span class="card-price">${r.formattedPrice}</span>
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${r.id}"
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
        <p>© 2025 Sugandha Sansaar — Your Premium Fragrance Destination</p>
    </footer>

</div>
</body>
</html>
