<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title"   value="Sugandha Sansaar — Dashboard" />
    <jsp:param name="cssFile" value="userDashboard" />
</jsp:include>
<body>
<div class="dashboard-page">

    <%-- Navbar --%>
    <nav class="dashboard-nav">
        <div class="nav-brand"> Sugandha Sansaar</div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/user/dashboard">Home</a>
            <a href="${pageContext.request.contextPath}/search">Shop</a>
            <a href="${pageContext.request.contextPath}/user/cart">
                🛒 Cart
                <c:if test="${cartCount > 0}">
                    <span class="cart-badge">${cartCount}</span>
                </c:if>
            </a>
            <a href="${pageContext.request.contextPath}/user/profile">Profile</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </nav>

    <%-- Welcome header --%>
    <div class="dashboard-header">
        <c:choose>
            <c:when test="${not empty sessionScope.loggedUser.profilePic}">
                <img src="${pageContext.request.contextPath}/static/images/profiles/${sessionScope.loggedUser.profilePic}"
                     alt="Profile" class="header-avatar" />
            </c:when>
            <c:otherwise>
                <img src="${pageContext.request.contextPath}/static/images/profiles/default.png"
                     alt="Profile" class="header-avatar" />
            </c:otherwise>
        </c:choose>
        <div>
            <h1>Welcome, <c:out value="${sessionScope.loggedUser.fullName}" />!</h1>
            <p>Explore our exclusive perfume collection</p>
        </div>
    </div>

    <%-- Search Bar --%>
    <div class="search-section">
        <form action="${pageContext.request.contextPath}/search" method="get">
            <input type="text"
                   name="keyword"
                   placeholder="Search perfumes by name or brand..."
                   value="<c:out value='${param.keyword}' default='' />" />
            <button type="submit">Search</button>
        </form>
    </div>

    <%-- My Orders --%>
    <div class="section">
        <h2>My Orders</h2>
        <c:choose>
            <c:when test="${empty orders}">
                <p class="no-data">You have not placed any orders yet.</p>
            </c:when>
            <c:otherwise>
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>Order #</th>
                        <th>Deliver To</th>
                        <th>City</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td><c:out value="${order.orderNumber}" /></td>
                            <td><c:out value="${order.deliveryName}" /></td>
                            <td><c:out value="${order.deliveryCity}" /></td>
                            <td>Rs. <c:out value="${order.totalAmount}" /></td>
                            <td>
                                    <span class="status-badge status-${order.status}">
                                        <c:out value="${order.status}" />
                                    </span>
                            </td>
                            <td><c:out value="${order.orderedAt}" /></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Cart Mini Preview --%>
    <div class="section">
        <h2>My Cart
            <a href="${pageContext.request.contextPath}/user/cart"
               class="btn-view-cart">View Full Cart →</a>
        </h2>
        <c:choose>
            <c:when test="${empty cartItems}">
                <p class="no-data">Your cart is empty.
                    <a href="${pageContext.request.contextPath}/search">
                        Start shopping →
                    </a>
                </p>
            </c:when>
            <c:otherwise>
                <div class="cart-mini">
                    <c:forEach var="item" items="${cartItems}">
                        <div class="cart-mini-item">
                            <img src="${pageContext.request.contextPath}/static/images/products/${item.productImageUrl}"
                                 alt="<c:out value='${item.productName}' />" />
                            <div class="cart-mini-details">
                                <strong><c:out value="${item.productName}" /></strong>
                                <span><c:out value="${item.productBrand}" /></span>
                                <span>Qty: <c:out value="${item.quantity}" /></span>
                                <span>Rs. <c:out value="${item.lineTotal}" /></span>
                            </div>
                            <form action="${pageContext.request.contextPath}/user/cart"
                                  method="post">
                                <input type="hidden" name="productId" value="${item.productId}" />
                                <input type="hidden" name="action"    value="remove" />
                                <input type="hidden" name="redirect"  value="dashboard" />
                                <button type="submit" class="btn-remove-mini">✕</button>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- Our Collection --%>
    <div class="section">
        <h2>Our Collection</h2>
        <div class="product-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/product?id=${product.id}"
                       class="product-link">
                        <img src="${pageContext.request.contextPath}/static/images/products/${product.imageUrl}"
                             alt="<c:out value='${product.name}' />" />
                        <div class="product-info">
                            <h3><c:out value="${product.name}" /></h3>
                            <p class="brand"><c:out value="${product.brand}" /></p>
                            <p class="price">Rs. <c:out value="${product.price}" /></p>
                            <c:if test="${not empty product.volume}">
                                <p class="volume"><c:out value="${product.volume}" /> ml</p>
                            </c:if>
                            <p class="stock-status">
                                <c:choose>
                                    <c:when test="${product.stock > 0}">
                                        <span class="in-stock">In Stock</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="out-of-stock">Out of Stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </a>
                    <c:if test="${product.stock > 0}">
                        <form action="${pageContext.request.contextPath}/user/cart"
                              method="post">
                            <input type="hidden" name="productId" value="${product.id}" />
                            <input type="hidden" name="action"    value="add" />
                            <input type="hidden" name="quantity"  value="1" />
                            <button type="submit" class="btn-add-cart">
                                🛒 Add to Cart
                            </button>
                        </form>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </div>

</div>
</body>
</html>