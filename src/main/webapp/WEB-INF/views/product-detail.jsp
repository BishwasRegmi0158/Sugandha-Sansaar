<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — ${product.name}" />
    <jsp:param name="cssFile" value="product-detail" />
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

    <!-- Breadcrumb -->
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/products">Collection</a>
        <span>›</span>
        <a href="${pageContext.request.contextPath}/products?category=${product.categoryId}">${product.categoryName}</a>
        <span>›</span>
        <span>${product.name}</span>
    </div>

    <main class="detail-main">

        <c:if test="${not empty errorMessage}">
            <div class="alert-error">${errorMessage}</div>
        </c:if>

        <div class="detail-layout">

            <!-- LEFT: Image -->
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
                    <c:if test="${product.stock == 0}">
                        <div class="oos-overlay">Out of Stock</div>
                    </c:if>
                </div>
                <a href="${pageContext.request.contextPath}/products" class="back-link">← Back to Collection</a>
            </div>

            <!-- RIGHT: Info -->
            <div class="detail-info-col">
                <p class="detail-brand">${product.brand}</p>
                <h1 class="detail-name">${product.name}</h1>

                <div class="detail-price-row">
                    <span class="detail-price">NPR ${product.price}</span>
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <span class="stock-badge in-stock">✓ In Stock (${product.stock} left)</span>
                        </c:when>
                        <c:otherwise>
                            <span class="stock-badge out-of-stock">✗ Out of Stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Details box -->
                <div class="detail-info-box">
                    <h3>Product Details</h3>
                    <table class="detail-table">
                        <tr><th>Category</th><td>${product.categoryName}</td></tr>
                        <tr><th>Volume</th>  <td>${product.volume} ml</td></tr>
                        <tr><th>Gender</th>  <td><c:out value="${product.gender}"/></td></tr>
                        <tr><th>Brand</th>   <td>${product.brand}</td></tr>
                    </table>
                </div>

                <c:if test="${not empty product.description}">
                    <div class="detail-description">
                        <h3>Description</h3>
                        <p>${product.description}</p>
                    </div>
                </c:if>

            </div>
        </div>

        <!-- RELATED PRODUCTS -->
        <c:if test="${not empty relatedProducts}">
            <section class="related-section">
                <h2>More from ${product.categoryName}</h2>
                <div class="related-grid">
                    <c:forEach var="r" items="${relatedProducts}">
                        <a href="${pageContext.request.contextPath}/product-detail?id=${r.id}" class="related-card">
                            <c:choose>
                                <c:when test="${not empty r.imageUrl}">
                                    <img src="${pageContext.request.contextPath}/static/images/${r.imageUrl}"
                                         alt="${r.name}"
                                         onerror="this.src=''"/>
                                </c:when>
                                <c:otherwise>
                                    <div class="related-img-placeholder">🌸</div>
                                </c:otherwise>
                            </c:choose>
                            <p class="related-brand">${r.brand}</p>
                            <p class="related-name">${r.name}</p>
                            <p class="related-price">NPR ${r.price}</p>
                        </a>
                    </c:forEach>
                </div>
            </section>
        </c:if>

    </main>
</div>
</body>
</html>
