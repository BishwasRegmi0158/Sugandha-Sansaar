<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sugandha Sansaar — Premium Fragrances</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>

<!-- NAVBAR — public home page, not logged in -->
<nav class="ss-nav">
    <div class="ss-nav-logo">Sugandha Sansaar</div>
    <div class="ss-nav-links">
        <a href="${pageContext.request.contextPath}/home" class="active">HOME</a>
        <a href="${pageContext.request.contextPath}/login">PRODUCT</a>
        <a href="${pageContext.request.contextPath}/login">MY DASHBOARD</a>
        <a href="${pageContext.request.contextPath}/about">ABOUT US</a>
        <a href="${pageContext.request.contextPath}/login">CART</a>
        <a href="${pageContext.request.contextPath}/login" class="nav-cta">LOGIN</a>
    </div>
</nav>

<div class="page-body">

    <!-- Hero — centered -->
    <section class="hero">
        <div class="hero-search">
            <form action="${pageContext.request.contextPath}/products" method="GET">
                <input type="text" name="search" placeholder="Search for fragrances, brands, or collections..."/>
                <button type="submit">⌕</button>
            </form>
        </div>
        <div class="hero-content">
            <p class="hero-eyebrow">Premium Fragrance Collection</p>
            <h1 class="hero-title">Sugandha Sansaar</h1>
            <p class="hero-desc">Discover your signature scent. Luxury perfumes crafted for elegance and allure.</p>
            <div class="hero-btns">
                <a href="${pageContext.request.contextPath}/products" class="btn-gold">Shop Collection</a>
                <a href="${pageContext.request.contextPath}/register" class="btn-outline">Join Now — It's Free</a>
            </div>
        </div>
    </section>

    <!-- Featured Highlights -->
    <section class="featured-highlights">
        <div class="section-header">
            <h2>Featured Highlights</h2>
            <div class="section-divider"></div>
        </div>
        <div class="highlights-grid">
            <c:forEach var="p" items="${featuredProducts}">
                <div class="highlight-card">
                    <div class="highlight-img">
                        <c:choose>
                            <c:when test="${not empty p.imageUrl}">
                                <img src="${pageContext.request.contextPath}/static/images/product_images/${p.imageUrl}" alt="${p.name}"/>
                            </c:when>
                            <c:otherwise><div class="highlight-img-ph">🌸</div></c:otherwise>
                        </c:choose>
                    </div>
                    <div class="highlight-body">
                        <h3 class="highlight-name">${p.name}</h3>
                        <p class="highlight-price">${p.formattedPrice}</p>
                        <a href="${pageContext.request.contextPath}/products" class="highlight-btn">View Details</a>
                    </div>
                </div>
            </c:forEach>
            <%-- Fallback if no products loaded --%>
            <c:if test="${empty featuredProducts}">
                <div class="highlight-card">
                    <div class="highlight-img"><div class="highlight-img-ph">🌸</div></div>
                    <div class="highlight-body"><h3 class="highlight-name">Midnight Noir</h3><p class="highlight-price">Rs. 1,200</p><a href="${pageContext.request.contextPath}/products" class="highlight-btn">View Details</a></div>
                </div>
                <div class="highlight-card">
                    <div class="highlight-img"><div class="highlight-img-ph">🌺</div></div>
                    <div class="highlight-body"><h3 class="highlight-name">Rose Whisper</h3><p class="highlight-price">Rs. 950</p><a href="${pageContext.request.contextPath}/products" class="highlight-btn">View Details</a></div>
                </div>
                <div class="highlight-card">
                    <div class="highlight-img"><div class="highlight-img-ph">✨</div></div>
                    <div class="highlight-body"><h3 class="highlight-name">Obsidian Essence</h3><p class="highlight-price">Rs. 1,450</p><a href="${pageContext.request.contextPath}/products" class="highlight-btn">View Details</a></div>
                </div>
            </c:if>
        </div>
    </section>

    <!-- CTA -->
    <section class="cta-band">
        <h2>Ready to find your scent?</h2>
        <p>Create a free account to browse our full collection, add to cart and order.</p>
        <div class="hero-btns">
            <a href="${pageContext.request.contextPath}/register" class="btn-gold">Register Free</a>
            <a href="${pageContext.request.contextPath}/about" class="btn-outline">Our Story</a>
        </div>
    </section>

    <footer class="ss-footer">
        <div class="ss-footer-inner">
            <div class="ss-footer-brand">Sugandha Sansaar</div>
            <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
        </div>
    </footer>
</div>
</body></html>
