<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>My Dashboard — Sugandha Sansaar</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>

<!-- NAVBAR -->
<nav class="ss-nav">
    <div class="ss-nav-logo">Sugandha Sansaar</div>
    <div class="ss-nav-search">
        <form action="${pageContext.request.contextPath}/products" method="GET">
            <input type="text" name="search" placeholder="Search for fragrances, brands, or collections..."/>
            <button type="submit">⌕</button>
        </form>
    </div>
    <div class="ss-nav-links">
        <a href="${pageContext.request.contextPath}/home">HOME</a>
        <a href="${pageContext.request.contextPath}/products">PRODUCT</a>
        <a href="${pageContext.request.contextPath}/user/dashboard" class="active">MY DASHBOARD</a>
        <a href="${pageContext.request.contextPath}/user/cart">CART<c:if test="${cartCount > 0}"> (${cartCount})</c:if></a>
        <a href="${pageContext.request.contextPath}/logout">LOGOUT</a>
    </div>
</nav>

<div class="page-body">

    <!-- Welcome Hero -->
    <div class="dash-hero">
        <c:choose>
            <c:when test="${not empty sessionScope.loggedUser.profilePic}">
                <img src="${pageContext.request.contextPath}/static/images/profiles/${sessionScope.loggedUser.profilePic}" class="dash-avatar" alt="Profile"/>
            </c:when>
            <c:otherwise>
                <div style="width:72px;height:72px;border-radius:50%;background:rgba(201,168,76,0.1);border:1.5px solid rgba(201,168,76,0.4);display:flex;align-items:center;justify-content:center;font-size:1.6rem;flex-shrink:0;">👤</div>
            </c:otherwise>
        </c:choose>
        <div class="dash-hero-text">
            <h1>Welcome back, <em><c:out value="${sessionScope.loggedUser.fullName}"/></em></h1>
            <p>Your personal fragrance dashboard</p>
        </div>
        <a href="${pageContext.request.contextPath}/user/profile" style="margin-left:auto;position:relative;z-index:1;" class="btn-ghost">Edit Profile</a>
    </div>

    <!-- Quick Actions -->
    <div style="display:flex;gap:14px;padding:24px 56px;background:#111;border-bottom:1px solid rgba(255,255,255,0.06);">
        <a href="${pageContext.request.contextPath}/products" class="btn-gold" style="font-size:0.7rem;padding:10px 22px;">Browse Collection</a>
        <a href="${pageContext.request.contextPath}/user/cart" class="btn-outline" style="font-size:0.7rem;padding:10px 22px;">View Cart</a>
        <a href="${pageContext.request.contextPath}/user/profile" class="btn-outline" style="font-size:0.7rem;padding:10px 22px;">My Profile</a>
    </div>

    <!-- My Orders -->
    <div class="dash-section">
        <div class="dash-section-title">My Orders</div>
        <c:choose>
            <c:when test="${empty orders}">
                <p class="no-data">No orders yet. <a href="${pageContext.request.contextPath}/products">Start shopping →</a></p>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                    <tr><th>Order #</th><th>Deliver To</th><th>City</th><th>Total</th><th>Status</th><th>Date</th></tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td><c:out value="${order.orderNumber}"/></td>
                            <td><c:out value="${order.deliveryName}"/></td>
                            <td><c:out value="${order.deliveryCity}"/></td>
                            <td>Rs. <c:out value="${order.totalAmount}"/></td>
                            <td><span class="status-badge status-${order.status}"><c:out value="${order.status}"/></span></td>
                            <td><c:out value="${order.orderedAt}"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- My Cart -->
    <div class="dash-section">
        <div class="dash-section-title">My Cart <a href="${pageContext.request.contextPath}/user/cart">View Full Cart →</a></div>
        <c:choose>
            <c:when test="${empty cartItems}">
                <p class="no-data">Your cart is empty. <a href="${pageContext.request.contextPath}/products">Start shopping →</a></p>
            </c:when>
            <c:otherwise>
                <div class="cart-mini">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="cart-mini-item">
                            <img src="${pageContext.request.contextPath}/static/images/product_images/${item.productImageUrl}" alt="${item.productName}" onerror="this.style.display='none'"/>
                            <div class="cart-mini-details">
                                <strong><c:out value="${item.productName}"/></strong>
                                <span><c:out value="${item.productBrand}"/></span>
                                <span>Qty: <c:out value="${item.quantity}"/> · Rs. <c:out value="${item.lineTotal}"/></span>
                            </div>
                            <form action="${pageContext.request.contextPath}/user/cart" method="post">
                                <input type="hidden" name="productId" value="${item.productId}"/>
                                <input type="hidden" name="action" value="remove"/>
                                <button type="submit" class="btn-remove">✕</button>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Featured Products (8 only — full catalogue is at /products) -->
    <div class="dash-section">
        <div class="dash-section-title">Featured Products <a href="${pageContext.request.contextPath}/products">View All →</a></div>
        <div class="product-grid">
            <c:forEach var="p" items="${featuredProducts}">
                <div class="product-card">
                    <div class="card-img">
                        <c:choose>
                            <c:when test="${not empty p.imageUrl}">
                                <img src="${pageContext.request.contextPath}/static/images/product_images/${p.imageUrl}" alt="${p.name}" onerror="this.parentElement.innerHTML='<div class=\'img-ph\'>🌸</div>'"/>
                            </c:when>
                            <c:otherwise><div class="img-ph">🌸</div></c:otherwise>
                        </c:choose>
                        <c:if test="${not empty p.categoryName}"><span class="card-badge">${p.categoryName}</span></c:if>
                    </div>
                    <div class="card-body">
                        <p class="card-brand">${p.brand}</p>
                        <h3 class="card-name">${p.name}</h3>
                        <div class="card-meta">
                            <c:if test="${p.volume != null}"><span>${p.volume}ml</span></c:if>
                        </div>
                        <div class="card-footer">
                            <span class="card-price">Rs. <c:out value="${p.price}"/></span>
                            <a href="${pageContext.request.contextPath}/product-detail?id=${p.id}" class="btn-ghost">View →</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <footer class="ss-footer">
        <div class="ss-footer-inner">
            <div class="ss-footer-brand">Sugandha Sansaar</div>
            <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
        </div>
    </footer>
</div>
</body></html>
