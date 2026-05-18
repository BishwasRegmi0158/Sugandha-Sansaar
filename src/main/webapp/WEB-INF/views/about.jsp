<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sugandha Sansaar — About Us</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
    <style>
        /* ── About page specific styles ── */
        .about-hero {
            text-align: center;
            padding: 80px 56px 64px;
            background: radial-gradient(ellipse at 50% 0%, rgba(201,168,76,0.08) 0%, transparent 60%), var(--black);
            border-bottom: 1px solid var(--borderw);
        }
        .about-hero-eyebrow {
            font-size: 0.68rem; font-weight: 600; letter-spacing: 3px;
            text-transform: uppercase; color: var(--gold);
            display: flex; align-items: center; justify-content: center; gap: 12px;
            margin-bottom: 18px;
        }
        .about-hero-eyebrow::before,
        .about-hero-eyebrow::after {
            content: ''; display: inline-block; width: 28px; height: 1px; background: var(--gold);
        }
        .about-hero h1 {
            font-family: var(--ff);
            font-size: clamp(2.4rem, 5vw, 4rem);
            font-weight: 300; color: var(--cream); margin-bottom: 16px;
        }
        .about-hero p {
            font-size: 0.92rem; color: var(--muted);
            max-width: 560px; margin: 0 auto; line-height: 1.9;
        }

        /* Story section */
        .about-story {
            display: grid; grid-template-columns: 1fr 1fr;
            gap: 64px; align-items: center;
            padding: 72px 80px;
            border-bottom: 1px solid var(--borderw);
        }
        .about-story-text h2 {
            font-family: var(--ff);
            font-size: clamp(1.8rem, 3vw, 2.6rem);
            font-weight: 300; color: var(--cream); margin-bottom: 20px;
        }
        .about-story-text h2 em { font-style: italic; color: var(--gold-l); }
        .about-story-text p {
            font-size: 0.88rem; color: var(--muted);
            line-height: 1.95; margin-bottom: 14px;
        }
        .about-story-stats {
            display: grid; grid-template-columns: repeat(3,1fr); gap: 24px;
        }
        .stat-box {
            text-align: center;
            padding: 28px 16px;
            background: var(--dark2);
            border: 1px solid var(--borderw);
            border-radius: 4px;
        }
        .stat-num {
            font-family: var(--ff); font-size: 2.4rem; font-weight: 300;
            color: var(--gold); display: block; margin-bottom: 4px;
        }
        .stat-label {
            font-size: 0.65rem; font-weight: 600; letter-spacing: 1.5px;
            text-transform: uppercase; color: var(--muted);
        }

        /* Team section */
        .team-section {
            padding: 72px 56px 96px;
        }
        .team-header {
            text-align: center; margin-bottom: 48px;
        }
        .team-header h2 {
            font-family: var(--ff);
            font-size: clamp(1.8rem, 3.5vw, 2.6rem);
            font-weight: 300; color: var(--cream); margin-bottom: 12px;
        }
        .team-header p {
            font-size: 0.88rem; color: var(--muted);
            max-width: 460px; margin: 0 auto; line-height: 1.8;
        }
        .team-divider {
            width: 40px; height: 2px; background: var(--gold);
            margin: 14px auto 0;
        }
        .team-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
            max-width: 1000px;
            margin: 0 auto;
        }
        .team-card {
            background: var(--dark2);
            border: 1px solid var(--borderw);
            border-radius: 4px; overflow: hidden;
            display: flex; flex-direction: column;
            transition: transform 0.3s ease, border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .team-card:hover {
            transform: translateY(-4px);
            border-color: rgba(201,168,76,0.35);
            box-shadow: 0 16px 40px rgba(0,0,0,0.5);
        }
        .team-img {
            height: 160px;
            background: linear-gradient(135deg, #1a1a1a, #222);
            display: flex; align-items: center; justify-content: center;
            overflow: hidden;
        }
        .team-img img {
            width: 100%; height: 100%; object-fit: cover; object-position: top;
            transition: transform 0.55s ease;
        }
        .team-card:hover .team-img img { transform: scale(1.05); }
        .team-avatar {
            width: 72px; height: 72px; border-radius: 50%;
            background: linear-gradient(135deg, rgba(201,168,76,0.15), rgba(201,168,76,0.05));
            border: 1px solid rgba(201,168,76,0.25);
            display: flex; align-items: center; justify-content: center;
            font-family: var(--ff); font-size: 1.5rem; font-weight: 300;
            color: var(--gold-l);
        }
        .team-body { padding: 14px 16px 16px; }
        .team-role {
            font-size: 0.58rem; font-weight: 600; letter-spacing: 2px;
            text-transform: uppercase; color: var(--gold); margin-bottom: 4px;
        }
        .team-name {
            font-family: var(--ff); font-size: 1.1rem; font-weight: 400;
            color: var(--cream); margin-bottom: 8px;
        }
        .team-desc {
            font-size: 0.78rem; color: var(--muted); line-height: 1.65;
            display: -webkit-box; -webkit-line-clamp: 3;
            -webkit-box-orient: vertical; overflow: hidden;
            margin-bottom: 10px;
        }
        .team-tags {
            list-style: none; padding: 0; margin: 0;
            display: flex; flex-wrap: wrap; gap: 4px;
        }
        .team-tags li {
            font-size: 0.58rem; font-weight: 600; letter-spacing: 0.8px;
            text-transform: uppercase; padding: 3px 8px;
            border: 1px solid rgba(201,168,76,0.22);
            border-radius: 2px; color: var(--gold);
            background: rgba(201,168,76,0.04);
        }

        /* Values section */
        .values-section {
            padding: 72px 80px;
            background: var(--dark);
            border-top: 1px solid var(--borderw);
            border-bottom: 1px solid var(--borderw);
        }
        .values-header {
            text-align: center; margin-bottom: 48px;
        }
        .values-header h2 {
            font-family: var(--ff);
            font-size: clamp(1.8rem, 3vw, 2.4rem);
            font-weight: 300; color: var(--cream); margin-bottom: 8px;
        }
        .values-grid {
            display: grid; grid-template-columns: repeat(3,1fr); gap: 24px;
        }
        .value-card {
            padding: 36px 28px;
            background: var(--dark2);
            border: 1px solid var(--borderw);
            border-radius: 4px; text-align: center;
            transition: border-color 0.3s ease;
        }
        .value-card:hover { border-color: rgba(201,168,76,0.3); }
        .value-icon { font-size: 1.5rem; margin-bottom: 14px; }
        .value-card h3 {
            font-family: var(--ff); font-size: 1.2rem; font-weight: 400;
            color: var(--cream); margin-bottom: 10px;
        }
        .value-card p {
            font-size: 0.82rem; color: var(--muted); line-height: 1.75;
        }

        @media (max-width: 900px) {
            .about-story { grid-template-columns: 1fr; padding: 52px 28px; gap: 40px; }
            .team-grid { grid-template-columns: repeat(2,1fr); }
            .values-grid { grid-template-columns: 1fr; }
            .values-section { padding: 52px 28px; }
            .team-section { padding: 52px 28px 72px; }
        }
        @media (max-width: 580px) {
            .team-grid { grid-template-columns: 1fr; }
            .about-story-stats { grid-template-columns: 1fr; }
            .about-hero { padding: 56px 24px 48px; }
        }
    </style>
</head>
<body>

<nav class="ss-nav">
    <div class="ss-nav-logo">Sugandha Sansaar</div>
    <div class="ss-nav-links">
        <c:choose>
            <c:when test="${not empty sessionScope.loggedUser}">
                <a href="${pageContext.request.contextPath}/user/dashboard">HOME</a>
                <a href="${pageContext.request.contextPath}/products">PRODUCT</a>
                <a href="${pageContext.request.contextPath}/user/dashboard">MY DASHBOARD</a>
                <a href="${pageContext.request.contextPath}/about" class="active">ABOUT US</a>
                <a href="${pageContext.request.contextPath}/user/cart">CART</a>
                <a href="${pageContext.request.contextPath}/logout">LOGOUT</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/home">HOME</a>
                <a href="${pageContext.request.contextPath}/products">PRODUCT</a>
                <a href="${pageContext.request.contextPath}/about" class="active">ABOUT US</a>
                <a href="${pageContext.request.contextPath}/login">CART</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-cta">LOGIN</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<div class="page-body">

    <!-- Hero -->
    <section class="about-hero">
        <p class="about-hero-eyebrow">Our Story</p>
        <h1>Crafting Fragrance <em>Experiences</em></h1>
        <p>Sugandha Sansaar was born from a love of fine fragrances and a desire to bring the world's finest scents to Nepal. We believe every person deserves a signature scent that tells their story.</p>
    </section>

    <!-- Story + Stats -->
    <section class="about-story">
        <div class="about-story-text">
            <h2>Born in Nepal,<br/><em>Loved Worldwide</em></h2>
            <p>Founded in 2024, Sugandha Sansaar started as a small passion project by a group of fragrance enthusiasts who believed that luxury scents should be accessible to everyone in Nepal.</p>
            <p>From international powerhouses like Davidoff and Al Haramain to beloved everyday brands like Fogg and Axe, we curate a collection that celebrates fragrance in all its forms — from the subtle to the bold, the floral to the woody.</p>
            <p>Every product in our store is sourced from authorised distributors, guaranteeing 100% authenticity so you can shop with complete confidence.</p>
        </div>
        <div class="about-story-stats">
            <div class="stat-box">
                <span class="stat-num">25+</span>
                <span class="stat-label">Products</span>
            </div>
            <div class="stat-box">
                <span class="stat-num">10+</span>
                <span class="stat-label">Brands</span>
            </div>
            <div class="stat-box">
                <span class="stat-num">100%</span>
                <span class="stat-label">Authentic</span>
            </div>
            <div class="stat-box">
                <span class="stat-num">2024</span>
                <span class="stat-label">Founded</span>
            </div>
            <div class="stat-box">
                <span class="stat-num">6</span>
                <span class="stat-label">Team Members</span>
            </div>
            <div class="stat-box">
                <span class="stat-num">NPR</span>
                <span class="stat-label">Local Currency</span>
            </div>
        </div>
    </section>

    <!-- Team -->
    <section class="team-section">
        <div class="team-header">
            <h2>Meet the Team</h2>
            <p>Six passionate individuals, each owning a core module of Sugandha Sansaar.</p>
            <div class="team-divider"></div>
        </div>
        <div class="team-grid">

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/roman.jpg --%>
                        <img src="${pageContext.request.contextPath}/static/images/team/biswas.jpg" alt="Bishwas Regmi"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 1 — Authentication</p>
                    <h3 class="team-name">Biswas Regmi</h3>
                    <p class="team-desc">Handles the entire authentication system — user registration, login/logout, password hashing, session &amp; cookie management, email validation, password rules, and role-based redirect for admin and user. This is the foundation of the entire system.</p>
                    <ul class="team-tags">
                        <li>User Registration</li>
                        <li>Login / Logout</li>
                        <li>Password Encryption</li>
                        <li>Session Management</li>
                        <li>Role-based Redirect</li>
                    </ul>
                </div>
            </div>

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/darshan.jpg --%>
                    <img src="${pageContext.request.contextPath}/static/images/team/aman.jpg" alt="Aman Gurung"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 4 — Admin Dashboard</p>
                    <h3 class="team-name">Aman Gurung</h3>
                    <p class="team-desc">Manages the full admin dashboard — add, edit, and delete perfumes, manage stock levels, manage categories and brands, and all backend CRUD logic. Ensures admins have full control over the product catalogue.</p>
                    <ul class="team-tags">
                        <li>Admin Dashboard UI</li>
                        <li>Add / Edit / Delete Perfume</li>
                        <li>Stock Management</li>
                        <li>Category &amp; Brand CRUD</li>
                    </ul>
                </div>
            </div>

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/aman.jpg --%>
                        <img src="${pageContext.request.contextPath}/static/images/team/roman.jpg" alt="Roman Thapa"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 3 — Product Browsing</p>
                    <h3 class="team-name">Roman Thapa</h3>
                    <p class="team-desc">Builds the main user interaction module — displaying all perfumes, the product detail page, search functionality, and filters by category, price, and brand. Fetches all data from the database and handles optional pagination.</p>
                    <ul class="team-tags">
                        <li>Product Listing</li>
                        <li>Product Detail Page</li>
                        <li>Search</li>
                        <li>Filter by Category / Brand</li>
                    </ul>
                </div>
            </div>

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/darshan.jpg --%>
                        <img src="${pageContext.request.contextPath}/static/images/team/aman.jpg" alt="Aman Gurung"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 4 — Admin Dashboard</p>
                    <h3 class="team-name">Aman Gurung</h3>
                    <p class="team-desc">Manages the full admin dashboard — add, edit, and delete perfumes, manage stock levels, manage categories and brands, and all backend CRUD logic. Ensures admins have full control over the product catalogue.</p>
                    <ul class="team-tags">
                        <li>Admin Dashboard UI</li>
                        <li>Add / Edit / Delete Perfume</li>
                        <li>Stock Management</li>
                        <li>Category &amp; Brand CRUD</li>
                    </ul>
                </div>
            </div>

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/bishwas.jpg --%>
                    <img src="${pageContext.request.contextPath}/static/images/team/darshan.jpg" alt="Darshan Gurung"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 2 — Home &amp; Navigation</p>
                    <h3 class="team-name">Darshan Gurung</h3>
                    <p class="team-desc">Owns the home page with dynamic featured perfumes, the shared navbar and footer used across all pages, the About page, contact page with backend form submission to the database, and basic routing and navigation throughout the site.</p>
                    <ul class="team-tags">
                        <li>Home Page</li>
                        <li>Navbar &amp; Footer</li>
                        <li>About &amp; Contact Page</li>
                        <li>Basic Routing</li>
                    </ul>
                </div>
            </div>

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/sumin.jpg --%>
                    <img src="${pageContext.request.contextPath}/static/images/team/sumin.jpg" alt="Sumin Basnet"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 5 — Cart &amp; Orders</p>
                    <h3 class="team-name">Sumin Basnet</h3>
                    <p class="team-desc">Handles all business logic — add to cart, remove from cart, update quantity, wishlist, the full checkout system, placing orders, order history, and total price calculation. This is the commerce engine of the platform.</p>
                    <ul class="team-tags">
                        <li>Cart &amp; Wishlist</li>
                        <li>Checkout System</li>
                        <li>Order History</li>
                        <li>Price Calculation</li>
                    </ul>
                </div>
            </div>

            <div class="team-card">
                <div class="team-img">
                    <%-- Replace src with your own image: static/images/team/samundra.jpg --%>
                    <img src="${pageContext.request.contextPath}/static/images/team/samundra.jpg" alt="Samundra Shahi"/>
                </div>
                <div class="team-body">
                    <p class="team-role">Module 6 — Database &amp; Integration</p>
                    <h3 class="team-name">Samundra Shahi</h3>
                    <p class="team-desc">Designs the full database — tables, relationships, SQL schema, and ER diagram. Connects the database with all modules, fixes integration issues across the system, debugs errors, and ensures the entire platform runs smoothly end to end.</p>
                    <ul class="team-tags">
                        <li>Database Design</li>
                        <li>SQL Schema &amp; ER Diagram</li>
                        <li>Module Integration</li>
                        <li>System Debugging</li>
                    </ul>
                </div>
            </div>

        </div>
    </section>

    <!-- Values -->
    <section class="values-section">
        <div class="values-header">
            <h2>Our Values</h2>
        </div>
        <div class="values-grid">
            <div class="value-card">
                <div class="value-icon">✦</div>
                <h3>Authenticity</h3>
                <p>Every product is sourced from authorised distributors. We guarantee 100% genuine fragrances — no counterfeits, no compromises.</p>
            </div>
            <div class="value-card">
                <div class="value-icon">✦</div>
                <h3>Accessibility</h3>
                <p>Luxury should not be exclusive. We work hard to offer premium fragrances at fair prices so everyone can discover their signature scent.</p>
            </div>
            <div class="value-card">
                <div class="value-icon">✦</div>
                <h3>Excellence</h3>
                <p>From our curated catalogue to our customer support, we hold ourselves to the highest standards in everything we do.</p>
            </div>
        </div>
    </section>

    <footer class="ss-footer">
        <div class="ss-footer-inner">
            <div class="ss-footer-brand">Sugandha Sansaar</div>
            <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
        </div>
    </footer>

</div>
</body>
</html>