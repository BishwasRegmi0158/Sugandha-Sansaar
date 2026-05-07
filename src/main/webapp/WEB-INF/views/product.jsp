<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — Our Collection" />
    <jsp:param name="cssFile" value="products" />
</jsp:include>

<body>
<div class="products-page">

    <!-- ===== HEADER ===== -->
    <header class="products-header">
        <div class="header-inner">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/products">
                    🌸 Sugandha Sansaar
                </a>
            </div>
            <nav>
                <a href="${pageContext.request.contextPath}/products" class="nav-link active">Collection</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a>
            </nav>
        </div>
    </header>

    <!-- ===== HERO ===== -->
    <section class="hero">
        <h1>Discover Your Scent</h1>
        <p>Premium fragrances crafted for every occasion</p>
    </section>

    <main class="products-main">

        <!-- Error message -->
        <c:if test="${param.error == 'notfound'}">
            <div class="alert-error">Product not found. Browse our collection below.</div>
        </c:if>

        <!-- ===== SEARCH BAR ===== -->
        <form action="${pageContext.request.contextPath}/products" method="GET" class="search-form">
            <input
                    type="text"
                    name="search"
                    class="search-input"
                    placeholder="Search by name, brand..."
                    value="${not empty searchKeyword ? searchKeyword : ''}"
            />
            <button type="submit" class="search-btn">Search</button>
            <c:if test="${not empty searchKeyword}">
                <a href="${pageContext.request.contextPath}/products" class="clear-btn">✕ Clear</a>
            </c:if>
        </form>

        <!-- ===== FILTERS ===== -->
        <div class="filters">
            <div class="filter-row">
                <span class="filter-label">Fragrance:</span>
                <a href="${pageContext.request.contextPath}/products"
                   class="filter-btn ${empty familyFilter && empty genderFilter && empty searchKeyword ? 'active' : ''}">All</a>
                <a href="${pageContext.request.contextPath}/products?family=Floral"
                   class="filter-btn ${familyFilter == 'Floral' ? 'active' : ''}">🌸 Floral</a>
                <a href="${pageContext.request.contextPath}/products?family=Woody"
                   class="filter-btn ${familyFilter == 'Woody' ? 'active' : ''}">🌲 Woody</a>
                <a href="${pageContext.request.contextPath}/products?family=Oriental"
                   class="filter-btn ${familyFilter == 'Oriental' ? 'active' : ''}">✨ Oriental</a>
                <a href="${pageContext.request.contextPath}/products?family=Fresh"
                   class="filter-btn ${familyFilter == 'Fresh' ? 'active' : ''}">🌊 Fresh</a>
                <a href="${pageContext.request.contextPath}/products?family=Citrus"
                   class="filter-btn ${familyFilter == 'Citrus' ? 'active' : ''}">🍋 Citrus</a>
            </div>
            <div class="filter-row">
                <span class="filter-label">Gender:</span>
                <a href="${pageContext.request.contextPath}/products?gender=Men"
                   class="filter-btn ${genderFilter == 'Men' ? 'active' : ''}">Men</a>
                <a href="${pageContext.request.contextPath}/products?gender=Women"
                   class="filter-btn ${genderFilter == 'Women' ? 'active' : ''}">Women</a>
                <a href="${pageContext.request.contextPath}/products?gender=Unisex"
                   class="filter-btn ${genderFilter == 'Unisex' ? 'active' : ''}">Unisex</a>
            </div>
        </div>

        <!-- Results count -->
        <p class="results-count">
            <c:choose>
                <c:when test="${not empty searchKeyword}">Showing ${products.size()} result(s) for "<strong>${searchKeyword}</strong>"</c:when>
                <c:when test="${not empty familyFilter}">Showing <strong>${products.size()}</strong> ${familyFilter} fragrances</c:when>
                <c:when test="${not empty genderFilter}">Showing <strong>${products.size()}</strong> fragrances for ${genderFilter}</c:when>
                <c:otherwise>Showing all <strong>${products.size()}</strong> fragrances</c:otherwise>
            </c:choose>
        </p>

        <!-- ===== PRODUCT GRID ===== -->
        <c:choose>
            <c:when test="${empty products}">
                <div class="empty-state">
                    <div class="empty-icon">💨</div>
                    <h2>No products found</h2>
                    <a href="${pageContext.request.contextPath}/products" class="btn-primary">View All</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="product-grid">
                    <c:forEach var="p" items="${products}">
                        <div class="product-card ${not p.inStock ? 'oos' : ''}">
                            <div class="card-image">
                                <c:choose>
                                    <c:when test="${not empty p.imageUrl}">
                                        <img src="${pageContext.request.contextPath}/static/images/${p.imageUrl}"
                                             alt="${p.productName}"
                                             onerror="this.parentElement.innerHTML='<div class=\'img-placeholder\'>🌸</div>'" />
                                    </c:when>
                                    <c:otherwise>
                                        <div class="img-placeholder">🌸</div>
                                    </c:otherwise>
                                </c:choose>
                                <span class="badge-family">${p.fragranceFamily}</span>
                                <c:if test="${not p.inStock}">
                                    <span class="badge-oos">Out of Stock</span>
                                </c:if>
                            </div>
                            <div class="card-body">
                                <p class="card-brand">${p.brand}</p>
                                <h3 class="card-name">${p.productName}</h3>
                                <div class="card-tags">
                                    <span>${p.gender}</span>
                                    <span>${p.scentStrength}</span>
                                    <span>${p.sizeMl}ml</span>
                                </div>
                                <div class="card-footer">
                                    <span class="card-price">${p.formattedPrice}</span>
                                    <a href="${pageContext.request.contextPath}/product-detail?id=${p.productId}"
                                       class="btn-view">View Details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </main>

    <!-- ===== FOOTER ===== -->
    <footer class="products-footer">
        <p>© 2024 Sugandha Sansaar — Your Premium Fragrance Destination</p>
    </footer>

</div>
</body>
</html>
