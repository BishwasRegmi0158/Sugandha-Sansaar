<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>

<footer class="ss-footer">
    <div class="ss-footer-inner">

        <!-- Brand + Tagline -->
        <div class="ss-footer-col ss-footer-brand-col">
            <div class="ss-footer-brand">Sugandha Sansaar</div>
            <p class="ss-footer-tagline">
                Premium fragrances crafted for<br/>elegance, allure, and identity.
            </p>
            <!-- Social Icons -->
            <div class="ss-footer-socials">
                <a href="#" aria-label="Facebook" class="ss-social-link">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
                        <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"/>
                    </svg>
                </a>
                <a href="#" aria-label="Twitter / X" class="ss-social-link">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
                        <path d="M4 4l16 16M20 4 4 20" stroke="currentColor" stroke-width="2" stroke-linecap="round" fill="none"/>
                        <path d="M2 3h6.5l13 18H15Z"/>
                        <path d="M15 3h4v4M9 21H5v-4"/>
                    </svg>
                </a>
                <a href="#" aria-label="Instagram" class="ss-social-link">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" width="18" height="18">
                        <rect x="2" y="2" width="20" height="20" rx="5" ry="5"/>
                        <circle cx="12" cy="12" r="4"/>
                        <circle cx="17.5" cy="6.5" r="1" fill="currentColor" stroke="none"/>
                    </svg>
                </a>
                <a href="#" aria-label="LinkedIn" class="ss-social-link">
                    <svg viewBox="0 0 24 24" fill="currentColor" width="18" height="18">
                        <path d="M16 8a6 6 0 0 1 6 6v7h-4v-7a2 2 0 0 0-2-2 2 2 0 0 0-2 2v7h-4v-7a6 6 0 0 1 6-6z"/>
                        <rect x="2" y="9" width="4" height="12"/>
                        <circle cx="4" cy="4" r="2"/>
                    </svg>
                </a>
            </div>
        </div>

        <!-- Quick Links -->
        <div class="ss-footer-col">
            <h4 class="ss-footer-heading">Explore</h4>
            <ul class="ss-footer-links">
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products">Collection</a></li>
                <li><a href="${pageContext.request.contextPath}/about">Our Story</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <li><a href="${pageContext.request.contextPath}/user/cart">Cart</a></li>
                        <li><a href="${pageContext.request.contextPath}/user/order">My Orders</a></li>
                        <li><a href="${pageContext.request.contextPath}/user/profile">Profile</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/login">Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/register">Register</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>

        <!-- About Blurb -->
        <div class="ss-footer-col ss-footer-about-col">
            <h4 class="ss-footer-heading">About the Company</h4>
            <p class="ss-footer-about">
                Sugandha Sansaar is Nepal's premier destination for luxury fragrances.
                We curate the world's finest perfumes — each bottle a story of craftsmanship,
                heritage, and sensory art. Find the scent that defines you.
            </p>
        </div>

        <!-- Contact Info -->
        <div class="ss-footer-col">
            <h4 class="ss-footer-heading">Contact</h4>
            <ul class="ss-footer-contact">
                <li>
                    <span class="ss-contact-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" width="16" height="16">
                            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                            <circle cx="12" cy="10" r="3"/>
                        </svg>
                    </span>
                    <span>Pokhara, Gandaki Pradesh<br/><strong>Nepal</strong></span>
                </li>
                <li>
                    <span class="ss-contact-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" width="16" height="16">
                            <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07A19.5 19.5 0 0 1 4.9 13.5 19.79 19.79 0 0 1 1.82 4.9 2 2 0 0 1 3.8 2.7h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L7.91 10.09a16 16 0 0 0 6 6l1.56-1.56a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
                        </svg>
                    </span>
                    <span>+977 9860573543</span>
                </li>
                <li>
                    <span class="ss-contact-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" width="16" height="16">
                            <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                            <polyline points="22,6 12,13 2,6"/>
                        </svg>
                    </span>
                    <a href="mailto:support@sugandhasansaar.com" class="ss-footer-email">
                        support@sugandhasansaar.com
                    </a>
                </li>
            </ul>
        </div>

    </div>

    <!-- Bottom Bar -->
    <div class="ss-footer-bottom">
        <p>&copy; 2025 Sugandha Sansaar &middot; Premium Fragrance Destination &middot; All rights reserved.</p>
    </div>
</footer>
