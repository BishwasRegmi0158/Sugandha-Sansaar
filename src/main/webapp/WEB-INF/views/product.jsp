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
                <a href="${pageContext.request.contextPath}/login"    class="nav-link">Login</a>
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

        <!-- SEARCH -->
        <form action="${pageContext.request.contextPath}/products" method="GET" class="search-form">
            <input type="text" name="search" class="search-input"
                   placeholder="Search by name, brand..."
                   value="${not empty searchKeyword ? searchKeyword : ''}"/>
            <button type="submit" class="search-btn">Search</button>
            <c:if test="${not empty searchKeyword}">
                <a href="${pageContext.request.contextPath}/products" class="clear-btn">✕ Clear</a>
            </c:if>
        </form>

        <!-- FILTERS -->
        <div class="filters">
            <div class="filter-row">
                <span class="filter-label">Category:</span>
                <a href="${pageContext.request.contextPath}/products"
                   class="filter-btn ${empty categoryFilter ? 'active' : ''}">All</a>
                <c:forEach var="cat" items="${categories}">
                    <a href="${pageContext.request.contextPath}/products?category=${cat.id}"
                       class="filter-btn ${categoryFilter == cat.id ? 'active' : ''}">${cat.name}</a>
                </c:forEach>
            </div>
            <div class="filter-row">
                <span class="filter-label">Gender:</span>
                <a href="${pageContext.request.contextPath}/products"
                   class="filter-btn ${empty genderFilter ? 'active' : ''}">All</a>
                <a href="${pageContext.request.contextPath}/products?gender=male"
                   class="filter-btn ${'male' == genderFilter ? 'active' : ''}">Male</a>
                <a href="${pageContext.request.contextPath}/products?gender=female"
                   class="filter-btn ${'female' == genderFilter ? 'active' : ''}">Female</a>
            </div>
        </div>

        <!-- PRODUCT GRID -->
        <c:choose>
            <c:when test="${empty products}">
                <div class="no-results">
                    <p>No perfumes found. <a href="${pageContext.request.contextPath}/products">View all</a></p>
                </div>
            </c:when>
            <c:otherwise>
                <p class="results-count">${products.size()} product(s) found</p>
                <div class="products-grid">
                    <c:forEach var="p" items="${products}">
                        <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}" class="product-card-link">
                            <div class="product-card">
                                <div class="product-img-wrap">
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
                                    <c:if test="${p.stock == 0}">
                                        <div class="oos-badge">Out of Stock</div>
                                    </c:if>
                                </div>
                                <div class="product-info">
                                    <p class="product-brand">${p.brand}</p>
                                    <h3 class="product-name">${p.name}</h3>
                                    <p class="product-meta">${p.categoryName} · ${p.volume}ml · ${p.gender}</p>
                                    <p class="product-price">NPR ${p.price}</p>
                                    <c:choose>
                                        <c:when test="${p.stock > 0}">
                                            <span class="stock-badge in-stock">In Stock</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock-badge out-of-stock">Out of Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

    </main>
</div>
</body>
</html>
