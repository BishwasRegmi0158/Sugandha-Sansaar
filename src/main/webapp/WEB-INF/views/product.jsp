<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — Our Collection" />
    <jsp:param name="cssFile" value="product" />
</jsp:include>

<body>
<div class="products-page">

    <header class="products-header">
        <div class="header-inner">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/products">🌸 Sugandha Sansaar</a>
            </div>
            <nav>
                <a href="${pageContext.request.contextPath}/products" class="nav-link active">Collection</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a>
            </nav>
        </div>
    </header>

    <section class="hero">
        <h1>Discover Your Scent</h1>
        <p>Premium fragrances crafted for every occasion</p>
    </section>

    <main class="products-main">

        <c:if test="${param.error == 'notfound'}">
            <div class="alert-error">Product not found. Browse our collection below.</div>
        </c:if>

        <%-- Search bar --%>
        <form action="${pageContext.request.contextPath}/products" method="GET" class="search-form">
            <input type="text" name="search" class="search-input"
                   placeholder="Search by name, brand..."
                   value="${not empty searchKeyword ? searchKeyword : ''}"/>
            <button type="submit" class="search-btn">Search</button>
            <c:if test="${not empty searchKeyword}">
                <a href="${pageContext.request.contextPath}/products" class="clear-btn">✕ Clear</a>
            </c:if>
        </form>

        <%-- Filters --%>
        <div class="filters">
            <div class="filter-row">
                <span class="filter-label">Category:</span>
                <a href="${pageContext.request.contextPath}/products"
                   class="filter-btn ${empty categoryFilter && empty genderFilter && empty searchKeyword ? 'active' : ''}">All</a>
                <c:forEach var="cat" items="${categories}">
                    <a href="${pageContext.request.contextPath}/products?category=${cat.id}"
                       class="filter-btn ${categoryFilter == cat.id ? 'active' : ''}">${cat.name}</a>
                </c:forEach>
            </div>
            <div class="filter-row">
                <span class="filter-label">Gender:</span>
                <a href="${pageContext.request.contextPath}/products?gender=male"
                   class="filter-btn ${genderFilter == 'male' ? 'active' : ''}">Men</a>
                <a href="${pageContext.request.contextPath}/products?gender=female"
                   class="filter-btn ${genderFilter == 'female' ? 'active' : ''}">Women</a>
            </div>
        </div>

        <%-- Results count --%>
        <p class="results-count">
            <c:choose>
                <c:when test="${not empty searchKeyword}">
                    ${products.size()} result(s) for "<strong>${searchKeyword}</strong>"
                </c:when>
                <c:otherwise>
                    Showing <strong>${products.size()}</strong> product(s)
                </c:otherwise>
            </c:choose>
        </p>

        <%-- Product grid --%>
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
                                             alt="${p.name}"
                                             onerror="this.parentElement.innerHTML='<div class=\'img-placeholder\'>🌸</div>'"/>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="img-placeholder">🌸</div>
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${not empty p.categoryName}">
                                    <span class="badge-family">${p.categoryName}</span>
                                </c:if>
                                <c:if test="${not p.inStock}">
                                    <span class="badge-oos">Out of Stock</span>
                                </c:if>
                            </div>
                            <div class="card-body">
                                <p class="card-brand">${p.brand}</p>
                                <h3 class="card-name">${p.name}</h3>
                                <div class="card-tags">
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
                                       class="btn-view">View Details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </main>

    <footer class="products-footer">
        <p>© 2025 Sugandha Sansaar — Your Premium Fragrance Destination</p>
    </footer>

</div>
</body>
</html>
